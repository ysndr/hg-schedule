package de.ysndr.android.hgschedule.functions;

import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.pacoworks.rxcomprehensions.RxComprehensions;
import com.pacoworks.rxfunctions2.RxFunctions;
import com.pacoworks.rxpartialapplication2.RxPartialFunction;

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
import fj.data.Set;
import io.reactivecache2.ReactiveCache;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
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
            (login, schedule) -> Observable.just(State.data(ScheduleData.of(schedule))))

            // initial value
            .startWith(State.empty(Empty.of().withLoading(true)))

            // Logs
            .doOnNext(n -> Timber.d("loaded schedule"))
            .doOnError(e -> Timber.d("an error occuren during refresh %s", e))
            .onErrorReturn(e -> State.error(Error.of(e)));
    }

    // used in a map() operation
    public static Function<
        Triplet<Entry, Set<Transformation<Schedule>>, State>,
        Pair<Set<Transformation<Schedule>>, State>> filter() {

        return (triplet) -> {
            // inputs unwrapped
            final Entry entry = triplet.getValue0();
            final Set<Transformation<Schedule>> transformations = triplet.getValue1();
            final State state = triplet.getValue2(); // should always be the original untouched state

            Function<Entry, Pair<Set<Transformation<Schedule>>, State>> filterer = RxFunctions.chain(
                // first: create transformation
                TransfFunc::createEntryFilter,
                // second toggle transformation
                RxPartialFunction.applyEnd(TransfFunc::toggleTransf, transformations),
                // third apply transformation set on input state
                (transformations_) -> Pair.with(
                    transformations_,
                    TransfFunc.transformState(state, transformations_)));

            return filterer.apply(entry);
        };
    }

}
