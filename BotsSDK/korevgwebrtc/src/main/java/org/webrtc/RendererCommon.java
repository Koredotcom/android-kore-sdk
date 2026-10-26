/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Matrix
 *  android.graphics.Point
 *  android.opengl.Matrix
 *  android.view.View
 *  android.view.View$MeasureSpec
 */
package org.webrtc;

import android.graphics.Point;
import android.opengl.Matrix;
import android.view.View;

public class RendererCommon {
    private static float BALANCED_VISIBLE_FRACTION = 0.5625f;

    public static float[] getLayoutMatrix(boolean mirror, float videoAspectRatio, float displayAspectRatio) {
        float scaleX = 1.0f;
        float scaleY = 1.0f;
        if (displayAspectRatio > videoAspectRatio) {
            scaleY = videoAspectRatio / displayAspectRatio;
        } else {
            scaleX = displayAspectRatio / videoAspectRatio;
        }
        if (mirror) {
            scaleX *= -1.0f;
        }
        float[] matrix = new float[16];
        Matrix.setIdentityM((float[])matrix, (int)0);
        Matrix.scaleM((float[])matrix, (int)0, (float)scaleX, (float)scaleY, (float)1.0f);
        RendererCommon.adjustOrigin(matrix);
        return matrix;
    }

    public static android.graphics.Matrix convertMatrixToAndroidGraphicsMatrix(float[] matrix4x4) {
        float[] values = new float[]{matrix4x4[0], matrix4x4[4], matrix4x4[12], matrix4x4[1], matrix4x4[5], matrix4x4[13], matrix4x4[3], matrix4x4[7], matrix4x4[15]};
        android.graphics.Matrix matrix = new android.graphics.Matrix();
        matrix.setValues(values);
        return matrix;
    }

    public static float[] convertMatrixFromAndroidGraphicsMatrix(android.graphics.Matrix matrix) {
        float[] values = new float[9];
        matrix.getValues(values);
        float[] matrix4x4 = new float[]{values[0], values[3], 0.0f, values[6], values[1], values[4], 0.0f, values[7], 0.0f, 0.0f, 1.0f, 0.0f, values[2], values[5], 0.0f, values[8]};
        return matrix4x4;
    }

    public static Point getDisplaySize(ScalingType scalingType, float videoAspectRatio, int maxDisplayWidth, int maxDisplayHeight) {
        return RendererCommon.getDisplaySize(RendererCommon.convertScalingTypeToVisibleFraction(scalingType), videoAspectRatio, maxDisplayWidth, maxDisplayHeight);
    }

    private static void adjustOrigin(float[] matrix) {
        matrix[12] = matrix[12] - 0.5f * (matrix[0] + matrix[4]);
        matrix[13] = matrix[13] - 0.5f * (matrix[1] + matrix[5]);
        matrix[12] = matrix[12] + 0.5f;
        matrix[13] = matrix[13] + 0.5f;
    }

    private static float convertScalingTypeToVisibleFraction(ScalingType scalingType) {
        switch (scalingType) {
            case SCALE_ASPECT_FIT: {
                return 1.0f;
            }
            case SCALE_ASPECT_FILL: {
                return 0.0f;
            }
            case SCALE_ASPECT_BALANCED: {
                return BALANCED_VISIBLE_FRACTION;
            }
        }
        throw new IllegalArgumentException();
    }

    public static Point getDisplaySize(float minVisibleFraction, float videoAspectRatio, int maxDisplayWidth, int maxDisplayHeight) {
        if (minVisibleFraction == 0.0f || videoAspectRatio == 0.0f) {
            return new Point(maxDisplayWidth, maxDisplayHeight);
        }
        int width = Math.min(maxDisplayWidth, Math.round((float)maxDisplayHeight / minVisibleFraction * videoAspectRatio));
        int height = Math.min(maxDisplayHeight, Math.round((float)maxDisplayWidth / minVisibleFraction / videoAspectRatio));
        return new Point(width, height);
    }

    public static enum ScalingType {
        SCALE_ASPECT_FIT,
        SCALE_ASPECT_FILL,
        SCALE_ASPECT_BALANCED;

    }

    public static class VideoLayoutMeasure {
        private float visibleFractionMatchOrientation = RendererCommon.convertScalingTypeToVisibleFraction(ScalingType.SCALE_ASPECT_BALANCED);
        private float visibleFractionMismatchOrientation = RendererCommon.convertScalingTypeToVisibleFraction(ScalingType.SCALE_ASPECT_BALANCED);

        public void setScalingType(ScalingType scalingType) {
            this.setScalingType(scalingType, scalingType);
        }

        public void setScalingType(ScalingType scalingTypeMatchOrientation, ScalingType scalingTypeMismatchOrientation) {
            this.visibleFractionMatchOrientation = RendererCommon.convertScalingTypeToVisibleFraction(scalingTypeMatchOrientation);
            this.visibleFractionMismatchOrientation = RendererCommon.convertScalingTypeToVisibleFraction(scalingTypeMismatchOrientation);
        }

        public void setVisibleFraction(float visibleFractionMatchOrientation, float visibleFractionMismatchOrientation) {
            this.visibleFractionMatchOrientation = visibleFractionMatchOrientation;
            this.visibleFractionMismatchOrientation = visibleFractionMismatchOrientation;
        }

        public Point measure(int widthSpec, int heightSpec, int frameWidth, int frameHeight) {
            int maxWidth = View.getDefaultSize((int)Integer.MAX_VALUE, (int)widthSpec);
            int maxHeight = View.getDefaultSize((int)Integer.MAX_VALUE, (int)heightSpec);
            if (frameWidth == 0 || frameHeight == 0 || maxWidth == 0 || maxHeight == 0) {
                return new Point(maxWidth, maxHeight);
            }
            float frameAspect = (float)frameWidth / (float)frameHeight;
            float displayAspect = (float)maxWidth / (float)maxHeight;
            float visibleFraction = frameAspect > 1.0f == displayAspect > 1.0f ? this.visibleFractionMatchOrientation : this.visibleFractionMismatchOrientation;
            Point layoutSize = RendererCommon.getDisplaySize(visibleFraction, frameAspect, maxWidth, maxHeight);
            if (View.MeasureSpec.getMode((int)widthSpec) == 0x40000000) {
                layoutSize.x = maxWidth;
            }
            if (View.MeasureSpec.getMode((int)heightSpec) == 0x40000000) {
                layoutSize.y = maxHeight;
            }
            return layoutSize;
        }
    }

    public static interface GlDrawer {
        public void drawOes(int var1, float[] var2, int var3, int var4, int var5, int var6, int var7, int var8);

        public void drawRgb(int var1, float[] var2, int var3, int var4, int var5, int var6, int var7, int var8);

        public void drawYuv(int[] var1, float[] var2, int var3, int var4, int var5, int var6, int var7, int var8);

        public void release();
    }

    public static interface RendererEvents {
        public void onFirstFrameRendered();

        public void onFrameResolutionChanged(int var1, int var2, int var3);
    }
}

