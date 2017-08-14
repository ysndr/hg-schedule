package de.ysndr.android.hgschedule.inject;

import dagger.Component;
import de.ysndr.android.hgschedule.inject.modules.DataServiceModule;
import de.ysndr.android.hgschedule.inject.modules.RetrofitModule;
import de.ysndr.android.hgschedule.inject.modules.StateModule;
import de.ysndr.android.hgschedule.inject.scopes.ScheduleScope;
import de.ysndr.android.hgschedule.view.schedulelist.ScheduleListController;
import de.ysndr.android.hgschedule.view.schedulelist.ScheduleListPresenter;
import de.ysndr.android.hgschedule.view.widget.preferences.SchoolPreference;

/**
 * Created by yannik on 8/22/16.
 */
@ScheduleScope
@Component(
    dependencies = {ActivityComponent.class},
    modules = {
        RetrofitModule.class,
        DataServiceModule.class,
        StateModule.class
    })
public interface ScheduleComponent {
    void inject(ScheduleListController preferenceActivity);
    void inject(SchoolPreference preference);

    ScheduleListPresenter getPresenter();
}
