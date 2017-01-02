package io.ysndr.android.hg_schedule.features.schedule.view;


import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;

import org.immutables.value.Value;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.ysndr.android.hg_schedule.R;
import io.ysndr.android.hg_schedule.features.schedule.models.Entry;
import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

/**
 * Created by yannik on 12/19/16.
 */

@Value.Immutable
public abstract class LabelViewWrapper extends ViewWrapper {

    private final CompositeSubscription subscriptions = new CompositeSubscription();
    private final Subject<Entry, Entry> filter$ = PublishSubject.create();

    public abstract Entry entry();

    @Value.Default
    @Value.Auxiliary
    public Observable<Entry> filterObsrv() {
        return filter$.asObservable();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder) {
        ViewHolder holder = (ViewHolder) viewHolder;
        holder.label.setText(DateFormat.format("yyyy MM dd", entry().day()));

        subscriptions.add(RxView.clicks(holder.label)
                .doOnNext(_void_ -> Timber.d("Label clicked"))
                .map(_void_ -> entry())
                .subscribe(filter$::onNext, filter$::onError));
    }

    @Override
    public void unbind() {
        super.unbind();
        subscriptions.clear();
    }

    @Override
    public ViewWrapper.TypeMapper typeMapper() {
        return new LabelViewWrapper.TypeMapper();
    }

    /* ---------------------------------------------------- */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.label_list_label)
        TextView label;


        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public static class TypeMapper extends ViewWrapper.TypeMapper {

        @Override
        public int viewID() {
            return R.layout.list_label;
        }

        @Override
        public int TYPE() {
            return 1;
        }

        @Override
        public RecyclerView.ViewHolder createViewHolder(View view) {
            return new ViewHolder(view);
        }

    }

}
