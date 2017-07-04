package de.ysndr.android.hgschedule.inject;

/**
 * Created by yannik on 1/2/17.
 */

import android.app.Application;

import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Subcomponent;
import de.ysndr.android.hgschedule.MainActivity;
import de.ysndr.android.hgschedule.inject.modules.AppModule;
import de.ysndr.android.hgschedule.inject.modules.DataServiceModule;
import de.ysndr.android.hgschedule.inject.modules.GsonModule;
import de.ysndr.android.hgschedule.inject.modules.RetrofitModule;
import de.ysndr.android.hgschedule.inject.scopes.ApplicationScope;

@ApplicationScope
@Component(dependencies = {CommonsComponent.class}, modules = {AppModule.class})
public interface AppComponent {
    void inject(MainActivity activity);
    Application application();
    Gson gson();
}
