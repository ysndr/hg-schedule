package de.ysndr.android.hgschedule.inject.modules;

import com.jakewharton.rxrelay.BehaviorRelay;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.ysndr.android.hgschedule.state.State;

/**
 * Created by yannik on 7/3/17.
 */

@Module
public class StateModule {

    @Provides
    @Singleton
    BehaviorRelay<State> provideState$() {
        return BehaviorRelay.create(State.empty());
    }


}
