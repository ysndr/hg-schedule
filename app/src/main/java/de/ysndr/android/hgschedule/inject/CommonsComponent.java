package de.ysndr.android.hgschedule.inject;

import com.google.gson.Gson;

import dagger.Component;
import dagger.Subcomponent;
import de.ysndr.android.hgschedule.inject.modules.AppModule;
import de.ysndr.android.hgschedule.inject.modules.GsonModule;
import de.ysndr.android.hgschedule.inject.scopes.CommonsScope;

/**
 * Created by yannik on 7/4/17.
 */
@CommonsScope
@Component(modules = {GsonModule.class})
public interface CommonsComponent {
    Gson gson();

//    AppComponent appComponent(AppModule appModule);
}
