/*
 * Decompiled with CFR 0.152.
 */
package org.webrtc;

import org.webrtc.GlGenericDrawer;
import org.webrtc.GlShader;

public class GlRectDrawer
extends GlGenericDrawer {
    private static final String FRAGMENT_SHADER = "void main() {\n  gl_FragColor = sample(tc);\n}\n";

    public GlRectDrawer() {
        super(FRAGMENT_SHADER, new ShaderCallbacks());
    }

    private static class ShaderCallbacks
    implements GlGenericDrawer.ShaderCallbacks {
        private ShaderCallbacks() {
        }

        @Override
        public void onNewShader(GlShader shader) {
        }

        @Override
        public void onPrepareShader(GlShader shader, float[] texMatrix, int frameWidth, int frameHeight, int viewportWidth, int viewportHeight) {
        }
    }
}

