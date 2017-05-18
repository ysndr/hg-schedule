package de.ysndr.android.hgschedule.view.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.ysndr.android.hgschedule.R;

public class HeaderView extends LinearLayout {

    @BindView(R.id.button_info_list_label)
    ImageView info;
    @BindView(R.id.text_label)
    TextView title;


    public HeaderView(Context context) {
        this(context, null);
    }

    public HeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeaderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
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