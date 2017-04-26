package de.ysndr.android.hgschedule.ui;

/**
 * Created by yannik on 1/2/17.
 */

import javax.inject.Singleton;

import dagger.Component;
import de.ysndr.android.hgschedule.modules.AppModule;
import de.ysndr.android.hgschedule.modules.RetrofitModule;

@Singleton
@Component(
        modules = {
                AppModule.class,
                RetrofitModule.class
        })
public interface AppComponent {
    void inject(MainActivity activity);

}
