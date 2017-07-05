package de.ysndr.android.hgschedule.functions;

import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.pacoworks.rxcomprehensions.RxComprehensions;
import com.pacoworks.rxfunctions.RxFunctions;
import com.pacoworks.rxpartialapplication.RxPartialFunc;

import org.javatuples.Pair;
import org.javatuples.Triplet;

import de.ysndr.android.hgschedule.functions.models.Transformation;
import de.ysndr.android.hgschedule.inject.RemoteDataService;
import de.ysndr.android.hgschedule.state.Empty;
import de.ysndr.android.hgschedule.state.Error;
import de.ysndr.android.hgschedule.state.ScheduleData;
import de.ysndr.android.hgschedule.state.State;
import de.ysndr.android.hgschedule.state.models.Entry;
import de.ysndr.android.hgschedule.state.models.Schedule;
import fj.Unit;
import fj.data.Set;
import io.reactivecache.ReactiveCache;
import rx.Observable;
import rx.functions.Func1;
import timber.log.Timber;

/**
 * Created by yannik on 7/4/17.
 */

public class Reactions {

    // used in a flatMap operation
    public static Func1<Unit, Observable<State>> reload(
        RxSharedPreferences preferences,
        RemoteDataService remote,
        ReactiveCache cache) {

        return (unit) -> RxComprehensions
            .doFlatMap(
                AuthFunc.login$(preferences),
                DataFunc.schedule(remote, cache),
                (login, schedule) -> Observable.just(State.data(ScheduleData.of(schedule))))
            // initial value
            .startWith(State.empty(Empty.of().withLoading(true)))
            // Logs
            .doOnNext(__ -> Timber.d("loaded schedule"))
            .doOnError(e -> Timber.d("an error occuren during refresh"))
            // Error catching
            .onErrorReturn(e -> State.error(Error.of(e)));
    }

    // used in a map() operation
    public static Func1<
        Triplet<Entry, Set<Transformation<Schedule>>, State>,
        Pair<Set<Transformation<Schedule>>, State>> filter() {

        return (triplet) -> {
            // inputs unwrapped
            final Entry entry = triplet.getValue0();
            final Set<Transformation<Schedule>> transformations = triplet.getValue1();
            final State state = triplet.getValue2(); // should always be the original untouched state

            Func1<Entry, Pair<Set<Transformation<Schedule>>, State>> filterer = RxFunctions.chain(
                // first: create transformation
                TransfFunc::createEntryFilter,
                // second toggle transformation
                RxPartialFunc.applyEnd(TransfFunc::toggleTransf, transformations),
                // third apply transformation set on input state
                (transformations_) -> Pair.with(
                    transformations_,
                    TransfFunc.transformState(state, transformations_)));

            return filterer.call(entry);
        };
    }

}
