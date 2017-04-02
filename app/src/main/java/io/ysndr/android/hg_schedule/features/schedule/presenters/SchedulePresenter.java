package io.ysndr.android.hg_schedule.features.schedule.presenters;

import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;

import javax.inject.Inject;

import fj.Ord;
import fj.Unit;
import fj.data.List;
import fj.data.Option;
import fj.data.Set;
import io.ysndr.android.hg_schedule.features.schedule.inject.CombinedDataService;
import io.ysndr.android.hg_schedule.features.schedule.models.Entry;
import io.ysndr.android.hg_schedule.features.schedule.models.ImmutableEntry;
import io.ysndr.android.hg_schedule.features.schedule.models.School;
import io.ysndr.android.hg_schedule.features.schedule.util.Filter;
import io.ysndr.android.hg_schedule.features.schedule.util.FilterDataTuple;
import io.ysndr.android.hg_schedule.features.schedule.util.Presentable;
import io.ysndr.android.hg_schedule.features.schedule.util.preferences.GsonPreferenceAdapter;
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
import timber.log.Timber;

/**
 * Created by yannik on 10/10/16.
 */

public class SchedulePresenter implements ISchedulePresenter, ScheduleDataSource {


    @Inject
    CombinedDataService mDataService;
    @Inject
    RxSharedPreferences preferences;

    Subject<Presentable<List<Entry>>, Presentable<List<Entry>>> values$;

    Set<Filter<FilterDataTuple, Boolean>> filters;


    public final FilterIntentSink FilterSink = new FilterIntentSink() {
        Subscription subscription;

        @Override
        public void bindIntent(FilterIntentSource source) {
            subscription = source.filterIntent$()
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
        }

        @Override
        public void unbind() {
            if (subscription != null && !subscription.isUnsubscribed())
                subscription.unsubscribe();
        }
    };

    public final ReloadIntentSink ReloadSink = new ReloadIntentSink() {
        Subscription subscription;

        @Override
        public void bindIntent(ReloadIntentSource source) {
            subscription = source.reloadIntent$()
                    .doOnNext(unit -> filters = filters.filter(f -> false))
                    .flatMap(unit -> update().startWith(Presentable.of(true, Option.none())))
                    .doOnNext(listPresentable -> Timber.d("Data received: " + listPresentable.result().isSome()))
                    .doOnNext(listPresentable -> Timber.d("Data received: " + listPresentable.result().orSome(List.nil())))
                    .doOnError(error -> Timber.e("an error occured"))
                    .doOnError(Timber::e)
                    .subscribe(values$::onNext, values$::onError);
        }

        @Override
        public void unbind() {
            if (subscription != null && !subscription.isUnsubscribed())
                subscription.unsubscribe();
        }
    };


    @Inject
    public SchedulePresenter(CombinedDataService dataService) {
        mDataService = dataService;
        this.values$ = BehaviorSubject.create();
        this.filters = Set.empty(Ord.hashOrd());
    }


    @Override
    public Observable<Presentable<List<Entry>>> data$() {
        return values$.observeOn(AndroidSchedulers.mainThread());
    }


    public Observable<Presentable<List<Entry>>> update() {
        Timber.d("mDataService: %s", mDataService);

        Preference<School> school = preferences.getObject("school", School.empty(), new GsonPreferenceAdapter<>(School.class));
        School schoolToLoad = school.get();

        if (schoolToLoad.equals(School.empty())) {
            return Observable.error(new Exception("No school selected"));
        }

        Timber.d("Loading data for school with id: %s", schoolToLoad.id());

        return mDataService.getData$(schoolToLoad.id())
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
        FilterSink.unbind();
        ReloadSink.unbind();
    }
}
