package de.ysndr.android.hgschedule.state;

import com.pacoworks.rxsealedunions2.Union3;
import com.pacoworks.rxsealedunions2.generic.UnionFactories;

import org.immutables.value.Value;

/**
 * Created by yannik on 5/7/17.
 */

@Value.Immutable
@Value.Style(
    typeAbstract = "*",
    typeImmutable = "Imm*",
    allMandatoryParameters = true
)
public abstract class State {

    private static final Union3.Factory<StateError, StateScheduleData, StateEmpty> FACTORY = UnionFactories.tripletFactory();

    public static State error(StateError error) {
        return ImmState.of(FACTORY.first(error));
    }

    public static State data(StateScheduleData data) {
        return ImmState.of(FACTORY.second(data));
    }

    public static State empty(StateEmpty empty) {
        return ImmState.of(FACTORY.third(empty));
    }
    public static State empty() {
        return ImmState.of(FACTORY.third(Empty.of()));
    }

    public abstract Union3<StateError, StateScheduleData, StateEmpty> union();

}
