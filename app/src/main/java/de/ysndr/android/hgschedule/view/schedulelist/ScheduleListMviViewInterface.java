package de.ysndr.android.hgschedule.view.schedulelist;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

import com.hannesdorfmann.mosby3.mvp.MvpView;

import de.ysndr.android.hgschedule.state.State;
import de.ysndr.android.hgschedule.state.models.Entry;
import de.ysndr.android.hgschedule.view.widget.preferences.SchoolPreference;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;

/**
 * Created by yannik on 7/20/17.
 */

interface ScheduleListMviViewInterface extends MvpView {

    @NonNull
    Observable<Entry> dialogIntent$();

    @NonNull
    Observable<Entry> filterIntent$();

    @NonNull
    Observable<Object> reloadIntent$();

    void render(State state);

    void dialog(DialogFragment d);


}
