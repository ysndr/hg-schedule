package io.ysndr.android.hg_schedule.features.schedule.middleware;

import android.content.SharedPreferences;
import android.util.Base64;
import android.widget.ShareActionProvider;

import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.google.common.base.Preconditions;
import com.jakewharton.rxbinding.support.design.widget.RxTabLayout;
import com.pacoworks.rxtuples.RxTuples;

import org.immutables.value.Value;

import javax.inject.Inject;

import io.ysndr.android.hg_schedule.features.schedule.models.School;
import io.ysndr.android.hg_schedule.features.schedule.util.preferences.GsonPreferenceAdapter;
import rx.Observable;
import rx.Single;

/**
 * Created by yannik on 4/8/17.
 */

public class AuthMiddleware {

    @Inject
    RxSharedPreferences prefs;

    public Observable.Transformer<?, Login> getLogin(){
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
    static abstract class Login {
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
