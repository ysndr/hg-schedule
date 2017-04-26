package de.ysndr.android.hgschedule.features.schedule.util;

import android.os.Bundle;

import com.hannesdorfmann.fragmentargs.bundler.ArgsBundler;

import org.parceler.Parcels;

import de.ysndr.android.hgschedule.features.schedule.models.DayInfo;

/**
 * Created by yannik on 1/8/17.
 */

public abstract class ParcelerBundlers<T, U> implements ArgsBundler<Object> {

    abstract Class getParcelType();

    @Override
    @SuppressWarnings({"unchecked"})
    public void put(String key, Object value, Bundle bundle) {
        bundle.putParcelable(key, Parcels.wrap(getParcelType(), value));
    }

    @Override
    public <V> V get(String key, Bundle bundle) {
        return Parcels.unwrap(bundle.getParcelable(key));
    }

    public static class DayInfoBundler extends ParcelerBundlers<DayInfo, DayInfo> {
        @Override
        Class getParcelType() {

            return DayInfo.class;
        }
    }

}