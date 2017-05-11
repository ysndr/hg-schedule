package de.ysndr.android.hgschedule.functions.models;

import org.immutables.value.Value;

import rx.functions.Func1;

/**
 * Created by yannik on 5/8/17.
 */
@Value.Immutable
@Value.Style(allParameters = true)
public interface Transformation<T> {
    String _seed();

    @Value.Auxiliary
    Func1<T, T> transform();
}
