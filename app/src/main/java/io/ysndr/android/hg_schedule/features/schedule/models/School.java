package io.ysndr.android.hg_schedule.features.schedule.models;

import com.google.gson.annotations.SerializedName;

import org.immutables.value.Value;

import java.util.List;

/**
 * Created by yannik on 1/10/17.
 */

@Value.Immutable
public abstract class School {

    public abstract String name();

    public abstract String variant();

    @SerializedName("schoolId")
    public abstract String id();

    public abstract List<Teacher> teachers();

}
