package de.ysndr.android.hgschedule.util.reactive;

import de.ysndr.android.hgschedule.state.models.School;
import rx.Observable;

/**
 * Created by yannik on 1/22/17.
 */

public interface ClickSource {
    Observable<School> getClick();
}
