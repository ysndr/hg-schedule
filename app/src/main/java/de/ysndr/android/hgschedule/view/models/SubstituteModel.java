package de.ysndr.android.hgschedule.view.models;

import android.view.View;

import com.airbnb.epoxy.EpoxyAttribute;
import com.airbnb.epoxy.EpoxyModelClass;
import com.airbnb.epoxy.EpoxyModelWithHolder;

import butterknife.BindView;
import de.ysndr.android.hgschedule.R;
import de.ysndr.android.hgschedule.state.models.Substitute;
import de.ysndr.android.hgschedule.view.BaseEpoxyHolder;
import de.ysndr.android.hgschedule.view.views.SubstituteView;
import timber.log.Timber;

/**
 * Created by yannik on 5/12/17.
 */

@EpoxyModelClass(layout = R.layout.model_substitute)
public abstract class SubstituteModel extends EpoxyModelWithHolder<SubstituteModel.SubstituteHolder> {

    @EpoxyAttribute
    Substitute substitute;

    @Override
    public void bind(SubstituteHolder holder) {
        super.bind(holder);
        Timber.d("in `bind()`");

        holder.display(substitute);
    }

    static class SubstituteHolder extends BaseEpoxyHolder {

        @BindView(R.id.substitute_view)
        SubstituteView view;

        @Override
        protected void bindView(View itemView) {
            super.bindView(itemView);
            Timber.d("binding view to SubstituteHolder");
        }

        void display(Substitute substitute) {
            view.setTitle(substitute.absent());
            view.setSubst(substitute.substitute());
            view.setClasses(substitute.classes());
            view.setLessons(substitute.hour());
            view.setDescription(substitute.description());
            view.setRoom(substitute.room());
            view.setAbsent(substitute.absent());
            // TODO maybe change to a visible/invisible filtering
        }

    }


}
