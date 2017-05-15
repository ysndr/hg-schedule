package de.ysndr.android.hgschedule.view;

import com.airbnb.epoxy.TypedEpoxyController;

import de.ysndr.android.hgschedule.state.State;
import de.ysndr.android.hgschedule.view.models.EntryCarouselModelGroup;
import fj.data.List;

/**
 * Created by yannik on 5/12/17.
 */

public class StateController extends TypedEpoxyController<State> {
    @Override
    protected void buildModels(State state) {
        state.union().continued(
            error -> {
            },
            data -> {
                List.iterableList(data.schedule().entries())
                    .foreachDoEffect(entry -> add(new EntryCarouselModelGroup(entry)));
            },
            empty -> {
            });
    }
}
