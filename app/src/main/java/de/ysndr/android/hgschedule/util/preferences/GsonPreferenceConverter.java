package de.ysndr.android.hgschedule.util.preferences;

import android.support.annotation.NonNull;

import com.f2prateek.rx.preferences2.Preference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.ysndr.android.hgschedule.state.models.GsonAdaptersModels;

/**
 * Created by yannik on 2/5/17.
 */

public class GsonPreferenceConverter<T> implements Preference.Converter<T> {
    final Gson gson;
    private Class<T> clazz;

    public GsonPreferenceConverter(Class<T> clazz) {
        this.clazz = clazz;
        gson = new GsonBuilder()
                .registerTypeAdapterFactory(new GsonAdaptersModels())
                .create();
    }


    @NonNull
    @Override
    public T deserialize(@NonNull String serialized) {
        return gson.fromJson(serialized, clazz);
    }

    @NonNull
    @Override
    public String serialize(@NonNull T value) {
        return gson.toJson(value);
    }
}
