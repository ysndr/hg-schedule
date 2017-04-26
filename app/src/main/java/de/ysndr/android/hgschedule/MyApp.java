package de.ysndr.android.hgschedule;

import android.app.Application;
import android.content.Context;

import de.ysndr.android.hgschedule.features.schedule.inject.DaggerScheduleComponent;
import de.ysndr.android.hgschedule.features.schedule.inject.DataServiceModule;
import de.ysndr.android.hgschedule.features.schedule.inject.ScheduleComponent;
import de.ysndr.android.hgschedule.modules.AppModule;
import de.ysndr.android.hgschedule.modules.RetrofitModule;
import de.ysndr.android.hgschedule.ui.AppComponent;
import de.ysndr.android.hgschedule.ui.DaggerAppComponent;
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
                    .retrofitModule(new RetrofitModule(BuildConfig.SERVER_URL))
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
