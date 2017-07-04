package de.ysndr.android.hgschedule.inject;

import android.app.Application;

import com.google.gson.Gson;

import dagger.Component;
import de.ysndr.android.hgschedule.MainActivity;
import de.ysndr.android.hgschedule.inject.scopes.ActivityScope;

/**
 * Created by yannik on 7/4/17.
 */

@ActivityScope
@Component(dependencies = {AppComponent.class})
public interface ActivityComponent {
    void inject(MainActivity activity);
    Application application();
    Gson gson();
}
