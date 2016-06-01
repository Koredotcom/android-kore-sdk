package websocket.async.util;

/**
 * Created by Ramachandra on 8/25/2015.
 */

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;



public final class ThreadPoolExecutorForPresence {
    private int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    private ThreadPoolExecutor executor;

    private static ThreadPoolExecutorForPresence mInstance;

    private ThreadPoolExecutorForPresence(){

    }
    public static synchronized ThreadPoolExecutorForPresence getInstance(){
        if(mInstance == null)
            mInstance = new ThreadPoolExecutorForPresence();
        return mInstance;
    }
    public ThreadPoolExecutor getExecutor(){
        if (executor == null || executor.isShutdown()) {
            executor = new ThreadPoolExecutor(NUMBER_OF_CORES*2,
                    NUMBER_OF_CORES*2,
                    60L,
                    TimeUnit.SECONDS,
                    new LinkedBlockingQueue<Runnable>());
        }
        return executor;
    }

    //Restricting cloning of this class
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new CloneNotSupportedException("Clone not supported");
    }
}

