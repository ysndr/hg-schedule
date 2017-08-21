package de.ysndr.android.hgschedule.view.schedulelist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;

import com.hannesdorfmann.mosby3.MviController;

import de.ysndr.android.hgschedule.MyApp;
import de.ysndr.android.hgschedule.R;
import de.ysndr.android.hgschedule.state.State;
import de.ysndr.android.hgschedule.state.models.Entry;
import io.reactivex.Observable;
import timber.log.Timber;

/**
 * Created by yannik on 7/19/17.
 */

public class ScheduleListController
    extends MviController<ScheduleListViewInterface, ScheduleListPresenter>
    implements ScheduleListViewInterface {

    private ScheduleListView view;

    public ScheduleListController() {
        this(null);
    }

    public ScheduleListController(Bundle args) {
        super(args);
        setRetainViewMode(RetainViewMode.RELEASE_DETACH);
    }

    @Override
    @NonNull
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        View view = inflater.inflate(R.layout.controller_schedule_list, container, false);
        this.view = (ScheduleListView) view;
        return view;
    }

    @Override
    @NonNull
    public ScheduleListPresenter createPresenter() {
        return MyApp.getScheduleComponent(this.getActivity()).getPresenter();
    }

//    @NonNull
//    @Override
//    public ScheduleListMviView getMvpView() {
//        return this.view;
//    }

    @Override
    public void setRestoringViewState(boolean restoringViewState) {
        // what to do here?
    }

    @Override
    @NonNull
    public Observable<Entry> dialogIntent$() {
        return view.dialogRequestIntent$;
    }

    @Override
    @NonNull
    public Observable<Entry> filterIntent$() {
        return view.filterRequestIntent$;
    }

    @Override
    @NonNull
    public Observable<Object> reloadIntent$() {
        return view.swipeRefreshIntent$;
    }

    @Override
    public void render(State state) {
        state.union().continued(
            error -> {},
            scheduleData -> {},
            uiSideEffect -> {
                uiSideEffect.effect().accept(this);
            },
            empty -> {}
            );
        view.render(state);
    }
}
