package de.ysndr.android.hgschedule.util.reactive;

import de.ysndr.android.hgschedule.state.models.Entry;
import io.reactivex.Observable;

/**
 * Created by yannik on 1/1/17.
 */

public interface FilterIntentSource extends Source {
    Observable<Entry> filterIntent$();
}
