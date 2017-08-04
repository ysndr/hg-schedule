package de.ysndr.android.hgschedule.view.schedulelist;

import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.hannesdorfmann.mosby3.mvi.MviBasePresenter;
import com.pacoworks.rxtuples2.RxTuples;

import org.javatuples.Pair;

import de.ysndr.android.hgschedule.functions.Reactions;
import de.ysndr.android.hgschedule.functions.TransfFunc;
import de.ysndr.android.hgschedule.inject.RemoteDataService;
import de.ysndr.android.hgschedule.state.State;
import io.reactivecache2.ReactiveCache;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

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


        Observable<State> reload$ = intent(view -> view.reloadIntent$())
            .doOnNext(obj -> Timber.d("reload?"))
            .map(obj -> State.empty());
            /*.flatMap((obj) ->
                Reactions.reload(prefs, remote, cache)
                    .subscribeOn(Schedulers.io()));*/


        Observable<State> filtered$ = intent(view -> view.filterIntent$())
            .doOnNext(__ -> Timber.d("triggered filter"))
            .withLatestFrom(reload$, RxTuples.toPair())
            .scan((state, pair) -> pair.setAt1(Reactions.filter(pair.getValue0(), state.getValue1())))
            .map(Pair::getValue1)
            .doOnNext(__ -> Timber.d("applied filters"));



        Observable<State> allIntents = Observable.merge(reload$, filtered$);


        Observable<State> stateObservable = allIntents
//            .scan(State.empty(), this::viewStateReducer)
            .doOnNext(__ -> Timber.d("transforming"))
            .map(TransfFunc::transformState)
//            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());

        subscribeViewState(stateObservable, ScheduleListMviViewInterface::render);
    }


//    private final State viewStateReducer(State previousState, State newState) {
//        return TransfFunc.;
//    }


}
