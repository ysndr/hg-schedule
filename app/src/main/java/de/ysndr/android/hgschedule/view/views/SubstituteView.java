package de.ysndr.android.hgschedule.view.views;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.ysndr.android.hgschedule.R;
import timber.log.Timber;

/**
 * TODO: document your custom view class.
 */
public class SubstituteView extends ConstraintLayout {

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
    
    public SubstituteView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        Timber.d("inflate view");
        inflate(getContext(), R.layout.view_substitute, this);
        ButterKnife.bind(this);
    }


    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void setDescription(String description) {
        this.description.setText(description);
    }

    public void setAbsent(String absent) {
        this.absent.setText(absent);
    }

    public void setSubst(String subst) {
        this.subst.setText(subst);
    }

    public void setLessons(String lessons) {
        this.lessons.setText(lessons);
    }

    public void setClasses(String classes) {
        this.classes.setText(classes);
    }

    public void setRoom(String room) {
        this.room.setText(room);
    }
}
