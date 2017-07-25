package de.ysndr.android.hgschedule.inject.modules;

import com.jakewharton.rxrelay2.BehaviorRelay;

import dagger.Module;
import dagger.Provides;
import de.ysndr.android.hgschedule.functions.models.Transformation;
import de.ysndr.android.hgschedule.inject.scopes.ScheduleScope;
import de.ysndr.android.hgschedule.state.State;
import de.ysndr.android.hgschedule.state.models.Schedule;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;

/**
 * Created by yannik on 7/3/17.
 */

@Module
public class StateModule {

    @Provides
    @ScheduleScope
    BehaviorRelay<State> provideState$() {
        return BehaviorRelay.createDefault(State.empty());
    }

    @Provides
    @ScheduleScope
    BehaviorRelay<Set<Transformation<Schedule>>> provideFilters$() {
        return BehaviorRelay.createDefault(HashSet.empty());
    }


}
