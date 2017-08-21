package de.ysndr.android.hgschedule.view.schedulelist;


import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import de.ysndr.android.hgschedule.state.SideEffect;
import de.ysndr.android.hgschedule.state.State;
import de.ysndr.android.hgschedule.state.models.Entry;
import de.ysndr.android.hgschedule.view.ScheduleDialog;
import de.ysndr.android.hgschedule.view.ScheduleDialogBuilder;
import io.reactivex.Observable;
import timber.log.Timber;

/**
 * Created by yannik on 8/21/17.
 */

public class SideEffects {

    static Observable<State> mergeSideEffectsIntoState(Observable<State> state$,
                                                       Observable<SideEffect> sideEffect$) {

        return state$.switchMap(state ->
            sideEffect$
                .map(State::uiSideEffect)
                .flatMap(effect -> Observable.just(effect, state))
                .startWith(state));
    };

    static Observable<SideEffect> dialog(Observable<Entry> entry$) {
        return entry$.map(entry -> SideEffect.of(controller ->  {
            AppCompatActivity activity = null;
            try {
                activity = ((AppCompatActivity) (controller.getActivity()));
            }
            catch (ClassCastException e) {
                String msg =
                    "Couldn't cast the parent Activity to AppCompatActivity. Most likely you have forgot to add \"MainActivity extends AppCompatActivity\".\"";
                Timber.e(msg);

            }

            if (activity == null) {
                String msg = "Could not get Activity";
                Timber.e(msg);
                throw new RuntimeException(msg);
            }

            FragmentManager fm = activity.getSupportFragmentManager();

            ScheduleDialog dialog = ScheduleDialogBuilder.newScheduleDialog(
                entry.date().day(),
                entry.info());

            dialog.show(fm, "tag");
        }));
    }
}
