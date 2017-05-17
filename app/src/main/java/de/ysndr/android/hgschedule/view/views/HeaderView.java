package de.ysndr.android.hgschedule.view.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import de.ysndr.android.hgschedule.R;

public class HeaderView extends LinearLayout {

    @BindView(R.id.button_info_list_label)
    Button info;
    @BindView(R.id.label_list_label)
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
        LayoutInflater.from(this.getContext()).inflate(R.layout.view_header, this);
//        ButterKnife.bind(this);
    }

    public void setTitle(String title) {
//        this.title.setText(title);
    }

//    public void setCaption(@StringRes int caption) {
//        this.caption.setText(caption);
//    }
}