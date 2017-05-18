package de.ysndr.android.hgschedule.view.models;

import com.airbnb.epoxy.EpoxyAttribute;
import com.airbnb.epoxy.EpoxyModel;
import com.airbnb.epoxy.EpoxyModelClass;

import java.text.SimpleDateFormat;
import java.util.Locale;

import de.ysndr.android.hgschedule.R;
import de.ysndr.android.hgschedule.state.models.Entry;
import de.ysndr.android.hgschedule.view.views.HeaderView;

/**
 * Created by yannik on 5/12/17.
 */


@EpoxyModelClass(layout = R.layout.model_header)
public abstract class HeaderModel extends EpoxyModel<HeaderView> {

    @EpoxyAttribute
    Entry entry;

//    @BindView(R.id.button_info_list_label)
//    Button info;
//    @BindView(R.id.text_label)
//    TextView title;
//
//    Unbinder unbinder;


    @Override
    public void bind(HeaderView view) {
        // unbinder = ButterKnife.bind(this, view);
        view.setTitle(new SimpleDateFormat("MMMM dd", Locale.getDefault()).format(entry.date().day()));
//        view.setCaption(caption);
    }

    @Override
    public void unbind(HeaderView view) {
        super.unbind(view);
//        unbinder.unbind();
    }
}