package kore.botssdk.fileupload.core;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.Gravity;
import android.widget.Toast;

import com.google.gson.Gson;

import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Objects;
import java.util.concurrent.ExecutorService;

import javax.net.ssl.HttpsURLConnection;

import kore.botssdk.fileupload.configurations.Constants;
import kore.botssdk.fileupload.configurations.FileUploadEndPoints;
import kore.botssdk.fileupload.listeners.ChunkUploadListener;
import kore.botssdk.fileupload.listeners.FileTokenListener;
import kore.botssdk.fileupload.listeners.FileUploadedListener;
import kore.botssdk.fileupload.managers.BotDBManager;
import kore.botssdk.fileupload.managers.FileTokenManager;
import kore.botssdk.fileupload.models.ChunkInfo;
import kore.botssdk.fileupload.models.FileUploadInfo;
import kore.botssdk.fileupload.models.MissingChunks;
import kore.botssdk.fileupload.models.UploadError;
import kore.botssdk.fileupload.services.KoreFileUploadServiceExecutor;
import kore.botssdk.fileupload.ssl.KoreHttpsUrlConnectionBuilder;
import kore.botssdk.fileupload.utils.StringUtils;
import kore.botssdk.utils.LogUtils;


public class UploadBulkFile implements Work, FileTokenListener, ChunkUploadListener {
    private static final DecimalFormat df2 = new DecimalFormat("###.##");
	public static final String error_msz_key="error_msz_key";
	public static final String isFileSizeMore_key="isFileSizeMore";
	public  static final String fileSizeBytes_key="fileSizeBytes";
	private final String LOG_TAG = getClass().getSimpleName();
	private final String fileName;
	private String outFilePath=null;
	private String accessToken =null;
//	private String userId = null;
	private String userOrTeamId = null;
	private String fileContext=null;
	private String fileToken = null;
	private String fileExtn = null;
	private String orientation = null;
	private final int BUFFER_SIZE;
	private final Messenger messenger;

//	private int chunkCount = 0;
	private final String thumbnailFilePath;
	private final String messageId;
//	private boolean isTeam;
	private final Context context;
	private FileUploadedListener listener;
	private final String componentType;
	private int noOfMergeAttempts;
	private final String host;

	final BotDBManager helper;
//	KoreBaseDao<FileUploadInfo, String> fileDao;
final FileUploadInfo uploadInfo = new FileUploadInfo();
    public static final long FILE_SIZE_20MB = 20 * 1024*1024;
//	KoreBaseDao<FileUploadInfo, String> uploadDao;


