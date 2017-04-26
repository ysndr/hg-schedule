package de.ysndr.android.hgschedule.features.schedule.util;


import org.immutables.value.Value;

import fj.data.Option;
import de.ysndr.android.hgschedule.features.schedule.models.Entry;
import de.ysndr.android.hgschedule.features.schedule.models.Substitute;
import de.ysndr.android.hgschedule.utils.Tuple;

/**
 * Created by yannik on 12/19/16.
 */
@Value.Immutable
@Tuple
public interface FilterData {
    Option<Entry> entry();

    Option<Substitute> substitute();
}
