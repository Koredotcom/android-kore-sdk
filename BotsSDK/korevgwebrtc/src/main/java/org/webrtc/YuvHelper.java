/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import java.nio.ByteBuffer;

public class YuvHelper {
    public static void I420Copy(ByteBuffer srcY, int srcStrideY, ByteBuffer srcU, int srcStrideU, ByteBuffer srcV, int srcStrideV, ByteBuffer dst, int dstWidth, int dstHeight, int dstStrideY, int dstSliceHeightY, int dstStrideU, int dstSliceHeightU) {
        int chromaWidth = (dstWidth + 1) / 2;
        int chromaHeight = (dstHeight + 1) / 2;
        boolean dstStartY = false;
        int dstEndY = 0 + dstStrideY * dstHeight;
        int dstStartU = 0 + dstStrideY * dstSliceHeightY;
        int dstEndU = dstStartU + dstStrideU * chromaHeight;
        int dstStartV = dstStartU + dstStrideU * dstSliceHeightU;
        int dstEndV = dstStartV + dstStrideU * (chromaHeight - 1) + chromaWidth;
        if (dst.capacity() < dstEndV) {
            throw new IllegalArgumentException("Expected destination buffer capacity to be at least " + dstEndV + " was " + dst.capacity());
        }
        dst.limit(dstEndY);
        dst.position(0);
        ByteBuffer dstY = dst.slice();
        dst.limit(dstEndU);
        dst.position(dstStartU);
        ByteBuffer dstU = dst.slice();
        dst.limit(dstEndV);
        dst.position(dstStartV);
        ByteBuffer dstV = dst.slice();
        YuvHelper.I420Copy(srcY, srcStrideY, srcU, srcStrideU, srcV, srcStrideV, dstY, dstStrideY, dstU, dstStrideU, dstV, dstStrideU, dstWidth, dstHeight);
    }

    public static void I420Copy(ByteBuffer srcY, int srcStrideY, ByteBuffer srcU, int srcStrideU, ByteBuffer srcV, int srcStrideV, ByteBuffer dst, int dstWidth, int dstHeight) {
        YuvHelper.I420Copy(srcY, srcStrideY, srcU, srcStrideU, srcV, srcStrideV, dst, dstWidth, dstHeight, dstWidth, dstHeight, (dstWidth + 1) / 2, (dstHeight + 1) / 2);
    }

    public static void I420Copy(ByteBuffer srcY, int srcStrideY, ByteBuffer srcU, int srcStrideU, ByteBuffer srcV, int srcStrideV, ByteBuffer dst, int dstWidth, int dstHeight, int dstStride, int dstSliceHeight) {
        YuvHelper.I420Copy(srcY, srcStrideY, srcU, srcStrideU, srcV, srcStrideV, dst, dstWidth, dstHeight, dstStride, dstSliceHeight, (dstStride + 1) / 2, (dstSliceHeight + 1) / 2);
    }

    public static void I420ToNV12(ByteBuffer srcY, int srcStrideY, ByteBuffer srcU, int srcStrideU, ByteBuffer srcV, int srcStrideV, ByteBuffer dst, int dstWidth, int dstHeight, int dstStrideY, int dstSliceHeightY) {
        int chromaHeight = (dstHeight + 1) / 2;
        int chromaWidth = (dstWidth + 1) / 2;
        boolean dstStartY = false;
        int dstEndY = 0 + dstStrideY * dstHeight;
        int dstStartUV = 0 + dstStrideY * dstSliceHeightY;
        int dstEndUV = dstStartUV + chromaWidth * chromaHeight * 2;
        if (dst.capacity() < dstEndUV) {
            throw new IllegalArgumentException("Expected destination buffer capacity to be at least " + dstEndUV + " was " + dst.capacity());
        }
        dst.limit(dstEndY);
        dst.position(0);
        ByteBuffer dstY = dst.slice();
        dst.limit(dstEndUV);
        dst.position(dstStartUV);
        ByteBuffer dstUV = dst.slice();
        YuvHelper.I420ToNV12(srcY, srcStrideY, srcU, srcStrideU, srcV, srcStrideV, dstY, dstStrideY, dstUV, chromaWidth * 2, dstWidth, dstHeight);
    }

    public static void I420ToNV12(ByteBuffer srcY, int srcStrideY, ByteBuffer srcU, int srcStrideU, ByteBuffer srcV, int srcStrideV, ByteBuffer dst, int dstWidth, int dstHeight) {
        YuvHelper.I420ToNV12(srcY, srcStrideY, srcU, srcStrideU, srcV, srcStrideV, dst, dstWidth, dstHeight, dstWidth, dstHeight);
    }

    public static void I420Rotate(ByteBuffer srcY, int srcStrideY, ByteBuffer srcU, int srcStrideU, ByteBuffer srcV, int srcStrideV, ByteBuffer dst, int srcWidth, int srcHeight, int rotationMode) {
        YuvHelper.checkNotNull(srcY, "srcY");
        YuvHelper.checkNotNull(srcU, "srcU");
        YuvHelper.checkNotNull(srcV, "srcV");
        YuvHelper.checkNotNull(dst, "dst");
        int dstWidth = rotationMode % 180 == 0 ? srcWidth : srcHeight;
        int dstHeight = rotationMode % 180 == 0 ? srcHeight : srcWidth;
        int dstChromaHeight = (dstHeight + 1) / 2;
        int dstChromaWidth = (dstWidth + 1) / 2;
        int minSize = dstWidth * dstHeight + dstChromaWidth * dstChromaHeight * 2;
        if (dst.capacity() < minSize) {
            throw new IllegalArgumentException("Expected destination buffer capacity to be at least " + minSize + " was " + dst.capacity());
        }
        boolean startY = false;
        int startU = dstHeight * dstWidth;
        int startV = startU + dstChromaHeight * dstChromaWidth;
        dst.position(0);
        ByteBuffer dstY = dst.slice();
        dst.position(startU);
        ByteBuffer dstU = dst.slice();
        dst.position(startV);
        ByteBuffer dstV = dst.slice();
        YuvHelper.nativeI420Rotate(srcY, srcStrideY, srcU, srcStrideU, srcV, srcStrideV, dstY, dstWidth, dstU, dstChromaWidth, dstV, dstChromaWidth, srcWidth, srcHeight, rotationMode);
    }

