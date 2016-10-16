package io.ysndr.android.hg_schedule.features.schedule.views;

import io.ysndr.android.hg_schedule.features.schedule.models.ScheduleEntry;
import io.ysndr.android.hg_schedule.views.MvpView;
import rx.Observable;

/**
 * Created by yannik on 10/10/16.
 */

public interface ScheduleListView extends MvpView {
    void updateView(Observable<ScheduleEntry> entry$);

    void setLoading(boolean loading);

    void showError(Throwable t);
}
