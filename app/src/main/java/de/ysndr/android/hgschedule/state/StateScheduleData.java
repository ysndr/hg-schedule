package de.ysndr.android.hgschedule.state;

import org.immutables.value.Value;

import java.util.List;

import de.ysndr.android.hgschedule.functions.models.Transformation;
import de.ysndr.android.hgschedule.state.models.Schedule;

/**
 * Created by yannik on 5/8/17.
 */
@Value.Immutable
@StateStyle
public abstract class StateScheduleData {
    public abstract Schedule schedule();

    public abstract List<Transformation<Schedule>> transformations();

    @Value.Default
    public boolean loading() {
        return false;
    }
}
