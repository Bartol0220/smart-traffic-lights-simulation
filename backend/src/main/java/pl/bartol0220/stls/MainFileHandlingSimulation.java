package pl.bartol0220.stls;

import pl.bartol0220.stls.exceptions.EmptyLightPhases;
import pl.bartol0220.stls.exceptions.InvalidTrafficLaneDirectionException;
import pl.bartol0220.stls.exceptions.MaxPhaseTimeLimit;
import pl.bartol0220.stls.observers.ConsoleSimulationPresenter;
import pl.bartol0220.stls.observers.OutputFileMaker;
import pl.bartol0220.stls.observers.SimulationObserver;
import pl.bartol0220.stls.simulation.FileSimulationRunner;
import pl.bartol0220.stls.simulation.Simulation;
import pl.bartol0220.stls.simulation.SimulationConfig;

import java.nio.file.Path;

public class MainFileHandlingSimulation {
    static void main(String[] args) {
        Path rootPath = Path.of("").toAbsolutePath().getParent();
        Path inputPath;
        Path outputPath;

        if (args.length == 2) {
            inputPath = rootPath.resolve(args[0]);
            outputPath = rootPath.resolve(args[1]);
        } else {
            System.out.println("Usage: ./gradlew run --args=\"<input path> <output path>\"");
            System.out.println("Using default files from 'example/' folder.");
            inputPath = rootPath.resolve("example", "inputExample.json");
            outputPath = rootPath.resolve("example", "outputExample.json");
        }
        try {
            Thread simulationThread = prepareSimulationThread(inputPath, outputPath);
            simulationThread.start();
        } catch (EmptyLightPhases | InvalidTrafficLaneDirectionException | MaxPhaseTimeLimit e) {
            System.err.println(e.getMessage());
        }
    }

    private static Thread prepareSimulationThread(Path inputPath, Path outputPath) throws InvalidTrafficLaneDirectionException, MaxPhaseTimeLimit, EmptyLightPhases {
        SimulationConfig simulationConfig = new SimulationConfig();
        simulationConfig.prepareDefaultSimulation();
        Simulation simulation = simulationConfig.makeSimulation();

        FileSimulationRunner runner = new FileSimulationRunner(simulation, inputPath);

        SimulationObserver consoleSimulationPresenter = new ConsoleSimulationPresenter();
        SimulationObserver outputFileMaker = new OutputFileMaker(outputPath);
        runner.registerObserver(consoleSimulationPresenter);
        runner.registerObserver(outputFileMaker);

        return new Thread(runner);
    }
}
