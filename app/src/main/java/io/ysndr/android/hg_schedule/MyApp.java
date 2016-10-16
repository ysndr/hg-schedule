package io.ysndr.android.hg_schedule;

import android.app.Application;
import android.content.Context;

import io.ysndr.android.hg_schedule.features.schedule.components.DaggerScheduleComponent;
import io.ysndr.android.hg_schedule.features.schedule.components.ScheduleComponent;
import io.ysndr.android.hg_schedule.features.schedule.modules.DataServiceModule;
import io.ysndr.android.hg_schedule.modules.RetrofitModule;
import timber.log.Timber;

/**
 * Created by yannik on 10/8/16.
 */
public class MyApp extends Application {
    private ScheduleComponent mScheduleComponent;

    public static ScheduleComponent getScheduleComponent(Context context) {
        MyApp app = (MyApp) context.getApplicationContext();
        if (app.mScheduleComponent == null) {
            app.mScheduleComponent = DaggerScheduleComponent.builder()
                    .retrofitModule(new RetrofitModule("http://192.168.178.54:3000/api/school/hg-bi-sek1/"))
                    .dataServiceModule(new DataServiceModule())
                    .build();

        }
        return app.mScheduleComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new Timber.DebugTree());

    }

}
