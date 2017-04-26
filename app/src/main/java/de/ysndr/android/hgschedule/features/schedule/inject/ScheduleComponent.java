package de.ysndr.android.hgschedule.features.schedule.inject;

import javax.inject.Singleton;

import dagger.Component;
import de.ysndr.android.hgschedule.features.schedule.view.ScheduleListFragment;
import de.ysndr.android.hgschedule.features.schedule.view.SchoolSelectionPreference;
import de.ysndr.android.hgschedule.modules.AppModule;
import de.ysndr.android.hgschedule.modules.RetrofitModule;
import de.ysndr.android.hgschedule.ui.MainActivity;

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

    void inject(SchoolSelectionPreference preference);
}
