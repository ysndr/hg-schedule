package io.ysndr.android.hg_schedule.presenters;

import io.ysndr.android.hg_schedule.views.MvpView;

/**
 * Created by yannik on 10/10/16.
 */

public interface MvpPresenter<V extends MvpView> {
    void attachView(V view);
    void detachView(boolean retainInstance);
}
