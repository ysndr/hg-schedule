package de.ysndr.android.hgschedule.state.models;


import org.immutables.value.Value;


/**
 * Created by yannik on 8/15/16.
 */
@Value.Immutable
public interface Substitute {

    String classes();

    String hour();

    String absent(); // absent teacher

    String substitute(); // substituting teacher

    String room();

    String text();

    String description();

    String canceled();

    String renew();
}
