package io.ysndr.android.hg_schedule.features.schedule.view;


import fj.data.List;
import fj.data.Option;
import io.ysndr.android.hg_schedule.features.schedule.models.Entry;
import io.ysndr.android.hg_schedule.features.schedule.util.FilterDataTuple;
import io.ysndr.android.hg_schedule.features.schedule.util.reactive.DialogRequestIntentSource;
import io.ysndr.android.hg_schedule.features.schedule.util.reactive.FilterIntentSource;
import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

/**
 * Created by yannik on 12/19/16.
 */

public class ListAdapter extends DelegatingListAdapter implements FilterIntentSource, DialogRequestIntentSource {

    Subject<FilterDataTuple, FilterDataTuple> filter$;
    Subject<Entry, Entry> dialog$;

    public ListAdapter() {
        super();
        filter$ = PublishSubject.create();
        dialog$ = PublishSubject.create();
    }

    @Override
    public void setContent(java.util.List<ViewWrapper> _wrappers) {
        super.setContent(_wrappers);
        List<LabelViewWrapper> _wrapper = List.iterableList(wrappers)
                .filter(wrapper -> wrapper instanceof LabelViewWrapper)
                .map(wrapper -> (LabelViewWrapper) wrapper);

        _wrapper
                .map(wrapper -> wrapper.filterObsrv()
                        .map(entry -> FilterDataTuple.of(Option.some(entry), Option.none())))
                .foldLeft((acc, filter$) -> acc.mergeWith(filter$), Observable.<FilterDataTuple>empty())
                .subscribe(filter$::onNext, filter$::onError);

        _wrapper
                .map(wrapper -> wrapper.dialogRequestObsrv())
                .foldLeft((acc, dialogRequest$) -> acc.mergeWith(dialogRequest$), Observable.<Entry>empty())
                .subscribe(dialog$::onNext, dialog$::onError);
    }

    @Override
    public Observable<FilterDataTuple> filterIntent$() {
        return filter$;
    }

    @Override
    public Observable<Entry> dialogRequest$() {
        return dialog$;
    }

}
