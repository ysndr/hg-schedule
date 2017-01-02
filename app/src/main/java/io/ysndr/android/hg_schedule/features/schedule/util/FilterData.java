package io.ysndr.android.hg_schedule.features.schedule.util;


import org.immutables.value.Value;

import fj.data.Option;
import io.ysndr.android.hg_schedule.features.schedule.models.Entry;
import io.ysndr.android.hg_schedule.features.schedule.models.Substitute;
import io.ysndr.android.hg_schedule.utils.Tuple;

/**
 * Created by yannik on 12/19/16.
 */
@Value.Immutable
@Tuple
public interface FilterData {
    Option<Entry> entry();

    Option<Substitute> substitute();
}
