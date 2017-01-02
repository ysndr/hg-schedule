package io.ysndr.android.hg_schedule.features.schedule.models;

import com.google.gson.annotations.SerializedName;

import org.immutables.gson.Gson;
import org.immutables.value.Value;

import java.util.Date;
import java.util.List;

/**
 * Created by yannik on 8/14/16.
 */

@Value.Immutable

public abstract class Entry {

    @Value.Default
    @Gson.Ignore
    public boolean visible() {
        return true;
    }

    @SerializedName("_id")
    public abstract String id();


    public abstract Date updated();

    public abstract Date day();

    public abstract DayInfo info();

    public abstract List<Substitute> substitutes();

}