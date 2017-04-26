package de.ysndr.android.hgschedule.features.schedule.util.reactive;

import de.ysndr.android.hgschedule.features.schedule.models.Entry;
import rx.Observable;

/**
 * Created by yannik on 1/3/17.
 */
public interface DialogRequestIntentSource extends Source {
    Observable<Entry> dialogRequest$();
}
