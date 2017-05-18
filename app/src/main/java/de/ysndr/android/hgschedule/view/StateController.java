package de.ysndr.android.hgschedule.view;

import android.support.v7.widget.RecyclerView;

import com.airbnb.epoxy.TypedEpoxyController;

import de.ysndr.android.hgschedule.state.State;
import de.ysndr.android.hgschedule.view.models.EntryCarouselModelGroup;
import fj.data.List;

/**
 * Created by yannik on 5/12/17.
 */

public class StateController extends TypedEpoxyController<State> {

    RecyclerView.RecycledViewPool recycledViewPool;

    StateController(RecyclerView.RecycledViewPool recycledViewPool) {
        super();
        this.recycledViewPool = recycledViewPool;
    }

    @Override
    protected void buildModels(State state) {
        state.union().continued(
            error -> {
            },
            data -> {
                List.iterableList(data.schedule().entries())
                    .foreachDoEffect(entry -> add(new EntryCarouselModelGroup(
                        entry, recycledViewPool)));
            },
            empty -> {
            });
    }
}
