package de.ysndr.android.hgschedule.inject.modules;

import android.app.Application;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import de.ysndr.android.hgschedule.inject.scopes.ApplicationScope;

/**
 * Created by yannik on 10/8/16.
 */
@Module
public class AppModule {

    private Application mApplication;

    public AppModule(Application application) {
        mApplication = application;
    }

    @Provides
    @ApplicationScope
    Application providesApplication() {
        return mApplication;
    }
}
