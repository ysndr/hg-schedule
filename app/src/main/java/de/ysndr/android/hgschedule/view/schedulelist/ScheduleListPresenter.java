package de.ysndr.android.hgschedule.view.schedulelist;

import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.hannesdorfmann.mosby3.mvi.MviBasePresenter;

import de.ysndr.android.hgschedule.functions.Reactions;
import de.ysndr.android.hgschedule.functions.TransfFunc;
import de.ysndr.android.hgschedule.inject.RemoteDataService;
import de.ysndr.android.hgschedule.state.State;
import io.reactivecache2.ReactiveCache;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yannik on 7/5/17.
 */

public class ScheduleListPresenter
    extends MviBasePresenter<ScheduleListMviViewInterface, State> {


    private RxSharedPreferences prefs;
    private RemoteDataService remote;
    private ReactiveCache cache;

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


        Observable<State> reload$ = intent(view -> view.reloadIntent$()
            .flatMap((object) -> Reactions.reload(prefs, remote, cache)));

        Observable<State> filtered$ = intent(view -> view.filterIntent$()
            .flatMap((entry) -> Reactions.filter(entry, reload$)));

        Observable<State> allIntents = Observable.merge(reload$, filtered$);
        Observable<State> stateObservable = allIntents
//            .scan(State.empty(), this::viewStateReducer)
            .map(TransfFunc::transformState)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());
        subscribeViewState(stateObservable, ScheduleListMviViewInterface::render);
    }


//    private final State viewStateReducer(State previousState, State newState) {
//        return TransfFunc.;
//    }


}
