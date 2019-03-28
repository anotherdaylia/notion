package section3;

import java.util.Arrays;
import java.util.List;

public interface TrafficSignal extends CarDetector {
    void startStateTransition();
    void showState();
}

class NorthSouthTrafficSignal implements TrafficSignal {
    private static List<State> states =
            Arrays.asList(
                    new TrafficLight(Direction.NORTH_SOUTH, StateName.WALK, LightColor.GREEN, 5),
                    new TrafficLight(Direction.NORTH_SOUTH, StateName.WARN, LightColor.YELLOW, 3),
                    new TrafficLight(Direction.NORTH_SOUTH, StateName.STOP, LightColor.RED, 10)
            );

    private State currentState = new TrafficLight(Direction.EAST_WEST, StateName.INIT, LightColor.GREEN, 3);

    @Override
    public void startStateTransition() {
        for (State state: states) {
            state.doAction();
        }
    }

    @Override
    public void showState() {
        currentState.displayState();
    }

    @Override
    public void reactToCarEvent(final CarEvent event) {
        if (event.getHasCar()) {
            startStateTransition();
        } else {
            System.out.println("No car on EastWest, NorthSouthTrafficSignal is in INIT state");
        }
    }
}

class EastWestTrafficSignal implements TrafficSignal, CarDetector {
    private static List<State> states =
            Arrays.asList(
                    new TrafficLight(Direction.EAST_WEST, StateName.WALK, LightColor.GREEN, 7),
                    new TrafficLight(Direction.EAST_WEST, StateName.WARN, LightColor.YELLOW, 3)
            );

    private State currentState = new TrafficLight(Direction.EAST_WEST, StateName.INIT, LightColor.RED, 3);

    @Override
    public void startStateTransition() {
        for (State state: states) {
            state.doAction();
        }
    }

    @Override
    public void showState() {
        currentState.displayState();
    }

    @Override
    public void reactToCarEvent(final CarEvent event) {
        if (event.getHasCar()) {
            startStateTransition();
        } else {
            System.out.println("No car on EastWest, EastWestTrafficSignal is in INIT state");
        }
    }
}
