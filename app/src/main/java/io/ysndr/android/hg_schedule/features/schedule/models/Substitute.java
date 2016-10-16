package io.ysndr.android.hg_schedule.features.schedule.models;

//import com.google.auto.value.AutoValue;

import com.google.gson.annotations.Expose;

import java.util.List;

import lombok.Data;

/**
 * Created by yannik on 8/15/16.
 */
@Data
public class Substitute {

    @Expose
    private Integer substitudeID;
    @Expose
    private List<String> affectedClasses;
    @Expose
    private String time;
    @Expose
    private String absent; // absent teacher
    @Expose
    private String substitute; // substituting teacher
    @Expose
    private String room;
    @Expose
    private String info;
    @Expose
    private String cancelled;
    @Expose
    private String renew;
/*
    public static Substitute create(Integer substitudeID, List<String> affectedClasses, String time,
                                    Teacher absent, Teacher substitute, String room,
                                    String info, String cancelled, String renew) {
        return new AutoValue_Substitute(substitudeID, affectedClasses, time, absent, substitute, room, info, cancelled, renew);
    }
*/

}
