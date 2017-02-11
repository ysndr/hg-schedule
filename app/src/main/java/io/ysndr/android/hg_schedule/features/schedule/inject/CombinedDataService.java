package io.ysndr.android.hg_schedule.features.schedule.inject;

import java.util.List;

import javax.inject.Inject;

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
    public CombinedDataService(RemoteDataService remoteDataService) {
        mRemoteDataService = remoteDataService;
    }

    public Observable<Schedule> getData$() {
        return mRemoteDataService.getScheduleEntries("hg-bi-sek1")
                .subscribeOn(Schedulers.io())
                .doOnNext(_void_ -> Timber.d("Incomming Schedule"));
        // TODO: caching & database
    }

    // TODO: Move this to its own service
    public Observable<List<School>> getSchools$() {
        return mRemoteDataService.getSchools()
                .subscribeOn(Schedulers.io())
                .doOnNext(_void_ -> Timber.d("Schools..."));
    }


}
