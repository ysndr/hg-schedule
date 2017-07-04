package de.ysndr.android.hgschedule.inject.modules;

import com.google.gson.Gson;

import dagger.Module;
import dagger.Provides;
import de.ysndr.android.hgschedule.inject.scopes.ScheduleScope;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by yannik on 8/21/16.
 */
@Module
public class RetrofitModule {
    private String mBaseUrl;

    public RetrofitModule(String baseUrl) {
        this.mBaseUrl = baseUrl;
    }

    @Provides
    @ScheduleScope
    Retrofit provideRetrofit(Gson gson) {
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(mBaseUrl)
                .build();
        return retrofit;
    }

}
