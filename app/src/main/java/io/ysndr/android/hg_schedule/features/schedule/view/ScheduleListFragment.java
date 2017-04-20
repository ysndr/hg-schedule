package io.ysndr.android.hg_schedule.features.schedule.view;


import android.annotation.SuppressLint;
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
import com.pacoworks.rxtuples.RxTuples;

import org.javatuples.Pair;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.ysndr.rxvaluestore.MemoryStore;
import fj.Unit;
import fj.data.List;
import fj.data.Option;
import fj.data.Set;
import io.reactivecache.ReactiveCache;
import io.ysndr.android.hg_schedule.MyApp;
import io.ysndr.android.hg_schedule.R;
import io.ysndr.android.hg_schedule.features.schedule.inject.RemoteDataService;
import io.ysndr.android.hg_schedule.features.schedule.middleware.AuthMiddleware;
import io.ysndr.android.hg_schedule.features.schedule.middleware.DataMiddleware;
import io.ysndr.android.hg_schedule.features.schedule.middleware.TransformationMiddleware;
import io.ysndr.android.hg_schedule.features.schedule.models.Entry;
import io.ysndr.android.hg_schedule.features.schedule.models.Schedule;
import io.ysndr.android.hg_schedule.features.schedule.util.Presentable;
import io.ysndr.android.hg_schedule.features.schedule.view.adapters.ImmutableLabelViewWrapper;
import io.ysndr.android.hg_schedule.features.schedule.view.adapters.ImmutableSubstituteViewWrapper;
import io.ysndr.android.hg_schedule.features.schedule.view.adapters.LabelViewWrapper;
import io.ysndr.android.hg_schedule.features.schedule.view.adapters.ListAdapter;
import io.ysndr.android.hg_schedule.features.schedule.view.adapters.SubstituteViewWrapper;
import io.ysndr.android.hg_schedule.features.schedule.view.adapters.ViewWrapper;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

/**
 * Created by yannik on 10/9/16.
 */

public class ScheduleListFragment extends Fragment {

    MemoryStore<Presentable<Pair<Set<TransformationMiddleware.Transformation<Schedule>>, Schedule>>> state;

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


    ListAdapter adapter;

    private Observable<Void> refreshes$;
    private Unbinder unbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApp.getScheduleComponent(this.getContext()).inject(this);

        subscriptions = new CompositeSubscription();
        state = MemoryStore.of(Presentable.of(true, Option.none()));
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

        subscriptions.addAll(
                refreshes$
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(Schedulers.io())
                        .compose(new AuthMiddleware().getLogin(prefs))
                        .compose(DataMiddleware.schedule(remote, cache))
                        .compose(new TransformationMiddleware().prependEmptyTransformations())
                        .compose(this.toPresentable())
                        .compose(state.update())
                        .compose(this.display())
                        .retry()
                        .repeat()
                        .subscribe(),

                adapter.filterIntent$()
                        .observeOn(Schedulers.computation())

                        .map(new TransformationMiddleware()::filterEntry)
                        .doOnNext(trans -> Timber.d("new transformation with seed `%s`", trans._seed()))

                        .flatMap(trans -> Observable.combineLatest(
                                Observable.just(trans),
                                state.observable()
                                        .filter(p -> p.result().isSome())
                                        .map(p -> p.result().some())
                                        .take(1),
                                RxTuples.toTripletFromSingle()))
                        .doOnNext(triplet -> Timber.d("applied transformer"))

                        .compose(new TransformationMiddleware().toggleTransformation())
                        .doOnNext(pair -> Timber.d("toggled transformation"))

                        .compose(this.toPresentable())
                        .compose(state.update())
//
//
//                .observeOn(AndroidSchedulers.mainThread())
                        .compose(this.display())
                        .subscribe(),

                adapter.dialogRequest$()
                        .subscribeOn(Schedulers.computation())
                        .throttleFirst(500, TimeUnit.MILLISECONDS)
                        .doOnNext(entry -> Timber.d("Showing information about entry `%s`", entry.id()))
                        .map(entry -> ScheduleDialogBuilder
                                .newScheduleDialog(entry.date().day(), entry.info())
                        )

                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(dialog -> {
                            dialog.show(getFragmentManager(), null);
                        })
        );

    }


    Observable.Transformer<
            Pair<Set<TransformationMiddleware.Transformation<Schedule>>, Schedule>,
            Presentable<Pair<Set<TransformationMiddleware.Transformation<Schedule>>, Schedule>>> toPresentable() {
        return source -> source.map(pair -> Presentable.of(false, Option.some(pair)));
    }

    Observable.Transformer<Presentable<Pair<Set<TransformationMiddleware.Transformation<Schedule>>, Schedule>>, Boolean> display() {
        return source -> source
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(presentable -> setLoading(presentable.loading()))
                .map(presentable -> presentable.result().some())
                .compose(new TransformationMiddleware().applyTransformations())
                .map(schedule -> wrapData(List.iterableList(schedule.entries())))
                .doOnNext(adapter::setContent)
                .doOnError(error -> {
                    this.showError(error);
                    this.setLoading(false);
                })
                .map(__ -> true)
                .onErrorReturn(e -> false);
    }





    public void setLoading(boolean loading) {
        swipeRefresh.setRefreshing(loading);
        progress.setVisibility(View.VISIBLE);
        if (!loading) {
            progress.setVisibility(View.GONE);
        }
    }


    @SuppressLint("ThrowableNotAtBeginning")
    public void showError(Throwable t) {
        String message = t.getMessage();
        Toast.makeText(
                this.getContext(),
                message,
                Toast.LENGTH_SHORT
        ).show();

        Timber.e("an error occured: %s", t);

    }


    public Observable<Unit> reloadIntent$() {
        return RxSwipeRefreshLayout.refreshes(swipeRefresh)
                .doOnNext(_void_ -> Timber.d("refresh"))
                .map(_void_ -> Unit.unit());
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
