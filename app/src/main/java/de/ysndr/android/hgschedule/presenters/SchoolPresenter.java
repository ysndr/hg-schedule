package de.ysndr.android.hgschedule.presenters;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import de.ysndr.android.hgschedule.inject.CombinedDataService;
import de.ysndr.android.hgschedule.state.models.School;
import de.ysndr.android.hgschedule.util.Presentable;
import de.ysndr.android.hgschedule.util.reactive.ReloadIntentSink;
import de.ysndr.android.hgschedule.util.reactive.ReloadIntentSource;
import de.ysndr.android.hgschedule.util.reactive.SchoolDataSource;
import fj.data.Option;
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
                    .map(res -> {
                        if (!res.isSuccessful()) {
                            Timber.e("Response Error: `%s`", res.message());
                        }

                        return res.isSuccessful()
                                ? Presentable.of(false, Option.some(res.body()))
                                : Presentable.of(false, Option.none());
                    });
        }

        @Override
        public void bindIntent(ReloadIntentSource source) {
            subscriptions.add(source.reloadIntent$()
                    .map(unit -> Presentable.<List<School>>of(true, Option.none()))
                    .flatMap(listPresentable -> update().startWith(listPresentable))
                    .doOnNext(listPresentable -> Timber.d("Data received: " + listPresentable.result().isSome()))
                    .doOnNext(listPresentable -> Timber.d("Data received: " + listPresentable.result().orSome(new ArrayList<>())))
                    .doOnError(e -> Timber.e("an error occured: %s", e))
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
