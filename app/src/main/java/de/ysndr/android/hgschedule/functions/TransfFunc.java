package de.ysndr.android.hgschedule.functions;

import de.ysndr.android.hgschedule.functions.models.ImmutableTransformation;
import de.ysndr.android.hgschedule.functions.models.Transformation;
import de.ysndr.android.hgschedule.state.ScheduleData;
import de.ysndr.android.hgschedule.state.State;
import de.ysndr.android.hgschedule.state.models.Entry;
import de.ysndr.android.hgschedule.state.models.ImmutableEntry;
import de.ysndr.android.hgschedule.state.models.ImmutableSchedule;
import de.ysndr.android.hgschedule.state.models.Schedule;
import fj.Ord;
import fj.data.Set;
import fj.java.util.ListUtil;
import timber.log.Timber;

/**
 * Created by yannik on 5/8/17.
 */

public class TransfFunc {

    private static <O> O applyTransformations(Set<Transformation<O>> set, O obj) {
        return set.toList()
            .foldLeft((schedule, transformation) -> {
                try {
                    return transformation.transform().apply(schedule);
                } catch (Exception e) {
                    return null; // TODO: ???s
                }
            }, obj);
    }

    public static State transformState(
        State state,
        Set<Transformation<Schedule>> transformations) {

        return state.union().join(
            State::error,
            data -> State.data(
                ScheduleData
                    .copyOf(data)
                    .withSchedule(applyTransformations(transformations, data.schedule()))),
            State::empty);
    }

    public static <O> Set<Transformation<O>> toggleTransf(Transformation<O> transformation,
                                                          Set<Transformation<O>> set) {

        Timber.d("toggling `%s` %s",
            transformation,
            set.member(transformation) ? "off" : "on");

        return set.member(transformation)
            ? set.delete(transformation)
            : set.insert(transformation);
    }

    public static Transformation<Schedule> createEntryFilter(Entry entry) {

        Transformation<Schedule> t = ImmutableTransformation.of(
            "entry_filter_" + entry.id(),
            // filter function
            schedule -> ImmutableSchedule.copyOf(schedule).withEntries(ListUtil.map(
                schedule.entries(),
                lentry -> lentry.equals(entry)
                    ? ImmutableEntry.copyOf(lentry).withSubstitutes()
                    : lentry)));

        Timber.d("new transformation with seed %s", t._seed());
        return t;
    }

    public static <O> Set<Transformation<O>> emptyTransformationSet() {
        return Set.empty(Ord.hashEqualsOrd());
    }

}
