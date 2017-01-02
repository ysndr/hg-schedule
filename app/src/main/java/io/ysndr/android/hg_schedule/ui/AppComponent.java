package io.ysndr.android.hg_schedule.ui;

/**
 * Created by yannik on 1/2/17.
 */

import javax.inject.Singleton;

import dagger.Component;
import io.ysndr.android.hg_schedule.modules.AppModule;
import io.ysndr.android.hg_schedule.modules.RetrofitModule;

@Singleton
@Component(
        modules = {
                AppModule.class,
                RetrofitModule.class
        })
public interface AppComponent {
    void inject(MainActivity activity);

}
