package section3;

import java.util.*;

import static section3.State.SECOND;

public class Simulation {

    public static void main(String[] args)  {
        TrafficSystem system = new TrafficSystem();
        system.run();
    }
}

class TrafficSystem {
    private static final int BOUND = 4 * SECOND;

    void run() {
        // init
        NorthSouthTrafficSignal nsSignal = new NorthSouthTrafficSignal();
        EastWestTrafficSignal ewSignal = new EastWestTrafficSignal();

        List<TrafficSignal> signals = Arrays.asList(
                nsSignal,
                ewSignal
        );

        Random random = new Random();

        while(true) {
            int pause = random.nextInt(BOUND);
            System.out.println("Pause for " + pause + " ms");
            try {
                Thread.sleep(pause);
                System.out.println("A car is waiting at east west direction");
                CarEvent event = new CarEvent(this,true);
                signals.forEach(signal -> {
                    signal.reactToCarEvent(event);
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class CarEvent extends EventObject {
    private boolean hasCar;

    public CarEvent(Object source, boolean setHasCar) {
        super(source);
        this.hasCar = setHasCar;
    }

    public boolean getHasCar() {
        return this.hasCar;
    }
}

interface CarDetector extends EventListener {
    void reactToCarEvent(CarEvent event);
}