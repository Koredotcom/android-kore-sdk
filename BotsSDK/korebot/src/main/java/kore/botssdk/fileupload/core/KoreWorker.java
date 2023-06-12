package kore.botssdk.fileupload.core;

import java.util.Vector;

import kore.botssdk.fileupload.listeners.FileUploadedListener;

/**
 * @author Ramachandra Pradeep Challa
 * Copyright (c) 2018 Kore.ai Inc. All rights reserved.
 */
public final class KoreWorker implements Runnable {
    private boolean quit = false;  
    private Vector<Work> queue = null;
    private volatile boolean isInitial = true;
    private volatile boolean isCallBack = false;
    private static KoreWorker _instance;

    private KoreWorker() {
    	queue = new Vector<Work>();
    	quit = false;
    }
    
    public static synchronized KoreWorker getInstance(){
    	if(_instance == null){
    		_instance = new KoreWorker();
    	}
    	return _instance;
    }
    private Work getNext() {
        Work task = null;
        if (!queue.isEmpty()) {  
            task = queue.firstElement();
        }  
        return task;  
    }  
  
   private final FileUploadedListener fileUploaded = new FileUploadedListener() {
		@Override
		public void fileUploaded() {
			synchronized (_instance) {
				isCallBack = true;
			}
			 /*synchronized (queue) {
			queue.notify();
			 }*/
		}
   };
	

	@Override
	public void run() {

		while (!quit) {

			if (isInitial || isCallBack) {
				Work task = getNext();
				if (task != null) {
					synchronized (_instance) {
						isCallBack = false;
					}
					task.initiateFileUpload(fileUploaded);
					synchronized (queue) {
						queue.removeElement(task);
					}

					isInitial = false;
				} else {
					//isInitial = true;
					synchronized (queue) {
						try {
							queue.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}

			} else if (queue.size() == 0) {
//        	synchronized (queue) {  
				try {
//                    queue.wait(); 
//                    System.gc();
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
//            }
			}

		}
	}
	
	private void startThread(){
		new Thread(_instance,"KOREWORKER").start();
	}
  
     public void addTask(Work task) {
        synchronized (queue) {  
            if (!quit) {  
                queue.addElement(task);
                if(isInitial)
                	startThread();
                if(isCallBack){
                	queue.notifyAll();
                }
            }  
        }
    }

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new CloneNotSupportedException("Clone not supported");
	}
}