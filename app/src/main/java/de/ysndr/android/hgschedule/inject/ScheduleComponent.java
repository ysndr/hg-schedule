package de.ysndr.android.hgschedule.inject;

import dagger.Component;
import de.ysndr.android.hgschedule.inject.modules.DataServiceModule;
import de.ysndr.android.hgschedule.inject.modules.RetrofitModule;
import de.ysndr.android.hgschedule.inject.modules.StateModule;
import de.ysndr.android.hgschedule.inject.scopes.ScheduleScope;
import de.ysndr.android.hgschedule.view.ScheduleListFragment;
import de.ysndr.android.hgschedule.view.widget.preferences.SchoolPreference;
import de.ysndr.android.hgschedule.view.widget.preferences.SchoolPreferenceActivity;
import de.ysndr.android.hgschedule.view.widget.preferences.SchoolSelectionPreference;

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
    void inject(SchoolPreferenceActivity preferenceActivity);
    void inject(ScheduleListFragment fragment);

    void inject(SchoolSelectionPreference dialog);
    void inject(SchoolPreference preference);
}
