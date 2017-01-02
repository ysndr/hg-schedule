package io.ysndr.android.hg_schedule.features.schedule.models;


import org.immutables.value.Value;

import java.util.List;


/**
 * Created by yannik on 8/15/16.
 */
@Value.Immutable

public interface DayInfo {
    List<String> absentTeachers();

    List<String> absentClasses();

    List<String> affectedClasses();

    List<String> affectedRooms();
}
