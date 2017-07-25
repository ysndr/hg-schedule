package de.ysndr.android.hgschedule.util.reactive;

import fj.Unit;
import io.reactivex.Observable;

/**
 * Created by yannik on 1/1/17.
 */

public interface ReloadIntentSource extends Source {


    Observable<Unit> reloadIntent$();


}
