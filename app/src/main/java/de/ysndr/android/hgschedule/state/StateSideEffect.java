package de.ysndr.android.hgschedule.state;

import com.bluelinelabs.conductor.Controller;

import org.immutables.value.Value;


import de.ysndr.android.hgschedule.state.models.Entry;
import io.reactivex.functions.Consumer;

/**
 * Created by yannik on 5/8/17.
 */
@Value.Immutable
@StateStyle
public abstract class StateSideEffect {
    public abstract Consumer<Controller> effect();
}
