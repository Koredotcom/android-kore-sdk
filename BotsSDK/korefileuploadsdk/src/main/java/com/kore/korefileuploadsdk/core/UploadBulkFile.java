package com.kore.korefileuploadsdk.core;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.google.gson.Gson;
import com.kore.korefileuploadsdk.configurations.Constants;
import com.kore.korefileuploadsdk.configurations.FileUploadEndPoints;
import com.kore.korefileuploadsdk.listeners.ChunkUploadListener;
import com.kore.korefileuploadsdk.listeners.FileTokenListener;
import com.kore.korefileuploadsdk.listeners.FileUploadedListener;
import com.kore.korefileuploadsdk.managers.BotDBManager;
import com.kore.korefileuploadsdk.managers.FileTokenManager;
import com.kore.korefileuploadsdk.models.ChunkInfo;
import com.kore.korefileuploadsdk.models.FileUploadInfo;
import com.kore.korefileuploadsdk.models.MissingChunks;
import com.kore.korefileuploadsdk.models.UploadError;
import com.kore.korefileuploadsdk.services.KoreFileUploadServiceExecutor;
import com.kore.korefileuploadsdk.ssl.KoreHttpsUrlConnectionBuilder;
import com.kore.korefileuploadsdk.utils.StringUtils;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.concurrent.ExecutorService;

import javax.crypto.CipherInputStream;
import javax.net.ssl.HttpsURLConnection;


public class UploadBulkFile implements Work, FileTokenListener,ChunkUploadListener {
    private static DecimalFormat df2 = new DecimalFormat("###.##");
	public static final String error_msz_key="error_msz_key";
	public static final String isFileSizeMore_key="isFileSizeMore";
	public  static final String fileSizeBytes_key="fileSizeBytes";
	private String LOG_TAG = getClass().getSimpleName();
	private String fileName;
	private String outFilePath=null;
	private String accessToken =null;
//	private String userId = null;
	private String userOrTeamId = null;
	private String fileContext=null;
	private String fileToken = null;
	private String fileExtn = null;
	private String orientation = null;
	private int BUFFER_SIZE;
	private Messenger messenger;

//	private int chunkCount = 0;
	private String thumbnailFilePath;
	private String messageId;
//	private boolean isTeam;
	private Context context;
	private FileUploadedListener listener;
	private String componentType;
	private int noOfMergeAttempts;
	private String host;

	BotDBManager helper;
//	KoreBaseDao<FileUploadInfo, String> fileDao;
	FileUploadInfo uploadInfo = new FileUploadInfo();;
	public static final long FILE_SIZE_20MB = 20 * 1024*1024;
//	KoreBaseDao<FileUploadInfo, String> uploadDao;


	ExecutorService executor = KoreFileUploadServiceExecutor.getInstance().getExecutor();
    private ProgressDialog pDialog;
	private boolean isAnonymousUser;
	private boolean isWebHook;
	private String botId;
	
	public UploadBulkFile(String fileName,String outFilePath, String accessToken, String userId, String fileContext,
						String fileExtn,int BUFFER_SIZE, Messenger messenger,
							String thumbnailFilePath, String messageId,Context context,String componentType,
						  String host, String orientation, boolean isAnonymousUser, boolean isWebHook, String botId){
		this.fileName = fileName;
		this.outFilePath = outFilePath;
		this.accessToken = accessToken;
//		this.userId = userId;
		this.fileContext = fileContext;
		this.fileExtn = fileExtn;
		this.BUFFER_SIZE = BUFFER_SIZE;
		this.messenger = messenger;
		this.thumbnailFilePath = thumbnailFilePath;
		this.messageId = messageId;
		this.context = context;
//		this.isTeam = isTeam;
		this.componentType = componentType;
		this.host = host;
		this.orientation = orientation;
		this.isAnonymousUser = isAnonymousUser;
		this.isWebHook = isWebHook;
		this.botId = botId;
		userOrTeamId = userId;
		
		helper = BotDBManager.getInstance();
		
		/*if(fileDao == null){
			try {
				fileDao = KoreDBManager.getHelperInstance(context, this.userId).getDao(FileUploadInfo.class);
				uploadInfo = fileDao.queryForId(fileName);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}*/
		
		
	}

