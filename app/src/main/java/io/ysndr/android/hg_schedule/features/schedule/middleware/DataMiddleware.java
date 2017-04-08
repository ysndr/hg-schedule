package io.ysndr.android.hg_schedule.features.schedule.middleware;

import java.util.List;

import javax.inject.Inject;

import de.ysndr.rxvaluestore.RxCacheStore;
import io.ysndr.android.hg_schedule.features.schedule.inject.RemoteDataService;
import io.ysndr.android.hg_schedule.features.schedule.models.Schedule;
import io.ysndr.android.hg_schedule.features.schedule.models.School;
import rx.Observable;

/**
 * Created by yannik on 4/8/17.
 */

public class DataMiddleware {

    @Inject
    RemoteDataService remote;
    @Inject
    RxCacheStore<Schedule, ?> cache;

    public Observable.Transformer<AuthMiddleware.Login, Schedule> schedule() {
        return source -> source.flatMap(login -> Observable
                .concat(cache$(login), remote$(login)).first());
    }

    public Observable.Transformer<?, List<School>> schools() {
        return source -> source.flatMap(__ -> remote.getSchools());
    }


    private Observable<Schedule> cache$(AuthMiddleware.Login login) {
        return cache.withKey(login.school().id())
                .observable().first()
                .onErrorResumeNext(Observable.<Schedule>empty());
    }

    private Observable<Schedule> remote$(AuthMiddleware.Login login) {
        return remote.getScheduleEntries(
                login.school().id(), login.auth())
                .compose(cache.withKey(login.school().id()).update());
    }

}
