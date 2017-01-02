package io.ysndr.android.hg_schedule.utils;

import org.immutables.value.Value;

/**
 * Created by yannik on 12/19/16.
 */

@Value.Style(
        // Generate construction method using all attributes as parameters
        allParameters = true,
        // Changing generated name just for fun
        typeImmutable = "*Tuple",
        // We may also disable builder
        defaults = @Value.Immutable(builder = false))
public @interface Tuple {
}

