package de.ysndr.android.hgschedule.functions;

import com.f2prateek.rx.preferences.RxSharedPreferences;

import de.ysndr.android.hgschedule.functions.models.Login;
import de.ysndr.android.hgschedule.state.models.School;
import de.ysndr.android.hgschedule.util.preferences.GsonPreferenceAdapter;
import rx.Observable;
import rx.functions.Func0;
import timber.log.Timber;

/**
 * Created by yannik on 4/8/17.
 */

public class AuthFunc {
    public static Func0<Observable<Login>> login$(RxSharedPreferences prefs) {
        return () -> {
            Observable<String> user = prefs.getString("user").asObservable();
            Observable<String> pass = prefs.getString("pass").asObservable();
            Observable<School> school = prefs.getObject(
                    "school",
                    School.empty(),
                    new GsonPreferenceAdapter<>(School.class)).asObservable();

            return Observable.zip(school, user, pass, Login::of)
                    .doOnNext(auth -> Timber.d("auth with: %s", auth));
        };
    }
}
