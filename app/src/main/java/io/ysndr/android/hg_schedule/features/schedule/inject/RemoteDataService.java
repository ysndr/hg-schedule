package io.ysndr.android.hg_schedule.features.schedule.inject;

import java.util.List;

import io.ysndr.android.hg_schedule.features.schedule.models.Schedule;
import io.ysndr.android.hg_schedule.features.schedule.models.School;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by yannik on 8/21/16.
 */
public interface RemoteDataService {
    @GET("school/{schoolId}/schedule")
    Observable<Schedule> getScheduleEntries(@Path("schoolId") String schoolId);

    @GET("schools")
    Observable<List<School>> getSchools();
}
