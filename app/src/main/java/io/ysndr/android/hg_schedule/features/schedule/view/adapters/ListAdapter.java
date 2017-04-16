package io.ysndr.android.hg_schedule.features.schedule.view.adapters;


import fj.data.List;
import io.ysndr.android.hg_schedule.features.schedule.models.Entry;
import io.ysndr.android.hg_schedule.features.schedule.util.reactive.DialogRequestIntentSource;
import io.ysndr.android.hg_schedule.features.schedule.util.reactive.FilterIntentSource;
import rx.Observable;
import rx.subjects.BehaviorSubject;
import rx.subjects.Subject;

/**
 * Created by yannik on 12/19/16.
 */

public class ListAdapter extends DelegatingListAdapter implements FilterIntentSource, DialogRequestIntentSource {

    Subject<Entry, Entry> filter$;
    Subject<Entry, Entry> dialog$;

    public ListAdapter() {
        super();
        filter$ = BehaviorSubject.create();
        dialog$ = BehaviorSubject.create();
    }

    @Override
    public void setContent(java.util.List<ViewWrapper> _wrappers) {
        super.setContent(_wrappers);
        List<LabelViewWrapper> _wrapper = List.iterableList(wrappers)
                .filter(wrapper -> wrapper instanceof LabelViewWrapper)
                .map(wrapper -> (LabelViewWrapper) wrapper);

        _wrapper
                .map(wrapper -> wrapper.filterObsrv())
                .foldLeft((acc, filter$) -> acc.mergeWith(filter$), Observable.<Entry>never())
                .subscribe(filter$::onNext, filter$::onError);

        _wrapper
                .map(wrapper -> wrapper.dialogRequestObsrv())
                .foldLeft((acc, dialogRequest$) -> acc.mergeWith(dialogRequest$), Observable.<Entry>never())
                .subscribe(dialog$::onNext, dialog$::onError);
    }

    @Override
    public Observable<Entry> filterIntent$() {
        return filter$.asObservable().onBackpressureDrop();
    }

    @Override
    public Observable<Entry> dialogRequest$() {
        return dialog$.asObservable().onBackpressureDrop();
    }

}
