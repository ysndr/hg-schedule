package io.ysndr.android.hg_schedule.features.schedule.util.reactive;

import io.ysndr.android.hg_schedule.features.schedule.models.Entry;
import rx.Observable;

/**
 * Created by yannik on 1/3/17.
 */
public interface DialogRequestIntentSource extends Source {
    Observable<Entry> dialogRequest$();
}
