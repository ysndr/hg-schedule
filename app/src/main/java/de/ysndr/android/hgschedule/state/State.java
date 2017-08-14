package de.ysndr.android.hgschedule.state;

import com.pacoworks.rxsealedunions2.Union4;
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


    private static final Union4.Factory<Error, ScheduleData, EntryDialogData, Empty> FACTORY = UnionFactories.quartetFactory();

    public static State error(Error error) {
        return ImmState.of(FACTORY.first(error));
    }

    public static State data(ScheduleData data) {
        return ImmState.of(FACTORY.second(data));
    }

    public static State entryDialogData(EntryDialogData data) {
        return ImmState.of(FACTORY.third(data));
    }

    public static State empty(Empty empty) {
        return ImmState.of(FACTORY.fourth(empty));
    }
    public static State empty() {
        return ImmState.of(FACTORY.fourth(Empty.of()));
    }

    public abstract Union4<Error, ScheduleData, EntryDialogData, Empty> union();

}
