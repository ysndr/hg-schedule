package de.ysndr.android.hgschedule.features.schedule.presenters;

import de.ysndr.android.hgschedule.features.schedule.view.ScheduleListView;
import de.ysndr.android.hgschedule.presenters.MvpPresenter;

/**
 * Created by yannik on 10/10/16.
 */

public interface ISchedulePresenter extends MvpPresenter<ScheduleListView> {

    void invokeUpdate();

    void toggleEntry(String id);
}
