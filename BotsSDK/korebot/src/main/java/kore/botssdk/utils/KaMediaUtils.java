package kore.botssdk.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

import kore.botssdk.exceptions.NoExternalStorageException;
import kore.botssdk.exceptions.NoWriteAccessException;
import kore.botssdk.models.KoreMedia;

/**
 * Created by Shiva Krishna on 4/3/2018.
 */


public class KaMediaUtils {
    public static final String MEDIA_APP_FOLDER = "Kore";
    public static final String DOWNLOADED_IMAGE_FOLDER="Kore Image";
    public static final String DOWNLOADED_AUDIO_FOLDER="Kore Audio";
    public static final String DOWNLOADED_VIDEO_FOLDER="Kore Video";
    public static final String DOWNLOADED_DOCUMENT_FOLDER="Kore Document";
    public static final String DOWNLOAD_ARCHIVE_FOLDER = "Kore Archieve";
    public static final String TEMP_FOLDER = "Kore Temp";
    String DIR_TYPE_TEMP = "temp";
    private static final String LOG_TAG = "MediaUtil";
    static boolean mExternalStorageAvailable = false;
    static boolean mExternalStorageWriteable = false;
    static File mediaStorageDir = null;
    static File mediaStorageDownloadsDir = null;
    private static String userId = null;
    static int PRIVATE_MODE = 0;
    public static class KaEnvironment {
        public static File getExternalStorageDirectory() {
            File f;
                f = Environment.getExternalStorageDirectory();
            return f;
        }

        public static File getExternalStoragePublicDirectory(String path) {

                return Environment.getExternalStoragePublicDirectory(path);
        }
    }

    public static void setupAppDir(String type, String _userId) {
        userId = _userId;
        try {
            if (mExternalStorageAvailable && mExternalStorageWriteable) {
//                KoreLogger.debugLog(LOG_TAG, "Storage available for read write");
                String path = "";
                path = KaEnvironment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
                if (type.equalsIgnoreCase(KoreMedia.MEDIA_TYPE_AUDIO))
                    mediaStorageDir = new File(path, DOWNLOADED_AUDIO_FOLDER);
                else if (type.equalsIgnoreCase(KoreMedia.MEDIA_TYPE_VIDEO))
                    mediaStorageDir = new File(path, DOWNLOADED_VIDEO_FOLDER);
                else if (type.equalsIgnoreCase(KoreMedia.MEDIA_TYPE_IMAGE))
                    mediaStorageDir = new File(path, DOWNLOADED_IMAGE_FOLDER);
                else if (type.equalsIgnoreCase(KoreMedia.MEDIA_TYPE_ARCHIVE)) {
                    mediaStorageDir = new File(path, DOWNLOAD_ARCHIVE_FOLDER);
                } else if (type.equalsIgnoreCase(KoreMedia.MEDIA_TYPE_DOCUMENT)) {
                    mediaStorageDir = new File(path, DOWNLOADED_DOCUMENT_FOLDER);
                }

                // Create the storage directory if it does not exist
                if (!mediaStorageDir.exists()) {
                    mediaStorageDir.mkdirs();
                }
            }
        } catch (Exception e) {
//            KoreLogger.errorLog("Kore", "Failed to create app directory");
        }
    }

    public static File setupDownloadsDir(String type, String userId) {
        try {
            if (mExternalStorageAvailable && mExternalStorageWriteable) {
                String path = KaEnvironment.getExternalStorageDirectory() + "/" + MEDIA_APP_FOLDER + "/" + userId;
                if (type.equalsIgnoreCase(KoreMedia.MEDIA_TYPE_AUDIO))
                    mediaStorageDownloadsDir = new File(path, DOWNLOADED_AUDIO_FOLDER);
                else if (type.equalsIgnoreCase(KoreMedia.MEDIA_TYPE_VIDEO))
                    mediaStorageDownloadsDir = new File(path, DOWNLOADED_VIDEO_FOLDER);
                else if (type.equalsIgnoreCase(KoreMedia.MEDIA_TYPE_IMAGE))
                    mediaStorageDownloadsDir = new File(path, DOWNLOADED_IMAGE_FOLDER);
                else if (type.equalsIgnoreCase(KoreMedia.MEDIA_TYPE_ARCHIVE)) {
                    mediaStorageDownloadsDir = new File(path, DOWNLOAD_ARCHIVE_FOLDER);
                } else if (type.equalsIgnoreCase(KoreMedia.MEDIA_TYPE_DOCUMENT)) {
                    mediaStorageDownloadsDir = new File(path, DOWNLOADED_DOCUMENT_FOLDER);
                }
//                else if (type.equalsIgnoreCase(KoreMedia.DIR_TYPE_TEMP)) {
//                    mediaStorageDownloadsDir = new File(path, TEMP_FOLDER);
//                }
                // Create the storage directory if it does not exist
                if (!mediaStorageDownloadsDir.exists()) {
                    mediaStorageDownloadsDir.mkdirs();
                    if (!mediaStorageDownloadsDir.mkdirs()) {
//                        KoreLogger.debugLog(LOG_TAG, "setupDownloadsDir(type, userId)... failed to create Kore.ai App downloads directory");
                    }
                }
            }
        } catch (Exception e) {
//            KoreLogger.errorLog(LOG_TAG, "setupDownloadsDir(type, userId) -- Failed to create app directory");
        }
//        KoreLogger.debugLog(LOG_TAG, "setupDownloadsDir(type, userId)... setup downloads directory is" + mediaStorageDownloadsDir);
        return mediaStorageDownloadsDir;
    }

