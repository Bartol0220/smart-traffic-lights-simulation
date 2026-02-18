package pl.bartol0220.stls.exceptions;

public class EmptyLightPhases extends Exception {
    public EmptyLightPhases() {
        super("Simulation setup failed: no light phases could be created.");
    }
}
