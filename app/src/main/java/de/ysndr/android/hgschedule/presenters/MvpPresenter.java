package de.ysndr.android.hgschedule.presenters;

import de.ysndr.android.hgschedule.views.MvpView;

/**
 * Created by yannik on 10/10/16.
 */

public interface MvpPresenter<V extends MvpView> {
    void attachView(V view);
    void detachView(boolean retainInstance);
}
