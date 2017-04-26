package de.ysndr.android.hgschedule.features.schedule.middleware;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivecache.ReactiveCache;
import io.rx_cache.RxCacheException;
import de.ysndr.android.hgschedule.features.schedule.inject.RemoteDataService;
import de.ysndr.android.hgschedule.features.schedule.models.Schedule;
import de.ysndr.android.hgschedule.features.schedule.models.School;
import retrofit2.Response;
import rx.Observable;
import timber.log.Timber;

/**
 * Created by yannik on 4/8/17.
 */


public class DataMiddleware {

    public static Observable.Transformer<AuthMiddleware.Login, Schedule> schedule(RemoteDataService remote, ReactiveCache cache) {
        return source -> source
                .doOnNext(__ -> Timber.d("about to load"))
                .flatMap(login -> Observable.concat(cache$(login, cache), remote$(login, remote, cache)).take(1)

//                                .compose(cache.withKey(login.school().id()).update())
                )
                .doOnNext(__ -> Timber.d("loaded"));
    }

    public static Observable.Transformer<?, Response<List<School>>> schools(RemoteDataService remote) {
        return source -> source.flatMap(__ -> remote.getSchools());
    }


    private static Observable<Schedule> cache$(AuthMiddleware.Login login, ReactiveCache cache) {
        return cache.<Schedule>provider().withKey(login.school().id()).read()
                .doOnNext(__ -> Timber.d("from cache"))
                .onErrorResumeNext(error ->
                        (error instanceof RxCacheException)
                                ? Observable.empty()
                                : Observable.error(error));
    }

    private static Observable<Schedule> remote$(AuthMiddleware.Login login, RemoteDataService remote, ReactiveCache cache) {
        return remote.getScheduleEntries(login.school().id(), login.auth())
                .compose(cache.<Schedule>provider().lifeCache(5, TimeUnit.MINUTES).withKey(login.school().id()).replace())
                .doOnNext(__ -> Timber.d("from net"))
                .doOnNext(__ -> cache$(login, cache).count().subscribe(i -> Timber.d("stored: " + ((i == 0) ? "false" : "true"))));
    }

}
