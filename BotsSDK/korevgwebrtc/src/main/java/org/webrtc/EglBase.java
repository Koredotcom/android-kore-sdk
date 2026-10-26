/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.graphics.SurfaceTexture
 *  android.opengl.EGLContext
 *  androidx.annotation.Nullable
 *  android.view.Surface
 *  javax.microedition.khronos.egl.EGLContext
 */
package org.webrtc;

import android.graphics.SurfaceTexture;
import android.opengl.EGLContext;
import androidx.annotation.Nullable;
import android.view.Surface;
import java.util.ArrayList;
import org.webrtc.EglBase10;
import org.webrtc.EglBase10Impl;
import org.webrtc.EglBase14;
import org.webrtc.EglBase14Impl;

public interface EglBase {
    public static final Object lock = new Object();
    public static final int EGL_OPENGL_ES2_BIT = 4;
    public static final int EGL_OPENGL_ES3_BIT = 64;
    public static final int EGL_RECORDABLE_ANDROID = 12610;
    public static final int[] CONFIG_PLAIN = EglBase.configBuilder().createConfigAttributes();
    public static final int[] CONFIG_RGBA = EglBase.configBuilder().setHasAlphaChannel(true).createConfigAttributes();
    public static final int[] CONFIG_PIXEL_BUFFER = EglBase.configBuilder().setSupportsPixelBuffer(true).createConfigAttributes();
    public static final int[] CONFIG_PIXEL_RGBA_BUFFER = EglBase.configBuilder().setHasAlphaChannel(true).setSupportsPixelBuffer(true).createConfigAttributes();
    public static final int[] CONFIG_RECORDABLE = EglBase.configBuilder().setIsRecordable(true).createConfigAttributes();

    public static ConfigBuilder configBuilder() {
        return new ConfigBuilder();
    }

    public static int getOpenGlesVersionFromConfig(int[] configAttributes) {
        for (int i = 0; i < configAttributes.length - 1; ++i) {
            if (configAttributes[i] != 12352) continue;
            switch (configAttributes[i + 1]) {
                case 4: {
                    return 2;
                }
                case 64: {
                    return 3;
                }
            }
            return 1;
        }
        return 1;
    }

    public static EglBase create(@Nullable Context sharedContext, int[] configAttributes) {
        if (sharedContext == null) {
            return EglBase14Impl.isEGL14Supported() ? EglBase.createEgl14(configAttributes) : EglBase.createEgl10(configAttributes);
        }
        if (sharedContext instanceof EglBase14.Context) {
            return EglBase.createEgl14((EglBase14.Context)sharedContext, configAttributes);
        }
        if (sharedContext instanceof EglBase10.Context) {
            return EglBase.createEgl10((EglBase10.Context)sharedContext, configAttributes);
        }
        throw new IllegalArgumentException("Unrecognized Context");
    }

    public static EglBase create() {
        return EglBase.create(null, CONFIG_PLAIN);
    }

    public static EglBase create(Context sharedContext) {
        return EglBase.create(sharedContext, CONFIG_PLAIN);
    }

    public static EglBase10 createEgl10(int[] configAttributes) {
        return new EglBase10Impl(null, configAttributes);
    }

    public static EglBase10 createEgl10(EglBase10.Context sharedContext, int[] configAttributes) {
        return new EglBase10Impl(sharedContext == null ? null : sharedContext.getRawContext(), configAttributes);
    }

    public static EglBase10 createEgl10(javax.microedition.khronos.egl.EGLContext sharedContext, int[] configAttributes) {
        return new EglBase10Impl(sharedContext, configAttributes);
    }

    public static EglBase14 createEgl14(int[] configAttributes) {
        return new EglBase14Impl(null, configAttributes);
    }

    public static EglBase14 createEgl14(EglBase14.Context sharedContext, int[] configAttributes) {
        return new EglBase14Impl(sharedContext == null ? null : sharedContext.getRawContext(), configAttributes);
    }

    public static EglBase14 createEgl14(EGLContext sharedContext, int[] configAttributes) {
        return new EglBase14Impl(sharedContext, configAttributes);
    }

    public void createSurface(Surface var1);

    public void createSurface(SurfaceTexture var1);

    public void createDummyPbufferSurface();

    public void createPbufferSurface(int var1, int var2);

    public Context getEglBaseContext();

    public boolean hasSurface();

    public int surfaceWidth();

    public int surfaceHeight();

    public void releaseSurface();

    public void release();

    public void makeCurrent();

    public void detachCurrent();

    public void swapBuffers();

    public void swapBuffers(long var1);

    public static class ConfigBuilder {
        private int openGlesVersion = 2;
        private boolean hasAlphaChannel;
        private boolean supportsPixelBuffer;
        private boolean isRecordable;

        public ConfigBuilder setOpenGlesVersion(int version) {
            if (version < 1 || version > 3) {
                throw new IllegalArgumentException("OpenGL ES version " + version + " not supported");
            }
            this.openGlesVersion = version;
            return this;
        }

        public ConfigBuilder setHasAlphaChannel(boolean hasAlphaChannel) {
            this.hasAlphaChannel = hasAlphaChannel;
            return this;
        }

        public ConfigBuilder setSupportsPixelBuffer(boolean supportsPixelBuffer) {
            this.supportsPixelBuffer = supportsPixelBuffer;
            return this;
        }

        public ConfigBuilder setIsRecordable(boolean isRecordable) {
            this.isRecordable = isRecordable;
            return this;
        }

        public int[] createConfigAttributes() {
            ArrayList<Integer> list = new ArrayList<Integer>();
            list.add(12324);
            list.add(8);
            list.add(12323);
            list.add(8);
            list.add(12322);
            list.add(8);
            if (this.hasAlphaChannel) {
                list.add(12321);
                list.add(8);
            }
            if (this.openGlesVersion == 2 || this.openGlesVersion == 3) {
                list.add(12352);
                list.add(this.openGlesVersion == 3 ? 64 : 4);
            }
            if (this.supportsPixelBuffer) {
                list.add(12339);
                list.add(1);
            }
            if (this.isRecordable) {
                list.add(12610);
                list.add(1);
            }
            list.add(12344);
            int[] res = new int[list.size()];
            for (int i = 0; i < list.size(); ++i) {
                res[i] = (Integer)list.get(i);
            }
            return res;
        }
    }

    public static interface Context {
        public static final long NO_CONTEXT = 0L;

        public long getNativeEglContext();
    }
}

