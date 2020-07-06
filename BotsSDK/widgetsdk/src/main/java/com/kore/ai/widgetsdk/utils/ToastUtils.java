package com.kore.ai.widgetsdk.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Looper;
import android.widget.Toast;


/**
 * Created by Shiva Krishna on 10/18/2017.
 */

public class ToastUtils {
    public static void showToast(Context context, String msg) {
        showToast(context, msg, Toast.LENGTH_SHORT);
    }

    public static void showToast(Context context, String msg, int length) {
        if (!(length == Toast.LENGTH_LONG || length == Toast.LENGTH_SHORT)) {
            length = Toast.LENGTH_SHORT;
        }

//        int yOffset = context.getResources().getDimensionPixelSize(R.dimen.toast_y_offset_default);
//        int gravity = context.getResources().getInteger(R.integer.toast_gravity_default);

        showToast(context, msg, length, 0, 0, 64);
    }

    /**
     * Centralized method to show toast message
     *
     * @param msg     Message to be displayed
     * @param length  Toast.LENGTH_SHORT or Toast.LENGTH_LONG
     * @param gravity Anchor point
     * @param xOffset x-offset
     * @param yOffset y-0ffset
     */
    public static void showToast(final Context context, final String msg, final int length,
                                 final int gravity, final int xOffset, final int yOffset) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Toast toast = Toast.makeText(context, msg, length);
            toast.setGravity(gravity, xOffset, yOffset);
            toast.show();

        } else {

            if (context == null) {
                return;
            }

            if (context instanceof Activity) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast toast = Toast.makeText(context, msg, length);
                        toast.setGravity(gravity, xOffset, yOffset);
                        toast.show();
                    }
                });
            }
        }
    }

}
