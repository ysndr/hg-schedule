package de.ysndr.android.hgschedule.view.schedulelist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hannesdorfmann.mosby3.mvp.MvpView;
import com.jakewharton.rxbinding2.support.v4.widget.RxSwipeRefreshLayout;
import com.jakewharton.rxrelay2.BehaviorRelay;
import com.jakewharton.rxrelay2.PublishRelay;
import com.jakewharton.rxrelay2.Relay;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.ysndr.android.hgschedule.R;
import de.ysndr.android.hgschedule.state.State;
import de.ysndr.android.hgschedule.state.models.Entry;
import de.ysndr.android.hgschedule.view.StateController;
import io.reactivex.Observable;
import timber.log.Timber;

/**
 * Created by yannik on 7/20/17.
 */

interface ScheduleListViewInterface extends MvpView {

    @NonNull
    Observable<Entry> dialogIntent$();
    @NonNull
    Observable<Entry> filterIntent$();
    @NonNull
    Observable<Object> reloadIntent$();
    void render(State state);

}