    private void upLoadProgressState(final int progress ,final boolean show){
        Handler mainHandler = new Handler(context.getMainLooper());

        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                if(show) {
                    if (pDialog == null) {
                        pDialog = new ProgressDialog(context);
                        pDialog.setCancelable(false);
                        pDialog.setIndeterminate(false);
                        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        pDialog.setMax(100);
                    }
                    pDialog.setMessage("Uploading...");
                    pDialog.setProgress(progress);
                    if(!pDialog.isShowing()) {
                        pDialog.show();
                    }
                }else{
                    if(pDialog != null && pDialog.isShowing()){
                        pDialog.dismiss();
                    }
                }
            }
        };
        Log.d("progress",progress+" done");
        mainHandler.post(myRunnable);

    }


    @Override
	public synchronized void fileTokenRecievedSuccessfully(Hashtable<String, String> hsh) {
		fileToken = hsh.get("fileToken");
		startUpload();
	}

	@Override
	public void fileTokenRecievedWithFailure(String errCode, String reason) {
		Log.d(LOG_TAG,"file upload failed because of getting file token and the reason is "+ reason);
		sendUploadFailedNotice(false);
	}

	private synchronized void startUpload() {
		noOfMergeAttempts = 1;
		ByteArrayOutputStream chunkbaos = new ByteArrayOutputStream();

		int dataSizeRead = 0;
//		int dataSizeReadForChunkCount = 0;

		int totalbyteswritten = 0;
		byte[] buffer = new byte[BUFFER_SIZE];

		try {
			FileInputStream fis =  new FileInputStream(outFilePath);
//				FileInputStream fisForChunkCount = new FileInputStream(outFilePath);
			if(fis.getChannel().size() > FILE_SIZE_20MB){
				sendUploadFailedNotice(false);
				showToastMsg("File size can't be more than 20 MB!");
				Log.d(LOG_TAG, "File size can't be more than 20 mb");
				return;
			}
				if (fis.available() > 0) {
					int chunkCount = (int)fis.getChannel().size()/BUFFER_SIZE;
					if((int)fis.getChannel().size()%BUFFER_SIZE > 0){
						chunkCount++;
					}
					setChunkCount(chunkCount);
//					fisForChunkCount.close();
				} else {
					sendUploadFailedNotice(false);
					showToastMsg("File not available");
					Log.d(LOG_TAG, "File not available");
					return;
				}

			if (fis.available() > 0) {
//				uploadDao = helper.getDao(FileUploadInfo.class);
				int chunkNo = 0;
				while ((dataSizeRead = fis.read(buffer)) > 0) {
					chunkbaos.write(buffer, 0, dataSizeRead);
					chunkbaos.flush();
					totalbyteswritten += dataSizeRead;
					Log.d(LOG_TAG, "5#################################The chunk number is " + chunkNo);


					ChunkInfo chunkInfo = new ChunkInfo();
					chunkInfo.setFileName(fileName);
					chunkInfo.setNumber(chunkNo);
					chunkInfo.setOffset(totalbyteswritten - buffer.length);
					chunkInfo.setSize(dataSizeRead);


					if(helper.getChunkInfoMap().get(fileToken) != null) {
						helper.getChunkInfoMap().get(fileToken).put(chunkNo, chunkInfo);
					}else{
						HashMap<Integer, ChunkInfo> hMap = new HashMap<>(1);
						hMap.put(chunkNo, chunkInfo);
						helper.getChunkInfoMap().put(fileToken,hMap);
					}
					Thread.sleep(10);
					System.gc();
                    upLoadProgressState(0,true);
					executor.execute(new UploadExecutor(context, fileName, fileToken, accessToken, userOrTeamId, chunkbaos.toByteArray(), chunkNo, this, host,isAnonymousUser, isWebHook, botId));
					chunkNo++;
					chunkbaos.reset(); /*chunk size doubles in next iteration*/
				}

			}

			if (chunkbaos != null)
				chunkbaos.close();
			if (fis != null)
				fis.close();
		} catch (Exception e) {
			e.printStackTrace();
			showToastMsg("Unable to attach file");
		}
	}

	private void showToastMsg(final String msg){
		Handler mainHandler = new Handler(context.getMainLooper());

		Runnable myRunnable = new Runnable() {
			@Override
			public void run() {
				Toast toastView = Toast.makeText(context, msg, Toast.LENGTH_LONG);
				toastView.setGravity(Gravity.CENTER,0,0);
				toastView.show();

			}
		};
		mainHandler.post(myRunnable);
	}

	private synchronized void startUploadFailedChunks(ArrayList<String> failedChunkList) {
		ByteArrayOutputStream chunkbaos = new ByteArrayOutputStream();

		int dataSizeRead = 0;
		int totalBytesWritten = 0;
		byte[] buffer = new byte[BUFFER_SIZE];

		try {
			InputStream fis;
				fis = new FileInputStream(outFilePath);
                if (fis.available() > 0) {
                    int chunkNo = 0;
                    while ((dataSizeRead = fis.read(buffer)) > 0) {
                        chunkbaos.write(buffer, 0, dataSizeRead);
                        chunkbaos.flush();
                        totalBytesWritten += dataSizeRead;
                        Log.d(LOG_TAG, "6########################The chunk number is " + chunkNo);

                        Thread.sleep(10);
                        System.gc();

                        if(failedChunkList.contains(chunkNo+"")) {
                            executor.execute(new UploadExecutor(context, fileName, fileToken, accessToken, userOrTeamId, chunkbaos.toByteArray(), chunkNo, this, host,isAnonymousUser, isWebHook, botId));
                        }
                        chunkNo++;
                        chunkbaos.reset(); /*chunk size doubles in next iteration*/
                    }
                }

//			}
			if (chunkbaos != null)
				chunkbaos.close();
			if (fis != null)
				fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public synchronized void notifyChunkUploadCompleted(String chunkNo, String fileName) {
//		uploadCount.addAndGet(1);
		try {
//			uploadDao.refresh(uploadInfo);
			uploadInfo.setUploadCount(uploadInfo.getUploadCount()+1);
            upLoadProgressState(uploadInfo.getUploadCount() * 100/uploadInfo.getTotalChunks(),true);
			ChunkInfo chInfo = helper.getChunkInfoMap().get(fileToken).get(Integer.parseInt(chunkNo));
			chInfo.setUploaded(true);
			helper.getChunkInfoMap().get(fileToken).put(Integer.parseInt(chunkNo),chInfo);
			helper.getFileUploadInfoMap().put(fileToken,uploadInfo);
//			uploadDao.update(uploadInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
//		if(isRecorded ){
			if(uploadInfo.getTotalChunks() == uploadInfo.getUploadCount()){

				Log.d(LOG_TAG, "Sending merge request"+fileName +"as no failed chunks in recorded");
				try {
					sendMergeSignal(uploadInfo.getTotalChunks(), fileContext);
				} catch (IOException e) {
					e.printStackTrace();
				}
//			}
			}/*else if(!isRecorded && uploadInfo.getTotalChunks() == uploadInfo.getUploadCount()){
				KoreLogger.debugLog(LOG_TAG, "Sending merge request"+fileName +"as no failed chunks");
				sendMergeSignal(chunkCount, fileContext);
			}*/
		}

	/*@Override
	public synchronized void chunkUploadFailed(String chunkNo, String fileName, byte[] dataToPost, String fileToken) {
		executor.execute(new UploadExecutor(context, fileName,fileToken,accessToken,userOrTeamId,dataToPost, Integer.parseInt(chunkNo), this,isTeam));
	}*/

	@Override
	public void initiateFileUpload(FileUploadedListener listener) {
		this.listener = listener;
		new FileTokenManager(host,this, accessToken.replace("bearer", "").trim(),context, userOrTeamId,isAnonymousUser, isWebHook, botId);
	}

	void setChunkCount(int n){
		if(uploadInfo != null){
			uploadInfo.setTotalChunks(n);
			try {
//				fileDao.update(uploadInfo);
				helper.getFileUploadInfoMap().put(fileToken,uploadInfo);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	private void sendMergeSignal(final int dataCount, String fileContext) throws IOException {
			Log.d(LOG_TAG,"(((( re attempt count "+noOfMergeAttempts);
			if(noOfMergeAttempts > Constants.MERGE_RE_ATTEMPT_COUNT){
				sendUploadFailedNotice(true);
				return;
			}
			noOfMergeAttempts++;
			String fileID = "";
			HttpsURLConnection httpsURLConnection = null;
			try {
				KoreHttpsUrlConnectionBuilder koreHttpsUrlConnectionBuilder;
				if(isAnonymousUser)
				{
					if(!isWebHook)
						koreHttpsUrlConnectionBuilder = new KoreHttpsUrlConnectionBuilder(context, host + String.format(FileUploadEndPoints.ANONYMOUS_MERGE, userOrTeamId,fileToken));
					else
						koreHttpsUrlConnectionBuilder = new KoreHttpsUrlConnectionBuilder(context, host + String.format(FileUploadEndPoints.WEBHOOK_ANONYMOUS_MERGE, botId, fileToken));
				}
				else
				{
					if(!isWebHook)
						koreHttpsUrlConnectionBuilder = new KoreHttpsUrlConnectionBuilder(context,host + String.format(FileUploadEndPoints.MERGE_FILE, userOrTeamId,fileToken));
					else
						koreHttpsUrlConnectionBuilder = new KoreHttpsUrlConnectionBuilder(context, host + String.format(FileUploadEndPoints.WEBHOOK_ANONYMOUS_MERGE, botId, fileToken));
				}

//				koreHttpsUrlConnectionBuilder.pinKoreCertificateToConnection();
				httpsURLConnection = koreHttpsUrlConnectionBuilder.getHttpsURLConnection();
				httpsURLConnection.setConnectTimeout(Constants.CONNECTION_TIMEOUT);
				httpsURLConnection.setUseCaches(false);
				httpsURLConnection.setDoOutput(true);
				httpsURLConnection.setDoInput(true);
				httpsURLConnection.setReadTimeout(Constants.CONNECTION_READ_TIMEOUT);
				
				MultipartEntity reqEntity = new MultipartEntity();
				
//				thumbnailFilePath = thumbnailFilePath;
				if(thumbnailFilePath != null && !thumbnailFilePath.equalsIgnoreCase("") && fileContext.equalsIgnoreCase("knowledge")){

					InputStream fis = null;
					try {

                            fis = new FileInputStream(thumbnailFilePath);
					}catch(FileNotFoundException e){
						e.printStackTrace();
					}
					ByteArrayOutputStream thumbBaos = null;


					if (fis != null && (fis instanceof CipherInputStream || fis.available() > 0)) {
						thumbBaos=new ByteArrayOutputStream();
						reqEntity.addPart("thumbnailUpload", new StringBody("true"));
						int startInd = thumbnailFilePath.lastIndexOf(File.separator) + 1;
						int endInd = thumbnailFilePath.indexOf(".",startInd);
						String thfileName = thumbnailFilePath.substring(startInd,endInd);
						
						int dataRead=0;
						byte[] buff=new byte[2*1024];
						/*file size is less than BUFFER_SIZE..just send the file*/
						while((dataRead=fis.read(buff))!=-1){
							thumbBaos.write(buff,0,dataRead);
						}
						
						
						
						reqEntity.addPart("thumbnail", new ByteArrayBody(thumbBaos.toByteArray(),"image/png",thfileName));
						reqEntity.addPart("thumbnailExtension",new StringBody("png"));
						Log.d(LOG_TAG, "PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP     Thumbnail uploaded");
						
					}else{
						reqEntity.addPart("thumbnailUpload", new StringBody("false"));
					}
					if(fis != null)fis.close();
					
				}else{
					reqEntity.addPart("thumbnailUpload", new StringBody("false"));

				Log.d(LOG_TAG, "PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP   No Thumbnail ");
			}

		Log.d(LOG_TAG,"M#####The chunk number is "+ dataCount);

		reqEntity.addPart("totalChunks", new StringBody(dataCount + ""));
		reqEntity.addPart("fileExtension", new StringBody(fileExtn));
		reqEntity.addPart("fileToken", new StringBody(fileToken));
		if(!StringUtils.isNullOrEmpty(fileName))
			reqEntity.addPart("filename",new StringBody(fileName));


		if (fileContext !=null) {
			Log.d(LOG_TAG,"Adding fileContext  !!!");
			reqEntity.addPart("fileContext", new StringBody(fileContext));
		}
		else{
			Log.d(LOG_TAG, "There is no fileContext !!");
		}
		Log.d(LOG_TAG, "***********AccessToken in SendMergeSignal**********" + accessToken);
		httpsURLConnection.setRequestProperty("User-Agent", Constants.getUserAgent());
		httpsURLConnection.setRequestProperty("Cache-Control", "no-cache");
		httpsURLConnection.setRequestProperty("Authorization", accessToken);
		httpsURLConnection.setRequestMethod("PUT");

    			/*if(Cookie != null && !Cookie.equalsIgnoreCase("")){
					httpsURLConnection.setRequestProperty("Cookie", Cookie);
    			}*/

				httpsURLConnection.setRequestProperty(reqEntity.getContentType().getName(),reqEntity.getContentType().getValue());
				DataOutputStream dataOutputStream = new DataOutputStream(httpsURLConnection.getOutputStream());
				reqEntity.writeTo(dataOutputStream);
				dataOutputStream.close();

				BufferedReader input = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));

				String serverResponse = "";
				for( int c = input.read(); c != -1; c = input.read() ) {
					serverResponse = serverResponse + (char)c;
				}
				input.close();
				httpsURLConnection.disconnect();
                upLoadProgressState(100,false);
				Log.d(LOG_TAG, "Got serverResponse for merge " + serverResponse);
				int statusCode = httpsURLConnection.getResponseCode();
				
				Log.d(LOG_TAG, "status code for merge" + statusCode);
				if (statusCode == 200) {
					uploadInfo.setUploaded(true);
//					RecordingStatus.setThumbnailFilePath(null);
//					String serverResponse = EntityUtils.toString(response.getEntity());
					JSONObject jsonObject = new JSONObject(serverResponse);
          			String thumbnailURL = null;
        			if (jsonObject.get("fileId")!=null) {
        				fileID = (String) jsonObject.get("fileId");

        				if(jsonObject.has("thumbnailURL")){
        					thumbnailURL = (String)jsonObject.get("thumbnailURL");
						}
        				if(fileID != null){
    						uploadInfo.setFileId(fileID);
    					}
    					
    					uploadInfo.setMergeTriggered(true);
    					
        				 Message msg = Message.obtain();
    					 Bundle data = new Bundle();
    					 data.putBoolean("success",true);
    					 data.putString(Constants.MESSAGE_ID, messageId);
    					 data.putString("fileExtn", fileExtn);
						 data.putString("filePath", outFilePath);
    					 data.putString("fileId", fileID);
    					 data.putString("fileName", fileName);
						 data.putString("componentType", componentType);
						 data.putString("fileSize", getFileSizeMegaBytes(new File(outFilePath)));
						 data.putString("thumbnailURL",thumbnailURL);
						 data.putString("orientation",orientation);
//						if(isTeam)
//							data.putString(Constants.TEAM_ID,userOrTeamId);
    					 msg.setData(data); //put the data here
    					 try {
    						 messenger.send(msg);
    					 } catch (RemoteException e) {
    						 Log.d("error", "error"+e.toString());
    					 }
        				if(listener !=null)
        					listener.fileUploaded(/*comp, messageId, fileExtn,messenger*/);
        				Log.d(LOG_TAG, "The fileId is " + fileID +" is listener is null "+(listener == null?"True ":"false"));
        			}
					
					//Updating file sent status into DB
					
					// TODO Here insert the file sent status with fileId
					
				} else {

//					String serverResponse = EntityUtils.toString(response.getEntity());
					Log.d(LOG_TAG,"The Resp is "+ serverResponse);
					sendUploadFailedNotice(true);

//					throw new Exception("Response code not 200");
				}
				

				
			} catch (Exception e) {
				if(e instanceof SocketTimeoutException){
					sendUploadFailedNotice(true);
				}else if(httpsURLConnection != null){
					int resCode = -1;
					try{
						resCode = httpsURLConnection.getResponseCode();
					}catch(Exception t){
					}
					Log.d(LOG_TAG, "Hi res code is " + resCode);
					if(resCode == Constants.UPLOAD_ERROR_CODE_404){
						handleErr404(httpsURLConnection.getErrorStream(),fileName);
					}else{
						sendUploadFailedNotice(true);
					}
				}else{
					sendUploadFailedNotice(true);
				}

				e.printStackTrace();
				Log.e(LOG_TAG, "Failed to send the merge initiation message " + e);
			}finally {
//				KoreBaseDao<FileUploadInfo, String> uploadDao = null;
				try {
//					uploadDao = helper.getDao(FileUploadInfo.class);
//					uploadDao.update(uploadInfo);
					helper.getFileUploadInfoMap().put(fileToken,uploadInfo);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
	}
	private  String getFileSizeMegaBytes(File file) {
		if(file.length() < (1024*1024)){
			return df2.format((double) file.length() / (1024)) + "kb";
		}else	return df2.format((double) file.length() / (1024 * 1024)) + "mb";
	}

	private void handleErr404(InputStream errorStream , String fileName) {
		Log.d(LOG_TAG,"Starting upload failed chunks");
		String response="";
		String line;
		BufferedReader br=new BufferedReader(new InputStreamReader(errorStream));
		try {
			while ((line=br.readLine()) != null) {
                response+=line;
            }
			if(!StringUtils.isNullOrEmpty(response)){
				Log.d(LOG_TAG,"Failed chunks are "+response);
				MissingChunks msc = new Gson().fromJson(response,MissingChunks.class);
				if(msc != null){
					UploadError uE = msc.getErrors().get(0);
					ArrayList<String> missingChunks = new ArrayList<String>(Arrays.asList(uE.getMsg().substring(1,uE.getMsg().length()-1).split("\\s*,\\s*")));
					changeUploadInfoForFailedChunks(missingChunks,fileName);
					startUploadFailedChunks(missingChunks);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void changeUploadInfoForFailedChunks(ArrayList<String> chunkNumbers, String fileName){
		try {
//			uploadDao.refresh(uploadInfo);
			uploadInfo.setUploadCount(uploadInfo.getUploadCount() - chunkNumbers.size());
			for(String chunkNo:chunkNumbers){
				ChunkInfo chInfo = helper.getChunkInfoMap().get(fileToken).get(chunkNo);
				chInfo.setUploaded(false);
				helper.getChunkInfoMap().get(fileToken).put(Integer.parseInt(chunkNo),chInfo);
			}
			uploadInfo.setUploaded(false);
			helper.getFileUploadInfoMap().put(fileToken,uploadInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void sendUploadFailedNotice(boolean isFromMergeSignal){
		uploadInfo.setUploaded(false);
		Message msg = Message.obtain();
		Bundle data = new Bundle();
		data.putString(Constants.MESSAGE_ID, messageId);
		data.putString("fileExtn", fileExtn);
		data.putString("fileId", Constants.LOCAL_FILE_ID);
		data.putString("fileName", fileName);
		data.putString("filePath", outFilePath);
		data.putString("componentType", componentType);
        data.putBoolean("success",false);
//		if(isTeam)
//			data.putString(Constants.TEAM_ID,userOrTeamId);
		msg.setData(data); //put the data here

		try {
			messenger.send(msg);
		} catch (RemoteException e) {
			Log.d("error", "error");
		}
		if(listener !=null)
			listener.fileUploaded(/*comp, messageId, fileExtn,messenger*/);

        upLoadProgressState(100,false);
		uploadInfo.setMergeTriggered(isFromMergeSignal);
	}
}
