package pl.bartol0220.stls.io;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import pl.bartol0220.stls.model.util.RoadsDirection;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = SimulationCommand.AddVehicle.class, name = "addVehicle"),
        @JsonSubTypes.Type(value = SimulationCommand.Step.class, name = "step")
})

public sealed interface SimulationCommand permits SimulationCommand.AddVehicle, SimulationCommand.Step {
    record AddVehicle(
            String vehicleId,
            RoadsDirection startRoad,
            RoadsDirection endRoad
    ) implements SimulationCommand {}

    record Step() implements SimulationCommand {}
}
