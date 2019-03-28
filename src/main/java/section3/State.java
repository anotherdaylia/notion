package section3;

public interface State {
    int SECOND = 1000;
    void displayState();
    void doAction();
}

class TrafficLight implements State {

    private Direction direction;
    private StateName stateName;
    private LightColor color;
    private int time;

    TrafficLight(Direction direction, StateName stateName, LightColor color, int time) {
        this.direction = direction;
        this.stateName = stateName;
        this.color = color;
        this.time = time;
    }

    @Override
    public void displayState() {
        System.out.println(
                direction + " traffic signal is showing " + color + " traffic light for " + time + " seconds"
        );
    }

    @Override
    public void doAction() {
        try {
            displayState();
            Thread.sleep(time * SECOND);
        } catch (InterruptedException e) {
            System.out.println("Got InterruptedException: " + Thread.currentThread().getName());
            e.printStackTrace();
        }
    }
}