package io.ysndr.android.hg_schedule.features.schedule.models;


import org.immutables.value.Value;
import org.parceler.ParcelFactory;
import org.parceler.ParcelProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yannik on 8/21/16.
 */
@Value.Immutable
//@Parcel
public interface Schedule extends Serializable {

    @ParcelFactory
    static Schedule instance(List<Entry> entries, School school) {
        return ImmutableSchedule.builder()
                .entries(entries)
                .school(school)
                .build();
    }

//    String school();
//    String schoolId();
//    String grade();
@ParcelProperty("entries")
    List<Entry> entries();

    @ParcelProperty("school")
    School school();
}
