package de.ysndr.android.hgschedule.view.models;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.airbnb.epoxy.EpoxyAttribute;
import com.airbnb.epoxy.EpoxyModel;
import com.airbnb.epoxy.EpoxyModelClass;
import com.airbnb.epoxy.EpoxyModelWithView;

import de.ysndr.android.hgschedule.view.views.EpoxyListView;
import io.vavr.collection.List;

/**
 * Created by yannik on 5/12/17.
 */
@EpoxyModelClass
public abstract class EpoxyListModel extends EpoxyModelWithView<EpoxyListView> {

    @EpoxyAttribute
    List<? extends EpoxyModel<?>> models;
//    @EpoxyAttribute
//    int numItemsExpectedOnDisplay;
@EpoxyAttribute(hash = false)
RecyclerView.RecycledViewPool recycledViewPool;

    @Override
    public void bind(EpoxyListView carousel) {
        // If there are multiple carousels showing the same item types, you can benefit by having a
        // shared view pool between those carousels
        // so new views aren't created for each new carousel.
//        if (recycledViewPool != null) {
//            carousel.setRecycledViewPool(recycledViewPool);
//        }

//        if (numItemsExpectedOnDisplay != 0) {
//            carousel.setInitialPrefetchItemCount(numItemsExpectedOnDisplay);
//        }

        carousel.setModels(models);
        carousel.setFocusableInTouchMode(false);
    }

    @Override
    public void unbind(EpoxyListView carousel) {
        carousel.clearModels();
    }


    @Override
    protected EpoxyListView buildView(ViewGroup parent) {
        return new EpoxyListView(parent.getContext(), null);
    }

    @Override
    public boolean shouldSaveViewState() {
        // Save the state of the scroll position
        return false;
    }
}
