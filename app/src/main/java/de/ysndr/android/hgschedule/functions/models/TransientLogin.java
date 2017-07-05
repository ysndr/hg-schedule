package de.ysndr.android.hgschedule.functions.models;

import android.util.Base64;

import org.immutables.value.Value;

import de.ysndr.android.hgschedule.state.models.School;

/**
 * Created by yannik on 5/7/17.
 */
@Value.Immutable
@TransientStyle
public abstract class TransientLogin {
    public abstract School school();
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