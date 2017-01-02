package io.ysndr.android.hg_schedule.features.schedule.util;

import org.immutables.value.Value;

import rx.functions.Func1;

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

public interface FilterDef<I, O> {

    @Value.Auxiliary
    Func1<I, O> filter();

    int ident();

}
