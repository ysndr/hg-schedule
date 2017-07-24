package de.ysndr.android.hgschedule;

import android.app.Application;
import android.content.Context;

import de.ysndr.android.hgschedule.inject.ActivityComponent;
import de.ysndr.android.hgschedule.inject.AppComponent;
import de.ysndr.android.hgschedule.inject.CommonsComponent;
import de.ysndr.android.hgschedule.inject.DaggerActivityComponent;
import de.ysndr.android.hgschedule.inject.DaggerAppComponent;
import de.ysndr.android.hgschedule.inject.DaggerCommonsComponent;
import de.ysndr.android.hgschedule.inject.DaggerScheduleComponent;
import de.ysndr.android.hgschedule.inject.ScheduleComponent;
import de.ysndr.android.hgschedule.inject.modules.AppModule;
import de.ysndr.android.hgschedule.inject.modules.DataServiceModule;
import de.ysndr.android.hgschedule.inject.modules.GsonModule;
import de.ysndr.android.hgschedule.inject.modules.RetrofitModule;
import timber.log.Timber;

/*import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.IoniconsModule;*/

/**
 * Created by yannik on 10/8/16.
 * // boje^^
 */
public class MyApp extends Application {
    private CommonsComponent mCommonsComponent;
    private AppComponent mAppComponent;
    private ActivityComponent mActivityComponent;
    private ScheduleComponent mScheduleComponent;

    public static ScheduleComponent getScheduleComponent(Context context) {
        MyApp app = (MyApp) context.getApplicationContext();
       if (app.mScheduleComponent == null) {
            app.mScheduleComponent = DaggerScheduleComponent.builder()
                .activityComponent(getActivityComponent(context))
                .retrofitModule(new RetrofitModule(BuildConfig.SERVER_URL))
                .dataServiceModule(new DataServiceModule())
                .build();
        }
        return app.mScheduleComponent;
    }

    public static ActivityComponent getActivityComponent(Context context) {
        MyApp app = (MyApp) context.getApplicationContext();
        if (app.mActivityComponent == null) {
            app.mActivityComponent = DaggerActivityComponent.builder()
                .appComponent(getAppComponent(context))
                .build();
        }
        return app.mActivityComponent;
    }

    public static AppComponent getAppComponent(Context context) {
        MyApp app = (MyApp) context.getApplicationContext();
        if (app.mAppComponent == null) {
            app.mAppComponent = DaggerAppComponent.builder()
                .commonsComponent(getCommonsComponent(context))
                .appModule(new AppModule(app))
                .build();
        }
        return app.mAppComponent;
    }

    public static CommonsComponent getCommonsComponent(Context context) {
        MyApp app = (MyApp) context.getApplicationContext();
        if (app.mCommonsComponent == null) {
            app.mCommonsComponent = DaggerCommonsComponent.builder()
                .gsonModule(new GsonModule())
                .build();
        }
        return app.mCommonsComponent;
    }


    @Override
    public void onCreate() {
        super.onCreate();

//        Iconify.with(new IoniconsModule());
        Timber.plant(new Timber.DebugTree());
//        RxPaperBook.init(this);
    }

}
