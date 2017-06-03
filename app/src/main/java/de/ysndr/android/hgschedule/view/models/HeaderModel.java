package de.ysndr.android.hgschedule.view.models;

import android.text.format.DateFormat;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyAttribute;
import com.airbnb.epoxy.EpoxyModel;
import com.airbnb.epoxy.EpoxyModelClass;
import com.jakewharton.rxbinding.view.RxView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.ysndr.android.hgschedule.R;
import de.ysndr.android.hgschedule.state.models.Entry;
import de.ysndr.android.hgschedule.view.views.HeaderView;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

import static com.airbnb.epoxy.EpoxyAttribute.Option.DoNotHash;

/**
 * Created by yannik on 5/12/17.
 */


@EpoxyModelClass(layout = R.layout.model_header)
public abstract class HeaderModel extends EpoxyModel<HeaderView> {

    @EpoxyAttribute
    Entry entry;

    @EpoxyAttribute(DoNotHash)
    Action1<Entry> dialogIntent;

    @EpoxyAttribute(DoNotHash)
    Action1<Entry> filterIntent;

    @BindView(R.id.button_info_list_label)
    ImageView info;
    @BindView(R.id.text_label)
    TextView title;

    Unbinder unbinder;
    CompositeSubscription subscriptions;



    @Override
    public void bind(HeaderView view) {
        unbinder = ButterKnife.bind(this, view);
        if (subscriptions == null) subscriptions = new CompositeSubscription();

        String date = DateFormat.getDateFormat(view.getContext())
            .format(entry.date().day());
        title.setText(date);

        subscriptions.add(
            RxView.clicks(title)
                .map(__ -> entry)
                .subscribe(filterIntent));

        subscriptions.add(
            RxView.clicks(info)
                .map(__ -> entry)
                .subscribe(dialogIntent));

//        view.setCaption(caption);
    }

    @Override
    public void unbind(HeaderView view) {

        super.unbind(view);
        if (unbinder != null) unbinder.unbind();
        if (subscriptions != null && !subscriptions.isUnsubscribed())
            subscriptions.clear();
    }
}
