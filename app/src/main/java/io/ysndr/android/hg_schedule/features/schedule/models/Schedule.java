package io.ysndr.android.hg_schedule.features.schedule.models;


import org.immutables.value.Value;

import java.util.List;

/**
 * Created by yannik on 8/21/16.
 */
@Value.Immutable

public interface Schedule {

//    String school();
//    String schoolId();
//    String grade();

    List<Entry> entries();

    List<Teacher> teachers();
}
