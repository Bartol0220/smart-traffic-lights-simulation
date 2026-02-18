package pl.bartol0220.stls.model.lightControllers;

public enum LightsColor {
    RED(0),
    RED_YELLOW(1),
    GREEN(2),
    YELLOW(3);

    private final int index;
    static final LightsColor[] LIGHTS_COLORS = values();

    LightsColor(int index) {
        this.index = index;
    }

    public LightsColor nextColor() {
        return LIGHTS_COLORS[(index + 1) % LIGHTS_COLORS.length];
    }

    public String toString() {
        return switch (this)  {
            case RED -> "Red";
            case RED_YELLOW -> "Red + Yellow";
            case GREEN -> "Green";
            case YELLOW -> "Yellow";
        };
    }
}
