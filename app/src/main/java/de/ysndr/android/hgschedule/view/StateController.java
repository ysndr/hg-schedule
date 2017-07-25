package de.ysndr.android.hgschedule.view;

import android.support.v7.widget.RecyclerView;

import com.airbnb.epoxy.EpoxyController;
import com.airbnb.epoxy.TypedEpoxyController;
import com.jakewharton.rxrelay2.BehaviorRelay;

import de.ysndr.android.hgschedule.state.State;
import de.ysndr.android.hgschedule.state.models.Entry;
import de.ysndr.android.hgschedule.view.models.HeaderModel_;
import de.ysndr.android.hgschedule.view.models.SubstituteModel_;
import fj.data.List;

/**
 * Created by yannik on 5/12/17.
 */

public class StateController extends TypedEpoxyController<State> {

    RecyclerView.RecycledViewPool recycledViewPool;
    BehaviorRelay<Entry> dialogReq$;
    BehaviorRelay<Entry> filterReq$;

    public StateController(RecyclerView.RecycledViewPool recycledViewPool,
                           BehaviorRelay<Entry> dialogReq$,
                           BehaviorRelay<Entry> filterReq$) {
        super();
        this.recycledViewPool = recycledViewPool;
        this.dialogReq$ = dialogReq$;
        this.filterReq$ = filterReq$;
    }

    @Override
    protected void buildModels(State state) {
        state.union().continued(
            error -> {
            },
            data -> {
                List.iterableList(data.schedule().entries())
                    .foreachDoEffect(entry -> addEntry(this, entry, dialogReq$, filterReq$));
            },
            empty -> {
            });
    }

    private void addEntry(EpoxyController controller,
                          Entry entry,
                          BehaviorRelay<Entry> dialogReq$,
                          BehaviorRelay<Entry> filterReq$) {

        new HeaderModel_()
            .id("header_" + entry.id())
            .filterIntent(filterReq$)
            .dialogIntent(dialogReq$)
            .entry(entry)
            .addTo(controller);

        List.iterableList(entry.substitutes())
            .zip(List.range(0, entry.substitutes().size()))
            .map(pair -> new SubstituteModel_()
                .id("substitute_" + pair._1().hashCode() + ":" + pair._2() + "@" + entry.id())
                .substitute(pair._1()))
            .foreachDoEffect(model -> model.addTo(controller));
    }

}
