package de.ysndr.android.hgschedule.view.views;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.ysndr.android.hgschedule.R;

public class HeaderView extends ConstraintLayout {

    @BindView(R.id.button_info_list_label)
    Button info;
    @BindView(R.id.label_list_label)
    TextView title;


    public HeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_header, this);
        ButterKnife.bind(this);
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

//    public void setCaption(@StringRes int caption) {
//        this.caption.setText(caption);
//    }
}