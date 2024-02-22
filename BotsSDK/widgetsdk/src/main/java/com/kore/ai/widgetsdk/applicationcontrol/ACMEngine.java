package com.kore.ai.widgetsdk.applicationcontrol;

import android.annotation.SuppressLint;

/**
 * Created by Ramachandra Pradeep Challa
 * Kore.ai
 */

@SuppressLint("UnknownNullness")
public class ACMEngine {

    public static ApplicationControl appControl;

    public static void processACMList(ACMModel model) {

        if (model == null) {
            appControl = new ApplicationControl();
            appControl.resetWithPositives();
            return;
        }
        ACMEngine.appControl = model.getApplicationControl();
    }
}