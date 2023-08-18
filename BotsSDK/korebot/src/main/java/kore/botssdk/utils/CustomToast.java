package kore.botssdk.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Looper;
import android.view.Gravity;
import android.widget.Toast;

import kore.botssdk.R;

/**
 * Created by Pradeep Mahato on 30-May-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class CustomToast {

    private static final boolean enableToast = true;

    public static void toastIt(Context context, CharSequence text, int duration) {
        if (enableToast) {
            Toast.makeText(context, text, duration).show();
        }
    }

    public static final void showToast(Context context, String msg) {
        if (enableToast) {
            showToast(context, msg, Toast.LENGTH_SHORT);
        }
    }

    public static final void showToast(Context context, String msg, int length) {

        if (enableToast) {
            if (!(length == Toast.LENGTH_LONG || length == Toast.LENGTH_SHORT)) {
                length = Toast.LENGTH_SHORT;
            }

            int yOffset = context.getResources().getDimensionPixelSize(R.dimen.toast_y_offset_default);
            int gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;

            showToast(context, msg, length, gravity, 0, yOffset);
        }
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

        if (enableToast) {
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

}
