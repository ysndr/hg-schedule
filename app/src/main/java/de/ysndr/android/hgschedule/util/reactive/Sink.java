package de.ysndr.android.hgschedule.util.reactive;


import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by yannik on 1/1/17.
 */

public interface Sink<T extends Source> {
    CompositeDisposable disposables = new CompositeDisposable();

    void bindIntent(T source);
    void unbind();
}
