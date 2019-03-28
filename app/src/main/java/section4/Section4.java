package section4;

import android.graphics.*;
import android.opengl.GLES20;
import android.util.Log;

import java.nio.IntBuffer;

public class Section4 {

    private static final String TAG = "Section4";

    public static Bitmap toGrayscaleBitmap(Bitmap bmpOriginal) {
        if (bmpOriginal == null) {
            Log.e(TAG, "Bitmap is null");
        }

        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, bmpOriginal.getConfig());
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

    public static int[][] multiplyMatrices(int[][] firstMatrix, int[][] secondMatrix) {
        if (firstMatrix == null || secondMatrix == null) {
            Log.e(TAG, "One of the input matrices is null");
            return null;
        }

        if (firstMatrix.length == 0 || secondMatrix.length == 0) {
            Log.e(TAG, "One of the input matrices is of invalid dimension");
            return null;
        }

        int r1 = firstMatrix.length;
        int c1 = firstMatrix[0].length;
        int c2 = secondMatrix[0].length;

        int[][] product = new int[r1][c2];
        for (int i = 0; i < r1; i++) {
            for (int j = 0; j < c2; j++) {
                for (int k = 0; k < c1; k++) {
                    product[i][j] += firstMatrix[i][k] * secondMatrix[k][j];
                }
            }
        }
        return product;
    }

    // reference
    // https://stackoverflow.com/questions/24421290/how-do-you-convert-opengl-texture-back-to-bitmap-in-android
    public static Bitmap textureToBitmap(int x, int y, int w, int h) {
        int b[] = new int[w * (y + h)];
        int bt[] = new int[w * h];
        IntBuffer ib = IntBuffer.wrap(b);
        ib.position(0);
        GLES20.glReadPixels(0, 0, w, h, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, ib);

        //remember, that OpenGL bitmap is incompatible with Android bitmap
        for (int i = 0, k = 0; i < h; i++, k++) {
            //and so, some correction need.
            for (int j = 0; j < w; j++) {
                int pix = b[i * w + j];
                int pb = (pix >> 16) & 0xff;
                int pr = (pix << 16) & 0x00ff0000;
                int pix1 = (pix & 0xff00ff00) | pr | pb;
                bt[(h - k - 1) * w + j] = pix1;
            }
        }

        Bitmap sb = Bitmap.createBitmap(bt, w, h, Bitmap.Config.ARGB_8888);
        return sb;
    }
}
