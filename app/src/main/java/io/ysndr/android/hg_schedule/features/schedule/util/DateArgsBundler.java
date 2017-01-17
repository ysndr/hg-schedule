package io.ysndr.android.hg_schedule.features.schedule.util;

import android.os.Bundle;

import com.hannesdorfmann.fragmentargs.bundler.ArgsBundler;

import java.util.Date;

/**
 * Created by yannik on 1/8/17.
 */

public class DateArgsBundler implements ArgsBundler<Date> {

    @Override
    public void put(String key, Date value, Bundle bundle) {

        bundle.putLong(key, value.getTime());
    }

    @Override
    public Date get(String key, Bundle bundle) {

        long timestamp = bundle.getLong(key);
        return new Date(timestamp);
    }

}
