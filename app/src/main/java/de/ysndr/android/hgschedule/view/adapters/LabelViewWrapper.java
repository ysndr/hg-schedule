package de.ysndr.android.hgschedule.view.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;

import org.immutables.value.Value;

import java.text.DateFormat;

import butterknife.BindView;
import de.ysndr.android.hgschedule.R;
import de.ysndr.android.hgschedule.state.models.Entry;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;
import timber.log.Timber;

/**
 * Created by yannik on 12/19/16.
 */

@Value.Immutable
public abstract class LabelViewWrapper extends ViewWrapper {

    private final CompositeDisposable disposables = new CompositeDisposable();
    private final Subject<Entry> filter$ = BehaviorSubject.create();
    private final Subject<Entry> dialogRequest$ = BehaviorSubject.create();

    public abstract Entry entry();


    @Value.Auxiliary
    public Observable<Entry> filterObsrv() {
        return filter$;
    }

    @Value.Auxiliary
    public Observable<Entry> dialogRequestObsrv() {
        return dialogRequest$;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder) {
        ViewHolder holder = (ViewHolder) viewHolder;
        holder.label.setText(DateFormat.getInstance().format(entry().date().day()));

        disposables.add(RxView.clicks(holder.label)
                .doOnNext(_void_ -> Timber.d("Label clicked"))
                .map(_void_ -> entry())
                .subscribe(filter$::onNext, filter$::onError));

        disposables.add(RxView.clicks(holder.button)
                .doOnNext(_void_ -> Timber.d("Dialog request triggered"))
                .map(_void_ -> entry())
                .subscribe(dialogRequest$::onNext, dialogRequest$::onError));
    }

    @Override
    public void unbind() {
        super.unbind();
        disposables.clear();
    }

    @Override
    public ViewWrapper.TypeMapper typeMapper() {
        return new LabelViewWrapper.TypeMapper();
    }

    /* ---------------------------------------------------- */
    public static class ViewHolder extends ViewWrapper.ViewHolder {

        @BindView(R.id.text_label)
        TextView label;

        @BindView(R.id.button_info_list_label)
        ImageView button;

        ViewHolder(View view) {
            super(view);
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
