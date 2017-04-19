package io.ysndr.android.hg_schedule.features.schedule.view;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import com.jakewharton.rxbinding.view.RxView;

import java.text.DateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import fj.Unit;
import io.ysndr.android.hg_schedule.R;
import io.ysndr.android.hg_schedule.features.schedule.models.DayInfo;
import io.ysndr.android.hg_schedule.features.schedule.util.DateArgsBundler;
import io.ysndr.android.hg_schedule.features.schedule.util.ParcelerBundlers;
import timber.log.Timber;

/**
 * Created by yannik on 1/3/17.
 */

@FragmentWithArgs
public class ScheduleDialog extends DialogFragment {

    @Arg(bundler = ParcelerBundlers.DayInfoBundler.class)
    DayInfo dayInfo;

    @Arg(bundler = DateArgsBundler.class)
    Date date;

    @BindView(R.id.text_affected_classes_dialog_entry)
    TextView affectedClasses;

    @BindView(R.id.text_absent_teachers_dialog_entry)
    TextView absentTeachers;

    @BindView(R.id.text_affected_rooms_dialog_entry)
    TextView affectedRooms;

    @BindView(R.id.text_info_dialog_entry)
    TextView info;

    @BindView(R.id.text_updated_dialog_entry)
    TextView updated;

    @BindView(R.id.button_close_dialog_entry)
    Button close;

    Unbinder unbinder;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Timber.d("thread: %s", Thread.currentThread().getName());

        View view = View.inflate(getContext(), R.layout.schedule_dialog_entry, null);
        Dialog dialog = new MaterialDialog.Builder(getContext())
                .customView(view, false)
                .build();
                

        FragmentArgs.inject(this);

        unbinder = ButterKnife.bind(this, view);

        affectedClasses.setText(dayInfo.affectedClasses().toString());
        absentTeachers.setText(dayInfo.absentTeachers().toString());
        affectedRooms.setText(dayInfo.affectedRooms().toString());
        info.setText(dayInfo.info().toString());
        updated.setText(DateFormat.getInstance().format(date));

        RxView.clicks(close).map(_void_ -> Unit.unit())
                .subscribe(unit -> dialog.dismiss());

        return dialog;
    }

}
