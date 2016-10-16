package io.ysndr.android.hg_schedule.features.schedule.views;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.ysndr.android.hg_schedule.R;
import io.ysndr.android.hg_schedule.features.schedule.models.ScheduleEntry;
import io.ysndr.android.hg_schedule.features.schedule.models.Substitute;
import io.ysndr.android.hg_schedule.utils.BaseViewHolder;
import io.ysndr.android.hg_schedule.utils.Either;

/**
 * Created by yannik on 10/10/16.
 */

public class ScheduleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Either<Substitute, Date>> entries;

    public ScheduleAdapter() {
        entries = new ArrayList<>();
    }

    public void addSchedule(ScheduleEntry entry) {
        int start = entries.size();
        entries.add(Either.right(entry.getDay()));
        for (Substitute substitute : entry.getSubstitutes()) {
            entries.add(Either.left(substitute));
        }
        notifyItemRangeInserted(start, entries.size() - start);
    }

    public void clear() {
        int count = entries.size();
        entries.clear();
        notifyItemRangeRemoved(0, count);
    }

    @Override
    public int getItemViewType(int position) {
        AtomicInteger type = new AtomicInteger();
        entries.get(position).fold(
                entry -> type.set(CardViewHolder.TYPE),
                date -> type.set(LabelViewHolder.TYPE)
        );
        return type.get();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout;
        View view;
        LayoutInflater inflater = LayoutInflater
                .from(parent.getContext());
        switch (viewType) {
            case CardViewHolder.TYPE:
                layout = R.layout.card_schedule_item;
                view = inflater.inflate(layout, parent, false);
                return new CardViewHolder(view);
            case LabelViewHolder.TYPE:
                layout = R.layout.list_label;
                view = inflater.inflate(layout, parent, false);
                return new LabelViewHolder(view);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        int type = getItemViewType(position);
        entries.get(position).fold(
                entry -> {
                    CardViewHolder holder = (CardViewHolder) viewHolder;
                    holder.absent.setText(entry.getAbsent());
                    holder.substitute.setText(entry.getSubstitute());
                    holder.classes.setText(TextUtils.join(", ", entry.getAffectedClasses()));
                    holder.lessons.setText(entry.getTime());
                    holder.info.setText(entry.getInfo());
                    holder.room.setText(entry.getRoom());
                    holder.title.setText(entry.getAbsent());
                },
                date -> {
                    LabelViewHolder holder = (LabelViewHolder) viewHolder;
                    holder.label.setText(DateFormat.format("EEEE dd-MM-yyyy", date));
                }
        );
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    static class CardViewHolder extends BaseViewHolder {
        public static final int TYPE = 0;

        @BindView(R.id.text_title_card_schedule_item)
        TextView title;
        @BindView(R.id.text_info_card_schedule_item)
        TextView info;
        @BindView(R.id.text_teacher_absent_card_schedule_item)
        TextView absent;
        @BindView(R.id.text_teacher_new_card_schedule_item)
        TextView substitute;
        @BindView(R.id.text_lessons_card_schedule_item)
        TextView lessons;
        @BindView(R.id.text_classes_card_schedule_item)
        TextView classes;

        @BindView(R.id.text_room_card_schedule_item)
        TextView room;

        public CardViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

    }

    static class LabelViewHolder extends BaseViewHolder {
        public static final int TYPE = 1;

        @BindView(R.id.label_list_label)
        TextView label;

        public LabelViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }


}
