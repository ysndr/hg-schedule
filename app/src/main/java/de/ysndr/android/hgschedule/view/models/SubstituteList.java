package de.ysndr.android.hgschedule.view.models;

import android.view.ViewGroup;

import com.airbnb.epoxy.EpoxyAttribute;
import com.airbnb.epoxy.EpoxyModel;
import com.airbnb.epoxy.EpoxyModelClass;
import com.airbnb.epoxy.EpoxyModelWithView;

import de.ysndr.android.hgschedule.view.views.Carousel;
import fj.data.List;

/**
 * Created by yannik on 5/12/17.
 */
@EpoxyModelClass
public abstract class SubstituteList extends EpoxyModelWithView<Carousel> {

    @EpoxyAttribute
    List<? extends EpoxyModel<?>> models;
//    @EpoxyAttribute
//    int numItemsExpectedOnDisplay;
//    @EpoxyAttribute(hash = false) RecycledViewPool recycledViewPool;

    @Override
    public void bind(Carousel carousel) {
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
    }

    @Override
    public void unbind(Carousel carousel) {
        carousel.clearModels();
    }


    @Override
    protected Carousel buildView(ViewGroup parent) {
        return new Carousel(parent.getContext(), null);
    }

    @Override
    public boolean shouldSaveViewState() {
        // Save the state of the scroll position
        return false;
    }
}
