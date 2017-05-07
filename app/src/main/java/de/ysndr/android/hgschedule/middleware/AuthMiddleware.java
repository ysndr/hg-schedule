package de.ysndr.android.hgschedule.middleware;

import android.util.Base64;

import com.f2prateek.rx.preferences.RxSharedPreferences;

import org.immutables.value.Value;

import de.ysndr.android.hgschedule.state.models.School;
import de.ysndr.android.hgschedule.util.preferences.GsonPreferenceAdapter;
import rx.Observable;

/**
 * Created by yannik on 4/8/17.
 */

public class AuthMiddleware {


    public <O> Observable.Transformer<O, Login> getLogin(RxSharedPreferences prefs) {
        return source -> source.flatMap(__ -> {
            Observable<String> user = prefs.getString("user").asObservable();
            Observable<String> pass = prefs.getString("pass").asObservable();
            Observable<School> school = prefs.getObject(
                    "school",
                    School.empty(),
                    new GsonPreferenceAdapter<>(School.class)).asObservable();

            return Observable.combineLatest(school, user, pass, ImmutableLogin::of);
        });
    }

    @Value.Immutable
    @Value.Style(allParameters = true)
    public static abstract class Login {
        abstract School school();
        abstract String user();
        abstract String pass();

        @Value.Derived
        public String auth() {
            String auth;
            auth = user().concat(":").concat(pass());
            auth = Base64.encodeToString(auth.getBytes(), Base64.NO_WRAP);
            auth = "BASIC ".concat(auth);
            return auth;
        }

    }



}
