package pl.bartol0220.stls.simulation;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import pl.bartol0220.stls.exceptions.EmptyLightPhases;
import pl.bartol0220.stls.exceptions.InvalidTrafficLaneDirectionException;
import pl.bartol0220.stls.exceptions.MaxPhaseTimeLimit;
import pl.bartol0220.stls.model.Intersection;
import pl.bartol0220.stls.model.lightControllers.AbstractLightController;
import pl.bartol0220.stls.model.lightControllers.TimeLightController;
import pl.bartol0220.stls.model.util.RoadsDirection;
import pl.bartol0220.stls.observers.OutputFileMaker;
import pl.bartol0220.stls.observers.SimulationObserver;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileSimulationRunnerTest {
    @Test
    void FileHandlingWorks() throws EmptyLightPhases, IOException, InvalidTrafficLaneDirectionException {
        Path inputPath = Path.of("src/test/resources/testInput.json");
        Path outputPath = Path.of("src/test/resources/testOutput.json");

        Intersection intersection = new Intersection();
        intersection.getRoad(RoadsDirection.NORTH).addTrafficLane(List.of(RoadsDirection.SOUTH));
        intersection.getRoad(RoadsDirection.SOUTH).addTrafficLane(List.of(RoadsDirection.NORTH));
        AbstractLightController lightController = new TimeLightController(intersection);
        Simulation simulation = new Simulation(intersection, lightController);

        FileSimulationRunner runner = new FileSimulationRunner(simulation, inputPath);

        SimulationObserver outputFileMaker = new OutputFileMaker(outputPath);
        runner.registerObserver(outputFileMaker);

        runner.run();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode actualJson = objectMapper.readTree(outputPath.toFile());

        String expectedJsonStr = """
                {
                  "stepStatuses" : [ {
                    "leftVehicles" : [ "v1", "v2" ]
                  } ]
                }""";
        JsonNode expectedJson = objectMapper.readTree(expectedJsonStr);

        assertEquals(expectedJson, actualJson);
    }

    @Test
    void testRunnerWithNonexistentFile() throws EmptyLightPhases, InvalidTrafficLaneDirectionException, MaxPhaseTimeLimit {
        Path inputPath = Path.of("src/test/resources/nonexistentFile.json");

        SimulationConfig simulationConfig = new SimulationConfig();
        simulationConfig.prepareDefaultSimulation();
        Simulation simulation = simulationConfig.makeSimulation();

        FileSimulationRunner runner = new FileSimulationRunner(simulation, inputPath);

        ByteArrayOutputStream errContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(errContent));
        runner.run();

        assertFalse(errContent.toString().isEmpty());
    }

}