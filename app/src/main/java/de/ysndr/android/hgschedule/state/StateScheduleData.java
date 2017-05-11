package de.ysndr.android.hgschedule.state;

import org.immutables.value.Value;

import de.ysndr.android.hgschedule.state.models.Schedule;

/**
 * Created by yannik on 5/8/17.
 */
@Value.Immutable
@StateStyle
public abstract class StateScheduleData {
    public abstract Schedule schedule();

    @Value.Default
    public boolean loading() {
        return false;
    }
}
