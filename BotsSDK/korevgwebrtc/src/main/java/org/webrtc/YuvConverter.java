/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.Matrix
 *  android.opengl.GLES20
 *  android.opengl.GLException
 *  androidx.annotation.Nullable
 */
package org.webrtc;

import android.graphics.Matrix;
import android.opengl.GLES20;
import android.opengl.GLException;
import androidx.annotation.Nullable;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import org.webrtc.GlGenericDrawer;
import org.webrtc.GlShader;
import org.webrtc.GlTextureFrameBuffer;
import org.webrtc.GlUtil;
import org.webrtc.JavaI420Buffer;
import org.webrtc.JniCommon;
import org.webrtc.Logging;
import org.webrtc.ThreadUtils;
import org.webrtc.VideoFrame;
import org.webrtc.VideoFrameDrawer;

public final class YuvConverter {
    private static final String TAG = "YuvConverter";
    private static final String FRAGMENT_SHADER = "uniform vec2 xUnit;\nuniform vec4 coeffs;\n\nvoid main() {\n  gl_FragColor.r = coeffs.a + dot(coeffs.rgb,\n      sample(tc - 1.5 * xUnit).rgb);\n  gl_FragColor.g = coeffs.a + dot(coeffs.rgb,\n      sample(tc - 0.5 * xUnit).rgb);\n  gl_FragColor.b = coeffs.a + dot(coeffs.rgb,\n      sample(tc + 0.5 * xUnit).rgb);\n  gl_FragColor.a = coeffs.a + dot(coeffs.rgb,\n      sample(tc + 1.5 * xUnit).rgb);\n}\n";
    private final ThreadUtils.ThreadChecker threadChecker = new ThreadUtils.ThreadChecker();
    private final GlTextureFrameBuffer i420TextureFrameBuffer = new GlTextureFrameBuffer(6408);
    private final ShaderCallbacks shaderCallbacks = new ShaderCallbacks();
    private final GlGenericDrawer drawer = new GlGenericDrawer("uniform vec2 xUnit;\nuniform vec4 coeffs;\n\nvoid main() {\n  gl_FragColor.r = coeffs.a + dot(coeffs.rgb,\n      sample(tc - 1.5 * xUnit).rgb);\n  gl_FragColor.g = coeffs.a + dot(coeffs.rgb,\n      sample(tc - 0.5 * xUnit).rgb);\n  gl_FragColor.b = coeffs.a + dot(coeffs.rgb,\n      sample(tc + 0.5 * xUnit).rgb);\n  gl_FragColor.a = coeffs.a + dot(coeffs.rgb,\n      sample(tc + 1.5 * xUnit).rgb);\n}\n", this.shaderCallbacks);
    private final VideoFrameDrawer videoFrameDrawer;

    public YuvConverter() {
        this(new VideoFrameDrawer());
    }

    public YuvConverter(VideoFrameDrawer videoFrameDrawer) {
        this.videoFrameDrawer = videoFrameDrawer;
        this.threadChecker.detachThread();
    }

    @Nullable
    public VideoFrame.I420Buffer convert(VideoFrame.TextureBuffer inputTextureBuffer) {
        try {
            return this.convertInternal(inputTextureBuffer);
        }
        catch (GLException e) {
            Logging.w(TAG, "Failed to convert TextureBuffer", e);
            return null;
        }
    }

