package de.ysndr.android.hgschedule.state;

import org.immutables.value.Value;

import de.ysndr.android.hgschedule.state.models.Entry;

/**
 * Created by yannik on 5/8/17.
 */
@Value.Immutable
@StateStyle
public abstract class StateEntryDialogData {
    public abstract Entry entry();
}
