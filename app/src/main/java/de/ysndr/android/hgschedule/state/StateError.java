package de.ysndr.android.hgschedule.state;

import org.immutables.value.Value;

import fj.data.Option;

/**
 * Created by yannik on 5/8/17.
 */
@Value.Immutable
@StateStyle
public abstract class StateError<T> {
    public abstract Throwable error();

    @Value.Default
    public Option<String> message() {
        return Option.some(error().getLocalizedMessage() != null
            ? error().getMessage()
            : error().toString());
    }

    @Value.Default
    public Option<T> payload() {
        return Option.none();
    }
}
