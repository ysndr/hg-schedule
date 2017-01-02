package io.ysndr.android.hg_schedule.features.schedule.util.reactive;


/**
 * Created by yannik on 1/1/17.
 */

public interface Sink<T extends Source> {
    void bindIntent(T source);
}
