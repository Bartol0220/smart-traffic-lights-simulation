package pl.bartol0220.stls.model.lightControllers.phase;

import java.util.List;

public record LightPhaseSequence(List<LightPhase> lightPhases) {
    @Override
    public List<LightPhase> lightPhases() {
        return List.copyOf(lightPhases);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (LightPhase phase : lightPhases) {
            sb.append(phase);
        }
        return sb.toString();
    }
}
