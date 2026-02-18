package pl.bartol0220.stls.simulation;

import org.springframework.web.bind.annotation.*;
import pl.bartol0220.stls.dto.SimulationDto;
import pl.bartol0220.stls.dto.VehicleDto;

@RestController
@RequestMapping("/simulation")
public class SimulationController {
    private final SimulationService service;

    public SimulationController(SimulationService service) {
        this.service = service;
    }

    @PostMapping("/step")
    public SimulationDto step() {
        return service.step();
    }

    @PostMapping("/vehicle")
    public Object addVehicle(@RequestBody VehicleDto vehicleDto) {
        return service.addVehicle(vehicleDto);
    }

    @GetMapping("/state")
    public SimulationDto getSimulationState() {
        return service.getCurrentState();
    }
}
