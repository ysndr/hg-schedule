package de.ysndr.android.hgschedule.view.schedulelist;


import com.f2prateek.rx.preferences2.RxSharedPreferences;

import de.ysndr.android.hgschedule.functions.Reactions;
import de.ysndr.android.hgschedule.functions.TransfFunc;
import de.ysndr.android.hgschedule.inject.RemoteDataService;
import de.ysndr.android.hgschedule.state.State;
import de.ysndr.android.hgschedule.state.models.Entry;
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

    static Observable<State> filter(Observable<State> state$, Observable<Entry> xformSeed$) {
        return state$.switchMap(
            _state -> xformSeed$.scan(
                // 1. build on top of _state as a basis
                _state,
                // 2. apply filters on state and use the resulting state as temporary result of
                //    all accumulated filtering actions
                //    State_N, filter -> State_N+1    // Start with _state as State_0
                (state, entry) -> Reactions.filter(entry, state)).startWith(_state));
    }

    static Observable<State> transform(Observable<State> state$) {

        return state$.doOnNext(__ -> Timber.d("transforming"))
            // do transformation if transformable type
            .doOnNext(__ -> Timber.d("attempt transform"))
            .map(state -> state.union().join(
                State::error,
                data -> State.data(TransfFunc.transformState(data)),
                State::uiSideEffect,
                State::empty));
    }


}
