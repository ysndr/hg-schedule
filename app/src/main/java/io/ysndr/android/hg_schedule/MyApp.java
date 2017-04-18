package io.ysndr.android.hg_schedule;

import android.app.Application;
import android.content.Context;

import io.ysndr.android.hg_schedule.features.schedule.inject.DaggerScheduleComponent;
import io.ysndr.android.hg_schedule.features.schedule.inject.DataServiceModule;
import io.ysndr.android.hg_schedule.features.schedule.inject.ScheduleComponent;
import io.ysndr.android.hg_schedule.modules.AppModule;
import io.ysndr.android.hg_schedule.modules.RetrofitModule;
import io.ysndr.android.hg_schedule.ui.AppComponent;
import io.ysndr.android.hg_schedule.ui.DaggerAppComponent;
import timber.log.Timber;

/*import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.IoniconsModule;*/

/**
 * Created by yannik on 10/8/16.
 */
public class MyApp extends Application {
    private ScheduleComponent mScheduleComponent;
    private AppComponent mAppComponent;

    public static ScheduleComponent getScheduleComponent(Context context) {
        MyApp app = (MyApp) context.getApplicationContext();
        if (app.mScheduleComponent == null) {
            app.mScheduleComponent = DaggerScheduleComponent.builder()
                    .retrofitModule(new RetrofitModule("http://192.168.1.106:3000/api/"))
                    .dataServiceModule(new DataServiceModule())
                    .appModule(new AppModule((MyApp) context.getApplicationContext()))
                    .build();

        }
        return app.mScheduleComponent;
    }

    public static AppComponent getAppComponent(Context context) {
        MyApp app = (MyApp) context.getApplicationContext();
        if (app.mAppComponent == null) {
            app.mAppComponent = DaggerAppComponent.create();
        }
        return app.mAppComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();

//        Iconify.with(new IoniconsModule());
        Timber.plant(new Timber.DebugTree());
//        RxPaperBook.init(this);
    }

}
