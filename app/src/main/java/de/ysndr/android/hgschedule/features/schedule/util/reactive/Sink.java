package de.ysndr.android.hgschedule.features.schedule.util.reactive;


import rx.subscriptions.CompositeSubscription;

/**
 * Created by yannik on 1/1/17.
 */

public interface Sink<T extends Source> {
    CompositeSubscription subscriptions = new CompositeSubscription();

    void bindIntent(T source);
    void unbind();
}
