package de.ysndr.android.hgschedule.view.schedulelist;


import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.hannesdorfmann.mosby3.mvi.MviBasePresenter;

import javax.inject.Inject;

import de.ysndr.android.hgschedule.inject.RemoteDataService;
import de.ysndr.android.hgschedule.state.SideEffect;
import de.ysndr.android.hgschedule.state.State;
import de.ysndr.android.hgschedule.state.models.Entry;
import io.reactivecache2.ReactiveCache;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by yannik on 7/5/17.
 */

public class ScheduleListPresenter
    extends MviBasePresenter<ScheduleListViewInterface, State> {


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


        // *************** State ****************** //
        final Observable<Object> reload$ = intent(ScheduleListViewInterface::reloadIntent$);
        final Observable<Entry> filter$ = intent(ScheduleListViewInterface::filterIntent$);

        final Observable<State> state$ = Functions.transform(
            Functions.filter(
                Functions.refresh(reload$, prefs, remote, cache),
                filter$));

        // *********** Side Effects ************** //
        final Observable<Entry> dialogEntryData$ = intent(ScheduleListViewInterface::dialogIntent$);

        final Observable<SideEffect> dialogs$ = SideEffects.dialog(dialogEntryData$);

        final Observable<State> merged$ = SideEffects.mergeSideEffectsIntoState(state$, dialogs$)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());


        subscribeViewState(merged$, ScheduleListViewInterface::render);
    }


//    private final State viewStateReducer(State previousState, State newState) {
//        return TransfFunc.;
//    }


}
