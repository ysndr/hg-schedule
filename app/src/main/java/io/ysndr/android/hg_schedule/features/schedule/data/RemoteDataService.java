package io.ysndr.android.hg_schedule.features.schedule.data;

import io.ysndr.android.hg_schedule.features.schedule.models.Schedule;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by yannik on 8/21/16.
 */
public interface RemoteDataService {
    @GET("schedule")
    Observable<Schedule> getData();
}
