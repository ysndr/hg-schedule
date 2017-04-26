package de.ysndr.android.hgschedule.features.schedule.inject;

import android.app.Application;
import android.support.v7.preference.PreferenceManager;

import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivecache.ReactiveCache;
import io.victoralbertos.jolyglot.GsonSpeaker;
import de.ysndr.android.hgschedule.features.schedule.models.GsonAdaptersModels;
import retrofit2.Retrofit;

/**
 * Created by yannik on 8/22/16.
 */
@Module
public class DataServiceModule {
    @Provides
    @Singleton
    RemoteDataService provideRemoteDataService(Retrofit retrofit) {
        return retrofit.create(RemoteDataService.class);
    }

    @Provides
    @Singleton
    RxSharedPreferences providePreferences(Application app) {
        return RxSharedPreferences.create(PreferenceManager.getDefaultSharedPreferences(app));
    }

    @Provides
    @Singleton
    ReactiveCache provideReactiveCache(Application app) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder
                .registerTypeAdapterFactory(new GsonAdaptersModels())
                .create();

        ReactiveCache reactiveCache = new ReactiveCache.Builder()
                .using(app.getCacheDir(), new GsonSpeaker(gson));

        return reactiveCache;
    }


//    @Provides
//    @Singleton
//    CombinedDataService provideCombinedDataService(RemoteDataService remote) {
//        return new CombinedDataService(remote);
//    }

}
