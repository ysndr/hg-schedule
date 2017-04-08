package io.ysndr.android.hg_schedule.features.schedule.util.reactive;

import java.util.List;

import io.ysndr.android.hg_schedule.features.schedule.models.School;
import io.ysndr.android.hg_schedule.features.schedule.util.Presentable;
import rx.Observable;

/**
 * Created by yannik on 1/22/17.
 */
public interface SchoolDataSource {
    Observable<Presentable<List<School>>> data$();
}
