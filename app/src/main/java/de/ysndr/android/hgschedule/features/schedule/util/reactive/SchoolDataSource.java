package de.ysndr.android.hgschedule.features.schedule.util.reactive;

import java.util.List;

import de.ysndr.android.hgschedule.features.schedule.models.School;
import de.ysndr.android.hgschedule.features.schedule.util.Presentable;
import rx.Observable;

/**
 * Created by yannik on 1/22/17.
 */
public interface SchoolDataSource {
    Observable<Presentable<List<School>>> data$();
}
