package io.ysndr.android.hg_schedule.features.schedule.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.ysndr.android.hg_schedule.features.schedule.data.RemoteDataService;
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
}
