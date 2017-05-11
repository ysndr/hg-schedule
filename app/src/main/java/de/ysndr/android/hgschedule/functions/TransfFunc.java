package de.ysndr.android.hgschedule.functions;

import com.pacoworks.rxcomprehensions.RxComprehensions;
import com.pacoworks.rxfunctions.RxFunctions;
import com.pacoworks.rxpartialapplication.RxPartialFunc;
import com.pacoworks.rxtuples.RxTuples;

import org.javatuples.Pair;

import de.ysndr.android.hgschedule.functions.models.ImmutableTransformation;
import de.ysndr.android.hgschedule.functions.models.Transformation;
import de.ysndr.android.hgschedule.state.ScheduleData;
import de.ysndr.android.hgschedule.state.State;
import de.ysndr.android.hgschedule.state.models.Entry;
import de.ysndr.android.hgschedule.state.models.ImmutableEntry;
import de.ysndr.android.hgschedule.state.models.ImmutableSchedule;
import de.ysndr.android.hgschedule.state.models.Schedule;
import fj.Ord;
import fj.data.List;
import fj.data.Set;
import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;
import timber.log.Timber;

/**
 * Created by yannik on 5/8/17.
 */

public class TransfFunc {

    public static <O> Func2<Set<Transformation<O>>,O, O> applyTransformations() {
        return (set, obj) -> set.toList()
            .foldLeft((schedule, transformation) ->
                transformation.transform().call(schedule), obj);
    }

    public static Observable<State> combineStateTransf(
        Observable<State> state$,
        Observable<Set<Transformation<Schedule>>> transf$) {
        return RxComprehensions.doFlatMap(
            () -> Observable.combineLatest( // when any changes
                transf$,
                state$.distinctUntilChanged(),
                RxTuples.toPair())

                .doOnNext(__ -> Timber.d("transformers or state changed")),

            tuple -> Observable.just(tuple.getValue0()),
            (tuple, trans) ->  Observable.just(tuple.getValue1()),

            (__, trans, state) -> Observable.just(
                state.union().join(
                    State::error,
                    data -> State.data(ScheduleData.copyOf(data)
                        .withSchedule(TransfFunc.<Schedule>applyTransformations()
                            .call(trans, data.schedule()))),
                    State::empty)));
    }


    public static <O> Func2<
        Transformation<O>,
        Set<Transformation<O>>,
        Set<Transformation<O>>> toggleTransf() {
        return (trans, set) -> {
            Timber.d("toggling `%s` %s",
                trans,
                set.member(trans) ? "off" : "on");

//            Timber.d("new transformers: %s", set.member(trans)
//                ? set.delete(trans)
//                : set.insert(trans));

            return set.member(trans)
                ? set.delete(trans)
                : set.insert(trans);
        };
    }


    public static Observable.Transformer<Schedule, Pair<Set<Transformation<Schedule>>, Schedule>> prependEmptyTransformations() {
        return source -> source.map(schedule ->
            Pair.with(Set.empty(Ord.hashEqualsOrd()), schedule));
    }

    private static Func1<Entry, Transformation<Schedule>> createFilterEntry() {
        return (entry) -> {
            Transformation<Schedule> t =  ImmutableTransformation.of(
                "entry_filter_" + entry.id(),
                schedule -> ImmutableSchedule.copyOf(schedule)
                    .withEntries(List.iterableList(schedule.entries())
                        .map(lentry -> lentry.equals(entry)
                            ? ImmutableEntry.copyOf(lentry).withSubstitutes()
                            : lentry)));
            Timber.d("new transformation with seed %s", t._seed());
            return t;
        };
    }

    public static Func2<
        Set<Transformation<Schedule>>,
        Entry,
        Set<Transformation<Schedule>>> toggleCreateFilter(){
        return (set, entry) -> RxFunctions.chain(
            TransfFunc.createFilterEntry(),
            RxPartialFunc.applyEnd(toggleTransf(), set)).call(entry);
    }
}
