package de.ysndr.android.hgschedule.view.adapters;


import de.ysndr.android.hgschedule.state.models.School;
import de.ysndr.android.hgschedule.util.reactive.ClickSource;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import io.vavr.collection.List;

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

        List.ofAll(_wrappers)
                .filter(wrapper -> wrapper instanceof SchoolLabelViewWrapper)
                .map(wrapper -> (SchoolLabelViewWrapper) wrapper);

        io.vavr.collection.List.ofAll(wrappers)
                .filter(wrapper -> wrapper instanceof SchoolLabelViewWrapper)
                .map(wrapper -> (SchoolLabelViewWrapper) wrapper)
                .map(wrapper -> wrapper.click$())
            .fold(Observable.empty(), (acc, click$) -> acc.mergeWith(click$))
                .subscribe(click$::onNext, click$::onError);
    }

    @Override
    public Observable<School> getClick() {
        return click$;
    }
}