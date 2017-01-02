package io.ysndr.android.hg_schedule.features.schedule.util;

import org.immutables.value.Value;

import fj.data.Option;

/**
 * Created by yannik on 1/1/17.
 */
@Value.Immutable
@Value.Style(
        typeAbstract = "*Def",
        typeImmutable = "*",
        allParameters = true, // all attributes will become constructor parameters
        // as if they are annotated with @Value.Parameter
        visibility = Value.Style.ImplementationVisibility.PUBLIC, // Generated class will be always public
        defaults = @Value.Immutable(builder = false)) // Disable copy methods and builder
public interface PresentableDef<T> {
    boolean loading();

    Option<T> result();
}
