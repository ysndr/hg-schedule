package de.ysndr.android.hgschedule.view.schedulelist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.hannesdorfmann.mosby3.MviController;

import javax.inject.Inject;

import de.ysndr.android.hgschedule.MyApp;
import de.ysndr.android.hgschedule.R;
import de.ysndr.android.hgschedule.inject.RemoteDataService;
import io.reactivecache2.ReactiveCache;

/**
 * Created by yannik on 7/19/17.
 */

public class ScheduleListController
    extends MviController<ScheduleListMviViewInterface, ScheduleListPresenter> {

    private ScheduleListView view;

    public ScheduleListController() {
        this(null);
    }

    public ScheduleListController(Bundle args) {
        super(args);
        setRetainViewMode(RetainViewMode.RELEASE_DETACH);
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        View view = inflater.inflate(R.layout.controller_schedule_list, container, false);
        this.view = (ScheduleListView) view;
        return view;
    }

    @NonNull
    @Override
    public ScheduleListPresenter createPresenter() {
        return MyApp.getScheduleComponent(this.getActivity()).getPresenter();
    }

    @NonNull
    @Override
    public ScheduleListView getMvpView() {
        return this.view;
    }

    @Override
    public void setRestoringViewState(boolean restoringViewState) {
        // what to do here?
    }
}
