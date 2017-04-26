package de.ysndr.android.hgschedule.features.schedule.models;


import org.immutables.value.Value;

/**
 * Created by yannik on 8/21/16.
 */

@Value.Immutable
public interface Teacher {
    String id();

    String realName();
}
