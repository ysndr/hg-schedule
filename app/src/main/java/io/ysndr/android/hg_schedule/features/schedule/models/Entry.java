package io.ysndr.android.hg_schedule.features.schedule.models;

import com.google.gson.annotations.SerializedName;

import org.immutables.gson.Gson;
import org.immutables.value.Value;
import org.parceler.ParcelFactory;
import org.parceler.ParcelProperty;

import java.util.Date;
import java.util.List;

/**
 * Created by yannik on 8/14/16.
 */

@Value.Immutable
//@Parcel
public abstract class Entry {

    @ParcelFactory
    public static Entry instance(String id, Date updated, Date day, DayInfo info, List<Substitute> substitutes) {
        return ImmutableEntry.builder()
                .id(id)
                .updated(updated)
                .day(day)
                .info(info)
                .substitutes(substitutes)
                .build();
    }

    @Value.Default
    @Gson.Ignore
    public boolean visible() {
        return true;
    }

    @SerializedName("_id")
    @ParcelProperty("id")
    public abstract String id();

    @ParcelProperty("updated")
    public abstract Date updated();

    @ParcelProperty("day")
    public abstract Date day();

    @ParcelProperty("info")
    public abstract DayInfo info();

    @ParcelProperty("substitutes")
    public abstract List<Substitute> substitutes();

}