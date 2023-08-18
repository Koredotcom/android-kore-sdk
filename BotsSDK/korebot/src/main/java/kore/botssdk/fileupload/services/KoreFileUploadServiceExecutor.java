package kore.botssdk.fileupload.services;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Ramachandra Pradeep on 9/14/2016.
 */
public final class KoreFileUploadServiceExecutor {
    private final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    private ThreadPoolExecutor executor;

    private static KoreFileUploadServiceExecutor mInstance;

    private KoreFileUploadServiceExecutor(){

    }
    public static synchronized KoreFileUploadServiceExecutor getInstance(){
        if(mInstance == null)
            mInstance = new KoreFileUploadServiceExecutor();
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
