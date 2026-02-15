package pl.bartol0220.stls.exceptions;

public class EmptyLightPhases extends Exception {
    public EmptyLightPhases() {
        super("There are no light phases");
    }
}
