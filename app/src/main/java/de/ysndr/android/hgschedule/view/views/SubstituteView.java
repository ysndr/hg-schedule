package de.ysndr.android.hgschedule.view.views;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;

import de.ysndr.android.hgschedule.R;

/**
 * TODO: document your custom view class.
 */
public class SubstituteView extends ConstraintLayout {
/*    @BindView(R.id.title_text) TextView title;
    @BindView(R.id.caption_text) TextView caption;*/

    public SubstituteView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_substitute, this);
//        ButterKnife.bind(this);
    }


//
//    public void setCaption(@StringRes int caption) {
//        this.caption.setText(caption);
//    }

}
