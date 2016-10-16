package io.ysndr.android.hg_schedule.features.schedule.models;


import com.google.gson.annotations.Expose;

import java.util.Set;

import lombok.Data;

/**
 * Created by yannik on 8/21/16.
 */
@Data
public class Schedule {
    @Expose
    private Set<ScheduleEntry> entries;
    @Expose
    private Set<Teacher> teachers;
}
