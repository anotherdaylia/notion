package section2;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class MultiThreadedImpl {

    public static void main(String[] args) throws IOException {
        int parallelism = 8;

        final BufferedImage image = ImageIO.read(MultiThreadedImpl.class.getClassLoader().getResourceAsStream("input.jpg"));
        int w = image.getWidth();
        int h = image.getHeight();

        // 4032 / 8 == 504 ... 0
        int subImageWidth = w / parallelism;

        // 3024 / 8 == 378 ... 0
        int subImageHeight = h / parallelism;

        List<RgbHistogramTask> tasks = new ArrayList<>();
        // "Divide" the image into sub images and compute (conquer) the histogram
        for (int i = 0; i < h; i += subImageHeight) {
            for (int j = 0; j < w; j += subImageWidth) {
                System.out.println(String.format("x:%d, y:%d, w:%d, h:%d", i, j, subImageWidth, subImageHeight));
                BufferedImage subImage = image.getSubimage(j, i, subImageWidth, subImageHeight);

                tasks.add(new RgbHistogramTask(subImage));
            }
        }


        int[] rHistogram = new int[256];
        int[] gHistogram = new int[256];
        int[] bHistogram = new int[256];

        ForkJoinPool pool = new ForkJoinPool(parallelism);

        for (RgbHistogramTask task: tasks) {
            pool.invoke(task);
            mergeHistogram(task.getRHistogram(), rHistogram);
            mergeHistogram(task.getGHistogram(), gHistogram);
            mergeHistogram(task.getBHistogram(), bHistogram);
        }

        NaiveImpl.printHistogram(rHistogram);
        NaiveImpl.printHistogram(gHistogram);
        NaiveImpl.printHistogram(bHistogram);
    }

    private static class RgbHistogramTask extends RecursiveAction {

        private static final int THRESHOLD = 600;
        private BufferedImage image;

        private int[] rHistogram = new int[256];
        private int[] gHistogram = new int[256];
        private int[] bHistogram = new int[256];

        private RgbHistogramTask(final BufferedImage image) {
            this.image = image;
        }

        public int[] getRHistogram() {
            return rHistogram;
        }

        public int[] getGHistogram() {
            return gHistogram;
        }

        public int[] getBHistogram() {
            return bHistogram;
        }

        @Override
        protected void compute() {
            int w = image.getWidth();
            int h = image.getHeight();

            // compute directly
            if (image.getWidth() <= THRESHOLD && image.getHeight() <= THRESHOLD) {
                for (int i = 0; i < h; i++) {
                    for (int j = 0; j < w; j++) {
                        int pixel = image.getRGB(j,i);
                        int alpha = (pixel >> 24) & 0xff;
                        int red = (pixel >> 16) & 0xff;
                        int green = (pixel >> 8) & 0xff;
                        int blue = (pixel) & 0xff;

                        rHistogram[red] += 1;
                        gHistogram[green] += 1;
                        bHistogram[blue] += 1;
                    }
                }
            } else {
                BufferedImage leftImage = image.getSubimage(0, 0, w/2, h/2);
                BufferedImage rightImage = image.getSubimage(0, 0, w/2, h/2);
                RgbHistogramTask left = new RgbHistogramTask(leftImage);
                RgbHistogramTask right = new RgbHistogramTask(rightImage);

                invokeAll(left, right);
                mergeHistogram(left.getRHistogram(), rHistogram);
                mergeHistogram(left.getGHistogram(), gHistogram);
                mergeHistogram(left.getBHistogram(), bHistogram);

                mergeHistogram(right.getRHistogram(), rHistogram);
                mergeHistogram(right.getGHistogram(), gHistogram);
                mergeHistogram(right.getBHistogram(), bHistogram);
            }
        }
    }

    private static void mergeHistogram(int[] source, int[] target) {
        assert(source.length == target.length);
        for (int i=0; i<target.length; ++i) {
            target[i] += source[i];
        }
    }
}
