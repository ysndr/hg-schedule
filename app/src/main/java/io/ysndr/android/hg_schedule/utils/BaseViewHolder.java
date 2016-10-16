package io.ysndr.android.hg_schedule.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by yannik on 10/16/16.
 */

public abstract class BaseViewHolder extends RecyclerView.ViewHolder {

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public static int getType() {
        return 0;
    }
}
