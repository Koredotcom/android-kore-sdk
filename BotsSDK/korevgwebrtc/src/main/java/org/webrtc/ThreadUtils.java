/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  android.os.Handler
 *  android.os.Looper
 *  android.os.SystemClock
 *  androidx.annotation.Nullable
 */
package org.webrtc;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import androidx.annotation.Nullable;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ThreadUtils {
    public static void checkIsOnMainThread() {
        if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
            throw new IllegalStateException("Not on main thread!");
        }
    }

    public static void executeUninterruptibly(BlockingOperation operation) {
        boolean wasInterrupted = false;
        while (true) {
            try {
                operation.run();
            }
            catch (InterruptedException e) {
                wasInterrupted = true;
                continue;
            }
            break;
        }
        if (wasInterrupted) {
            Thread.currentThread().interrupt();
        }
    }

    public static boolean joinUninterruptibly(Thread thread, long timeoutMs) {
        long startTimeMs = SystemClock.elapsedRealtime();
        long timeRemainingMs = timeoutMs;
        boolean wasInterrupted = false;
        while (timeRemainingMs > 0L) {
            try {
                thread.join(timeRemainingMs);
                break;
            }
            catch (InterruptedException e) {
                wasInterrupted = true;
                long elapsedTimeMs = SystemClock.elapsedRealtime() - startTimeMs;
                timeRemainingMs = timeoutMs - elapsedTimeMs;
            }
        }
        if (wasInterrupted) {
            Thread.currentThread().interrupt();
        }
        return !thread.isAlive();
    }

    public static void joinUninterruptibly(final Thread thread) {
        ThreadUtils.executeUninterruptibly(new BlockingOperation(){

            @Override
            public void run() throws InterruptedException {
                thread.join();
            }
        });
    }

    public static void awaitUninterruptibly(final CountDownLatch latch) {
        ThreadUtils.executeUninterruptibly(new BlockingOperation(){

            @Override
            public void run() throws InterruptedException {
                latch.await();
            }
        });
    }

    public static boolean awaitUninterruptibly(CountDownLatch barrier, long timeoutMs) {
        long startTimeMs = SystemClock.elapsedRealtime();
        long timeRemainingMs = timeoutMs;
        boolean wasInterrupted = false;
        boolean result = false;
        while (true) {
            try {
                result = barrier.await(timeRemainingMs, TimeUnit.MILLISECONDS);
            }
            catch (InterruptedException e) {
                long elapsedTimeMs;
                wasInterrupted = true;
                if ((timeRemainingMs = timeoutMs - (elapsedTimeMs = SystemClock.elapsedRealtime() - startTimeMs)) > 0L) continue;
            }
            break;
        }
        if (wasInterrupted) {
            Thread.currentThread().interrupt();
        }
        return result;
    }

    public static <V> V invokeAtFrontUninterruptibly(Handler handler, final Callable<V> callable) {
        if (handler.getLooper().getThread() == Thread.currentThread()) {
            try {
                return callable.call();
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        class Result {
            public V value;

            Result() {
            }
        }
        final Result result = new Result();
        class CaughtException {
            Exception e;

            CaughtException() {
            }
        }
        final CaughtException caughtException = new CaughtException();
        final CountDownLatch barrier = new CountDownLatch(1);
        handler.post(new Runnable(){
            {
            }

            @Override
            public void run() {
                try {
                    result.value = callable.call();
                }
                catch (Exception e) {
                    caughtException.e = e;
                }
                barrier.countDown();
            }
        });
        ThreadUtils.awaitUninterruptibly(barrier);
        if (caughtException.e != null) {
            RuntimeException runtimeException = new RuntimeException(caughtException.e);
            runtimeException.setStackTrace(ThreadUtils.concatStackTraces(caughtException.e.getStackTrace(), runtimeException.getStackTrace()));
            throw runtimeException;
        }
        return result.value;
    }

    public static void invokeAtFrontUninterruptibly(Handler handler, final Runnable runner) {
        ThreadUtils.invokeAtFrontUninterruptibly(handler, new Callable<Void>(){

            @Override
            public Void call() {
                runner.run();
                return null;
            }
        });
    }

    static StackTraceElement[] concatStackTraces(StackTraceElement[] inner, StackTraceElement[] outer) {
        StackTraceElement[] combined = new StackTraceElement[inner.length + outer.length];
        System.arraycopy(inner, 0, combined, 0, inner.length);
        System.arraycopy(outer, 0, combined, inner.length, outer.length);
        return combined;
    }

    public static interface BlockingOperation {
        public void run() throws InterruptedException;
    }

    public static class ThreadChecker {
        @Nullable
        private Thread thread = Thread.currentThread();

        public void checkIsOnValidThread() {
            if (this.thread == null) {
                this.thread = Thread.currentThread();
            }
            if (Thread.currentThread() != this.thread) {
                throw new IllegalStateException("Wrong thread");
            }
        }

        public void detachThread() {
            this.thread = null;
        }
    }
}