	final ExecutorService executor = KoreFileUploadServiceExecutor.getInstance().getExecutor();
    private ProgressDialog pDialog;
	private final boolean isAnonymousUser;
	private final boolean isWebHook;
	private final String botId;
	
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
		LogUtils.d("progress",progress+" done");
        mainHandler.post(myRunnable);

    }


    @Override
	public synchronized void fileTokenRecievedSuccessfully(Hashtable<String, String> hsh) throws IOException {
		fileToken = hsh.get("fileToken");
		startUpload();
	}

	@Override
	public void fileTokenRecievedWithFailure(String errCode, String reason) {
		LogUtils.d(LOG_TAG,"file upload failed because of getting file token and the reason is "+ reason);
		sendUploadFailedNotice(false);
	}

	private synchronized void startUpload() throws IOException {
		noOfMergeAttempts = 1;
		ByteArrayOutputStream chunkbaos = new ByteArrayOutputStream();

		int dataSizeRead = 0;
//		int dataSizeReadForChunkCount = 0;

		int totalbyteswritten = 0;
		byte[] buffer = new byte[BUFFER_SIZE];
		FileInputStream fis = null;

		try {
			fis =  new FileInputStream(outFilePath);
			if(fis.getChannel().size() > FILE_SIZE_20MB){
				sendUploadFailedNotice(false);
				showToastMsg("File size can't be more than 20 MB!");
				LogUtils.d(LOG_TAG, "File size can't be more than 20 mb");
				return;
			}
				if (fis.available() > 0) {
					int chunkCount = (int)fis.getChannel().size()/BUFFER_SIZE;
					if((int)fis.getChannel().size()%BUFFER_SIZE > 0){
						chunkCount++;
					}
					setChunkCount(chunkCount);
				} else {
					sendUploadFailedNotice(false);
					showToastMsg("File not available");
					LogUtils.d(LOG_TAG, "File not available");
					return;
				}

			if (fis.available() > 0) {
				int chunkNo = 0;
				while ((dataSizeRead = fis.read(buffer)) > 0) {
					chunkbaos.write(buffer, 0, dataSizeRead);
					chunkbaos.flush();
					totalbyteswritten += dataSizeRead;
					LogUtils.d(LOG_TAG, "5#################################The chunk number is " + chunkNo);

					ChunkInfo chunkInfo = new ChunkInfo();
					chunkInfo.setFileName(fileName);
					chunkInfo.setNumber(chunkNo);
					chunkInfo.setOffset(totalbyteswritten - buffer.length);
					chunkInfo.setSize(dataSizeRead);

					if(helper.getChunkInfoMap().get(fileToken) != null) {
						Objects.requireNonNull(helper.getChunkInfoMap().get(fileToken)).put(chunkNo, chunkInfo);
					}else{
						HashMap<Integer, ChunkInfo> hMap = new HashMap<>(1);
						hMap.put(chunkNo, chunkInfo);
						helper.getChunkInfoMap().put(fileToken,hMap);
					}

					upLoadProgressState(0,true);
					executor.execute(new UploadExecutor(context, fileName, fileToken, accessToken, userOrTeamId, chunkbaos.toByteArray(), chunkNo, this, host,isAnonymousUser, isWebHook, botId));
					chunkNo++;
					chunkbaos.reset(); /*chunk size doubles in next iteration*/
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			showToastMsg("Unable to attach file");
		}
		finally {
			if (fis != null)
				fis.close();

			chunkbaos.close();
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

	private synchronized void startUploadFailedChunks(ArrayList<String> failedChunkList) throws IOException {
		ByteArrayOutputStream chunkbaos = new ByteArrayOutputStream();

		int dataSizeRead = 0;
		byte[] buffer = new byte[BUFFER_SIZE];
		InputStream fis = null;

		try {
				fis = new FileInputStream(outFilePath);
                if (fis.available() > 0) {
                    int chunkNo = 0;
                    while ((dataSizeRead = fis.read(buffer)) > 0)
					{
						chunkbaos.write(buffer, 0, dataSizeRead);
						chunkbaos.flush();
						LogUtils.d(LOG_TAG, "6########################The chunk number is " + chunkNo);

                        if(failedChunkList.contains(chunkNo+"")) {
                            executor.execute(new UploadExecutor(context, fileName, fileToken, accessToken, userOrTeamId, chunkbaos.toByteArray(), chunkNo, this, host,isAnonymousUser, isWebHook, botId));
                        }
                        chunkNo++;
                        chunkbaos.reset(); /*chunk size doubles in next iteration*/
                    }
                }
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {

			if (fis != null)
				fis.close();

			chunkbaos.close();
		}
	}

	@Override
	public synchronized void notifyChunkUploadCompleted(String chunkNo, String fileName) {
		try {
			uploadInfo.setUploadCount(uploadInfo.getUploadCount()+1);
            upLoadProgressState(uploadInfo.getUploadCount() * 100/uploadInfo.getTotalChunks(),true);
			ChunkInfo chInfo = Objects.requireNonNull(helper.getChunkInfoMap().get(fileToken)).get(Integer.parseInt(chunkNo));
			assert chInfo != null;
			chInfo.setUploaded(true);
			Objects.requireNonNull(helper.getChunkInfoMap().get(fileToken)).put(Integer.parseInt(chunkNo),chInfo);
			helper.getFileUploadInfoMap().put(fileToken,uploadInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if(uploadInfo.getTotalChunks() == uploadInfo.getUploadCount()){

			LogUtils.d(LOG_TAG, "Sending merge request"+fileName +"as no failed chunks in recorded");
			try {
				sendMergeSignal(uploadInfo.getTotalChunks(), fileContext);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

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
		LogUtils.d(LOG_TAG,"(((( re attempt count "+noOfMergeAttempts);
		if(noOfMergeAttempts > Constants.MERGE_RE_ATTEMPT_COUNT){
			sendUploadFailedNotice(true);
			return;
		}
		noOfMergeAttempts++;
		String fileID = "";
		HttpsURLConnection httpsURLConnection = null;
		InputStream fis = null;
		ByteArrayOutputStream thumbBaos = null;
		BufferedReader input = null;
		InputStreamReader inputStreamReader = null;
		OutputStream outputStream = null;
		DataOutputStream dataOutputStream = null;
		InputStream inputStream = null;

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

			httpsURLConnection = koreHttpsUrlConnectionBuilder.getHttpsURLConnection();
			httpsURLConnection.setConnectTimeout(Constants.CONNECTION_TIMEOUT);
			httpsURLConnection.setUseCaches(false);
			httpsURLConnection.setDoOutput(true);
			httpsURLConnection.setDoInput(true);
			httpsURLConnection.setReadTimeout(Constants.CONNECTION_READ_TIMEOUT);

			MultipartEntity reqEntity = new MultipartEntity();

			if(thumbnailFilePath != null && !thumbnailFilePath.equalsIgnoreCase("") && fileContext.equalsIgnoreCase("knowledge")){

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
					fis = Files.newInputStream(Paths.get(thumbnailFilePath));
				}
				else
					fis = new FileInputStream(thumbnailFilePath);

				if (fis.available() > 0)
				{
					thumbBaos=new ByteArrayOutputStream();
					reqEntity.addPart("thumbnailUpload", new StringBody("true", ContentType.TEXT_PLAIN));
					int startInd = thumbnailFilePath.lastIndexOf(File.separator) + 1;
					int endInd = thumbnailFilePath.indexOf(".",startInd);
					String thfileName = thumbnailFilePath.substring(startInd,endInd);

					int dataRead=0;
					byte[] buff=new byte[2*1024];
					/*file size is less than BUFFER_SIZE..just send the file*/
					while((dataRead=fis.read(buff))!=-1){
						thumbBaos.write(buff,0,dataRead);
					}

					reqEntity.addPart("thumbnail", new ByteArrayBody(thumbBaos.toByteArray(),ContentType.getByMimeType("image/png"),thfileName));
					reqEntity.addPart("thumbnailExtension",new StringBody("png", ContentType.TEXT_PLAIN));
					LogUtils.d(LOG_TAG, "PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP     Thumbnail uploaded");

				}else{
					reqEntity.addPart("thumbnailUpload", new StringBody("false", ContentType.TEXT_PLAIN));
				}
			}else{
				reqEntity.addPart("thumbnailUpload", new StringBody("false", ContentType.TEXT_PLAIN));

				LogUtils.d(LOG_TAG, "PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP   No Thumbnail ");
		}

		LogUtils.d(LOG_TAG,"M#####The chunk number is "+ dataCount);

		reqEntity.addPart("totalChunks", new StringBody(dataCount + "", ContentType.TEXT_PLAIN));
		reqEntity.addPart("fileExtension", new StringBody(fileExtn, ContentType.TEXT_PLAIN));
		reqEntity.addPart("fileToken", new StringBody(fileToken, ContentType.TEXT_PLAIN));
		if(!StringUtils.isNullOrEmpty(fileName))
			reqEntity.addPart("filename",new StringBody(fileName, ContentType.TEXT_PLAIN));


		if (fileContext !=null) {
			LogUtils.d(LOG_TAG,"Adding fileContext  !!!");
			reqEntity.addPart("fileContext", new StringBody(fileContext, ContentType.TEXT_PLAIN));
		}
		else{
			LogUtils.d(LOG_TAG, "There is no fileContext !!");
		}
			LogUtils.d(LOG_TAG, "***********AccessToken in SendMergeSignal**********" + accessToken);

		httpsURLConnection.setRequestProperty("User-Agent", Constants.getUserAgent());
		httpsURLConnection.setRequestProperty("Cache-Control", "no-cache");
		httpsURLConnection.setRequestProperty("Authorization", accessToken);
		httpsURLConnection.setRequestMethod("PUT");

		httpsURLConnection.setRequestProperty(reqEntity.getContentType().getName(),reqEntity.getContentType().getValue());
		outputStream = httpsURLConnection.getOutputStream();
		dataOutputStream = new DataOutputStream(outputStream);
		reqEntity.writeTo(dataOutputStream);

		inputStream = httpsURLConnection.getInputStream();
		inputStreamReader = new InputStreamReader(inputStream);
		input = new BufferedReader(inputStreamReader);

		StringBuilder serverResponse = new StringBuilder();
		for( int c = input.read(); c != -1; c = input.read() ) {
			serverResponse.append((char) c);
		}

		upLoadProgressState(100,false);
		LogUtils.d(LOG_TAG, "Got serverResponse for merge " + serverResponse);
		int statusCode = httpsURLConnection.getResponseCode();

		LogUtils.d(LOG_TAG, "status code for merge" + statusCode);
		if (statusCode == 200) {
			uploadInfo.setUploaded(true);
			JSONObject jsonObject = new JSONObject(serverResponse.toString());
			String thumbnailURL = null;
			jsonObject.get("fileId");
			fileID = (String) jsonObject.get("fileId");

			if(jsonObject.has("thumbnailURL")){
				thumbnailURL = (String)jsonObject.get("thumbnailURL");
			}
			uploadInfo.setFileId(fileID);

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
			msg.setData(data); //put the data here
			messenger.send(msg);
			if(listener !=null)
				listener.fileUploaded(/*comp, messageId, fileExtn,messenger*/);
			LogUtils.d(LOG_TAG, "The fileId is " + fileID +" is listener is null "+(listener == null?"True ":"false"));
		} else {
			LogUtils.d(LOG_TAG,"The Resp is "+ serverResponse);
			sendUploadFailedNotice(true);
		}

		}
		catch (Exception e) {
			if(e instanceof SocketTimeoutException)
			{
				sendUploadFailedNotice(true);
			}
			else if(httpsURLConnection != null)
			{
				int resCode = -1;
				try{
					resCode = httpsURLConnection.getResponseCode();
				}catch(Exception ignored){
				}
				LogUtils.d(LOG_TAG, "Hi res code is " + resCode);
				if(resCode == Constants.UPLOAD_ERROR_CODE_404){
					handleErr404(httpsURLConnection.getErrorStream(),fileName);
				}else{
					sendUploadFailedNotice(true);
				}
			}else{
				sendUploadFailedNotice(true);
			}

			try {
				if(outputStream != null)
					outputStream.close();

				if(inputStream != null)
					inputStream.close();

				if(dataOutputStream != null)
					dataOutputStream.close();

				if(inputStreamReader != null)
					inputStreamReader.close();

				if(httpsURLConnection != null)
					httpsURLConnection.disconnect();

				if(fis != null)
					fis.close();

				if(thumbBaos != null)
					thumbBaos.close();

				if(input != null)
					input.close();

				helper.getFileUploadInfoMap().put(fileToken,uploadInfo);
			} catch (Exception ex) {
				ex.printStackTrace();
			}

			e.printStackTrace();
			LogUtils.e(LOG_TAG, "Failed to send the merge initiation message " + e);
		}
		finally {
			try {
				if(outputStream != null)
					outputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if(inputStream != null)
					inputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if(dataOutputStream != null)
					dataOutputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if(inputStreamReader != null)
					inputStreamReader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if(fis != null)
					fis.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if(thumbBaos != null)
					thumbBaos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if(input != null)
					input.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				if(httpsURLConnection != null)
					httpsURLConnection.disconnect();

			} catch (Exception e) {
				e.printStackTrace();
			}

			helper.getFileUploadInfoMap().put(fileToken,uploadInfo);
		}
	}
	private  String getFileSizeMegaBytes(File file) {
		if(file.length() < (1024*1024)){
			return df2.format((double) file.length() / (1024)) + "kb";
		}else
			return df2.format((double) file.length() / (1024 * 1024)) + "mb";
	}

	private void handleErr404(InputStream errorStream , String fileName) throws IOException {
		LogUtils.d(LOG_TAG,"Starting upload failed chunks");
		StringBuilder response= new StringBuilder();
		String line;
		try (BufferedReader br = new BufferedReader(new InputStreamReader(errorStream))) {
			while ((line = br.readLine()) != null) {
				response.append(line);
			}
			if (!StringUtils.isNullOrEmpty(response.toString())) {
				LogUtils.d(LOG_TAG, "Failed chunks are " + response);
				MissingChunks msc = new Gson().fromJson(response.toString(), MissingChunks.class);
				if (msc != null) {
					UploadError uE = msc.getErrors().get(0);
					ArrayList<String> missingChunks = new ArrayList<String>(Arrays.asList(uE.getMsg().substring(1, uE.getMsg().length() - 1).split("\\s*,\\s*")));
					changeUploadInfoForFailedChunks(missingChunks, fileName);
					startUploadFailedChunks(missingChunks);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void changeUploadInfoForFailedChunks(ArrayList<String> chunkNumbers, String fileName){
		try {
			uploadInfo.setUploadCount(uploadInfo.getUploadCount() - chunkNumbers.size());
			for(String chunkNo:chunkNumbers){
				ChunkInfo chInfo = Objects.requireNonNull(helper.getChunkInfoMap().get(fileToken)).get(chunkNo);
				if(chInfo != null) {
					chInfo.setUploaded(false);
					Objects.requireNonNull(helper.getChunkInfoMap().get(fileToken)).put(Integer.parseInt(chunkNo), chInfo);
				}
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
		msg.setData(data); //put the data here

		try {
			messenger.send(msg);
		} catch (RemoteException e) {
			LogUtils.d("error", "error");
		}

		if(listener !=null) {
			listener.fileUploaded(/*comp, messageId, fileExtn,messenger*/);
		}

		upLoadProgressState(100,false);
		uploadInfo.setMergeTriggered(isFromMergeSignal);
	}
}