    public static void updateExternalStorageState() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            mExternalStorageAvailable = mExternalStorageWriteable = true;
//            KoreLogger.debugLog(LOG_TAG, "updateExternalStorageState() -- SDcard Mounted !!!");
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }
    }

    /**
     * Create a File for saving an image or video
     */
    public static File getOutputMediaFile(String type,String fileName) throws NoExternalStorageException, NoWriteAccessException {
        // Create a media file name
        if (fileName != null && fileName.indexOf(".") > 0)
            fileName = fileName.substring(0, fileName.lastIndexOf("."));
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(new Date()),
                appDirPath = getAppDir();
        File mediaFile = null;
        int attemptCount = 0;
        while(mediaFile == null || mediaFile.exists()) {
            String name = StringUtils.isNullOrEmptyWithTrim(fileName) ? timeStamp : fileName;
            if(attemptCount !=0){
                name = name+attemptCount;
            }
            attemptCount++;
            if (type.equalsIgnoreCase(KoreMedia.MEDIA_TYPE_AUDIO)) {
                mediaFile = new File(appDirPath + File.separator +  name + getMediaExtension(KoreMedia.MEDIA_TYPE_AUDIO, false));
            } else if (type.equalsIgnoreCase(KoreMedia.MEDIA_TYPE_VIDEO)) {
                mediaFile = new File(appDirPath + File.separator +  name + getMediaExtension(KoreMedia.MEDIA_TYPE_VIDEO, false));
            } else if (type.equalsIgnoreCase(KoreMedia.MEDIA_TYPE_IMAGE)) {
                mediaFile = new File(appDirPath + File.separator + name + getMediaExtension(KoreMedia.MEDIA_TYPE_IMAGE, false));
            } else if (type.equalsIgnoreCase(KoreMedia.MEDIA_TYPE_ARCHIVE)) {
                mediaFile = new File(appDirPath + File.separator +  name + ".kore");    //".kore"
            } else {
                mediaFile = new File(appDirPath + File.separator + name + "." + type);    //".kore"
                //return null;
            }
        }
        return mediaFile;
    }

    public static String getLocalPath(String fileName) throws NoExternalStorageException, NoWriteAccessException {
        return getAppDir() + File.separator + fileName;
    }

    public static String getAppDir() throws NoExternalStorageException, NoWriteAccessException {
        if (!mExternalStorageWriteable) {
            throw new NoWriteAccessException();
        }

        if (!mExternalStorageAvailable) {
            throw new NoExternalStorageException();
        }

        return mediaStorageDir.getPath();
    }

    public static String saveFileToKoreWithStream(Context mContext,Uri uri,String fileName,String extn){
        InputStream inputStream = null;
        OutputStream out = null;
        try{
            ContentResolver contentResolver = mContext.getContentResolver();
            File file =  KaMediaUtils.getOutputMediaFile(BitmapUtils.obtainMediaTypeOfExtn(extn),fileName);
            inputStream = contentResolver.openInputStream(uri);
            out = new FileOutputStream(file);

            int read = 0;
            byte[] bytes = new byte[1024];

            if(inputStream != null) {
                while ((read = inputStream.read(bytes)) != -1) {
                    out.write(bytes, 0, read);
                }
            }
            return file.getAbsolutePath();
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            try {
                //Closing output stream
                if (out != null) out.close();
                //Closing input stream
                if (inputStream != null) inputStream.close();
            }
            catch (Exception e){e.printStackTrace();}
        }
        return null;
    }

    public static void saveFileToKorePath(String sourceFilePath, String destinationFilePath) {

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        FileInputStream fis = null;

        if (sourceFilePath == null)
            return;

        try {
            fis = new FileInputStream(sourceFilePath);
            bis = new BufferedInputStream(fis);
            fos = new FileOutputStream(destinationFilePath, false);
            bos = new BufferedOutputStream(fos);
            byte[] buf = new byte[1024];
            do {
                bos.write(buf);
            } while (bis.read(buf) != -1);
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bos != null) bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fos != null) fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (bis != null) bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fis != null) fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveFileFromUrlToKorePath(Context context, String sourceFilePath)
    {
        new DownloadFileFromURL(context).execute(sourceFilePath);
    }

    public static String getMediaExtension(String MEDIA_TYPE, boolean isPlain) {
        String audio_extn = null;
        String video_extn = null;

        audio_extn = ".m4a";
        video_extn = ".mp4";
        if (MEDIA_TYPE.equalsIgnoreCase(KoreMedia.MEDIA_TYPE_VIDEO)) {
            return video_extn;
        } else if (MEDIA_TYPE.equalsIgnoreCase(KoreMedia.MEDIA_TYPE_IMAGE)) {
            return ".jpg";
        } else {
            return audio_extn;
        }
    }

    public static String getRealPath(final Context context, final Uri uri) {
        // DocumentProvider
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return KaEnvironment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);

                if (!TextUtils.isEmpty(id)) {
                    if (id.startsWith("raw:")) {
                        return id.replaceFirst("raw:", "");
                    }

                    String[] contentUriPrefixesToTry = new String[]{
                            "content://downloads/public_downloads",
                            "content://downloads/my_downloads",
                            "content://downloads/all_downloads"
                    };

                    for (String contentUriPrefix : contentUriPrefixesToTry) {
                        Uri contentUri = ContentUris.withAppendedId(Uri.parse(contentUriPrefix), Long.parseLong(id));
                        try {
                            String path = getDataColumn(context, contentUri, null, null);
                            if (path != null) {
                                return path;
                            }
                        } catch (Exception e) {e.printStackTrace();}
                    }
                }
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            if (isDeviceContactsUri(uri))
                return uri.getLastPathSegment();


            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }



    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is GooglePhotos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Contacts.
     */
    public static boolean isDeviceContactsUri(Uri uri) {
        return "com.android.contacts".equals(uri.getAuthority());
    }

    public static boolean fileAvailable(String fileName, String type, String userId) {
        boolean isAvailable;
        StringBuilder builder = new StringBuilder();

        updateExternalStorageState();
        File downloadPath = setupDownloadsDir(type, userId);

        builder.append(downloadPath);
        builder.append(File.separator);
        if (fileName.contains(".")) {
            builder.append(fileName);
        } else {
            builder.append(fileName);
            builder.append(getMediaExtension(type, false));
        }

        File downloadedFile = new File(builder.toString());
        isAvailable = (downloadedFile.exists() && downloadedFile.length() > 0);
//        KoreLogger.debugLog(LOG_TAG, "File name : " + fileName + " Type : " + type + " isAvailable :" + isAvailable);

        return isAvailable;
    }

    public static boolean createFolderIfNotExist(String dirPath) {
        boolean flag = true;
        File dir = new File(KaEnvironment.getExternalStorageDirectory(), dirPath);
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                flag = false;
            }
        }

        return flag;
    }

    /**
     * Background Async Task to download file
     * */
    static class DownloadFileFromURL extends AsyncTask<String, String, String> {

        private final Context context;

        public DownloadFileFromURL(Context context)
        {
            this.context = context;
        }

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ToastUtils.showToast(context, "Downloading...");
        }


        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            OutputStream output = null;
            InputStream input = null;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                // getting file length
                int lenghtOfFile = conection.getContentLength();

                // input stream to read file - with 8k buffer
                input = new BufferedInputStream(url.openStream(), 8192);

                // Output stream to write file
                output = new FileOutputStream(KaMediaUtils.getAppDir() + File.separator + StringUtils.getFileNameFromUrl(url.toString()));
                byte[] data = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();
            } catch (Exception e) {
                LogUtils.e("Error: ", e.getMessage());
            }
            finally {
                try {
                    //Closing output Stream
                    if(output != null) output.close();
                    //Closing input stream
                    if(input != null) input.close();
                }
                catch (Exception e){e.printStackTrace();}
            }

            return null;
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
//            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(String file_url) {
            ToastUtils.showToast(context, "Downloading completed");
        }
    }
}