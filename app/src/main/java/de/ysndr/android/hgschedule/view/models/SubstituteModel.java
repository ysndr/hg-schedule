package de.ysndr.android.hgschedule.view.models;

import com.airbnb.epoxy.EpoxyAttribute;
import com.airbnb.epoxy.EpoxyModel;
import com.airbnb.epoxy.EpoxyModelClass;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.ysndr.android.hgschedule.R;
import de.ysndr.android.hgschedule.state.models.Substitute;
import de.ysndr.android.hgschedule.view.views.SubstituteView;
import timber.log.Timber;

/**
 * Created by yannik on 5/12/17.
 */

@EpoxyModelClass(layout = R.layout.model_substitute)
public abstract class SubstituteModel extends EpoxyModel<SubstituteView> {

    @EpoxyAttribute
    Substitute substitute;
    /*
        @BindView(R.id.text_title_card_schedule_item)
        TextView title;
        @BindView(R.id.text_description_card_schedule_item)
        TextView description;
        @BindView(R.id.text_teacher_absent_card_schedule_item)
        TextView absent;
        @BindView(R.id.text_teacher_new_card_schedule_item)
        TextView subst;
        @BindView(R.id.text_lessons_card_schedule_item)
        TextView lessons;
        @BindView(R.id.text_classes_card_schedule_item)
        TextView classes;

        @BindView(R.id.text_room_card_schedule_item)
        TextView room;*/
    Unbinder unbinder;

    @Override
    public void bind(SubstituteView view) {
        super.bind(view);
        unbinder = ButterKnife.bind(this, view);

        Timber.d("in `bind()`");

        view.setAbsent(substitute.absent());
        view.setSubst(substitute.substitute());
        view.setClasses(substitute.classes());
        view.setLessons(substitute.hour());
        view.setDescription(substitute.description());
        view.setRoom(substitute.room());
        view.setTitle(substitute.absent());
        // TODO maybe change to a visible/invisible filtering
    }

    @Override
    public void unbind(SubstituteView view) {
        super.unbind(view);

        Timber.d("in `unbind(...)`");
        unbinder.unbind();
    }

    @Override
    public void onViewDetachedFromWindow(SubstituteView view) {
        super.onViewDetachedFromWindow(view);
        Timber.d("in `detatch`");
    }
}
