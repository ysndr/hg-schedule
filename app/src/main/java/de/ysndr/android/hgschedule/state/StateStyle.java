package de.ysndr.android.hgschedule.state;

import org.immutables.value.Value;

/**
 * Created by yannik on 5/8/17.
 */


@Value.Style(
        allMandatoryParameters = true,
        typeAbstract = "State*",
        typeImmutable = "*",
        defaults = @Value.Immutable(builder = false)
)
public @interface StateStyle {
}
