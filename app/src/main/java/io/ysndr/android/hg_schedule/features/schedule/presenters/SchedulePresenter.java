package io.ysndr.android.hg_schedule.features.schedule.presenters;

import javax.inject.Inject;

import io.ysndr.android.hg_schedule.features.schedule.data.CombinedDataService;
import io.ysndr.android.hg_schedule.features.schedule.models.Schedule;
import io.ysndr.android.hg_schedule.features.schedule.views.ScheduleListView;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

/**
 * Created by yannik on 10/10/16.
 */

public class SchedulePresenter implements ISchedulePresenter {

    @Inject
    CombinedDataService mDataService;

    private ScheduleListView mScheduleListView;

    private Observable<Schedule> schedule$;
    private Subscription scheduleSubscription;
    private Subscription errorSubscription;

    @Inject
    public SchedulePresenter(CombinedDataService dataService) {
        mDataService = dataService;
    }

    @Override
    public void attachView(ScheduleListView view) {
        mScheduleListView = view;
        reset();
    }

    private void reset() {
        if (scheduleSubscription != null && !scheduleSubscription.isUnsubscribed())
            scheduleSubscription.unsubscribe();
        if (errorSubscription != null && !errorSubscription.isUnsubscribed())
            errorSubscription.unsubscribe();


        scheduleSubscription = mDataService.getValue$()
                .map(schedule -> Observable.from(schedule.getEntries()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(entry$ -> {
                    mScheduleListView.setLoading(false);
                    mScheduleListView.updateView(entry$);
                });

        errorSubscription = mDataService.getError$()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(error -> {
                            Timber.d("An error occurred while fetching data!");
                            mScheduleListView.setLoading(false);
                            mScheduleListView.showError(error);
                        }
                );

    }

    @Override
    public void detachView(boolean retainInstance) {
        if (scheduleSubscription != null && !errorSubscription.isUnsubscribed()) {
            scheduleSubscription.unsubscribe();
        }
        if (errorSubscription != null && !errorSubscription.isUnsubscribed())
            errorSubscription.unsubscribe();
    }

    @Override
    public void invokeUpdate() {
        mScheduleListView.setLoading(true);
        mDataService.getSchedule();
    }


}
