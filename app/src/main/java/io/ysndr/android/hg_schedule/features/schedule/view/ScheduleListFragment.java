package io.ysndr.android.hg_schedule.features.schedule.view;


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
import fj.Unit;
import fj.data.List;
import io.ysndr.android.hg_schedule.MyApp;
import io.ysndr.android.hg_schedule.R;
import io.ysndr.android.hg_schedule.features.schedule.models.Entry;
import io.ysndr.android.hg_schedule.features.schedule.presenters.SchedulePresenter;
import io.ysndr.android.hg_schedule.features.schedule.util.FilterDataTuple;
import io.ysndr.android.hg_schedule.features.schedule.util.reactive.FilterIntentSource;
import io.ysndr.android.hg_schedule.features.schedule.util.reactive.ReloadIntentSource;
import io.ysndr.android.hg_schedule.features.schedule.util.reactive.ScheduleDataSink;
import io.ysndr.android.hg_schedule.features.schedule.util.reactive.ScheduleDataSource;
import rx.Observable;
import rx.Subscription;
import rx.subjects.BehaviorSubject;
import rx.subjects.Subject;
import timber.log.Timber;

/**
 * Created by yannik on 10/9/16.
 */

public class ScheduleListFragment extends Fragment implements ScheduleListView, ReloadIntentSource, FilterIntentSource {


    @BindView(R.id.content_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.content_swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    Observable refreshes$;

    @BindView(R.id.progress_spinner)
    ProgressBar progress;

    @Inject
    SchedulePresenter mSchedulePresenter;

    ListAdapter adapter;
    public final ScheduleDataSink ScheduleDataSink = new ScheduleDataSink() {
        private Subscription dataSubscription;

        @Override
        public void bindIntent(ScheduleDataSource source) {
            dataSubscription = source.data$().subscribe(presentable -> {
                setLoading(presentable.loading());
                presentable.result().toEither(Unit.unit()).either(
                        loading -> {
                            Timber.d("Clear Adapter");
                            adapter.clear();
                            return Unit.unit();
                        },
                        data -> {
                            Timber.d("Updating Adapter");
                            adapter.setContent(wrapData(data));
                            return Unit.unit();
                        }
                );
            });
        }

        @Override
        public void unbind() {
            if (dataSubscription != null && !dataSubscription.isUnsubscribed()) {
                dataSubscription.unsubscribe();
            }
        }
    };
    private Unbinder unbinder;
    private Subject<FilterDataTuple, FilterDataTuple> filter$;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApp.getScheduleComponent(this.getContext()).inject(this);

        filter$ = BehaviorSubject.create();

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_main, container, false);
        unbinder = ButterKnife.bind(this, layout);


        refreshes$ = RxSwipeRefreshLayout.refreshes(swipeRefresh);


        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        adapter = new ListAdapter();
        adapter.registerTypeMapping(new SubstituteViewWrapper.TypeMapper());
        adapter.registerTypeMapping(new LabelViewWrapper.TypeMapper());

        recyclerView.setAdapter(adapter);
        return layout;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSchedulePresenter.FilterSink.bindIntent(this.adapter);
        mSchedulePresenter.ReloadSink.bindIntent(this);
        this.ScheduleDataSink.bindIntent(mSchedulePresenter);

        //mSchedulePresenter.invokeUpdate();
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
    public Observable<Unit> reloadIntent$() {
        return RxSwipeRefreshLayout.refreshes(swipeRefresh)
                .doOnNext(_void_ -> Timber.d("refresh"))
                .map(_void_ -> Unit.unit());
    }

    @Override
    public Observable<FilterDataTuple> filterIntent$() {

        return filter$.asObservable();
    }


    private java.util.List<ViewWrapper> wrapData(List<Entry> list) {
        return list.bind(entry -> List.<ViewWrapper>nil()
                .append(List.list(ImmutableLabelViewWrapper.builder()
                        .entry(entry)
                        .build()))

                .append(List.iterableList(entry.substitutes())
                        .map(substitute -> ImmutableSubstituteViewWrapper.builder()
                                .substitute(substitute)
                                .build())))
                .toJavaList();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        ScheduleDataSink.unbind();
        mSchedulePresenter.detachView(true);
    }

    /* -------------------------------------------------------*/
    @Override
    public void updateView(List<? extends Entry> entries) {
    }

    @Override
    public void clearView() {
        adapter.clear();
    }

}
