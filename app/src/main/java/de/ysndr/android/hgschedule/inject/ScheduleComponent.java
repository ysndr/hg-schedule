package de.ysndr.android.hgschedule.inject;

import javax.inject.Singleton;

import dagger.Component;
import de.ysndr.android.hgschedule.MainActivity;
import de.ysndr.android.hgschedule.inject.modules.AppModule;
import de.ysndr.android.hgschedule.inject.modules.DataServiceModule;
import de.ysndr.android.hgschedule.inject.modules.RetrofitModule;
import de.ysndr.android.hgschedule.view.ScheduleListFragment;
import de.ysndr.android.hgschedule.view.SchoolSelectionPreference;

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
