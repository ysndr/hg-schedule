package de.ysndr.android.hgschedule.state;

import org.immutables.value.Value;

import de.ysndr.android.hgschedule.functions.models.Transformation;
import de.ysndr.android.hgschedule.state.models.Schedule;
import fj.data.Set;

/**
 * Created by yannik on 5/8/17.
 */
@Value.Immutable
@StateStyle
public abstract class StateScheduleData {
    public abstract Schedule schedule();

    public abstract Set<Transformation<Schedule>> transformations();

    @Value.Default
    public boolean loading() {
        return false;
    }
}
