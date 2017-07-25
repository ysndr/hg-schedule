package de.ysndr.android.hgschedule.functions;

import java.util.List;
import java.util.concurrent.TimeUnit;

import de.ysndr.android.hgschedule.functions.models.Login;
import de.ysndr.android.hgschedule.inject.RemoteDataService;
import de.ysndr.android.hgschedule.state.models.Schedule;
import de.ysndr.android.hgschedule.state.models.School;
import io.reactivecache2.ReactiveCache;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.rx_cache2.RxCacheException;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by yannik on 4/8/17.
 */


public class DataFunc {

    public static Observable<Schedule> schedule(
        RemoteDataService remote,
        ReactiveCache cache,
        Login login) {

        return Observable.concatDelayError(
            io.vavr.collection.List.of(
                cache$(login, cache),
                remote$(login, remote, cache))).take(1)
            // LOGS //
            .doOnError(e -> Timber.d("error getting schedule: %s", e))
            .doOnNext(data -> Timber.d("got data: %s", data));
    }


    public static Function<?, Observable<Response<List<School>>>> schools(RemoteDataService remote) {
        return __ -> remote.getSchools();
    }

    /*
    * data providers
    * */

    private static Observable<Schedule> cache$(Login login, ReactiveCache cache) {
        Timber.d("requesting cache");

        return cache.<Schedule>provider().withKey(login.school().id()).read()
            .toObservable()
                .doOnNext(__ -> Timber.d("from cache"))
                .onErrorResumeNext(error ->
                        (error instanceof RxCacheException)
                                ? Observable.empty()
                                : Observable.error(error));
    }

    private static Observable<Schedule> remote$(Login login, RemoteDataService remote, ReactiveCache cache) {
        Timber.d("requesting remote");

        return remote.getScheduleEntries(login.school().id(), login.auth()).singleOrError()
                .compose(cache.<Schedule>provider()
                        .lifeCache(5, TimeUnit.MINUTES)
                        .withKey(login.school().id())
                    .replace())

            .doOnSuccess(__ -> Timber.d("from net"))
            .doOnSuccess(__ -> cache$(login, cache).count().subscribe(i -> Timber.d("stored: " + ((i == 0) ? "false" : "true")))).toObservable();
    }

}
