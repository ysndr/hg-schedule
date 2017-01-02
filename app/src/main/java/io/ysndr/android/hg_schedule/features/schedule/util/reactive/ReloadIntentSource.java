package io.ysndr.android.hg_schedule.features.schedule.util.reactive;

import fj.Unit;
import rx.Observable;

/**
 * Created by yannik on 1/1/17.
 */

public interface ReloadIntentSource extends Source {


    Observable<Unit> reloadIntent$();


}
