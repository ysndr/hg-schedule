package io.ysndr.android.hg_schedule.features.schedule.presenters;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import fj.data.Option;
import io.ysndr.android.hg_schedule.features.schedule.inject.CombinedDataService;
import io.ysndr.android.hg_schedule.features.schedule.models.School;
import io.ysndr.android.hg_schedule.features.schedule.util.Presentable;
import io.ysndr.android.hg_schedule.features.schedule.util.reactive.ReloadIntentSink;
import io.ysndr.android.hg_schedule.features.schedule.util.reactive.ReloadIntentSource;
import io.ysndr.android.hg_schedule.features.schedule.util.reactive.SchoolDataSource;
import rx.Observable;
import rx.subjects.BehaviorSubject;
import rx.subjects.Subject;
import timber.log.Timber;

/**
 * Created by yannik on 1/22/17.
 */

public class SchoolPresenter implements SchoolDataSource {

    @Inject
    CombinedDataService mDataService;

    Subject<Presentable<List<School>>, Presentable<List<School>>> values$;


    public final ReloadIntentSink reloadIntentSink = new ReloadIntentSink() {

        Observable<Presentable<List<School>>> update() {
            return mDataService.getSchools$()
                    .map(schools -> Presentable.of(false, Option.some(schools)));
        }

        @Override
        public void bindIntent(ReloadIntentSource source) {
            subscriptions.add(source.reloadIntent$()
                    .map(unit -> Presentable.<List<School>>of(true, Option.none()))
                    .flatMap(listPresentable -> update().startWith(listPresentable))
                    .doOnNext(listPresentable -> Timber.d("Data received: " + listPresentable.result().isSome()))
                    .doOnNext(listPresentable -> Timber.d("Data received: " + listPresentable.result().orSome(new ArrayList<>())))
                    .doOnError(e -> Timber.e(e))
                    .subscribe(values$::onNext, values$::onError));
        }

        @Override
        public void unbind() {
            subscriptions.clear();
        }
    };

    @Inject
    public SchoolPresenter(CombinedDataService dataService) {
        mDataService = dataService;
        this.values$ = BehaviorSubject.create();
    }

    @Override
    public Observable<Presentable<List<School>>> data$() {
        return values$.asObservable();
    }


}
