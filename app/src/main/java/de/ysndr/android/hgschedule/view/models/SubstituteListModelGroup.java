package de.ysndr.android.hgschedule.view.models;

import com.airbnb.epoxy.EpoxyModel;
import com.airbnb.epoxy.EpoxyModelGroup;

import de.ysndr.android.hgschedule.R;
import de.ysndr.android.hgschedule.state.models.Entry;
import io.vavr.collection.List;

/**
 * Created by yannik on 5/12/17.
 */

public class SubstituteListModelGroup extends EpoxyModelGroup {
    public SubstituteListModelGroup(Entry entry) {
        super(R.layout.model_group_substitutes, buildModels(entry).toJavaList());
        id("substitutes_" + entry.id());
    }

    private static List<EpoxyModel<?>> buildModels(Entry entry) {
        return List.<EpoxyModel<?>>empty()
            .appendAll(List.ofAll(entry.substitutes())
                .map(substitute -> new SubstituteModel_()
                    .id("substitute_" + substitute.hashCode())
                    .substitute(substitute)
                ));

    }
}
