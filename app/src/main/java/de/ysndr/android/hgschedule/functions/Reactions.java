package de.ysndr.android.hgschedule.functions;

import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.pacoworks.rxcomprehensions.RxComprehensions;
import com.pacoworks.rxpartialapplication2.RxPartialFunction;

import de.ysndr.android.hgschedule.functions.models.Transformation;
import de.ysndr.android.hgschedule.inject.RemoteDataService;
import de.ysndr.android.hgschedule.state.Empty;
import de.ysndr.android.hgschedule.state.Error;
import de.ysndr.android.hgschedule.state.ScheduleData;
import de.ysndr.android.hgschedule.state.State;
import de.ysndr.android.hgschedule.state.models.Entry;
import de.ysndr.android.hgschedule.state.models.Schedule;
import io.reactivecache2.ReactiveCache;
import io.reactivex.Observable;
import timber.log.Timber;

/**
 * Created by yannik on 7/4/17.
 */

public class Reactions {

    // used in a flatMap operation
    public static Observable<State> reload(
        RxSharedPreferences preferences,
        RemoteDataService remote,
        ReactiveCache cache) {

        return RxComprehensions.doFlatMap(
            () -> AuthFunc.login$(preferences),
            RxPartialFunction.apply(DataFunc::schedule, remote, cache),
            (login, schedule) -> Observable
                .just(State.data(ScheduleData.of(schedule, TransfFunc.emptyTransformationSet())))
                .doOnNext(n -> Timber.d("loaded schedule")))

            // initial value
            .startWith(State.empty(Empty.of().withLoading(true)))

            // Logs
            .doOnError(e -> Timber.d("an error occuren during refresh: %s", e.getCause()))
            .onErrorReturn(e -> State.error(Error.of(e)));
    }

    /*
    * 1. creates a new Entry, filtering transformation
    * 2. toggles this transformation on the state object
    * */
    public static State filter(Entry entry, State state) {

        Transformation<Schedule> transformation = TransfFunc.createEntryFilter(entry);
        return state.union().join(
            State::error,
            data -> State.data(data.withTransformations(
                TransfFunc.toggleTransf(transformation, data.transformations()))),
            State::entryDialogData,
            State::empty);
    }
}
