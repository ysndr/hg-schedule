package io.ysndr.android.hg_schedule.features.schedule.models;

import com.google.gson.annotations.Expose;

import lombok.Data;

/**
 * Created by yannik on 8/21/16.
 */

@Data
public class Teacher {
    @Expose
    private String id;
    @Expose
    private String reamName;

    /*
    public static Teacher create(String shortName) {
        return new AutoValue_Teacher(shortName);
    }
    */
}
