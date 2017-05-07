package de.ysndr.android.hgschedule.state;

import com.pacoworks.rxsealedunions.Union3;
import com.pacoworks.rxsealedunions.generic.GenericUnions;

import org.immutables.value.Value;

import de.ysndr.android.hgschedule.state.models.Schedule;
import fj.data.Option;

/**
 * Created by yannik on 5/7/17.
 */

public abstract class State {

    private static final Union3.Factory<State.Error, State.Data, State.Empty> FACTORY = GenericUnions.tripletFactory();

    public Union3<State.Error, State.Data, State.Empty> error(State.Error error) {
        return FACTORY.first(error);
    }

    public Union3<State.Error, State.Data, State.Empty> data(State.Data data) {
        return FACTORY.second(data);
    }

    public Union3<State.Error, State.Data, State.Empty> empty(State.Empty empty) {
        return FACTORY.third(empty);
    }

    @Value.Immutable
    @Value.Style(allMandatoryParameters = true)
    public abstract class Error<T> {
        abstract Throwable error();

        @Value.Default
        Option<String> message() {
            return Option.some(error().getLocalizedMessage());
        }

        @Value.Default
        Option<T> payload() {
            return Option.none();
        }
    }

    @Value.Immutable
    @Value.Style(allMandatoryParameters = true)
    public abstract class Data {
        abstract Schedule schedule();

        @Value.Default
        boolean loading() {
            return false;
        }
    }

    @Value.Immutable
    @Value.Style(allMandatoryParameters = true)
    public abstract class Empty {
        boolean loading() {
            return false;
        }
    }


}
