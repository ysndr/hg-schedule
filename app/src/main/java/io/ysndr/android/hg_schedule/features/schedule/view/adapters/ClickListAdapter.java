package io.ysndr.android.hg_schedule.features.schedule.view.adapters;


import fj.data.List;
import io.ysndr.android.hg_schedule.features.schedule.models.School;
import io.ysndr.android.hg_schedule.features.schedule.util.reactive.ClickSource;
import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

/**
 * Created by yannik on 12/19/16.
 */

public class ClickListAdapter extends DelegatingListAdapter implements ClickSource {

    Subject click$;

    public ClickListAdapter() {
        super();
        click$ = PublishSubject.create();
    }

    @Override
    public void setContent(java.util.List<ViewWrapper> _wrappers) {
        super.setContent(_wrappers);

        List.iterableList(_wrappers)
                .filter(wrapper -> wrapper instanceof SchoolLabelViewWrapper)
                .map(wrapper -> (SchoolLabelViewWrapper) wrapper);

        List.iterableList(wrappers)
                .filter(wrapper -> wrapper instanceof SchoolLabelViewWrapper)
                .map(wrapper -> (SchoolLabelViewWrapper) wrapper)
                .map(wrapper -> wrapper.click$())
                .foldLeft((acc, click$) -> acc.mergeWith(click$), Observable.<School>empty())
                .subscribe(click$::onNext, click$::onError);
    }

    @Override
    public Observable<School> getClick() {
        return click$.asObservable();
    }
}