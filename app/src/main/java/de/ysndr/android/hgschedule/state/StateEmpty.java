package de.ysndr.android.hgschedule.state;

import org.immutables.value.Value;

/**
 * Created by yannik on 5/8/17.
 */
@Value.Immutable
@StateStyle
public abstract class StateEmpty {
    @Value.Default
    public boolean loading() {
        return false;
    }
}
