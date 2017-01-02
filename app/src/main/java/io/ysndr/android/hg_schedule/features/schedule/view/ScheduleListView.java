package io.ysndr.android.hg_schedule.features.schedule.view;


import fj.data.List;
import io.ysndr.android.hg_schedule.features.schedule.models.Entry;
import io.ysndr.android.hg_schedule.views.MvpView;

/**
 * Created by yannik on 10/10/16.
 */

public interface ScheduleListView extends MvpView {
    void updateView(List<? extends Entry> entries);

    void clearView();

    void setLoading(boolean loading);

    void showError(Throwable t);
}
