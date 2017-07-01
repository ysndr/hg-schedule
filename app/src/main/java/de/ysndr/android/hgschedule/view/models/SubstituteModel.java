package de.ysndr.android.hgschedule.view.models;

import android.view.View;
import android.widget.TextView;

import com.airbnb.epoxy.EpoxyAttribute;
import com.airbnb.epoxy.EpoxyModelClass;
import com.airbnb.epoxy.EpoxyModelWithHolder;

import butterknife.BindView;
import de.ysndr.android.hgschedule.R;
import de.ysndr.android.hgschedule.state.models.Substitute;
import de.ysndr.android.hgschedule.view.BaseEpoxyHolder;
import timber.log.Timber;

/**
 * Created by yannik on 5/12/17.
 */

@EpoxyModelClass(layout = R.layout.model_substitute)
public abstract class SubstituteModel extends EpoxyModelWithHolder<SubstituteModel.SubstituteHolder> {

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

    @Override
    public void bind(SubstituteHolder holder) {
        super.bind(holder);
        Timber.d("in `bind()`");

        holder.absent.setText(substitute.absent());
        holder.subst.setText(substitute.substitute());
        holder.classes.setText(substitute.classes());
        holder.lessons.setText(substitute.hour());
        holder.description.setText(substitute.description());
        holder.room.setText(substitute.room());
        holder.title.setText(substitute.absent());
        // TODO maybe change to a visible/invisible filtering
    }


    static class SubstituteHolder extends BaseEpoxyHolder {
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
        TextView room;

        @Override
        protected void bindView(View itemView) {
            super.bindView(itemView);
            Timber.d("binding view to SubstituteHolder");
        }
    }


}
