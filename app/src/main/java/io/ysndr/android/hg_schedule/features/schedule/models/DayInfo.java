package io.ysndr.android.hg_schedule.features.schedule.models;


import com.google.gson.annotations.Expose;

import java.util.List;

import lombok.Data;

/**
 * Created by yannik on 8/15/16.
 */
@Data
public class DayInfo {
    @Expose
    private List<String> absentTeachers;
    @Expose
    private List<String> absentClasses;
    @Expose
    private List<String> affectedClasses;
    @Expose
    private List<String> affectedRooms;
}
