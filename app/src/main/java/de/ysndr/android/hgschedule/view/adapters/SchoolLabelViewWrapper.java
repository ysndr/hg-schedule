package de.ysndr.android.hgschedule.view.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jakewharton.rxbinding2.view.RxView;

import org.immutables.value.Value;

import java.io.Serializable;

import butterknife.BindView;
import de.ysndr.android.hgschedule.R;
import de.ysndr.android.hgschedule.state.models.School;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subjects.BehaviorSubject;
import timber.log.Timber;

/**
 * Created by yannik on 1/17/17.
 */
@Value.Immutable
public abstract class SchoolLabelViewWrapper extends ViewWrapper implements Serializable {

    private final BehaviorSubject click$ = BehaviorSubject.create();
    private final CompositeDisposable disposables = new CompositeDisposable();

    public abstract School school();

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder) {
        ViewHolder holder = (ViewHolder) viewHolder;
        holder.name.setText(school().name());
        holder.variant.setText(school().variant());

        disposables.add(RxView.clicks(holder.itemView)
                .doOnNext(_void_ -> Timber.d("Label clicked"))
                .map(_void_ -> school())
                .subscribe(click$::onNext, click$::onError));
    }

    @Override
    public void unbind() {
        super.unbind();
        disposables.clear();
    }

    public Observable<School> click$() {
        return click$;
    }

    @Override
    public TypeMapper typeMapper() {
        return new SchoolLabelViewWrapper.TypeMapper();
    }


    /* ---------------------------------------------------- */
    public static class ViewHolder extends ViewWrapper.ViewHolder {

        @BindView(R.id.item_main_school_name)
        TextView name;

        @BindView(R.id.item_secondary_school_variant)
        TextView variant;

        ViewHolder(View view) {
            super(view);
        }

    }

    public static class TypeMapper extends ViewWrapper.TypeMapper {

        @Override
        public int viewID() {
            return R.layout.spinner_item_school;
        }

        @Override
        public int TYPE() {
            return 0;
        }

        @Override
        public RecyclerView.ViewHolder createViewHolder(View view) {
            return new SchoolLabelViewWrapper.ViewHolder(view);
        }

    }

}
