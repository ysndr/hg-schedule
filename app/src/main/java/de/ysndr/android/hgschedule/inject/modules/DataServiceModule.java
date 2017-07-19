package de.ysndr.android.hgschedule.inject.modules;

import android.app.Application;
import android.support.v7.preference.PreferenceManager;

import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dagger.Module;
import dagger.Provides;
import de.ysndr.android.hgschedule.inject.RemoteDataService;
import de.ysndr.android.hgschedule.inject.scopes.ScheduleScope;
import de.ysndr.android.hgschedule.state.models.GsonAdaptersModels;
import io.reactivecache2.ReactiveCache;
import io.victoralbertos.jolyglot.GsonSpeaker;
import retrofit2.Retrofit;

/**
 * Created by yannik on 8/22/16.
 */
@Module
public class DataServiceModule {
    @Provides
    @ScheduleScope
    RemoteDataService provideRemoteDataService(Retrofit retrofit) {
        return retrofit.create(RemoteDataService.class);
    }

    @Provides
    @ScheduleScope
    RxSharedPreferences providePreferences(Application app) {
        return RxSharedPreferences.create(PreferenceManager.getDefaultSharedPreferences(app));
    }

    @Provides
    @ScheduleScope
    ReactiveCache provideReactiveCache(Application app) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder
                .registerTypeAdapterFactory(new GsonAdaptersModels())
                .create();

        ReactiveCache reactiveCache = new ReactiveCache.Builder()
                .using(app.getCacheDir(), new GsonSpeaker(gson));

        return reactiveCache;
    }

}
