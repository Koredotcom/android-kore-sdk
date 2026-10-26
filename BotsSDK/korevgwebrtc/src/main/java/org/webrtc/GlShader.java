/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.opengl.GLES20
 */
package org.webrtc;

import android.opengl.GLES20;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import org.webrtc.EglBase;
import org.webrtc.GlUtil;
import org.webrtc.Logging;

public class GlShader {
    private static final String TAG = "GlShader";
    private int program;

    private static int compileShader(int shaderType, String source) {
        int shader = GLES20.glCreateShader((int)shaderType);
        if (shader == 0) {
            throw new RuntimeException("glCreateShader() failed. GLES20 error: " + GLES20.glGetError());
        }
        GLES20.glShaderSource((int)shader, (String)source);
        GLES20.glCompileShader((int)shader);
        int[] compileStatus = new int[]{0};
        GLES20.glGetShaderiv((int)shader, (int)35713, (int[])compileStatus, (int)0);
        if (compileStatus[0] != 1) {
            Logging.e(TAG, "Compile error " + GLES20.glGetShaderInfoLog((int)shader) + " in shader:\n" + source);
            throw new RuntimeException(GLES20.glGetShaderInfoLog((int)shader));
        }
        GlUtil.checkNoGLES2Error("compileShader");
        return shader;
    }

    public GlShader(String vertexSource, String fragmentSource) {
        int vertexShader = GlShader.compileShader(35633, vertexSource);
        int fragmentShader = GlShader.compileShader(35632, fragmentSource);
        this.program = GLES20.glCreateProgram();
        if (this.program == 0) {
            throw new RuntimeException("glCreateProgram() failed. GLES20 error: " + GLES20.glGetError());
        }
        GLES20.glAttachShader((int)this.program, (int)vertexShader);
        GLES20.glAttachShader((int)this.program, (int)fragmentShader);
        GLES20.glLinkProgram((int)this.program);
        int[] linkStatus = new int[]{0};
        GLES20.glGetProgramiv((int)this.program, (int)35714, (int[])linkStatus, (int)0);
        if (linkStatus[0] != 1) {
            Logging.e(TAG, "Could not link program: " + GLES20.glGetProgramInfoLog((int)this.program));
            throw new RuntimeException(GLES20.glGetProgramInfoLog((int)this.program));
        }
        GLES20.glDeleteShader((int)vertexShader);
        GLES20.glDeleteShader((int)fragmentShader);
        GlUtil.checkNoGLES2Error("Creating GlShader");
    }

    public int getAttribLocation(String label) {
        if (this.program == -1) {
            throw new RuntimeException("The program has been released");
        }
        int location = GLES20.glGetAttribLocation((int)this.program, (String)label);
        if (location < 0) {
            throw new RuntimeException("Could not locate '" + label + "' in program");
        }
        return location;
    }

    public void setVertexAttribArray(String label, int dimension, FloatBuffer buffer) {
        this.setVertexAttribArray(label, dimension, 0, buffer);
    }

    public void setVertexAttribArray(String label, int dimension, int stride, FloatBuffer buffer) {
        if (this.program == -1) {
            throw new RuntimeException("The program has been released");
        }
        int location = this.getAttribLocation(label);
        GLES20.glEnableVertexAttribArray((int)location);
        GLES20.glVertexAttribPointer((int)location, (int)dimension, (int)5126, (boolean)false, (int)stride, (Buffer)buffer);
        GlUtil.checkNoGLES2Error("setVertexAttribArray");
    }

    public int getUniformLocation(String label) {
        if (this.program == -1) {
            throw new RuntimeException("The program has been released");
        }
        int location = GLES20.glGetUniformLocation((int)this.program, (String)label);
        if (location < 0) {
            throw new RuntimeException("Could not locate uniform '" + label + "' in program");
        }
        return location;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void useProgram() {
        if (this.program == -1) {
            throw new RuntimeException("The program has been released");
        }
        Object object = EglBase.lock;
        synchronized (object) {
            GLES20.glUseProgram((int)this.program);
        }
        GlUtil.checkNoGLES2Error("glUseProgram");
    }

    public void release() {
        Logging.d(TAG, "Deleting shader.");
        if (this.program != -1) {
            GLES20.glDeleteProgram((int)this.program);
            this.program = -1;
        }
    }
}

