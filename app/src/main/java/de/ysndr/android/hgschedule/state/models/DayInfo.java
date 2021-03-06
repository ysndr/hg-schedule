package de.ysndr.android.hgschedule.state.models;


import org.immutables.value.Value;
import org.parceler.Parcel;
import org.parceler.ParcelFactory;
import org.parceler.ParcelProperty;

import java.util.List;


/**
 * Created by yannik on 8/15/16.
 */

@Value.Immutable
@Parcel(Parcel.Serialization.VALUE)
public abstract class DayInfo {
    @ParcelFactory
    @Value.Auxiliary
    public static ImmutableDayInfo newInstance(List<String> absentTeachers,
                                               List<String> affectedClasses,
                                               List<String> affectedRooms,
                                               List<String> blockedRooms,
                                               List<String> info) {

        return ImmutableDayInfo.builder()
                .absentTeachers(absentTeachers)
                .affectedClasses(affectedClasses)
                .affectedRooms(affectedRooms)
                .blockedRooms(blockedRooms)
                .info(info)
                .build();

    }

    @ParcelProperty("absentTeachers")
    public abstract List<String> absentTeachers();

    @ParcelProperty("affectedClasses")
    public abstract List<String> affectedClasses();

    @ParcelProperty("affectedRooms")
    public abstract List<String> affectedRooms();

    @ParcelProperty("blockedRooms")
    public abstract List<String> blockedRooms();

    @ParcelProperty("info")
    public abstract List<String> info();

}
