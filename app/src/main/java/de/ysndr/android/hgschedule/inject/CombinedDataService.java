package de.ysndr.android.hgschedule.inject;

import java.util.List;

import javax.inject.Inject;

import de.ysndr.android.hgschedule.state.models.School;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by yannik on 10/10/16.
 */

public class CombinedDataService {
    @Inject
    RemoteDataService mRemoteDataService;

    @Inject
    public CombinedDataService(RemoteDataService remoteDataService) {
        mRemoteDataService = remoteDataService;
    }

    // TODO: Move this to its own service
    public Observable<Response<List<School>>> getSchools$() {
        return mRemoteDataService.getSchools()
                .subscribeOn(Schedulers.io())
                .doOnNext(_void_ -> Timber.d("Schools..."));
    }


}
