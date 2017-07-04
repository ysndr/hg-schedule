package de.ysndr.android.hgschedule.inject.modules;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dagger.Module;
import dagger.Provides;
import de.ysndr.android.hgschedule.inject.scopes.CommonsScope;
import de.ysndr.android.hgschedule.state.models.GsonAdaptersModels;

/**
 * Created by yannik on 7/4/17.
 */
@Module
public class GsonModule {
    @Provides
    @CommonsScope
    public Gson provideGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder
            .registerTypeAdapterFactory(new GsonAdaptersModels())
            .create();
        return gson;
    }
}

