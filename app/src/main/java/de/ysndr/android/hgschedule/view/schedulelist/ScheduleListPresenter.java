package de.ysndr.android.hgschedule.view.schedulelist;

import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.hannesdorfmann.mosby3.mvi.MviBasePresenter;

import de.ysndr.android.hgschedule.functions.Reactions;
import de.ysndr.android.hgschedule.inject.RemoteDataService;
import de.ysndr.android.hgschedule.state.ScheduleData;
import de.ysndr.android.hgschedule.state.State;
import fj.data.List;
import io.reactivecache2.ReactiveCache;
import io.reactivex.Observable;

/**
 * Created by yannik on 7/5/17.
 */

public class ScheduleListPresenter extends MviBasePresenter<ScheduleListMviViewInterface, State> {


    RxSharedPreferences prefs;
    RemoteDataService remote;
    ReactiveCache cache;

    public ScheduleListPresenter(
        RxSharedPreferences prefs,
        RemoteDataService remote,
        ReactiveCache cache) {

        this.prefs = prefs;
        this.remote = remote;
        this.cache = cache;
    }


    @Override
    protected void bindIntents() {
        List<Observable<State>> observables = List.list(
            intent(view -> view.reloadIntent$()
                .flatMap(Reactions.reload(prefs, remote, cache))
                .map(state -> state.union().join(
                    error -> State.error(error),
                    data -> State.data(ScheduleData.of(data.schedule())),
                    empty -> State.empty())))
        );

        Observable<State> stateObservable = Observable.merge(observables);
        subscribeViewState(stateObservable, ScheduleListView::render);
    }


}
