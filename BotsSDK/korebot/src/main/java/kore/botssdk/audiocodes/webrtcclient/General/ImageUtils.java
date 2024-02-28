package kore.botssdk.audiocodes.webrtcclient.General;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ImageUtils {
    private static String TAG="ImageUtils";
    public static Bitmap getContactBitmapFromURI(Context context, Uri uri) {
        try {
            InputStream input = context.getContentResolver().openInputStream(uri);
            if (input == null) {
                return null;
            }
            return BitmapFactory.decodeStream(input);
        } catch (FileNotFoundException e) {
            Log.e(TAG,"error: "+e);

        }
        return null;
    }

    public static Bitmap getCroppedRoundBitmap(Bitmap bmp, int radius) {
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bmp, radius, radius, false);
        return getCroppedRoundBitmap(resizedBitmap);
    }

    public static Bitmap getCroppedRoundBitmap(Bitmap bmp) {
        Bitmap bitmap = bmp.copy(Bitmap.Config.ARGB_8888, true);
        int w = bmp.getWidth();
        int h = bmp.getHeight();
        int radius = h < w?h:w;
        Bitmap roundBitmap = getCroppedBitmap(bitmap, radius);
        return roundBitmap;
    }

    public static Bitmap getCroppedBitmap(Bitmap bmp, int radius) {
        bmp = cropToSquare(bmp);
        Bitmap sbmp;
        if(bmp.getWidth() == radius && bmp.getHeight() == radius) {
            sbmp = bmp;
        } else {
            sbmp = Bitmap.createScaledBitmap(bmp, radius, radius, false);
        }

        Bitmap output = Bitmap.createBitmap(sbmp.getWidth(), sbmp.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        int color = -6187148;
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#ffffff"));
        canvas.drawCircle((float)(sbmp.getWidth() / 2), (float)(sbmp.getHeight() / 2), (float)(sbmp.getWidth() / 2), paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(sbmp, rect, rect, paint);
        return output;
    }

    public static Bitmap cropToSquare(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = height > width?width:height;
        int newHeight = height > width?height - (height - width):height;
        int cropW = (width - height) / 2;
        cropW = cropW < 0?0:cropW;
        int cropH = (height - width) / 2;
        cropH = cropH < 0?0:cropH;
        Bitmap cropImg = Bitmap.createBitmap(bitmap, cropW, cropH, newWidth, newHeight);
        return cropImg;
    }
}
