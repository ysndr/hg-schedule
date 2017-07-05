package de.ysndr.android.hgschedule.inject.modules;

import com.jakewharton.rxrelay.BehaviorRelay;

import dagger.Module;
import dagger.Provides;
import de.ysndr.android.hgschedule.functions.TransfFunc;
import de.ysndr.android.hgschedule.functions.models.Transformation;
import de.ysndr.android.hgschedule.inject.scopes.ScheduleScope;
import de.ysndr.android.hgschedule.state.State;
import de.ysndr.android.hgschedule.state.models.Schedule;
import fj.data.Set;

/**
 * Created by yannik on 7/3/17.
 */

@Module
public class StateModule {

    @Provides
    @ScheduleScope
    BehaviorRelay<State> provideState$() {
        return BehaviorRelay.create(State.empty());
    }

    @Provides
    @ScheduleScope
    BehaviorRelay<Set<Transformation<Schedule>>> provideFilters$() {
        return BehaviorRelay.create(TransfFunc.<Schedule>emptyTransformationSet());
    }


}
