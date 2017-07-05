package de.ysndr.android.hgschedule.functions;

import java.util.List;
import java.util.concurrent.TimeUnit;

import de.ysndr.android.hgschedule.functions.models.Login;
import de.ysndr.android.hgschedule.inject.RemoteDataService;
import de.ysndr.android.hgschedule.state.models.Schedule;
import de.ysndr.android.hgschedule.state.models.School;
import io.reactivecache.ReactiveCache;
import io.rx_cache.RxCacheException;
import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;
import timber.log.Timber;

/**
 * Created by yannik on 4/8/17.
 */


public class DataFunc {

    public static Func1<Login, Observable<Schedule>> schedule(
            RemoteDataService remote,
            ReactiveCache cache) {

        return (login) -> Observable.concatDelayError(
            cache$(login, cache),
            remote$(login, remote, cache)).take(1)
            // LOGS //
            .doOnError(e -> Timber.d("error getting schedule: %s", e))
            .doOnNext(data -> Timber.d("got data: %s", data));
    }


    public static Func1<?, Observable<Response<List<School>>>> schools(RemoteDataService remote) {
        return __ -> remote.getSchools();
    }

    /*
    * data providers
    * */

    private static Observable<Schedule> cache$(Login login, ReactiveCache cache) {
        Timber.d("requesting cache");

        return cache.<Schedule>provider().withKey(login.school().id()).read()
                .doOnNext(__ -> Timber.d("from cache"))
                .onErrorResumeNext(error ->
                        (error instanceof RxCacheException)
                                ? Observable.empty()
                                : Observable.error(error));
    }

    private static Observable<Schedule> remote$(Login login, RemoteDataService remote, ReactiveCache cache) {
        Timber.d("requesting remote");

        return remote.getScheduleEntries(login.school().id(), login.auth())
                .compose(cache.<Schedule>provider()
                        .lifeCache(5, TimeUnit.MINUTES)
                        .withKey(login.school().id())
                        .replace())
                .doOnNext(__ -> Timber.d("from net"))
                .doOnNext(__ -> cache$(login, cache).count().subscribe(i -> Timber.d("stored: " + ((i == 0) ? "false" : "true"))));
    }

}
