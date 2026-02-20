package pl.bartol0220.stls.simulation;

import org.springframework.web.bind.annotation.*;
import pl.bartol0220.stls.dto.AddLaneDto;
import pl.bartol0220.stls.dto.SimulationConfigDto;
import pl.bartol0220.stls.dto.UpdateConfigDto;
import pl.bartol0220.stls.model.util.IntersectionType;
import pl.bartol0220.stls.model.util.RoadsDirection;

import java.util.Map;

@RestController
@RequestMapping("/config")
@CrossOrigin(origins = "*")
public class SimulationConfigController {
    private final SimulationService service;

    public SimulationConfigController(SimulationService service) {
        this.service = service;
    }

    @PostMapping("/priorities")
    public Object newSimulationConfig(@RequestBody(required = false) Map<RoadsDirection, Integer> priorities) {
        if (priorities == null) {
            return service.newSimulationConfig();
        }
        return service.newSimulationConfig(priorities);
    }

    @GetMapping()
    public SimulationConfigDto getSimulationConfig() {
        return service.getSimulationConfig();
    }

    @PostMapping("/lanes")
    public Object addLane(@RequestBody AddLaneDto addLaneDto) {
        return service.addLane(addLaneDto);
    }

    @DeleteMapping("/lanes/{entryDirection}/{index}")
    public SimulationConfigDto removeLane(@PathVariable RoadsDirection entryDirection, @PathVariable int index) {
        return service.removeLane(entryDirection, index);
    }

    @PatchMapping()
    public Object updateConfig(@RequestBody UpdateConfigDto updateConfigDto) {
        return service.updateConfig(updateConfigDto);
    }

    @PatchMapping("/intersection")
    public Object prepareIntersection(@RequestBody IntersectionType type) {
        return service.prepareIntersection(type);
    }
}
