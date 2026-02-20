package pl.bartol0220.stls.model.lightControllers.model;

public class TrafficLight {
    private LightsColor color = LightsColor.RED;

    public boolean canVehiclePass() {
        return color == LightsColor.GREEN;
    }

    public void changeLightToGreen() {
        if (color == LightsColor.RED) {
            color = color.nextColor();
            color = color.nextColor();
        }
    }

    public void changeLightToRed() {
        if (color == LightsColor.GREEN) {
            color = color.nextColor();
            color = color.nextColor();
        }
    }

    public String toString() {
        return "TL: " + color.toString();
    }

    public LightsColor getColor() {
        return color;
    }
}
