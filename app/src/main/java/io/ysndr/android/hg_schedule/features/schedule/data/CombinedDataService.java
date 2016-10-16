package io.ysndr.android.hg_schedule.features.schedule.data;

import javax.inject.Inject;

import io.ysndr.android.hg_schedule.features.schedule.models.Schedule;
import rx.Observable;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * Created by yannik on 10/10/16.
 */

public class CombinedDataService {
    @Inject
    RemoteDataService mRemoteDataService;

    private PublishSubject<Schedule> value$;
    private PublishSubject<Throwable> error$;

    @Inject
    public CombinedDataService(RemoteDataService remoteDataService) {
        this.mRemoteDataService = remoteDataService;
        value$ = PublishSubject.create();
        error$ = PublishSubject.create();
    }

    public void getSchedule() {
        mRemoteDataService.getData()
                .subscribeOn(Schedulers.io())
                .distinctUntilChanged()
                .first()
                .subscribe(value$::onNext, error$::onNext);

//        Observable<Schedule> remote$ = ...
//        Observable<Schedule> disk$ = Observable.empty();
//
//        remote$.merge(disk§).distinctUntilChanged()
//                .map(mDiskDataService::writeToDisk)
    }

    public Observable<Schedule> getValue$() {
        return value$;
    }

    public Observable<Throwable> getError$() {
        return error$;
    }

}
