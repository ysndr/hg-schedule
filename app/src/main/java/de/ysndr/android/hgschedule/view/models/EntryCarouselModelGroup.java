package de.ysndr.android.hgschedule.view.models;

import com.airbnb.epoxy.EpoxyModel;
import com.airbnb.epoxy.EpoxyModelGroup;

import de.ysndr.android.hgschedule.R;
import de.ysndr.android.hgschedule.state.models.Entry;
import fj.data.List;

/**
 * Created by yannik on 5/12/17.
 */

public class EntryCarouselModelGroup extends EpoxyModelGroup {
    public EntryCarouselModelGroup(Entry entry) {
        super(R.layout.model_group_entry, buildModels(entry).toJavaList());
        id(entry.id());
    }

    private static List<EpoxyModel<?>> buildModels(Entry entry) {
        return List.<EpoxyModel<?>>list()
            .snoc(new HeaderModel_()
                .id("header_" + entry.id())
                .entry(entry))
            .snoc(new EpoxyListModel_()
                .id("substitutes_" + entry.id())
                .models(List.iterableList(entry.substitutes())
                    .map(substitute -> new SubstituteModel_()
                        .id("substitute_" + substitute.hashCode())
                        .substitute(substitute)
                    )));
    }
}
