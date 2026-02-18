package pl.bartol0220.stls.simulation;

import org.springframework.stereotype.Service;
import pl.bartol0220.stls.dto.ErrorDto;
import pl.bartol0220.stls.dto.SimulationDto;
import pl.bartol0220.stls.dto.VehicleDto;
import pl.bartol0220.stls.exceptions.EmptyLightPhases;
import pl.bartol0220.stls.exceptions.IllegalVehicleDestination;
import pl.bartol0220.stls.exceptions.InvalidTrafficLaneDirectionException;

@Service
public class SimulationService {
    private WebAppSimulationRunner simulationRunner;

    public SimulationService() {
        initSimulation();
    }

    public Object initSimulation() {
        try {
            simulationRunner = new WebAppSimulationRunner(new Simulation());
        } catch (EmptyLightPhases | InvalidTrafficLaneDirectionException e) {
            return new ErrorDto(e.getMessage());
        }
        return simulationRunner.getSimulationDto();
    }

    public SimulationDto getCurrentState() {
        return simulationRunner.getSimulationDto();
    }

    public SimulationDto step() {
        return simulationRunner.step();
    }

    public Object addVehicle(VehicleDto vehicleDto) {
        try {
            return simulationRunner.addVehicle(vehicleDto.id(), vehicleDto.startRoad(), vehicleDto.endRoad());
        } catch (IllegalVehicleDestination e) {
            return new ErrorDto(e.getMessage());
        }
    }
}
