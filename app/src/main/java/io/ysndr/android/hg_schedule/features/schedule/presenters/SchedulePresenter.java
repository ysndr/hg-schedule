package io.ysndr.android.hg_schedule.features.schedule.presenters;

import javax.inject.Inject;

import fj.Ord;
import fj.Unit;
import fj.data.List;
import fj.data.Option;
import fj.data.Set;
import io.ysndr.android.hg_schedule.features.schedule.inject.CombinedDataService;
import io.ysndr.android.hg_schedule.features.schedule.models.Entry;
import io.ysndr.android.hg_schedule.features.schedule.models.ImmutableEntry;
import io.ysndr.android.hg_schedule.features.schedule.util.Filter;
import io.ysndr.android.hg_schedule.features.schedule.util.FilterDataTuple;
import io.ysndr.android.hg_schedule.features.schedule.util.Presentable;
import io.ysndr.android.hg_schedule.features.schedule.util.reactive.FilterIntentSink;
import io.ysndr.android.hg_schedule.features.schedule.util.reactive.FilterIntentSource;
import io.ysndr.android.hg_schedule.features.schedule.util.reactive.ReloadIntentSink;
import io.ysndr.android.hg_schedule.features.schedule.util.reactive.ReloadIntentSource;
import io.ysndr.android.hg_schedule.features.schedule.util.reactive.ScheduleDataSource;
import io.ysndr.android.hg_schedule.features.schedule.view.ScheduleListView;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.BehaviorSubject;
import rx.subjects.Subject;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

/**
 * Created by yannik on 10/10/16.
 */

public class SchedulePresenter implements ISchedulePresenter, ScheduleDataSource {


    @Inject
    CombinedDataService mDataService;

    Subject<Presentable<List<Entry>>, Presentable<List<Entry>>> values$;

    CompositeSubscription subscriptions;

    Set<Filter<FilterDataTuple, Boolean>> filters;


    public final FilterIntentSink FilterSink = new FilterIntentSink() {
        @Override
        public void bindIntent(FilterIntentSource source) {

            Subscription subscription = source.filterIntent$()
                    .doOnNext(filterDataTuple ->
                            Timber.d("Toggle filter for entry_id: " + filterDataTuple.entry().some().id()))
                    .map(original -> {
                        //if (original.entry().isNone()) throw new IllegalArgumentException("Entry may not be None!");
                        return Filter.of( // original : FilterDataTuple
                                (FilterDataTuple tested) -> !tested.entry().some().id()
                                        .equals(original.entry().some().id()),
                                original.entry().some().id().hashCode());
                    })

                    .doOnNext(filter ->
                            filters = filters.member(filter)
                                    ? filters.delete(filter)
                                    : filters.insert(filter))
                    .doOnNext(filter -> Timber.d("Active filters: " + filters.toString()))
                    .map(filterData -> Unit.unit())
                    .flatMap(unit -> update())
                    .subscribe(values$::onNext, values$::onError);

            subscriptions.add(subscription);
        }
    };

    public final ReloadIntentSink ReloadSink = new ReloadIntentSink() {
        @Override
        public void bindIntent(ReloadIntentSource source) {
            Subscription subscription = source.reloadIntent$()
                    .map(unit -> Presentable.<List<Entry>>of(true, Option.none()))
                    .flatMap(listPresentable -> update().startWith(listPresentable))
                    .doOnNext(listPresentable -> Timber.d("Data received: " + listPresentable.result().isSome()))
                    .doOnNext(listPresentable -> Timber.d("Data received: " + listPresentable.result().orSome(List.nil())))
                    .doOnError(e -> Timber.e(e))
                    .subscribe(values$::onNext, values$::onError);

            subscriptions.add(subscription);
        }
    };


    @Inject
    public SchedulePresenter(CombinedDataService dataService) {
        mDataService = dataService;
        this.values$ = BehaviorSubject.create();
        this.subscriptions = new CompositeSubscription();
        this.filters = Set.empty(Ord.hashOrd());
    }


    @Override
    public Observable<Presentable<List<Entry>>> data$() {
        return values$.observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<Presentable<List<Entry>>> update() {
        Timber.d("mDataService: %s", mDataService);
        return mDataService.getData$()
                .map(schedule -> List.iterableList(schedule.entries()))
                .map(entries -> entries
                        .map(entry -> ImmutableEntry.copyOf(entry).withSubstitutes(
                                List.iterableList(entry.substitutes())
                                        .filter(substitute -> {
                                            FilterDataTuple filterData = FilterDataTuple.of(Option.some(entry), Option.some(substitute));
                                            return filters.toList()
                                                    .forall(filter -> filter.filter().call(filterData));
                                        })
                                        .toJavaList()
                        )).map(entry -> (Entry) entry)
                )
                .map(entries -> Presentable.of(false, Option.some(entries)));
    }



/*------------------------------------------------------------*/

    @Override
    public void invokeUpdate() {

    }

    @Override
    public void toggleEntry(String id) {

    }

    @Override
    public void attachView(ScheduleListView view) {

    }

    @Override
    public void detachView(boolean retainInstance) {
        subscriptions.clear();
    }
}
