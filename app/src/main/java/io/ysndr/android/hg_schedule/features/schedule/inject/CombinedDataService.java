package io.ysndr.android.hg_schedule.features.schedule.inject;

import android.util.Base64;

import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.pacoworks.rxobservablediskcache.RxObservableDiskCache;
import com.pacoworks.rxobservablediskcache.policy.TimeAndVersionPolicy;

import java.util.List;

import javax.inject.Inject;

import io.ysndr.android.hg_schedule.BuildConfig;
import io.ysndr.android.hg_schedule.features.schedule.models.Schedule;
import io.ysndr.android.hg_schedule.features.schedule.models.School;
import rx.Observable;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by yannik on 10/10/16.
 */

public class CombinedDataService {
    @Inject
    RemoteDataService mRemoteDataService;
    @Inject
    RxSharedPreferences preferences;
    @Inject
    RxObservableDiskCache diskCache;

    @Inject
    public CombinedDataService(RemoteDataService remoteDataService) {
        mRemoteDataService = remoteDataService;
    }

    public Observable<Schedule> getData$(String schoolid) {
        String auth = getAuth();

        Observable<Schedule> remote$ = mRemoteDataService.getScheduleEntries(schoolid, auth);
        Observable<Schedule> cache$ = RxObservableDiskCache
                .transform(
                        remote$.toSingle(),
                        schoolid,
                        TimeAndVersionPolicy.create(BuildConfig.VERSION_CODE),
                        TimeAndVersionPolicy.validate(1000 * 60 * 5, BuildConfig.VERSION_CODE))
                .doOnNext(cached -> Timber.d("Incomming Schedule from : %s", cached.isFromDisk ? "disk" : "remote"))
                .map(cached -> cached.value)
                .distinctUntilChanged()
                .subscribeOn(Schedulers.io());

        return remote$.subscribeOn(Schedulers.io()).observeOn(Schedulers.io()).share();
    }

    private String getAuth() {
        String user = preferences.getString("user", "").get();
        String pass = preferences.getString("pass", "").get();

        String auth;
        auth = user.concat(":").concat(pass);
        auth = Base64.encodeToString(auth.getBytes(), Base64.NO_WRAP);
        auth = "BASIC ".concat(auth);

        return auth;
    }


    // TODO: Move this to its own service
    public Observable<List<School>> getSchools$() {
        return mRemoteDataService.getSchools()
                .subscribeOn(Schedulers.io())
                .doOnNext(_void_ -> Timber.d("Schools..."));
    }


}