    private VideoFrame.I420Buffer convertInternal(VideoFrame.TextureBuffer inputTextureBuffer) {
        VideoFrame.TextureBuffer preparedBuffer = (VideoFrame.TextureBuffer)this.videoFrameDrawer.prepareBufferForViewportSize(inputTextureBuffer, inputTextureBuffer.getWidth(), inputTextureBuffer.getHeight());
        int frameWidth = preparedBuffer.getWidth();
        int frameHeight = preparedBuffer.getHeight();
        int stride = (frameWidth + 7) / 8 * 8;
        int uvHeight = (frameHeight + 1) / 2;
        int totalHeight = frameHeight + uvHeight;
        ByteBuffer i420ByteBuffer = JniCommon.nativeAllocateByteBuffer(stride * totalHeight);
        int viewportWidth = stride / 4;
        Matrix renderMatrix = new Matrix();
        renderMatrix.preTranslate(0.5f, 0.5f);
        renderMatrix.preScale(1.0f, -1.0f);
        renderMatrix.preTranslate(-0.5f, -0.5f);
        this.i420TextureFrameBuffer.setSize(viewportWidth, totalHeight);
        GLES20.glBindFramebuffer((int)36160, (int)this.i420TextureFrameBuffer.getFrameBufferId());
        GlUtil.checkNoGLES2Error("glBindFramebuffer");
        this.shaderCallbacks.setPlaneY();
        VideoFrameDrawer.drawTexture(this.drawer, preparedBuffer, renderMatrix, frameWidth, frameHeight, 0, 0, viewportWidth, frameHeight);
        this.shaderCallbacks.setPlaneU();
        VideoFrameDrawer.drawTexture(this.drawer, preparedBuffer, renderMatrix, frameWidth, frameHeight, 0, frameHeight, viewportWidth / 2, uvHeight);
        this.shaderCallbacks.setPlaneV();
        VideoFrameDrawer.drawTexture(this.drawer, preparedBuffer, renderMatrix, frameWidth, frameHeight, viewportWidth / 2, frameHeight, viewportWidth / 2, uvHeight);
        GLES20.glReadPixels((int)0, (int)0, (int)this.i420TextureFrameBuffer.getWidth(), (int)this.i420TextureFrameBuffer.getHeight(), (int)6408, (int)5121, (Buffer)i420ByteBuffer);
        GlUtil.checkNoGLES2Error("YuvConverter.convert");
        GLES20.glBindFramebuffer((int)36160, (int)0);
        boolean yPos = false;
        int uPos = 0 + stride * frameHeight;
        int vPos = uPos + stride / 2;
        i420ByteBuffer.position(0);
        i420ByteBuffer.limit(0 + stride * frameHeight);
        ByteBuffer dataY = i420ByteBuffer.slice();
        i420ByteBuffer.position(uPos);
        int uvSize = stride * (uvHeight - 1) + stride / 2;
        i420ByteBuffer.limit(uPos + uvSize);
        ByteBuffer dataU = i420ByteBuffer.slice();
        i420ByteBuffer.position(vPos);
        i420ByteBuffer.limit(vPos + uvSize);
        ByteBuffer dataV = i420ByteBuffer.slice();
        preparedBuffer.release();
        return JavaI420Buffer.wrap(frameWidth, frameHeight, dataY, stride, dataU, stride, dataV, stride, () -> JniCommon.nativeFreeByteBuffer(i420ByteBuffer));
    }

    public void release() {
        this.threadChecker.checkIsOnValidThread();
        this.drawer.release();
        this.i420TextureFrameBuffer.release();
        this.videoFrameDrawer.release();
        this.threadChecker.detachThread();
    }

    private static class ShaderCallbacks
    implements GlGenericDrawer.ShaderCallbacks {
        private static final float[] yCoeffs = new float[]{0.256788f, 0.504129f, 0.0979059f, 0.0627451f};
        private static final float[] uCoeffs = new float[]{-0.148223f, -0.290993f, 0.439216f, 0.501961f};
        private static final float[] vCoeffs = new float[]{0.439216f, -0.367788f, -0.0714274f, 0.501961f};
        private int xUnitLoc;
        private int coeffsLoc;
        private float[] coeffs;
        private float stepSize;

        private ShaderCallbacks() {
        }

        public void setPlaneY() {
            this.coeffs = yCoeffs;
            this.stepSize = 1.0f;
        }

        public void setPlaneU() {
            this.coeffs = uCoeffs;
            this.stepSize = 2.0f;
        }

        public void setPlaneV() {
            this.coeffs = vCoeffs;
            this.stepSize = 2.0f;
        }

        @Override
        public void onNewShader(GlShader shader) {
            this.xUnitLoc = shader.getUniformLocation("xUnit");
            this.coeffsLoc = shader.getUniformLocation("coeffs");
        }

        @Override
        public void onPrepareShader(GlShader shader, float[] texMatrix, int frameWidth, int frameHeight, int viewportWidth, int viewportHeight) {
            GLES20.glUniform4fv((int)this.coeffsLoc, (int)1, (float[])this.coeffs, (int)0);
            GLES20.glUniform2f((int)this.xUnitLoc, (float)(this.stepSize * texMatrix[0] / (float)frameWidth), (float)(this.stepSize * texMatrix[1] / (float)frameWidth));
        }
    }
}

