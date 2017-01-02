package io.ysndr.android.hg_schedule.features.schedule.inject;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
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

//    @Provides
//    @Singleton
//    CombinedDataService provideCombinedDataService(RemoteDataService remote) {
//        return new CombinedDataService(remote);
//    }

}
