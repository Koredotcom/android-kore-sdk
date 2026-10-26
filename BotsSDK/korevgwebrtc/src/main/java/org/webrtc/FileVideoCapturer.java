/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.content.Context
 *  android.os.SystemClock
 */
package org.webrtc;

import android.content.Context;
import android.os.SystemClock;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import org.webrtc.CapturerObserver;
import org.webrtc.JavaI420Buffer;
import org.webrtc.Logging;
import org.webrtc.SurfaceTextureHelper;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoFrame;

public class FileVideoCapturer
implements VideoCapturer {
    private static final String TAG = "FileVideoCapturer";
    private final VideoReader videoReader;
    private CapturerObserver capturerObserver;
    private final Timer timer = new Timer();
    private final TimerTask tickTask = new TimerTask(){

        @Override
        public void run() {
            FileVideoCapturer.this.tick();
        }
    };

    public FileVideoCapturer(String inputFile) throws IOException {
        try {
            this.videoReader = new VideoReaderY4M(inputFile);
        }
        catch (IOException e) {
            Logging.d(TAG, "Could not open video file: " + inputFile);
            throw e;
        }
    }

    public void tick() {
        VideoFrame videoFrame = this.videoReader.getNextFrame();
        this.capturerObserver.onFrameCaptured(videoFrame);
        videoFrame.release();
    }

    @Override
    public void initialize(SurfaceTextureHelper surfaceTextureHelper, Context applicationContext, CapturerObserver capturerObserver) {
        this.capturerObserver = capturerObserver;
    }

    @Override
    public void startCapture(int width, int height, int framerate) {
        this.timer.schedule(this.tickTask, 0L, (long)(1000 / framerate));
    }

    @Override
    public void stopCapture() throws InterruptedException {
        this.timer.cancel();
    }

    @Override
    public void changeCaptureFormat(int width, int height, int framerate) {
    }

    @Override
    public void dispose() {
        this.videoReader.close();
    }

    @Override
    public boolean isScreencast() {
        return false;
    }

    private static class VideoReaderY4M
    implements VideoReader {
        private static final String TAG = "VideoReaderY4M";
        private static final String Y4M_FRAME_DELIMETER = "FRAME";
        private static final int FRAME_DELIMETER_LENGTH = "FRAME".length() + 1;
        private final int frameWidth;
        private final int frameHeight;
        private final long videoStart;
        private final RandomAccessFile mediaFile;
        private final FileChannel mediaFileChannel;

        public VideoReaderY4M(String file) throws IOException {
            this.mediaFile = new RandomAccessFile(file, "r");
            this.mediaFileChannel = this.mediaFile.getChannel();
            StringBuilder builder = new StringBuilder();
            while (true) {
                int c;
                if ((c = this.mediaFile.read()) == -1) {
                    throw new RuntimeException("Found end of file before end of header for file: " + file);
                }
                if (c == 10) break;
                builder.append((char)c);
            }
            this.videoStart = this.mediaFileChannel.position();
            String header = builder.toString();
            String[] headerTokens = header.split("[ ]");
            int w = 0;
            int h = 0;
            String colorSpace = "";
            block6: for (String tok : headerTokens) {
                char c = tok.charAt(0);
                switch (c) {
                    case 'W': {
                        w = Integer.parseInt(tok.substring(1));
                        continue block6;
                    }
                    case 'H': {
                        h = Integer.parseInt(tok.substring(1));
                        continue block6;
                    }
                    case 'C': {
                        colorSpace = tok.substring(1);
                    }
                }
            }
            Logging.d(TAG, "Color space: " + colorSpace);
            if (!colorSpace.equals("420") && !colorSpace.equals("420mpeg2")) {
                throw new IllegalArgumentException("Does not support any other color space than I420 or I420mpeg2");
            }
            if (w % 2 == 1 || h % 2 == 1) {
                throw new IllegalArgumentException("Does not support odd width or height");
            }
            this.frameWidth = w;
            this.frameHeight = h;
            Logging.d(TAG, "frame dim: (" + w + ", " + h + ")");
        }

        @Override
        public VideoFrame getNextFrame() {
            long captureTimeNs = TimeUnit.MILLISECONDS.toNanos(SystemClock.elapsedRealtime());
            JavaI420Buffer buffer = JavaI420Buffer.allocate(this.frameWidth, this.frameHeight);
            ByteBuffer dataY = buffer.getDataY();
            ByteBuffer dataU = buffer.getDataU();
            ByteBuffer dataV = buffer.getDataV();
            int chromaHeight = (this.frameHeight + 1) / 2;
            int sizeY = this.frameHeight * buffer.getStrideY();
            int sizeU = chromaHeight * buffer.getStrideU();
            int sizeV = chromaHeight * buffer.getStrideV();
            try {
                String frameDelimStr;
                ByteBuffer frameDelim = ByteBuffer.allocate(FRAME_DELIMETER_LENGTH);
                if (this.mediaFileChannel.read(frameDelim) < FRAME_DELIMETER_LENGTH) {
                    this.mediaFileChannel.position(this.videoStart);
                    if (this.mediaFileChannel.read(frameDelim) < FRAME_DELIMETER_LENGTH) {
                        throw new RuntimeException("Error looping video");
                    }
                }
                if (!(frameDelimStr = new String(frameDelim.array(), Charset.forName("US-ASCII"))).equals("FRAME\n")) {
                    throw new RuntimeException("Frames should be delimited by FRAME plus newline, found delimter was: '" + frameDelimStr + "'");
                }
                this.mediaFileChannel.read(dataY);
                this.mediaFileChannel.read(dataU);
                this.mediaFileChannel.read(dataV);
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
            return new VideoFrame(buffer, 0, captureTimeNs);
        }

        @Override
        public void close() {
            try {
                this.mediaFile.close();
            }
            catch (IOException e) {
                Logging.e(TAG, "Problem closing file", e);
            }
        }
    }

    private static interface VideoReader {
        public VideoFrame getNextFrame();

        public void close();
    }
}

