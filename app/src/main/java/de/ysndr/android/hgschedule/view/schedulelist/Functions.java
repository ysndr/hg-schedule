package de.ysndr.android.hgschedule.view.schedulelist;


import com.f2prateek.rx.preferences2.RxSharedPreferences;

import de.ysndr.android.hgschedule.functions.Reactions;
import de.ysndr.android.hgschedule.inject.RemoteDataService;
import de.ysndr.android.hgschedule.state.State;
import io.reactivecache2.ReactiveCache;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by yannik on 8/21/17.
 */

class Functions {
    static Observable<State> refresh(
        Observable<Object> refresh$,
        RxSharedPreferences prefs,
        RemoteDataService remote,
        ReactiveCache cache) {

        return refresh$.doOnNext(obj -> Timber.d("reload?"))
            .flatMap((obj) -> Reactions.reload(prefs, remote, cache).subscribeOn(Schedulers.io()))
            .doOnNext(state -> Timber.d("got state"));
    }

    static


}
