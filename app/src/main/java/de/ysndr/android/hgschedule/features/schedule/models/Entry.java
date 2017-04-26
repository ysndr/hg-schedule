package de.ysndr.android.hgschedule.features.schedule.models;

import com.google.gson.annotations.SerializedName;

import org.immutables.gson.Gson;
import org.immutables.value.Value;
import org.parceler.ParcelFactory;
import org.parceler.ParcelProperty;

import java.util.List;

/**
 * Created by yannik on 8/14/16.
 */

@Value.Immutable
//@Parcel
public abstract class Entry {

    @ParcelFactory
    public static Entry instance(String id, java.util.Date updated, Entry.Date day, DayInfo info, List<Substitute> substitutes) {
        return ImmutableEntry.builder()
                .id(id)
                .updated(updated)
                .date(day)
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
    public abstract java.util.Date updated();

    @ParcelProperty("date")
    public abstract Entry.Date date();

    @ParcelProperty("text")
    @Gson.Named("day")
    public abstract DayInfo info();

    @ParcelProperty("substitutes")
    @Gson.Named("substitutions")
    public abstract List<Substitute> substitutes();

    @Value.Immutable
    public interface Date {
        java.util.Date day();

        String week();
    }

}