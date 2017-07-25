package de.ysndr.android.hgschedule.functions;

import de.ysndr.android.hgschedule.functions.models.ImmutableTransformation;
import de.ysndr.android.hgschedule.functions.models.Transformation;
import de.ysndr.android.hgschedule.state.State;
import de.ysndr.android.hgschedule.state.models.Entry;
import de.ysndr.android.hgschedule.state.models.ImmutableEntry;
import de.ysndr.android.hgschedule.state.models.ImmutableSchedule;
import de.ysndr.android.hgschedule.state.models.Schedule;
import io.reactivex.Observable;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Set;
import timber.log.Timber;

/**
 * Created by yannik on 5/8/17.
 */

public class TransfFunc {

    private static <O> O applyTransformations(Set<Transformation<O>> set, O obj) {
        return Observable.fromIterable(set)
            .map(Transformation::transform)
            .scan(obj, (o, func) -> func.apply(o))
            .lastElement().blockingGet(obj);
    }

    public static State transformState(State state) {
        return state.union().join(
            State::error,
            data -> State.data(
                data.withSchedule(applyTransformations(data.transformations(), data.schedule()))),
            State::empty);
    }

    public static <O> Set<Transformation<O>> toggleTransf(Transformation<O> transformation,
                                                          Set<Transformation<O>> set) {

        Timber.d("toggling `%s` %s",
            transformation,
            set.contains(transformation) ? "off" : "on");

        return set.contains(transformation)
            ? set.remove(transformation)
            : set.add(transformation);
    }

    public static Transformation<Schedule> createEntryFilter(Entry entry) {
        Transformation<Schedule> t = ImmutableTransformation.of(
            "entry_filter_" + entry.id(),
            // filter function
            schedule -> ImmutableSchedule.copyOf(schedule)
                .withEntries(List.ofAll(schedule.entries())
                    .map(lentry -> lentry.equals(entry)
                    ? ImmutableEntry.copyOf(lentry).withSubstitutes()
                    : lentry)));

        Timber.d("new transformation with seed %s", t._seed());
        return t;
    }

    public static <O> Set<Transformation<O>> emptyTransformationSet() {
        return HashSet.empty();
    }

}
