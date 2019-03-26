package section2;

import java.io.IOException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class MultiThreadedImpl {
    public static void main() throws IOException {
        // Use fork join pool

        int numTasks = 8;

        RgbHistogramTask task = new RgbHistogramTask();
        ForkJoinPool pool = new ForkJoinPool(numTasks);
        pool.invoke(task);

    }

    private static class RgbHistogramTask extends RecursiveAction {

        private RgbHistogramTask() {}

        @Override
        protected void compute() {

        }
    }
}
