package de.ysndr.android.hgschedule.features.schedule.inject;

import java.util.List;

import de.ysndr.android.hgschedule.features.schedule.models.Schedule;
import de.ysndr.android.hgschedule.features.schedule.models.School;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by yannik on 8/21/16.
 */
public interface RemoteDataService {
    @GET("school/{schoolId}/schedule")
    Observable<Schedule> getScheduleEntries(@Path("schoolId") String schoolId, @Header("Authorization") String auth);

    @GET("schools")
    Observable<Response<List<School>>> getSchools();
}
