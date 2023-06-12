package kore.botssdk.view.viewUtils;

import static kore.botssdk.view.viewUtils.DimensionUtil.dp1;

import android.content.res.Resources;

/**
 * Created by Pradeep Mahato on 01-Jun-16.
 * Copyright (c) 2014 Kore Inc. All rights reserved.
 */
public class BubbleViewUtil {

    public static int getBubbleContentWidth() {
        int viewWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        return (int) (viewWidth -54*dp1);
    }

    public static int getSlotsContentWidth() {
        int viewWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        return (int) (0.75 * viewWidth);
    }

    public static int getSlotConfirmationWidth() {
        int viewWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        return (int) (0.8 * viewWidth);
    }

    public static int getMeetingSlotConfirmationWidth() {
        int viewWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        return (int) (0.94 * viewWidth);
    }

    public static int getBotBubbleContentWidth() {
        int viewWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        return (int) (0.65 * viewWidth);
    }

    /**
     * @return dimens for what each media content should take as a Base-platform
     */
    public static int getBubbleContentHeight() {
        //This is bad... would be rectified based on the % of space available
        int dp81 = (int) (81 * dp1);
        return dp81;
    }

}
