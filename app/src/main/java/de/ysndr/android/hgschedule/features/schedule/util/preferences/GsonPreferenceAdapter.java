package de.ysndr.android.hgschedule.features.schedule.util.preferences;

import android.content.SharedPreferences;

import com.f2prateek.rx.preferences.Preference;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.ysndr.android.hgschedule.features.schedule.models.GsonAdaptersModels;

/**
 * Created by yannik on 2/5/17.
 */

public class GsonPreferenceAdapter<T> implements Preference.Adapter<T> {
    final Gson gson;
    private Class<T> clazz;

    public GsonPreferenceAdapter(Class<T> clazz) {
        this.clazz = clazz;
        gson = new GsonBuilder()
                .registerTypeAdapterFactory(new GsonAdaptersModels())
                .create();
    }

    @Override
    public T get(String key, SharedPreferences preferences) {
        return gson.fromJson(preferences.getString(key, null), clazz);
    }

    @Override
    public void set(String key, T value, SharedPreferences.Editor editor) {
        editor.putString(key, gson.toJson(value));
    }
}
