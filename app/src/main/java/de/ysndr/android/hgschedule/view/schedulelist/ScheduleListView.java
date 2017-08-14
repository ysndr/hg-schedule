package de.ysndr.android.hgschedule.view.schedulelist;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import timber.log.Timber;

public class ScheduleListView extends LinearLayout {

    final BehaviorRelay<Entry> dialogRequestIntent$;
    final BehaviorRelay<Entry> filterRequestIntent$;
    final Relay<Object> swipeRefreshIntent$;
    @BindView(R.id.content_recycler_view)
    RecyclerView recyclerView;

    /*
    * Intents
    * */
    @BindView(R.id.content_swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    StateController controller;
    RecyclerView.Adapter adapter;


    public ScheduleListView(Context context) {
        this(context, null);
    }

    public ScheduleListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScheduleListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        dialogRequestIntent$ = BehaviorRelay.create();
        filterRequestIntent$ = BehaviorRelay.create();
        swipeRefreshIntent$ = PublishRelay.create();
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        inflate(getContext(), R.layout.view_schedule_list, this);
        ButterKnife.bind(this);

        RxSwipeRefreshLayout.refreshes(swipeRefresh)
            .doOnNext(obj -> Timber.d("swipe refresh triggered"))
            .subscribe(swipeRefreshIntent$);

        RxSwipeRefreshLayout.refreshes(swipeRefresh).subscribe(swipeRefreshIntent$);
        controller = new StateController(
            recyclerView.getRecycledViewPool(),
            dialogRequestIntent$,
            filterRequestIntent$);

        adapter = controller.getAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    /*
    * Draw
    * */
    public void render(State state) {
        state.union().continued(
            error -> {
                swipeRefresh.setRefreshing(false);
                this.toast(error.message().orSome("Internal error"));
            },
            scheduleData -> swipeRefresh.setRefreshing(scheduleData.loading()),
            // not handled here
            entryDialogData -> {
            },
            // `setRefreshig == true` ≙ loading state
            empty -> swipeRefresh.setRefreshing(empty.loading())
        );
        controller.setData(state);
    }


    void toast(String message) {
        Toast toast = Toast.makeText(this.getContext(), message, Toast.LENGTH_SHORT);
        toast.show();
    }

}