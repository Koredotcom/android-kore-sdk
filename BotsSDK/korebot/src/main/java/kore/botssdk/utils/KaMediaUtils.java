package kore.botssdk.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

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
import kore.botssdk.models.KoreComponentModel;
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
                if(Build.VERSION.SDK_INT  > Build.VERSION_CODES.M){
                    path = KaEnvironment.getExternalStoragePublicDirectory(/*"/" + Constants.MEDIA_APP_FOLDER + */"/" + userId).getPath();
                }else {
                    path = KaEnvironment.getExternalStorageDirectory() + "/" + MEDIA_APP_FOLDER + "/" + userId;

                } //File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), Constants.KORE_APP_FOLDER);
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
                    if (!mediaStorageDir.mkdirs()) {
//                        KoreLogger.debugLog(LOG_TAG, "failed to create Kore.ai App directory");
                    }
                }

                // Create the storage directory if it does not exist
                if (!mediaStorageDir.exists()) {
                    mediaStorageDir.mkdirs();
                    if (!mediaStorageDir.mkdirs()) {
//                        KoreLogger.debugLog(LOG_TAG, "failed to create Kore.ai App directory");
                    }
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

    public static boolean isExternalStorageAvailable() {
        boolean isExternalStorageAvailable;
        boolean isExternalStorageWriteable;
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // We can read and write the media
            isExternalStorageAvailable = isExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // We can only read the media
            isExternalStorageAvailable = true;
            isExternalStorageWriteable = false;
        } else {
            // Something else is wrong. It may be one of many other states, but all we need
            //  to know is we can neither read nor write
            isExternalStorageAvailable = isExternalStorageWriteable = false;
        }

        return isExternalStorageAvailable && isExternalStorageWriteable;

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

    public static File getStoragePath() {
        return mediaStorageDir;
    }

//    public static String getImageFilePath(String fileName) throws NoExternalStorageException, NoWriteAccessException {
//        if (!mExternalStorageAvailable) {
//            throw new NoExternalStorageException();
//        }
//        KoreLogger.debugLog(LOG_TAG, "temp userid for getImageFilePath " + userId);
//        String imagePath = KaEnvironment.getExternalStorageDirectory() + "/" + MEDIA_APP_FOLDER + "/" + userId + "/" + DOWNLOADED_IMAGE_FOLDER + "/" + fileName;
//        KoreLogger.debugLog(LOG_TAG, "image folder file path for thumbnail " + imagePath);
//        return (imagePath != null) ? imagePath : "";
//    }

    public static String getAppDir() throws NoExternalStorageException, NoWriteAccessException {
        if (!mExternalStorageWriteable) {
            throw new NoWriteAccessException();
        }

        if (!mExternalStorageAvailable) {
            throw new NoExternalStorageException();
        }

        return mediaStorageDir.getPath();
    }

    public static int generateId(String stringID) {
        return stringID.hashCode();
    }

    /*public static String getTimestamp(){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
   	 return timeStamp;
   }*/

    public static void copySourceToDestination(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        // Transfer bytes from in to out
        byte[] buf = new byte[1024];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }


    public static String saveFileToKoreWithStream(Context mContext,Uri uri,String fileName,String extn){
        try{
            ContentResolver contentResolver = mContext.getContentResolver();
            File file =  KaMediaUtils.getOutputMediaFile(BitmapUtils.obtainMediaTypeOfExtn(extn),fileName);
            InputStream inputStream = contentResolver.openInputStream(uri);
            OutputStream out = new FileOutputStream(file);

            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }

            inputStream.close();
            out.close();
            Log.d("file create","success scenario"+fileName+extn);
            return file.getAbsolutePath();
        }catch (Exception e){
            Log.d("file create","fail scenario");
            e.printStackTrace();
        }
        return null;
    }

    public static void saveFileToKorePath(String sourceFilePath, String destinationFilePath) {

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        if (sourceFilePath == null)
            return;

        try {
            bis = new BufferedInputStream(new FileInputStream(new File(sourceFilePath)));
            bos = new BufferedOutputStream(new FileOutputStream(destinationFilePath, false));
            byte[] buf = new byte[1024];
            bis.read(buf);
            do {
                bos.write(buf);
            } while (bis.read(buf) != -1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) bis.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveFileFromUrlToKorePath(Context context, String sourceFilePath)
    {
        new DownloadFileFromURL(context).execute(sourceFilePath);
    }

    public static String renameFile(String filePath, String newFileName) {
        File from = new File(filePath);
        if (from.exists()) {
            String newFilePath = filePath.substring(0, filePath.lastIndexOf("/") + 1) + newFileName;
            File to = new File(newFilePath);
            from.renameTo(to);
            return to.getPath();
        }
        return from.getPath();
    }

    public static String getMediaExtension(String MEDIA_TYPE, boolean isPlain) {
        String audio_extn = null;
        String video_extn = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1) {
            if (isPlain) {
                audio_extn = "m4a";
                video_extn = "mp4";
            } else {
                audio_extn = ".m4a";
                video_extn = ".mp4";
            }

        } else {
            if (isPlain) {
                audio_extn = "amr";
                video_extn = "3gp";
            } else {
                audio_extn = ".amr";
                video_extn = ".3gp";
            }
        }
        if (MEDIA_TYPE.equalsIgnoreCase(KoreMedia.MEDIA_TYPE_VIDEO)) {
            return video_extn;
        } else if (MEDIA_TYPE.equalsIgnoreCase(KoreMedia.MEDIA_TYPE_IMAGE)) {
            if (isPlain)
                return "jpg";
            else
                return ".jpg";
        } else {
            return audio_extn;
        }
    }

    public static int getVideoIdFromFilePath(Context context, Uri uri) {

        int photoId = 1;
        final String[] columns = {MediaStore.Video.Media.DATA, MediaStore.Video.Media._ID, MediaStore.Video.Media.DURATION};
        final String orderBy = MediaStore.Video.Media.DATE_TAKEN;

        // TODO This will break if we have no matching item in the MediaStore.
        Cursor cursor = context.getContentResolver().query(uri, columns, null, null, orderBy);
        if (cursor == null)
            cursor = context.getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);
        cursor.moveToLast();

        if (cursor.getCount() > 0) {
            int image_column_index = cursor
                    .getColumnIndex(MediaStore.Video.Media._ID);
            if(image_column_index != - 1) {
                String file = cursor.getString(image_column_index);
                photoId = cursor.getInt(image_column_index);
            }
        }

        cursor.close();
        return photoId;

    }

    public static int getThumbnailIdForGallery(Context context, String path) {

        try {
            Cursor ca = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.MediaColumns._ID}, MediaStore.MediaColumns.DATA + "=?", new String[]{new File(path).getAbsolutePath()}, null);
            if (ca != null && ca.moveToFirst()) {
                int id = ca.getInt(ca.getColumnIndex(MediaStore.MediaColumns._ID));
                ca.close();
                return id;
            }

            ca.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;

    }

    public static String getRealPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return KaEnvironment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
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
                        Uri contentUri = ContentUris.withAppendedId(Uri.parse(contentUriPrefix), Long.valueOf(id));
                        try {
                            String path = getDataColumn(context, contentUri, null, null);
                            if (path != null) {
                                return path;
                            }
                        } catch (Exception e) {}
                    }
                    /*try {
                        final Uri contentUri = ContentUris.withAppendedId(
                                Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                        return getDataColumn(context, contentUri, null, null);
                    } catch (NumberFormatException e) {
                        return null;
                    }*/
                }

                /*final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);*/
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
        // MediaStore (and general)

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

    public static boolean deleteFile(String path) {
        File file = new File(path);
        return file.delete();
    }

    public static void deletePartialDownload(String targetPath) {
        File file = new File(targetPath);
        if (file.delete()) {
//            KoreLogger.debugLog(LOG_TAG, "deletePartialDownload() - deleted uncomplete file");
        }
    }

    public static boolean isFileExist(String fileUri) {
        File localFile = new File(fileUri);
        return localFile.exists() && localFile.length() > 0;
    }

    public static KoreComponentModel createTextComponent(String message) {
        KoreComponentModel comp = new KoreComponentModel();
        comp.setMediaType(KoreMedia.MEDIA_TYPE_NONE);
        comp.setComponentBody(message);
        comp.setComponentDescription("this is text");
        comp.setComponentTitle("text");
        comp.setMediaFileName("text");
        return comp;
    }

    /**
     * Background Async Task to download file
     * */
    static class DownloadFileFromURL extends AsyncTask<String, String, String> {

        private Context context;

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
//            showDialog(progress_bar_type);
            ToastUtils.showToast(context, "Downloading...");
        }


        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                // getting file length
                int lenghtOfFile = conection.getContentLength();

                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                // Output stream to write file
                OutputStream output = new FileOutputStream(KaMediaUtils.getAppDir() + File.separator + StringUtils.getFileNameFromUrl(url.toString()));
                byte data[] = new byte[1024];

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

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
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
            // dismiss the dialog after the file was downloaded
//            dismissDialog(progress_bar_type);
            ToastUtils.showToast(context, "Downloading completed");
            // Displaying downloaded image into image view
            // Reading image path from sdcard
//            String imagePath = Environment.getExternalStorageDirectory().toString() + "/downloadedfile.jpg";
            // setting downloaded into image view
//            my_image.setImageDrawable(Drawable.createFromPath(imagePath));
        }
    }

//    public static String getTargetMediaPath(String userId, String type, String fileName)
//            throws NoExternalStorageException, NoWriteAccessException {
//
//        updateExternalStorageState();
//        setupAppDir(type, userId);
//        String fullFileName = fileName + getMediaExtension(type, false);
//        return getAppDir() + File.separator + fullFileName;
//    }


}