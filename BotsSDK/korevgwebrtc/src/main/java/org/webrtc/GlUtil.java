/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.opengl.GLES20
 *  android.opengl.GLException
 */
package org.webrtc;

import android.opengl.GLES20;
import android.opengl.GLException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class GlUtil {
    private GlUtil() {
    }

    public static void checkNoGLES2Error(String msg) {
        int error = GLES20.glGetError();
        if (error != 0) {
            throw error == 1285 ? new GlOutOfMemoryException(error, msg) : new GLException(error, msg + ": GLES20 error: " + error);
        }
    }

    public static FloatBuffer createFloatBuffer(float[] coords) {
        ByteBuffer bb = ByteBuffer.allocateDirect(coords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        FloatBuffer fb = bb.asFloatBuffer();
        fb.put(coords);
        fb.position(0);
        return fb;
    }

    public static int generateTexture(int target) {
        int[] textureArray = new int[1];
        GLES20.glGenTextures((int)1, (int[])textureArray, (int)0);
        int textureId = textureArray[0];
        GLES20.glBindTexture((int)target, (int)textureId);
        GLES20.glTexParameterf((int)target, (int)10241, (float)9729.0f);
        GLES20.glTexParameterf((int)target, (int)10240, (float)9729.0f);
        GLES20.glTexParameterf((int)target, (int)10242, (float)33071.0f);
        GLES20.glTexParameterf((int)target, (int)10243, (float)33071.0f);
        GlUtil.checkNoGLES2Error("generateTexture");
        return textureId;
    }

    public static class GlOutOfMemoryException
    extends GLException {
        public GlOutOfMemoryException(int error, String msg) {
            super(error, msg);
        }
    }
}

