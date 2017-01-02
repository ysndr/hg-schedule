package io.ysndr.android.hg_schedule.features.schedule.inject;

import javax.inject.Inject;

import io.ysndr.android.hg_schedule.features.schedule.models.Schedule;
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
        return mRemoteDataService.getData()
                .subscribeOn(Schedulers.io())
                .doOnNext(_void_ -> Timber.d("Hello"));
        // TODO: caching & database
    }

}
