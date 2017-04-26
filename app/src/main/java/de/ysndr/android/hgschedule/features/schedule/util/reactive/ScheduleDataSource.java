package de.ysndr.android.hgschedule.features.schedule.util.reactive;

import fj.data.List;
import de.ysndr.android.hgschedule.features.schedule.models.Entry;
import de.ysndr.android.hgschedule.features.schedule.util.Presentable;
import rx.Observable;

/**
 * Created by yannik on 1/1/17.
 */

public interface ScheduleDataSource extends Source {


    Observable<Presentable<List<Entry>>> data$();


}
