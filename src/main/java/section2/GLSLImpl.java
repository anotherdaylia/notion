package section2;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.BufferedImageUtil;
import org.newdawn.slick.util.ResourceLoader;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static section2.NaiveImpl.printHistogram;

public class GLSLImpl {

    /** The texture that will hold the image details */
    private Texture texture;


    /**
     * Start the example
     */
    public void start() {
        initGL(800,600);
        init();

        while (true) {
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
            //render();

            Display.update();
            Display.sync(100);

            if (Display.isCloseRequested()) {
                Display.destroy();
                System.exit(0);
            }
        }
    }

    /**
     * Initialise the GL display
     *
     * @param width The width of the display
     * @param height The height of the display
     */
    private void initGL(int width, int height) {
        try {
            Display.setDisplayMode(new DisplayMode(width,height));
            Display.create();
            Display.setVSyncEnabled(true);
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(0);
        }

        GL11.glEnable(GL11.GL_TEXTURE_2D);

        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        // enable alpha blending
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glViewport(0,0,width,height);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, width, height, 0, 1, -1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
    }

    /**
     * Initialise resources
     */
    public void init() {

        try {
            // load texture from JPG file
            texture = TextureLoader.getTexture("JPG", ResourceLoader.getResourceAsStream("input.jpg"));

            Image image = new Image(texture);

            int w = image.getWidth();
            int h = image.getHeight();

            System.out.println("Width: " + w + " Height: " + h);

            int[] rHistogram = new int[256];
            int[] gHistogram = new int[256];
            int[] bHistogram = new int[256];
            Graphics graphics = image.getGraphics();

            for (int i = 0; i < h; i++) {
                for (int j = 0; j < w; j++) {
                    Color pixel = graphics.getPixel(j, i);

                    int alpha = pixel.getAlpha();
                    int red = pixel.getRed();
                    int green = pixel.getGreen();
                    int blue = pixel.getBlue();

                    rHistogram[red] += 1;
                    gHistogram[green] += 1;
                    bHistogram[blue] += 1;
                }
            }

            printHistogram(rHistogram);
            printHistogram(gHistogram);
            printHistogram(bHistogram);
        } catch (SlickException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * draw a quad with the image on it
     */
    public void render() {
        Color.white.bind();
        texture.bind(); // or GL11.glBind(texture.getTextureID());

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0,0);
        GL11.glVertex2f(100,100);
        GL11.glTexCoord2f(1,0);
        GL11.glVertex2f(100+texture.getTextureWidth(),100);
        GL11.glTexCoord2f(1,1);
        GL11.glVertex2f(100+texture.getTextureWidth(),100+texture.getTextureHeight());
        GL11.glTexCoord2f(0,1);
        GL11.glVertex2f(100,100+texture.getTextureHeight());
        GL11.glEnd();
    }

    /**
     * Main Class
     */
    public static void main(String[] argv) {
        GLSLImpl glslImpl = new GLSLImpl();
        glslImpl.start();
    }
}
