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
import io.reactivex.Observable;
import timber.log.Timber;

/**
 * Created by yannik on 5/8/17.
 */

public class TransfFunc {

    private static <O> O applyTransformations(Set<Transformation<O>> set, O obj) {
        return Observable.fromIterable(set)
            .map(Transformation::transform)
            .doOnNext(__ -> Timber.d("about to scan object"))
            .scan(obj, (o, func) -> func.apply(o))
            .lastElement().blockingGet(obj);
    }

    public static ScheduleData transformState(ScheduleData data) {
        return data.withSchedule(applyTransformations(data.transformations(), data.schedule()));
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
            schedule -> {
                Timber.d("applying transformation");

                return ImmutableSchedule.copyOf(schedule).withEntries(ListUtil.map(
                    schedule.entries(),
                    lentry -> lentry.equals(entry)
                        ? ImmutableEntry.copyOf(lentry).withSubstitutes()
                        : lentry));
            });

        Timber.d("new transformation with seed %s", t._seed());
        return t;
    }

    public static <O> Set<Transformation<O>> emptyTransformationSet() {
        return Set.empty(Ord.hashEqualsOrd());
    }

}
