package io.ysndr.android.hg_schedule.features.schedule.util.reactive;

import fj.data.List;
import io.ysndr.android.hg_schedule.features.schedule.models.Entry;
import io.ysndr.android.hg_schedule.features.schedule.util.Presentable;
import rx.Observable;

/**
 * Created by yannik on 1/1/17.
 */

public interface ScheduleDataSource extends Source {


    Observable<Presentable<List<Entry>>> data$();


}
