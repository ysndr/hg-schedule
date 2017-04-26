package de.ysndr.android.hgschedule.features.schedule.util.reactive;

import de.ysndr.android.hgschedule.features.schedule.models.Entry;
import rx.Observable;

/**
 * Created by yannik on 1/1/17.
 */

public interface FilterIntentSource extends Source {
    Observable<Entry> filterIntent$();
}
