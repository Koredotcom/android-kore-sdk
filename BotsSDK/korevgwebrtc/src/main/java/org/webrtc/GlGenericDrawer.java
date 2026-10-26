/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.opengl.GLES20
 *  androidx.annotation.Nullable
 */
package org.webrtc;

import android.opengl.GLES20;
import androidx.annotation.Nullable;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import org.webrtc.GlShader;
import org.webrtc.GlUtil;
import org.webrtc.RendererCommon;

class GlGenericDrawer
implements RendererCommon.GlDrawer {
    private static final String INPUT_VERTEX_COORDINATE_NAME = "in_pos";
    private static final String INPUT_TEXTURE_COORDINATE_NAME = "in_tc";
    private static final String TEXTURE_MATRIX_NAME = "tex_mat";
    private static final String DEFAULT_VERTEX_SHADER_STRING = "varying vec2 tc;\nattribute vec4 in_pos;\nattribute vec4 in_tc;\nuniform mat4 tex_mat;\nvoid main() {\n  gl_Position = in_pos;\n  tc = (tex_mat * in_tc).xy;\n}\n";
    private static final FloatBuffer FULL_RECTANGLE_BUFFER = GlUtil.createFloatBuffer(new float[]{-1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f});
    private static final FloatBuffer FULL_RECTANGLE_TEXTURE_BUFFER = GlUtil.createFloatBuffer(new float[]{0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f});
    private final String genericFragmentSource;
    private final String vertexShader;
    private final ShaderCallbacks shaderCallbacks;
    @Nullable
    private ShaderType currentShaderType;
    @Nullable
    private GlShader currentShader;
    private int inPosLocation;
    private int inTcLocation;
    private int texMatrixLocation;

    static String createFragmentShaderString(String genericFragmentSource, ShaderType shaderType) {
        StringBuilder stringBuilder = new StringBuilder();
        if (shaderType == ShaderType.OES) {
            stringBuilder.append("#extension GL_OES_EGL_image_external : require\n");
        }
        stringBuilder.append("precision mediump float;\n");
        stringBuilder.append("varying vec2 tc;\n");
        if (shaderType == ShaderType.YUV) {
            stringBuilder.append("uniform sampler2D y_tex;\n");
            stringBuilder.append("uniform sampler2D u_tex;\n");
            stringBuilder.append("uniform sampler2D v_tex;\n");
            stringBuilder.append("vec4 sample(vec2 p) {\n");
            stringBuilder.append("  float y = texture2D(y_tex, p).r * 1.16438;\n");
            stringBuilder.append("  float u = texture2D(u_tex, p).r;\n");
            stringBuilder.append("  float v = texture2D(v_tex, p).r;\n");
            stringBuilder.append("  return vec4(y + 1.59603 * v - 0.874202,\n");
            stringBuilder.append("    y - 0.391762 * u - 0.812968 * v + 0.531668,\n");
            stringBuilder.append("    y + 2.01723 * u - 1.08563, 1);\n");
            stringBuilder.append("}\n");
            stringBuilder.append(genericFragmentSource);
        } else {
            String samplerName = shaderType == ShaderType.OES ? "samplerExternalOES" : "sampler2D";
            stringBuilder.append("uniform ").append(samplerName).append(" tex;\n");
            stringBuilder.append(genericFragmentSource.replace("sample(", "texture2D(tex, "));
        }
        return stringBuilder.toString();
    }

    public GlGenericDrawer(String genericFragmentSource, ShaderCallbacks shaderCallbacks) {
        this(DEFAULT_VERTEX_SHADER_STRING, genericFragmentSource, shaderCallbacks);
    }

    public GlGenericDrawer(String vertexShader, String genericFragmentSource, ShaderCallbacks shaderCallbacks) {
        this.vertexShader = vertexShader;
        this.genericFragmentSource = genericFragmentSource;
        this.shaderCallbacks = shaderCallbacks;
    }

    GlShader createShader(ShaderType shaderType) {
        return new GlShader(this.vertexShader, GlGenericDrawer.createFragmentShaderString(this.genericFragmentSource, shaderType));
    }

    @Override
    public void drawOes(int oesTextureId, float[] texMatrix, int frameWidth, int frameHeight, int viewportX, int viewportY, int viewportWidth, int viewportHeight) {
        this.prepareShader(ShaderType.OES, texMatrix, frameWidth, frameHeight, viewportWidth, viewportHeight);
        GLES20.glActiveTexture((int)33984);
        GLES20.glBindTexture((int)36197, (int)oesTextureId);
        GLES20.glViewport((int)viewportX, (int)viewportY, (int)viewportWidth, (int)viewportHeight);
        GLES20.glDrawArrays((int)5, (int)0, (int)4);
        GLES20.glBindTexture((int)36197, (int)0);
    }

    @Override
    public void drawRgb(int textureId, float[] texMatrix, int frameWidth, int frameHeight, int viewportX, int viewportY, int viewportWidth, int viewportHeight) {
        this.prepareShader(ShaderType.RGB, texMatrix, frameWidth, frameHeight, viewportWidth, viewportHeight);
        GLES20.glActiveTexture((int)33984);
        GLES20.glBindTexture((int)3553, (int)textureId);
        GLES20.glViewport((int)viewportX, (int)viewportY, (int)viewportWidth, (int)viewportHeight);
        GLES20.glDrawArrays((int)5, (int)0, (int)4);
        GLES20.glBindTexture((int)3553, (int)0);
    }

    @Override
    public void drawYuv(int[] yuvTextures, float[] texMatrix, int frameWidth, int frameHeight, int viewportX, int viewportY, int viewportWidth, int viewportHeight) {
        int i;
        this.prepareShader(ShaderType.YUV, texMatrix, frameWidth, frameHeight, viewportWidth, viewportHeight);
        for (i = 0; i < 3; ++i) {
            GLES20.glActiveTexture((int)(33984 + i));
            GLES20.glBindTexture((int)3553, (int)yuvTextures[i]);
        }
        GLES20.glViewport((int)viewportX, (int)viewportY, (int)viewportWidth, (int)viewportHeight);
        GLES20.glDrawArrays((int)5, (int)0, (int)4);
        for (i = 0; i < 3; ++i) {
            GLES20.glActiveTexture((int)(33984 + i));
            GLES20.glBindTexture((int)3553, (int)0);
        }
    }

    private void prepareShader(ShaderType shaderType, float[] texMatrix, int frameWidth, int frameHeight, int viewportWidth, int viewportHeight) {
        GlShader shader;
        if (shaderType.equals((Object)this.currentShaderType)) {
            shader = this.currentShader;
        } else {
            this.currentShaderType = null;
            if (this.currentShader != null) {
                this.currentShader.release();
                this.currentShader = null;
            }
            shader = this.createShader(shaderType);
            this.currentShaderType = shaderType;
            this.currentShader = shader;
            shader.useProgram();
            if (shaderType == ShaderType.YUV) {
                GLES20.glUniform1i((int)shader.getUniformLocation("y_tex"), (int)0);
                GLES20.glUniform1i((int)shader.getUniformLocation("u_tex"), (int)1);
                GLES20.glUniform1i((int)shader.getUniformLocation("v_tex"), (int)2);
            } else {
                GLES20.glUniform1i((int)shader.getUniformLocation("tex"), (int)0);
            }
            GlUtil.checkNoGLES2Error("Create shader");
            this.shaderCallbacks.onNewShader(shader);
            this.texMatrixLocation = shader.getUniformLocation(TEXTURE_MATRIX_NAME);
            this.inPosLocation = shader.getAttribLocation(INPUT_VERTEX_COORDINATE_NAME);
            this.inTcLocation = shader.getAttribLocation(INPUT_TEXTURE_COORDINATE_NAME);
        }
        shader.useProgram();
        GLES20.glEnableVertexAttribArray((int)this.inPosLocation);
        GLES20.glVertexAttribPointer((int)this.inPosLocation, (int)2, (int)5126, (boolean)false, (int)0, (Buffer)FULL_RECTANGLE_BUFFER);
        GLES20.glEnableVertexAttribArray((int)this.inTcLocation);
        GLES20.glVertexAttribPointer((int)this.inTcLocation, (int)2, (int)5126, (boolean)false, (int)0, (Buffer)FULL_RECTANGLE_TEXTURE_BUFFER);
        GLES20.glUniformMatrix4fv((int)this.texMatrixLocation, (int)1, (boolean)false, (float[])texMatrix, (int)0);
        this.shaderCallbacks.onPrepareShader(shader, texMatrix, frameWidth, frameHeight, viewportWidth, viewportHeight);
        GlUtil.checkNoGLES2Error("Prepare shader");
    }

    @Override
    public void release() {
        if (this.currentShader != null) {
            this.currentShader.release();
            this.currentShader = null;
            this.currentShaderType = null;
        }
    }

    public static interface ShaderCallbacks {
        public void onNewShader(GlShader var1);

        public void onPrepareShader(GlShader var1, float[] var2, int var3, int var4, int var5, int var6);
    }

    public static enum ShaderType {
        OES,
        RGB,
        YUV;

    }
}

