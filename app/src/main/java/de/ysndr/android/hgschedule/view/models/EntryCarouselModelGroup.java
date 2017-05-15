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
            .append(List.list(new HeaderModel_()
                .entry(entry)))
            .append(List.list(new SubstituteCarousel_().models(
                List.iterableList(entry.substitutes())
                    .map(substitute -> new SubstituteModel_().substitute(substitute)))));
    }
}
