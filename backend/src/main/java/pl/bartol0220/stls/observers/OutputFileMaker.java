package pl.bartol0220.stls.observers;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import pl.bartol0220.stls.io.SimulationOutput;
import pl.bartol0220.stls.io.StepStatus;
import pl.bartol0220.stls.model.Intersection;
import pl.bartol0220.stls.model.util.RoadsDirection;
import pl.bartol0220.stls.model.vehicles.Vehicle;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public class OutputFileMaker implements SimulationObserver {
    private final List<StepStatus> stepResults = new ArrayList<>();
    private final Path outputFilePath;

    public OutputFileMaker(Path outputFilePath) {
        this.outputFilePath = outputFilePath;
    }

    @Override
    public void newStep(List<Vehicle> leftVehicles, Intersection intersection) {
        List<String> leftVehicleIds = leftVehicles
                .stream()
                .map(Vehicle::getId)
                .toList();
        stepResults.add(new StepStatus(leftVehicleIds));
    }

    @Override
    public void newVehicle(String vehicleId, RoadsDirection startRoad, RoadsDirection endRoad) {}

    @Override
    public void endOfSimulation() {
        SimulationOutput output = new SimulationOutput(stepResults);

        JsonMapper objectMapper = JsonMapper
                .builder()
                .enable(SerializationFeature.INDENT_OUTPUT)
                .build();
        try {
            objectMapper.writeValue(outputFilePath.toFile(), output);
            System.out.println("Output file has been written to: " + outputFilePath);
        } catch (IOException e) {
            System.err.println("Output file error: " + e.getMessage());
        }
    }
}
