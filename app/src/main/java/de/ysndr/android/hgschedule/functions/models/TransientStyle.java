package de.ysndr.android.hgschedule.functions.models;

/**
 * Created by yannik on 7/5/17.
 */

import org.immutables.value.Value;

@Value.Style(
    allMandatoryParameters = true,
    typeAbstract = "Transient*",
    typeImmutable = "*",
    defaults = @Value.Immutable(builder = false))
public @interface TransientStyle {
}
