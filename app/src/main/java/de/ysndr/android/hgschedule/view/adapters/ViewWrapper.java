package de.ysndr.android.hgschedule.view.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by yannik on 12/19/16.
 */


public abstract class ViewWrapper {


    public abstract void onBindViewHolder(RecyclerView.ViewHolder viewHolder);

    public void unbind() {
    }

    public abstract TypeMapper typeMapper();

    public abstract static class TypeMapper {
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            int layout;
            View view;
            LayoutInflater inflater = LayoutInflater
                    .from(parent.getContext());

            layout = viewID();
            view = inflater.inflate(layout, parent, false);
            return createViewHolder(view);
        }


        public abstract int viewID();

        public abstract int TYPE();

        public abstract RecyclerView.ViewHolder createViewHolder(View view);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }

}
