package engine;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class Utils {
    /**
     * Gets resized bitmap.
     * @param bm src bitmap.
     * @param newWidth new width.
     * @param newHeight new height.
     * @return resized bitmap.
     */
    public static Bitmap getResizedBitmap(final Bitmap bm, final float newWidth, final float newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        final float scaleWidth = newWidth / width;
        final float scaleHeight = newHeight / height;
        final Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        final Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }
}
