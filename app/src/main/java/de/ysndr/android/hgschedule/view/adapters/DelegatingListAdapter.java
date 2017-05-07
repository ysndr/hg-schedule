package de.ysndr.android.hgschedule.view.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

/**
 * Created by yannik on 12/19/16.
 */

public class DelegatingListAdapter extends RecyclerView.Adapter {
    protected List<ViewWrapper> wrappers;
    private Map<Integer, ViewWrapper.TypeMapper> wrapperTypeMapping;

    public DelegatingListAdapter() {
        wrappers = new ArrayList<>();
        // TODO: find a better data structure
        wrapperTypeMapping = new HashMap<>();
    }


    public void setContent(List<ViewWrapper> _wrappers) {
        for (ViewWrapper wrapper : wrappers) wrapper.unbind();
        wrappers.clear();
        wrappers.addAll(_wrappers);

        this.notifyDataSetChanged();
    }

    public void clear() {
        wrappers.clear();
    }

    public void registerTypeMapping(ViewWrapper.TypeMapper mapper) {
        wrapperTypeMapping.put(mapper.TYPE(), mapper);
    }

    public void unregisterTypeMapping(ViewWrapper.TypeMapper mapper) {
        wrapperTypeMapping.remove(mapper.TYPE());
    }

    @Override
    public int getItemViewType(int position) {
        return wrappers.get(position).typeMapper().TYPE();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Timber.d("TypeMapper: %s", wrapperTypeMapping.get(viewType).getClass().getCanonicalName());

        ViewWrapper.TypeMapper mapper = wrapperTypeMapping.get(viewType);
        if (mapper != null) return mapper.onCreateViewHolder(parent, viewType);
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        wrappers.get(position).onBindViewHolder(holder);
    }

    @Override
    public int getItemCount() {
        return wrappers.size();
    }


}
