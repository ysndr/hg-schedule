package io.ysndr.android.hg_schedule.features.schedule.util.reactive;

import io.ysndr.android.hg_schedule.features.schedule.models.School;
import rx.Observable;

/**
 * Created by yannik on 1/22/17.
 */

public interface ClickSource {
    Observable<School> getClick();
}
