package de.ysndr.android.hgschedule.util.reactive;

import java.util.List;

import de.ysndr.android.hgschedule.state.models.School;
import de.ysndr.android.hgschedule.util.Presentable;
import rx.Observable;

/**
 * Created by yannik on 1/22/17.
 */
public interface SchoolDataSource {
    Observable<Presentable<List<School>>> data$();
}