    public static void copyPlane(ByteBuffer src, int srcStride, ByteBuffer dst, int dstStride, int width, int height) {
        YuvHelper.nativeCopyPlane(YuvHelper.checkNotNull(src, "src"), srcStride, YuvHelper.checkNotNull(dst, "dst"), dstStride, width, height);
    }

    public static void ABGRToI420(ByteBuffer src, int srcStride, ByteBuffer dstY, int dstStrideY, ByteBuffer dstU, int dstStrideU, ByteBuffer dstV, int dstStrideV, int width, int height) {
        YuvHelper.nativeABGRToI420(YuvHelper.checkNotNull(src, "src"), srcStride, YuvHelper.checkNotNull(dstY, "dstY"), dstStrideY, YuvHelper.checkNotNull(dstU, "dstU"), dstStrideU, YuvHelper.checkNotNull(dstV, "dstV"), dstStrideV, width, height);
    }

    public static void I420Copy(ByteBuffer srcY, int srcStrideY, ByteBuffer srcU, int srcStrideU, ByteBuffer srcV, int srcStrideV, ByteBuffer dstY, int dstStrideY, ByteBuffer dstU, int dstStrideU, ByteBuffer dstV, int dstStrideV, int width, int height) {
        YuvHelper.checkNotNull(srcY, "srcY");
        YuvHelper.checkNotNull(srcU, "srcU");
        YuvHelper.checkNotNull(srcV, "srcV");
        YuvHelper.checkNotNull(dstY, "dstY");
        YuvHelper.checkNotNull(dstU, "dstU");
        YuvHelper.checkNotNull(dstV, "dstV");
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("I420Copy: width and height should not be negative");
        }
        YuvHelper.nativeI420Copy(srcY, srcStrideY, srcU, srcStrideU, srcV, srcStrideV, dstY, dstStrideY, dstU, dstStrideU, dstV, dstStrideV, width, height);
    }

    public static void I420ToNV12(ByteBuffer srcY, int srcStrideY, ByteBuffer srcU, int srcStrideU, ByteBuffer srcV, int srcStrideV, ByteBuffer dstY, int dstStrideY, ByteBuffer dstUV, int dstStrideUV, int width, int height) {
        YuvHelper.checkNotNull(srcY, "srcY");
        YuvHelper.checkNotNull(srcU, "srcU");
        YuvHelper.checkNotNull(srcV, "srcV");
        YuvHelper.checkNotNull(dstY, "dstY");
        YuvHelper.checkNotNull(dstUV, "dstUV");
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("I420ToNV12: width and height should not be negative");
        }
        YuvHelper.nativeI420ToNV12(srcY, srcStrideY, srcU, srcStrideU, srcV, srcStrideV, dstY, dstStrideY, dstUV, dstStrideUV, width, height);
    }

    public static void I420Rotate(ByteBuffer srcY, int srcStrideY, ByteBuffer srcU, int srcStrideU, ByteBuffer srcV, int srcStrideV, ByteBuffer dstY, int dstStrideY, ByteBuffer dstU, int dstStrideU, ByteBuffer dstV, int dstStrideV, int srcWidth, int srcHeight, int rotationMode) {
        YuvHelper.checkNotNull(srcY, "srcY");
        YuvHelper.checkNotNull(srcU, "srcU");
        YuvHelper.checkNotNull(srcV, "srcV");
        YuvHelper.checkNotNull(dstY, "dstY");
        YuvHelper.checkNotNull(dstU, "dstU");
        YuvHelper.checkNotNull(dstV, "dstV");
        YuvHelper.nativeI420Rotate(srcY, srcStrideY, srcU, srcStrideU, srcV, srcStrideV, dstY, dstStrideY, dstU, dstStrideU, dstV, dstStrideV, srcWidth, srcHeight, rotationMode);
    }

    private static <T> T checkNotNull(T obj, String description) {
        if (obj == null) {
            throw new NullPointerException(description + " should not be null");
        }
        return obj;
    }

    private static native void nativeCopyPlane(ByteBuffer var0, int var1, ByteBuffer var2, int var3, int var4, int var5);

    private static native void nativeI420Copy(ByteBuffer var0, int var1, ByteBuffer var2, int var3, ByteBuffer var4, int var5, ByteBuffer var6, int var7, ByteBuffer var8, int var9, ByteBuffer var10, int var11, int var12, int var13);

    private static native void nativeI420ToNV12(ByteBuffer var0, int var1, ByteBuffer var2, int var3, ByteBuffer var4, int var5, ByteBuffer var6, int var7, ByteBuffer var8, int var9, int var10, int var11);

    private static native void nativeI420Rotate(ByteBuffer var0, int var1, ByteBuffer var2, int var3, ByteBuffer var4, int var5, ByteBuffer var6, int var7, ByteBuffer var8, int var9, ByteBuffer var10, int var11, int var12, int var13, int var14);

    private static native void nativeABGRToI420(ByteBuffer var0, int var1, ByteBuffer var2, int var3, ByteBuffer var4, int var5, ByteBuffer var6, int var7, int var8, int var9);
}

