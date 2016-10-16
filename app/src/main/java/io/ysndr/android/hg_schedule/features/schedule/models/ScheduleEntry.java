package io.ysndr.android.hg_schedule.features.schedule.models;

import com.google.gson.annotations.Expose;

import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * Created by yannik on 8/14/16.
 */

@Data
public class ScheduleEntry {

    @Expose
    private Date updated;
    @Expose
    private Date day;
    @Expose
    private DayInfo info;
    @Expose
    private List<Substitute> substitutes;

    // factories
    /*public static ScheduleEntry create(Date updated, Date day, DayInfo info, List<Substitute> substitutes) {
        return new AutoValue_ScheduleEntry(updated, day, info, substitutes);
    }
*/


}
