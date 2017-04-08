package io.ysndr.android.hg_schedule.features.schedule.middleware;


import com.pacoworks.rxtuples.RxTuples;

import org.immutables.value.Value;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.javatuples.Tuple;

import fj.Equal;
import fj.Ord;
import fj.data.List;
import fj.data.Set;
import io.ysndr.android.hg_schedule.features.schedule.models.Entry;
import io.ysndr.android.hg_schedule.features.schedule.models.ImmutableSchedule;
import io.ysndr.android.hg_schedule.features.schedule.models.Schedule;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by yannik on 4/3/17.
 */

public class FilterMiddleware {
    public Observable.Transformer<Pair<Set<Transformation<Schedule>>, Schedule>, Schedule> applyTransformations() {
        return source -> source
                .map(pair -> pair.getValue0().toList()
                        .foldLeft((schedule, transformation) -> transformation
                                .transform().call(schedule), pair.getValue1())
                );
    }


    public Observable.Transformer<Schedule, Pair<Set<Transformation<Schedule>>, Schedule>> appendEmptyTransformations() {
        return source -> source.map(schedule ->
                Pair.with(Set.empty(Ord.hashEqualsOrd()), schedule));
    }


    public Observable.Transformer<Triplet<Transformation<?>, Set<Transformation<?>>, ?>, Pair<Set<Transformation<?>>, ?>> toggleTransformation() {

        return source -> source.map(triplet -> {
            Set<Transformation<?>> set = triplet.getValue1().member(triplet.getValue0())
                    ? triplet.getValue1().delete(triplet.getValue0())
                    : triplet.getValue1().insert(triplet.getValue0());

            return Pair.with(set, triplet.getValue2());
        });
    }

    Transformation<Schedule> filterEntry(Entry entry) {
        return ImmutableTransformation.of(
                "entry_filter_" + entry.id(),
                schedule -> ImmutableSchedule.copyOf(schedule)
                        .withEntries(List.iterableList(schedule.entries())
                                .delete(entry, Equal.anyEqual())));
    }

    @Value.Immutable
    @Value.Style(allParameters = true)
    interface Transformation<T> {
        String _seed();

        @Value.Auxiliary
        Func1<T, T> transform();
    }
}
