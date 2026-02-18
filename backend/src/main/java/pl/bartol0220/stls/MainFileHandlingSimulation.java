package pl.bartol0220.stls;

import pl.bartol0220.stls.exceptions.EmptyLightPhases;
import pl.bartol0220.stls.observers.ConsoleSimulationPresenter;
import pl.bartol0220.stls.observers.OutputFileMaker;
import pl.bartol0220.stls.observers.SimulationObserver;
import pl.bartol0220.stls.simulation.FileSimulationRunner;
import pl.bartol0220.stls.simulation.Simulation;

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
            Simulation simulation = new Simulation();

            FileSimulationRunner runner = new FileSimulationRunner(simulation, inputPath);

            SimulationObserver consoleSimulationPresenter = new ConsoleSimulationPresenter();
            SimulationObserver outputFileMaker = new OutputFileMaker(outputPath);
            runner.registerObserver(consoleSimulationPresenter);
            runner.registerObserver(outputFileMaker);

            Thread simulationThread = new Thread(runner);
            simulationThread.start();
        } catch (EmptyLightPhases e) {
            System.err.println(e.getMessage());
        }
    }
}
