package section2;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class NaiveImpl {
    public static void main(String[] args) throws IOException {
        BufferedImage image = ImageIO.read(NaiveImpl.class.getClassLoader().getResourceAsStream("input.jpg"));
        int w = image.getWidth();
        int h = image.getHeight();
        int[] rHistogram = new int[256];
        int[] gHistogram = new int[256];
        int[] bHistogram = new int[256];
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                //System.out.println("x,y: " + j + ", " + i);
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

        printHistogram(rHistogram);
        printHistogram(gHistogram);
        printHistogram(bHistogram);

        //System.out.println("GOT MY HISTOGRAMS");
    }

    public static void printHistogram(int [] hist) {
        for (int i=0; i<hist.length; ++i) {
            System.out.println(i + ": " + hist[i]);
        }
        System.out.println();
    }
}
