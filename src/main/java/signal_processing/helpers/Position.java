package signal_processing.helpers;

public class Position {
    private double position = 0;
    private double speed;

    public void setPosition(double time) {
        position = time * speed;
    }

    public double getPosition() {
        return position;
    }

    public double getSpeed() {
        return speed;
    }

    public Position(double speed) {
        this.speed = speed;
    }
}
