package de.ysndr.android.hgschedule.features.schedule.view;


import fj.data.List;
import de.ysndr.android.hgschedule.features.schedule.models.Entry;
import de.ysndr.android.hgschedule.views.MvpView;

/**
 * Created by yannik on 10/10/16.
 */

public interface ScheduleListView extends MvpView {
    void updateView(List<? extends Entry> entries);

    void clearView();

    void setLoading(boolean loading);

    void showError(Throwable t);
}
