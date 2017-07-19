package de.ysndr.android.hgschedule.view.models;

import android.text.format.DateFormat;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyAttribute;
import com.airbnb.epoxy.EpoxyModel;
import com.airbnb.epoxy.EpoxyModelClass;
import com.jakewharton.rxbinding2.view.RxView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.ysndr.android.hgschedule.R;
import de.ysndr.android.hgschedule.state.models.Entry;
import de.ysndr.android.hgschedule.view.views.HeaderView;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

import static com.airbnb.epoxy.EpoxyAttribute.Option.DoNotHash;

/**
 * Created by yannik on 5/12/17.
 */


@EpoxyModelClass(layout = R.layout.model_header)
public abstract class HeaderModel extends EpoxyModel<HeaderView> {

    @EpoxyAttribute
    Entry entry;

    @EpoxyAttribute(DoNotHash)
    Consumer<Entry> dialogIntent;

    @EpoxyAttribute(DoNotHash)
    Consumer<Entry> filterIntent;

    @BindView(R.id.button_info_list_label)
    ImageView info;
    @BindView(R.id.text_label)
    TextView title;

    Unbinder unbinder;
    CompositeDisposable disposables;



    @Override
    public void bind(HeaderView view) {
        unbinder = ButterKnife.bind(this, view);
        if (disposables == null) disposables = new CompositeDisposable();

        String date = DateFormat.getDateFormat(view.getContext())
            .format(entry.date().day());
        title.setText(date);

        disposables.add(
            RxView.clicks(title)
                .map(__ -> entry)
                .subscribe(filterIntent));

        disposables.add(
            RxView.clicks(info)
                .map(__ -> entry)
                .subscribe(dialogIntent));

//        view.setCaption(caption);
    }

    @Override
    public void unbind(HeaderView view) {

        super.unbind(view);
        if (unbinder != null) unbinder.unbind();
        if (disposables != null && !disposables.isDisposed())
            disposables.clear();
    }
}
