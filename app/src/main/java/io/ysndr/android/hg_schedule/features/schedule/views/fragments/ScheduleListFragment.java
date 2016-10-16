package io.ysndr.android.hg_schedule.features.schedule.views.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.jakewharton.rxbinding.support.v4.widget.RxSwipeRefreshLayout;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.ysndr.android.hg_schedule.MyApp;
import io.ysndr.android.hg_schedule.R;
import io.ysndr.android.hg_schedule.features.schedule.models.ScheduleEntry;
import io.ysndr.android.hg_schedule.features.schedule.presenters.SchedulePresenter;
import io.ysndr.android.hg_schedule.features.schedule.views.ScheduleAdapter;
import io.ysndr.android.hg_schedule.features.schedule.views.ScheduleListView;
import rx.Observable;
import rx.Subscription;
import timber.log.Timber;

/**
 * Created by yannik on 10/9/16.
 */

public class ScheduleListFragment extends Fragment implements ScheduleListView {


    @BindView(R.id.content_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.content_swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    Subscription refreshes$;

    @BindView(R.id.progress_spinner)
    ProgressBar progress;


    @Inject
    SchedulePresenter mSchedulePresenter;

    ScheduleAdapter schedAdapter;


    private Unbinder unbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApp.getScheduleComponent(this.getContext()).inject(this);

        mSchedulePresenter.attachView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_main, container, false);
        unbinder = ButterKnife.bind(this, layout);
        refreshes$ = RxSwipeRefreshLayout.refreshes(swipeRefresh)
                .subscribe(_void_ -> {
                    mSchedulePresenter.invokeUpdate();
                });

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        schedAdapter = new ScheduleAdapter();
        recyclerView.setAdapter(schedAdapter);

        return layout;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //mSchedulePresenter.invokeUpdate();
    }

    @Override
    public void updateView(Observable<ScheduleEntry> entry$) {
        schedAdapter.clear();
        entry$.subscribe(
                schedule -> schedAdapter.addSchedule(schedule),
                t -> t.printStackTrace(),
                () -> Timber.d("done?")
        );
    }

    @Override
    public void setLoading(boolean loading) {
        swipeRefresh.setRefreshing(loading);
        progress.setVisibility(View.VISIBLE);
        if (!loading) {
            progress.setVisibility(View.GONE);
        }
    }

    @Override
    public void showError(Throwable t) {
        String message = t.getMessage();
        Toast.makeText(
                this.getContext(),
                message,
                Toast.LENGTH_SHORT
        ).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        refreshes$.unsubscribe();
        mSchedulePresenter.detachView(true);
    }

}
