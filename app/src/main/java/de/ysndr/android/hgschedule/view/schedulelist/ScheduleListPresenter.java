package de.ysndr.android.hgschedule.view.schedulelist;


import android.support.v4.app.DialogFragment;

import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.hannesdorfmann.mosby3.mvi.MviBasePresenter;

import javax.inject.Inject;

import de.ysndr.android.hgschedule.functions.Reactions;
import de.ysndr.android.hgschedule.functions.TransfFunc;
import de.ysndr.android.hgschedule.inject.RemoteDataService;
import de.ysndr.android.hgschedule.state.EntryDialogData;
import de.ysndr.android.hgschedule.state.State;
import de.ysndr.android.hgschedule.state.models.Entry;
import de.ysndr.android.hgschedule.view.ScheduleDialog;
import de.ysndr.android.hgschedule.view.ScheduleDialogBuilder;
import io.reactivecache2.ReactiveCache;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by yannik on 7/5/17.
 */

public class ScheduleListPresenter
    extends MviBasePresenter<ScheduleListMviViewInterface, State> {


    private RxSharedPreferences prefs;
    private RemoteDataService remote;
    private ReactiveCache cache;

    @Inject
    ScheduleListPresenter(
        RxSharedPreferences prefs,
        RemoteDataService remote,
        ReactiveCache cache) {

        this.prefs = prefs;
        this.remote = remote;
        this.cache = cache;
    }

    @Override
    protected void bindIntents() {

        Observable<State> reload$ = intent(view -> view.reloadIntent$())
            .doOnNext(obj -> Timber.d("reload?"))
            .flatMap((obj) -> Reactions.reload(prefs, remote, cache).subscribeOn(Schedulers.io()))
            .doOnNext(state -> Timber.d("got state"));

        Observable<Entry> filter$ = intent(view -> view.filterIntent$())
            .doOnNext(__ -> Timber.d("triggered filter"));

        Observable<State> allIntents = reload$.switchMap(
            _state -> filter$.scan(
                // 1. build on top of _state as a basis
                _state,
                // 2. apply filters on state and use the resulting state as temporary result of
                //    all accumulated filtering actions
                //    State_N, filter -> State_N+1    // Start with _state as State_0
                (state, entry) -> Reactions.filter(entry, state)).startWith(_state));

        Observable<State> dialogData$ = intent(view -> view.dialogIntent$())
            .map(entry -> State.entryDialogData(EntryDialogData.of(entry)));

        Observable<State> intentsWithDialogs = allIntents.switchMap(
            state -> dialogData$.flatMap(dialogState ->
                Observable.just(dialogState, state)).startWith(state));


        Observable<State> stateObservable = intentsWithDialogs
//            .scan(State.empty(), this::viewStateReducer)
            .doOnNext(__ -> Timber.d("transforming"))
            .map(TransfFunc::transformState)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());







        subscribeViewState(stateObservable, ScheduleListMviViewInterface::render);
    }


//    private final State viewStateReducer(State previousState, State newState) {
//        return TransfFunc.;
//    }


}
