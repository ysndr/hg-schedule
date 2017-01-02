package io.ysndr.android.hg_schedule.features.schedule.util.reactive;

import io.ysndr.android.hg_schedule.features.schedule.util.FilterDataTuple;
import rx.Observable;

/**
 * Created by yannik on 1/1/17.
 */

public interface FilterIntentSource extends Source {


    Observable<FilterDataTuple> filterIntent$();


}
