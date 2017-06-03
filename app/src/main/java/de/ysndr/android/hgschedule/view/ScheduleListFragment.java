package de.ysndr.android.hgschedule.view;


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

import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.jakewharton.rxbinding.support.v4.widget.RxSwipeRefreshLayout;
import com.jakewharton.rxrelay.BehaviorRelay;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.ysndr.android.hgschedule.MyApp;
import de.ysndr.android.hgschedule.R;
import de.ysndr.android.hgschedule.functions.DataFunc;
import de.ysndr.android.hgschedule.functions.TransfFunc;
import de.ysndr.android.hgschedule.functions.models.Transformation;
import de.ysndr.android.hgschedule.inject.RemoteDataService;
import de.ysndr.android.hgschedule.state.Empty;
import de.ysndr.android.hgschedule.state.State;
import de.ysndr.android.hgschedule.state.StateError;
import de.ysndr.android.hgschedule.state.models.Entry;
import de.ysndr.android.hgschedule.state.models.Schedule;
import de.ysndr.android.hgschedule.view.adapters.ImmutableLabelViewWrapper;
import de.ysndr.android.hgschedule.view.adapters.ImmutableSubstituteViewWrapper;
import de.ysndr.android.hgschedule.view.adapters.ViewWrapper;
import fj.Ord;
import fj.data.List;
import fj.data.Set;
import io.reactivecache.ReactiveCache;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

/**
 * Created by yannik on 10/9/16.
 */

public class ScheduleListFragment extends Fragment {

    // Inputs
    BehaviorRelay<Void> refresh$;
    BehaviorRelay<Entry> dialogRequest$;
    BehaviorRelay<Entry> filterRequest$;

    // state
    BehaviorRelay<Set<Transformation<Schedule>>> transformers$;
    BehaviorRelay<State> state$;

    // effects
    Subscription stateSubscription;
    Subscription dialogShowSubscription;

    CompositeSubscription subscriptions;


    @BindView(R.id.content_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.content_swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.progress_spinner)
    ProgressBar progress;

    @Inject
    RxSharedPreferences prefs;
    @Inject
    RemoteDataService remote;
    @Inject
    ReactiveCache cache;

    RecyclerView.RecycledViewPool recycledViewPool;

    StateController controller;


    private Observable<Void> refreshes$;
    private Unbinder unbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApp.getScheduleComponent(this.getContext()).inject(this);

        subscriptions = new CompositeSubscription();

        state$ = BehaviorRelay.create(State.empty(Empty.of()));
        transformers$ = BehaviorRelay.create();
        refresh$ = BehaviorRelay.create();
        dialogRequest$ = BehaviorRelay.create();
        filterRequest$ = BehaviorRelay.create();

        recycledViewPool = new RecyclerView.RecycledViewPool();
        recycledViewPool.setMaxRecycledViews(R.layout.model_substitute, Integer.MAX_VALUE);
        recycledViewPool.setMaxRecycledViews(R.layout.model_header, Integer.MAX_VALUE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_main, container, false);
        unbinder = ButterKnife.bind(this, layout);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setRecycledViewPool(recycledViewPool);

        recyclerView.setItemViewCacheSize(20);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
/*
        adapter = new ListAdapter();
        adapter.registerTypeMapping(new SubstituteViewWrapper.TypeMapper());
        adapter.registerTypeMapping(new LabelViewWrapper.TypeMapper());
*/

        controller = new StateController(recycledViewPool, dialogRequest$, filterRequest$);
        recyclerView.setAdapter(controller.getAdapter());
        return layout;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Observable<Void> _refresh$ = RxSwipeRefreshLayout.refreshes(swipeRefresh)
            .onBackpressureDrop();
       /* Observable<Entry> _dialogRequest$ = adapter.dialogRequest$()
                .subscribeOn(Schedulers.computation())
                .throttleFirst(500, TimeUnit.MILLISECONDS);*/
//        Observable<Entry> _filterRequest$ = adapter.filterIntent$();

        // proxies
        _refresh$.subscribe(refresh$);
//        _dialogRequest$.subscribe(dialogRequest$);
//        _filterRequest$.subscribe(filterRequest$);


        Observable<State> freshState$ = refresh$
                .doOnNext(__ -> Timber.d("initiating refresh..."))
                .observeOn(Schedulers.io())
                .flatMap((__) -> DataFunc.refresh(prefs, remote, cache))
                .doOnNext(data -> Timber.d("new state: %s", data));

        Observable<Set<Transformation<Schedule>>> transformations$ = Observable.merge(
                Observable.zip(transformers$, filterRequest$, TransfFunc.toggleCreateFilter()),
                setupClearFilter$());

        // proxies
        freshState$.subscribeOn(Schedulers.io()).subscribe(state$);
        transformations$.subscribeOn(Schedulers.computation()).subscribe(transformers$);


        Observable<State> stateAfter$ = TransfFunc.combineStateTransf(state$, transformers$).subscribeOn(Schedulers.computation());
        stateAfter$.compose(display()).subscribe();

        dialogRequest$.asObservable()
                .map(entry -> ScheduleDialogBuilder.newScheduleDialog(entry.date().day(), entry.info()))
                .doOnNext(dialog -> dialog.show(this.getFragmentManager(), "schedule entry info"))
                .subscribe();
    }


    private Observable<Set<Transformation<Schedule>>> setupClearFilter$() {
        return refresh$
                .map(__ -> Set.<Transformation<Schedule>>empty(Ord.hashEqualsOrd()));
    }

    Observable.Transformer<State, Boolean> display() {
        return source -> source
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(state -> setLoading(state.union().join(
                            error -> false,
                            data -> data.loading(),
                            empty -> empty.loading())))
                .doOnNext(state -> state.union().continued(
                    error -> controller.setData((State.error(error))),
                    data -> controller.setData(State.data(data)),
                    empty -> controller.setData(State.empty(empty))
                ))
            .doOnNext(state -> state.union().continued(
                this::showError,
                data -> {
                },
                empty -> {
                }
            ))
                .map(__ -> true)
                .doOnError(e -> Timber.d("an error occured somewhere"))
                .onErrorReturn(e -> false);
    }

    public void setLoading(boolean loading) {
        swipeRefresh.setRefreshing(loading);
        progress.setVisibility(View.VISIBLE);
        if (!loading) {
            progress.setVisibility(View.GONE);
        }
    }

    public void showError(StateError<?> error) {
        String message = error.message().orSome("internal error");
        Toast.makeText(
                this.getContext(),
                message,
                Toast.LENGTH_SHORT
        ).show();

        Timber.e("an error occured: %s", message);

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
        subscriptions.clear();
    }

}
