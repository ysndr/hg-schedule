package de.ysndr.android.hgschedule.features.schedule.middleware;


import org.immutables.value.Value;
import org.javatuples.Pair;
import org.javatuples.Triplet;

import fj.Ord;
import fj.data.List;
import fj.data.Set;
import de.ysndr.android.hgschedule.features.schedule.models.Entry;
import de.ysndr.android.hgschedule.features.schedule.models.ImmutableEntry;
import de.ysndr.android.hgschedule.features.schedule.models.ImmutableSchedule;
import de.ysndr.android.hgschedule.features.schedule.models.Schedule;
import rx.Observable;
import rx.functions.Func1;
import timber.log.Timber;

/**
 * Created by yannik on 4/3/17.
 */

public class TransformationMiddleware {
    public Observable.Transformer<Pair<Set<Transformation<Schedule>>, Schedule>, Schedule> applyTransformations() {
        return source -> source
                .map(pair -> pair.getValue0().toList()
                        .foldLeft((schedule, transformation) -> transformation
                                .transform().call(schedule), pair.getValue1())
                );
    }


    public Observable.Transformer<Schedule, Pair<Set<Transformation<Schedule>>, Schedule>> prependEmptyTransformations() {
        return source -> source.map(schedule ->
                Pair.with(Set.empty(Ord.hashEqualsOrd()), schedule));
    }


    public <O> Observable.Transformer<Triplet<Transformation<O>, Set<Transformation<O>>, O>, Pair<Set<Transformation<O>>, O>> toggleTransformation() {

        return source -> source.map(triplet -> {
            Timber.d("toggling `%s` %s",
                    triplet.getValue0(),
                    triplet.getValue1().member(triplet.getValue0()) ? "off" : "on");

            Set<Transformation<O>> set = triplet.getValue1().member(triplet.getValue0())
                    ? triplet.getValue1().delete(triplet.getValue0())
                    : triplet.getValue1().insert(triplet.getValue0());

            return Pair.with(set, triplet.getValue2());
        });
    }

    public Transformation<Schedule> filterEntry(Entry entry) {
        return ImmutableTransformation.of(
                "entry_filter_" + entry.id(),
                schedule -> ImmutableSchedule.copyOf(schedule)
                        .withEntries(List.iterableList(schedule.entries())
                                .map(lentry -> lentry.equals(entry)
                                        ? ImmutableEntry.copyOf(lentry).withSubstitutes()
                                        : lentry)));
    }

    @Value.Immutable
    @Value.Style(allParameters = true)
    public interface Transformation<T> {
        String _seed();

        @Value.Auxiliary
        Func1<T, T> transform();
    }
}
