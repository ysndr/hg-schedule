package de.ysndr.android.hgschedule.util;


import org.immutables.value.Value;

import de.ysndr.android.hgschedule.state.models.Entry;
import de.ysndr.android.hgschedule.state.models.Substitute;
import de.ysndr.android.hgschedule.utils.Tuple;
import fj.data.Option;

/**
 * Created by yannik on 12/19/16.
 */
@Value.Immutable
@Tuple
public interface FilterData {
    Option<Entry> entry();

    Option<Substitute> substitute();
}
