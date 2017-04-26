package de.ysndr.android.hgschedule.features.schedule.util.reactive;

import de.ysndr.android.hgschedule.features.schedule.models.School;
import rx.Observable;

/**
 * Created by yannik on 1/22/17.
 */

public interface ClickSource {
    Observable<School> getClick();
}
