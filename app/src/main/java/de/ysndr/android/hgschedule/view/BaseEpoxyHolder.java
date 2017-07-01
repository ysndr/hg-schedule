package de.ysndr.android.hgschedule.view;

import android.support.annotation.CallSuper;
import android.view.View;

import com.airbnb.epoxy.EpoxyHolder;

import butterknife.ButterKnife;

/**
 * Created by yannik on 7/1/17.
 */

public class BaseEpoxyHolder extends EpoxyHolder {

    @CallSuper
    @Override
    protected void bindView(View itemView) {
        ButterKnife.bind(this, itemView);
    }
}
