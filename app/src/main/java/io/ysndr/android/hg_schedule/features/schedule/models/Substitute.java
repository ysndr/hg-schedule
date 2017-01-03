package io.ysndr.android.hg_schedule.features.schedule.models;


import org.immutables.value.Value;

import java.util.List;


/**
 * Created by yannik on 8/15/16.
 */
@Value.Immutable
public interface Substitute {

    List<String> affectedClasses();

    String time();

    String absent(); // absent teacher

    String substitute(); // substituting teacher

    String room();

    String info();

    String description();

    String cancelled();

    String renew();
}
