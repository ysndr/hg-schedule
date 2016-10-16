package io.ysndr.android.hg_schedule.features.schedule.components;

import javax.inject.Singleton;

import dagger.Component;
import io.ysndr.android.hg_schedule.features.schedule.modules.DataServiceModule;
import io.ysndr.android.hg_schedule.features.schedule.views.fragments.ScheduleListFragment;
import io.ysndr.android.hg_schedule.modules.AppModule;
import io.ysndr.android.hg_schedule.modules.RetrofitModule;
import io.ysndr.android.hg_schedule.ui.MainActivity;

/**
 * Created by yannik on 8/22/16.
 */
@Singleton
@Component(modules = {
        AppModule.class,
        RetrofitModule.class,
        DataServiceModule.class
})
public interface ScheduleComponent {
    void inject(MainActivity activity);

    void inject(ScheduleListFragment fragment);
}
