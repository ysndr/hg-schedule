package io.ysndr.android.hg_schedule.features.schedule.presenters;

import io.ysndr.android.hg_schedule.features.schedule.views.ScheduleListView;
import io.ysndr.android.hg_schedule.presenters.MvpPresenter;

/**
 * Created by yannik on 10/10/16.
 */

public interface ISchedulePresenter extends MvpPresenter<ScheduleListView> {

    void invokeUpdate();
}
