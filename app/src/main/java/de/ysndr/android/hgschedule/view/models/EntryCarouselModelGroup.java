package de.ysndr.android.hgschedule.view.models;

import android.support.v7.widget.RecyclerView;

import com.airbnb.epoxy.EpoxyModel;
import com.airbnb.epoxy.EpoxyModelGroup;
import com.jakewharton.rxrelay.BehaviorRelay;

import java.util.Random;

import de.ysndr.android.hgschedule.R;
import de.ysndr.android.hgschedule.state.models.Entry;
import fj.data.List;

/**
 * Created by yannik on 5/12/17.
 */

public class EntryCarouselModelGroup extends EpoxyModelGroup {
    public EntryCarouselModelGroup(Entry entry,
                                   RecyclerView.RecycledViewPool recycledViewPool,
                                   BehaviorRelay<Entry> dialogReq$,
                                   BehaviorRelay<Entry> filterReq$) {
        super(R.layout.model_group_entry, buildModels(
            entry,
            recycledViewPool,
            dialogReq$,
            filterReq$).toJavaList());
        id(entry.id());
    }

    private static List<EpoxyModel<?>> buildModels(Entry entry,
                                                   RecyclerView.RecycledViewPool recycledViewPool,
                                                   BehaviorRelay<Entry> dialogReq$,
                                                   BehaviorRelay<Entry> filterReq$) {
        return List.<EpoxyModel<?>>list()
            .snoc(new HeaderModel_()
                .id("header_" + entry.id())
                .filterIntent(filterReq$.asAction())
                .dialogIntent(dialogReq$.asAction())
                .entry(entry))
//            .snoc(new SubstituteListModelGroup(entry));

            .snoc(new EpoxyListModel_()
                .id("substitutes_" + entry.id())
                .recycledViewPool(recycledViewPool)
                .models(List.iterableList(entry.substitutes())
                    .map(substitute -> new SubstituteModel_()
                        .id("substitute_" + substitute.hashCode() + "@" + entry.id() + ":" + new Random().nextLong())
                        .substitute(substitute)
                    )));
    }
}
