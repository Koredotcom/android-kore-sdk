/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.opengl.GLES20
 */
package org.webrtc;

import android.opengl.GLES20;
import org.webrtc.GlUtil;

public class GlTextureFrameBuffer {
    private final int pixelFormat;
    private int frameBufferId;
    private int textureId;
    private int width;
    private int height;

    public GlTextureFrameBuffer(int pixelFormat) {
        switch (pixelFormat) {
            case 6407: 
            case 6408: 
            case 6409: {
                this.pixelFormat = pixelFormat;
                break;
            }
            default: {
                throw new IllegalArgumentException("Invalid pixel format: " + pixelFormat);
            }
        }
        this.width = 0;
        this.height = 0;
    }

    public void setSize(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Invalid size: " + width + "x" + height);
        }
        if (width == this.width && height == this.height) {
            return;
        }
        this.width = width;
        this.height = height;
        if (this.textureId == 0) {
            this.textureId = GlUtil.generateTexture(3553);
        }
        if (this.frameBufferId == 0) {
            int[] frameBuffers = new int[1];
            GLES20.glGenFramebuffers((int)1, (int[])frameBuffers, (int)0);
            this.frameBufferId = frameBuffers[0];
        }
        GLES20.glActiveTexture((int)33984);
        GLES20.glBindTexture((int)3553, (int)this.textureId);
        GLES20.glTexImage2D((int)3553, (int)0, (int)this.pixelFormat, (int)width, (int)height, (int)0, (int)this.pixelFormat, (int)5121, null);
        GLES20.glBindTexture((int)3553, (int)0);
        GlUtil.checkNoGLES2Error("GlTextureFrameBuffer setSize");
        GLES20.glBindFramebuffer((int)36160, (int)this.frameBufferId);
        GLES20.glFramebufferTexture2D((int)36160, (int)36064, (int)3553, (int)this.textureId, (int)0);
        int status = GLES20.glCheckFramebufferStatus((int)36160);
        if (status != 36053) {
            throw new IllegalStateException("Framebuffer not complete, status: " + status);
        }
        GLES20.glBindFramebuffer((int)36160, (int)0);
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getFrameBufferId() {
        return this.frameBufferId;
    }

    public int getTextureId() {
        return this.textureId;
    }

    public void release() {
        GLES20.glDeleteTextures((int)1, (int[])new int[]{this.textureId}, (int)0);
        this.textureId = 0;
        GLES20.glDeleteFramebuffers((int)1, (int[])new int[]{this.frameBufferId}, (int)0);
        this.frameBufferId = 0;
        this.width = 0;
        this.height = 0;
    }
}

