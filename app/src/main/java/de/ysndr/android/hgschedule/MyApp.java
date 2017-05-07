package de.ysndr.android.hgschedule;

import android.app.Application;
import android.content.Context;

import de.ysndr.android.hgschedule.inject.AppComponent;
import de.ysndr.android.hgschedule.inject.DaggerAppComponent;
import de.ysndr.android.hgschedule.inject.DaggerScheduleComponent;
import de.ysndr.android.hgschedule.inject.ScheduleComponent;
import de.ysndr.android.hgschedule.inject.modules.AppModule;
import de.ysndr.android.hgschedule.inject.modules.DataServiceModule;
import de.ysndr.android.hgschedule.inject.modules.RetrofitModule;
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
