package kore.botssdk.activity;

import static kore.botssdk.utils.BundleConstants.CAPTURE_IMAGE_BUNDLED_PREMISSION_REQUEST;
import static kore.botssdk.utils.BundleConstants.CAPTURE_IMAGE_CHOOSE_FILES_BUNDLED_PREMISSION_REQUEST;
import static kore.botssdk.utils.BundleConstants.CAPTURE_IMAGE_CHOOSE_FILES_RECORD_BUNDLED_PREMISSION_REQUEST;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.OrientationEventListener;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import io.reactivex.annotations.NonNull;
import kore.botssdk.R;
import kore.botssdk.exceptions.NoExternalStorageException;
import kore.botssdk.exceptions.NoWriteAccessException;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.KoreContact;
import kore.botssdk.models.KoreMedia;
import kore.botssdk.utils.BitmapUtils;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.KaMediaUtils;
import kore.botssdk.utils.KaPermissionsHelper;


/**
 * Created by Shiva Krishna on 4/5/2018.
 */


public class KaCaptureImageActivity extends KaAppCompatActivity implements KoreMedia, ActivityResultCallback<ActivityResult> {

    //keep track of camera capture intent
    private final int CAMERA_CAPTURE = 1;
    public static final int THUMBNAIL_WIDTH=320;
    public static final int THUMNAIL_HEIGHT=240;

    public static final String THUMBNAIL_FILE_PATH = "filePathThumbnail";
    // keep track of choose image intent
    private final int CHOOSE_IMAGE = 3;
    private final int CHOOSE_VIDEO = 4;
    private final int CHOOSE_FILE = 5;
    private final int CHOOSE_IMAGE_VIDEO = 6;

    private static Uri cameraImgUri;
    private String imagePickType = null;
    private String fileContext = null;
    private static boolean NORMAL_PORTRAIT;
    private final int compressQualityInt = 100;
    private OrientationEventListener mOrientationEventListener;
    private String MEDIA_TYPE = MEDIA_TYPE_IMAGE;
    private String MEDIA_FILENAME;
    private String MEDIA_FILE_PATH;
    private String MEDIA_EXTENSION;
    private String thumbnailFilePath;
    private Intent resultIntent = null;
    String mCurrentPhotoPath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ka_capture_image);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            imagePickType = extras.getString(BundleConstants.PICK_TYPE);
            Log.d(LOG_TAG, "onCreate() :: extras :: imagePickType ::::" + imagePickType);
            fileContext = extras.getString(BundleConstants.FILE_CONTEXT);
            Log.d(LOG_TAG, "onCreate() :: extras :: fileContext ::::" + fileContext);
            MEDIA_TYPE = extras.containsKey("mediaType") ? extras.getString("mediaType") : MEDIA_TYPE_IMAGE;
        }

        checkForPermissionAccessAndRequest();
    }

    private void checkForPermissionAccessAndRequest() {
        if (CHOOSE_TYPE_CAMERA.equalsIgnoreCase(imagePickType)) {

            if (KaPermissionsHelper.hasPermission(this,Manifest.permission.CAMERA,Manifest.permission.READ_MEDIA_IMAGES)) {
                openImageIntent(imagePickType);
            } else {
                KaPermissionsHelper.requestForPermission(this, CAPTURE_IMAGE_BUNDLED_PREMISSION_REQUEST,
                        Manifest.permission.CAMERA);
            }
        } else if (CHOOSE_TYPE_IMAGE_VIDEO.equalsIgnoreCase(imagePickType) ||
                CHOOSE_TYPE_GALLERY.equalsIgnoreCase(imagePickType) ||
                CHOOSE_TYPE_VIDEO_GALLERY.equalsIgnoreCase((imagePickType))) {
            if (Build.VERSION.SDK_INT >= 33 ? KaPermissionsHelper.hasPermission(this,Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO) :
                    KaPermissionsHelper.hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                openImageIntent(imagePickType);
            } else {
                if(Build.VERSION.SDK_INT >= 33)
                {
                    KaPermissionsHelper.requestForPermission(this, CAPTURE_IMAGE_CHOOSE_FILES_BUNDLED_PREMISSION_REQUEST,
                            Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO);
                }
                else
                {
                    KaPermissionsHelper.requestForPermission(this, CAPTURE_IMAGE_CHOOSE_FILES_BUNDLED_PREMISSION_REQUEST,
                            Manifest.permission.READ_EXTERNAL_STORAGE);
                }
            }
        } else if (CHOOSE_TYPE_FILE.equalsIgnoreCase(imagePickType)) {
            if (Build.VERSION.SDK_INT >= 33 ? KaPermissionsHelper.hasPermission(this,Manifest.permission.READ_MEDIA_IMAGES) :
                    KaPermissionsHelper.hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                openImageIntent(imagePickType);
            } else {
                if(Build.VERSION.SDK_INT >= 33)
                {
                    KaPermissionsHelper.requestForPermission(this, CAPTURE_IMAGE_CHOOSE_FILES_BUNDLED_PREMISSION_REQUEST,
                            Manifest.permission.READ_MEDIA_IMAGES);
                }
                else
                {
                    KaPermissionsHelper.requestForPermission(this, CAPTURE_IMAGE_CHOOSE_FILES_BUNDLED_PREMISSION_REQUEST,
                            Manifest.permission.READ_EXTERNAL_STORAGE);
                }
            }
        } else {
            Log.e(LOG_TAG, "no pickType found. Please assign one and invoke this activity, again.");
        }

    }

    private boolean checkForPermissionAccess() {
        if (CHOOSE_TYPE_CAMERA.equalsIgnoreCase(imagePickType)) {
            return KaPermissionsHelper.hasPermission(this,Manifest.permission.CAMERA);
        } else if (CHOOSE_TYPE_IMAGE_VIDEO.equalsIgnoreCase(imagePickType) ||
                CHOOSE_TYPE_GALLERY.equalsIgnoreCase(imagePickType) ||
                CHOOSE_TYPE_VIDEO_GALLERY.equalsIgnoreCase((imagePickType))) {
            return Build.VERSION.SDK_INT >= 33 ? KaPermissionsHelper.hasPermission(this, Manifest.permission.READ_MEDIA_IMAGES) : KaPermissionsHelper.hasPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE);
        } else if (CHOOSE_TYPE_FILE.equalsIgnoreCase(imagePickType)) {
            return Build.VERSION.SDK_INT >= 33 ? KaPermissionsHelper.hasPermission(this, Manifest.permission.READ_MEDIA_IMAGES) : KaPermissionsHelper.hasPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE);
        } else {
            Log.e(LOG_TAG, "no pickType found. Please assign one and invoke this activity, again.");
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == CAPTURE_IMAGE_BUNDLED_PREMISSION_REQUEST) {
            if (KaPermissionsHelper.hasPermission(this, Manifest.permission.CAMERA)) {
                openImageIntent(imagePickType);
            }
        } else if (requestCode == CAPTURE_IMAGE_CHOOSE_FILES_BUNDLED_PREMISSION_REQUEST) {
            if (Build.VERSION.SDK_INT >= 33 ? KaPermissionsHelper.hasPermission(this,Manifest.permission.READ_MEDIA_IMAGES) :
                    KaPermissionsHelper.hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE))
            {
                openImageIntent(imagePickType);
            }
        } else if (requestCode == CAPTURE_IMAGE_CHOOSE_FILES_RECORD_BUNDLED_PREMISSION_REQUEST) {
            if (Build.VERSION.SDK_INT >= 33 ? KaPermissionsHelper.hasPermission(this,Manifest.permission.READ_MEDIA_IMAGES) :
                    KaPermissionsHelper.hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE))
            {
                openImageIntent(imagePickType);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void openImageIntent(String imagePickType) {
        try {
            getUserinfo();
            KaMediaUtils.updateExternalStorageState();
            KaMediaUtils.setupAppDir(MEDIA_TYPE, "");

            // in case of picture taken from camera capture
            if (CHOOSE_TYPE_CAMERA.equals(imagePickType)) {
                //use standard intent to capture an image
                Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                captureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);

                    captureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    captureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                try {
                    Uri photoURI = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", createImageFile()); captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    cameraImgUri = photoURI;
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //we will handle the returned data in onActivityResult
                startActivityForResult(captureIntent, CAMERA_CAPTURE);
            } else if (CHOOSE_TYPE_IMAGE_VIDEO.equals(imagePickType)) {
                // in case picture is choosen from gallery

                //use standard intent to pick an image from gallery
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/* video/*");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                //we will handle the returned data in onActivityResult
                startActivityForResult(intent, CHOOSE_IMAGE_VIDEO);
            }else if (CHOOSE_TYPE_GALLERY.equals(imagePickType)) {
                // in case picture is choosen from gallery

                //use standard intent to pick an image from gallery
                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                photoPickerIntent.setType("image/*");
                //we will handle the returned data in onActivityResult
                startActivityForResult(photoPickerIntent, CHOOSE_IMAGE);
            } else if(imagePickType.equals(CHOOSE_TYPE_VIDEO_GALLERY)) {
                //use standard intent to pick a video from gallery
                Intent videoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                videoPickerIntent.setType("video/*");
                //we will handle the returned data in onActivityResult
                startActivityForResult(videoPickerIntent, CHOOSE_VIDEO);
            } else if(imagePickType.equals(CHOOSE_TYPE_FILE)) {
                Intent videoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                String[] mime = {"text/plain",
                        "application/pdf", "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                "application/vnd.ms-excel", "application/vnd.ms-excel.sheet.binary.macroenabled.12","application/rtf",
                "application/vnd.ms-excel.sheet.macroenabled.12","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.template","application/vnd.ms-excel.template.macroEnabled.12",
                "application/vnd.ms-excel.addin.macroEnabled.12", "application/vnd.ms-powerpoint","application/vnd.oasis.opendocument.text",
                 "application/vnd.openxmlformats-officedocument.presentationml.presentation","audio/*"};
                videoPickerIntent.setType("text/*");
                videoPickerIntent.putExtra(Intent.EXTRA_MIME_TYPES,mime);
                videoPickerIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(videoPickerIntent, CHOOSE_FILE);
            }

        } catch (ActivityNotFoundException anfe) {
            //display an error message
            String errorMessage = "Your device doesn't support capturing images!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @SuppressLint("SimpleDateFormat")
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (checkForPermissionAccess()) {
                if (requestCode == CAMERA_CAPTURE) {
                    //get the Uri for the captured image
                    if (fileContext.equalsIgnoreCase(FOR_MESSAGE) || fileContext.equalsIgnoreCase(FOR_PROFILE)) {
                        getFullImage();
                        if (resultIntent == null) resultIntent = new Intent();
                        resultIntent.putExtra("action", "IMAGE_DONE");
                        resultIntent.putExtra("fileName", MEDIA_FILENAME);
                        resultIntent.putExtra("filePath", MEDIA_FILE_PATH);
                        resultIntent.putExtra(THUMBNAIL_FILE_PATH, thumbnailFilePath);
                        setResult(resultCode, resultIntent);
                        finish();
                    }

                } else if (requestCode == CHOOSE_IMAGE) {
                    Uri selectedImage = data.getData();
                    if ((fileContext.equalsIgnoreCase(FOR_MESSAGE) || fileContext.equalsIgnoreCase(FOR_PROFILE)) && selectedImage != null) {
                        getImageForGalleryFooter(selectedImage, resultCode);
                    } else if (selectedImage == null) {
                        finishAndCancelOperation();
                    }
                } else if (requestCode == CHOOSE_IMAGE_VIDEO) {
                    Uri selectedImage = data.getData();
                    if (fileContext.equalsIgnoreCase(KoreContact.PROFILE) && selectedImage != null) {
                        FetchImageDataAndCrop fetchImageDataAndCrop = new FetchImageDataAndCrop(selectedImage);
                        fetchImageDataAndCrop.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[]) null);
                    } else if (fileContext.equalsIgnoreCase(FOR_MESSAGE) && selectedImage != null) {
                        getImageForGalleryFooter(selectedImage, resultCode);
                    } else if (selectedImage == null) {
                        finishAndCancelOperation();
                    }
                } else if (requestCode == CHOOSE_VIDEO) {
                    Uri selectedImage = data.getData();
                    if (fileContext.equalsIgnoreCase(FOR_MESSAGE) && selectedImage != null) {

                        File file = null;
                        try {
                            file = KaMediaUtils.getOutputMediaFile(KoreMedia.MEDIA_TYPE_VIDEO, null);
                        } catch (NoExternalStorageException | NoWriteAccessException e) {
                            e.printStackTrace();
                        }
                        if (file != null) MEDIA_FILE_PATH = file.getAbsolutePath();

                        MEDIA_FILENAME = MEDIA_FILE_PATH.substring(MEDIA_FILE_PATH.lastIndexOf("/") + 1);
                        MEDIA_FILENAME = MEDIA_FILENAME.substring(0, MEDIA_FILENAME.lastIndexOf("."));
                        MEDIA_EXTENSION = MEDIA_FILE_PATH.substring(MEDIA_FILE_PATH.lastIndexOf(".") + 1);
                        //display the returned video
                        finishOperation(selectedImage, BitmapUtils.getExtensionFromFileName(MEDIA_FILE_PATH), resultCode);
                    } else if (selectedImage == null) {
                        finishAndCancelOperation();
                    }
                } else if (requestCode == CHOOSE_FILE) {
                    Uri selectedFile = data.getData();
                    String fileExtn;
                    if (selectedFile == null) {
                        Toast.makeText(KaCaptureImageActivity.this, "Could not attach a file there was a problem", Toast.LENGTH_SHORT).show();
                    }
                    String realPath = getFileNameByUri(KaCaptureImageActivity.this, selectedFile);
                    String realFileName = "";
                    if (realPath != null) {
                        try {
                            realFileName = realPath.substring(realPath.lastIndexOf("/") + 1);
                        } catch (Exception e) {
                            Toast.makeText(KaCaptureImageActivity.this, "Could not attach a file there was a problem", Toast.LENGTH_SHORT).show();
                            finishAndCancelOperation();
                            return;
                        }
                        fileExtn = BitmapUtils.getExtensionFromFileName(realFileName);  // will not be null but "" and will return as not whitelisted
                        File file = null;
                        try {
                            file = KaMediaUtils.getOutputMediaFile(BitmapUtils.obtainMediaTypeOfExtn(fileExtn), realFileName);
                        } catch (NoExternalStorageException | NoWriteAccessException e) {
                            e.printStackTrace();
                        }
                        if (file != null) {
                            MEDIA_FILE_PATH = file.getAbsolutePath();
                        }

                        if (!fileExtn.equals(BitmapUtils.EXT_VIDEO))
                            KaMediaUtils.saveFileToKorePath(realPath, MEDIA_FILE_PATH);

                        MEDIA_FILENAME = MEDIA_FILE_PATH.substring(MEDIA_FILE_PATH.lastIndexOf("/") + 1);
                        finishOperation(selectedFile, fileExtn, resultCode);

                    } else {
                        SaveFileTask saveFileTask = new SaveFileTask(selectedFile, resultCode);
                        saveFileTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[]) null);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Access denied. Operation failed !!", Toast.LENGTH_LONG).show();
                    if (!CHOOSE_TYPE_IMAGE_VIDEO.equalsIgnoreCase(imagePickType) &&
                            !CHOOSE_TYPE_GALLERY.equalsIgnoreCase(imagePickType) &&
                            !CHOOSE_TYPE_VIDEO_GALLERY.equalsIgnoreCase((imagePickType)) &&
                            !CHOOSE_TYPE_FILE.equalsIgnoreCase(imagePickType)) {
                                Log.e(LOG_TAG, "no pickType found. Please assign one and invoke this activity, again.");
                            }
                }
            } else {
                finish();
            }

        } else if (resultCode == RESULT_CANCELED) {
            finishAndCancelOperation();
        } else {
            finishAndCancelOperation();
        }
    }

    private String getFileNameByUri(Context context, Uri uri)
    {
        String filepath = "";
        File file;
        if (uri.getScheme().compareTo("content") == 0)
        {
            Cursor cursor = context.getContentResolver().query(uri, new String[] { android.provider.MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.ORIENTATION }, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            cursor.moveToFirst();

            String mImagePath = cursor.getString(column_index);
            cursor.close();
            filepath = mImagePath;

        }
        else
        if (uri.getScheme().compareTo("file") == 0)
        {
            try
            {
                file = new File(new URI(uri.toString()));
                if (file.exists())
                    filepath = file.getAbsolutePath();

            }
            catch (URISyntaxException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            filepath = uri.getPath();
        }
        return filepath;
    }

    @Override
    public void onActivityResult(ActivityResult result) {
        if (result != null && result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
            final int takeFlags = (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            // take persistable Uri Permission for future use
            getContentResolver().takePersistableUriPermission(Objects.requireNonNull(result.getData().getData()), takeFlags);
            SharedPreferences preferences = getSharedPreferences(BotResponse.THEME_NAME, Context.MODE_PRIVATE);
            preferences.edit().putString("filestorageuri", result.getData().getData().toString()).apply();
        } else {
            Log.e("FileUtility", "Some Error Occurred : " + result);
        }
    }

    @SuppressLint("StaticFieldLeak")
    class SaveFileTask extends AsyncTask<Void,Void,String>{
        Uri uri;
        String fileExtn;
        int resultCode;
        public SaveFileTask(Uri uri,int resultCode){
            this.uri = uri;
            this.resultCode = resultCode;

        }

        @Override
        protected void onPreExecute() {
            showProgress("Downloading...",false);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            Cursor returnCursor =
                    getContentResolver().query(uri, null, null, null, null);
            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            returnCursor.moveToFirst();
            MEDIA_FILENAME =  returnCursor.getString(nameIndex);
            fileExtn = BitmapUtils.getExtensionFromFileName(MEDIA_FILENAME);  // will not be null but "" and will return as not whitelisted
            MEDIA_FILE_PATH = KaMediaUtils.saveFileToKoreWithStream(KaCaptureImageActivity.this, uri,MEDIA_FILENAME,fileExtn);
            return MEDIA_FILE_PATH;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dismissProgress();
            if(MEDIA_FILE_PATH != null) {
                MEDIA_FILENAME = MEDIA_FILE_PATH.substring(MEDIA_FILE_PATH.lastIndexOf("/") + 1);
            }
            finishOperation(null,fileExtn,resultCode);
        }
    }

    void finishOperation(Uri uri,String fileExtn,int resultCode){

        resultIntent = new Intent();
        resultIntent.putExtra("action", CHOOSE_TYPE_FILE);
        resultIntent.putExtra("fileName", MEDIA_FILENAME);
        resultIntent.putExtra("filePath", MEDIA_FILE_PATH);
        resultIntent.putExtra("fileUri", uri);
        resultIntent.putExtra("fileExtn", fileExtn);

        setResult(resultCode, resultIntent);
        finish();
    }

    @SuppressLint("StaticFieldLeak")
    class FetchImageDataAndCrop extends AsyncTask<Void, Void, String>{
        private final Uri selectedImage;

        public FetchImageDataAndCrop(Uri selectedImage){
            this.selectedImage = selectedImage;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress("",false);
        }

        @Override
        protected String doInBackground(Void... voids) {
            return getPath(KaCaptureImageActivity.this, selectedImage);
        }

        @Override
        protected void onPostExecute(String imageUrl) {
            super.onPostExecute(imageUrl);

            if(imageUrl != null) {
                imageUrl = !(imageUrl.contains("file://")) ? "file://" + imageUrl : imageUrl;
            }
            else{
                Toast.makeText(KaCaptureImageActivity.this, "image fetch failed", Toast.LENGTH_LONG).show();
                finish();
            }

            dismissProgress();
        }
    }

    private void finishAndCancelOperation() {
        if (resultIntent == null) resultIntent = new Intent();
        resultIntent.putExtra("action", "IMAGE_CANCEL");
        resultIntent.putExtra("fileName", "");
        setResult(RESULT_CANCELED, resultIntent);
        this.finish();
    }

    private void getImageForGalleryFooter(Uri imageUri, int resultCode) {
        OutputStream fOut = null;

        try {
            Bitmap highResBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri); //Todo: OOM encountered over here
            Bitmap bitmapPic = BitmapUtils.getScaledBitmap(highResBitmap);
            Log.d(LOG_TAG, "getImageForGalleryFooter() :: ***** picture height ::" + bitmapPic.getHeight() + " and width::" + bitmapPic.getWidth());
            String fileName = null;
            try{
                Cursor returnCursor =
                        getContentResolver().query(imageUri, null, null, null, null);
                int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                returnCursor.moveToFirst();
                returnCursor.getString(nameIndex);
            }catch (Exception e){
                e.printStackTrace();
            }

            File file = KaMediaUtils.getOutputMediaFile(MEDIA_TYPE,fileName);
            fOut = new FileOutputStream(file);
            bitmapPic.compress(Bitmap.CompressFormat.JPEG, compressQualityInt, fOut);
            fOut.flush();

            MEDIA_FILE_PATH = file.getAbsolutePath();
            Log.d(LOG_TAG, " file absolute path::" + MEDIA_FILE_PATH);

            MEDIA_FILENAME = MEDIA_FILE_PATH.substring(MEDIA_FILE_PATH.lastIndexOf("/") + 1);
            MEDIA_FILENAME = MEDIA_FILENAME.substring(0, MEDIA_FILENAME.lastIndexOf("."));
            MEDIA_EXTENSION = MEDIA_FILE_PATH.substring(MEDIA_FILE_PATH.lastIndexOf(".") + 1);

            bitmapPic = rotateIfNecessary(MEDIA_FILE_PATH, bitmapPic);

            // create thumbnail and return as base64
            createImageThumbnail(MEDIA_FILE_PATH, bitmapPic);
         } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(fOut != null)
                    fOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //display the returned cropped image
        if (resultIntent == null) resultIntent = new Intent();
        resultIntent.putExtra("action", CHOOSE_TYPE_GALLERY);
        resultIntent.putExtra("fileName", MEDIA_FILENAME);
        resultIntent.putExtra("filePath", MEDIA_FILE_PATH);
        resultIntent.putExtra("filePathThumbnail", thumbnailFilePath);
        resultIntent.putExtra("fileExtension", MEDIA_EXTENSION);
        setResult(resultCode, resultIntent);
        finish();
    }


    private void getFullImage() {

        String path = null;
        path = mCurrentPhotoPath;
        File file = new File(path);

        Log.d(LOG_TAG, "real image path  " + path);

        MEDIA_FILE_PATH = file.getAbsolutePath();
        Log.d(LOG_TAG, "getFullImage() :: full image file absolute path::" + MEDIA_FILE_PATH);

        Bitmap thePic = BitmapUtils.decodeBitmapFromFile(file, 800, 600);
        OutputStream fOut = null;

        try {
            // compress the image
            File _file = new File(MEDIA_FILE_PATH);

            Log.d(LOG_TAG, "getFullImage() :: file.exists() ---------------------------------------- " + _file.exists());
            fOut = new FileOutputStream(_file);

            thePic.compress(Bitmap.CompressFormat.JPEG, compressQualityInt, fOut);
            fOut.flush();
            thePic = rotateIfNecessary(file.getAbsolutePath(), thePic);
        } catch (Exception e) {
            Log.e(LOG_TAG, e.toString());
        }
        finally {
            try {
                assert fOut != null;
                fOut.close();
            }
            catch (Exception e){e.printStackTrace();}
        }

        MEDIA_FILENAME = MEDIA_FILE_PATH.substring(MEDIA_FILE_PATH.lastIndexOf("/") + 1);
        MEDIA_FILENAME = MEDIA_FILENAME.substring(0, MEDIA_FILENAME.lastIndexOf("."));
        MEDIA_EXTENSION = MEDIA_FILE_PATH.substring(MEDIA_FILE_PATH.lastIndexOf(".") + 1);

        getFileTokenAndStartUpload();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            createImageThumbnail(MEDIA_FILE_PATH, thePic);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        if (cameraImgUri != null) {
            outState.putParcelable("uri", cameraImgUri);
            Log.d(LOG_TAG, "onSaveInstanceState() :: +++++++On Save Instance " + cameraImgUri.getPath());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        cameraImgUri = savedInstanceState.getParcelable("uri");
    }

    private void createImageThumbnail(String filePath, Bitmap finalMap) {
        Bitmap scaledBitmap = null;
        Bitmap extBitmap = null;
        int originalOrien = 0;
        int degrees = 0;
        int original_H = finalMap.getHeight();
        int original_W = finalMap.getWidth();
        int target_W = 0;
        int target_H = 0;
        Log.d(LOG_TAG, "createImageThumbnail() :: original height" + original_H);
        Log.d(LOG_TAG, "createImageThumbnail() :: original width" + original_W);

        try {
            ExifInterface fullImageExif = new ExifInterface(filePath);
            originalOrien = fullImageExif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

            Log.d(LOG_TAG, "createImageThumbnail() :: Original image orientation" + originalOrien);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!NORMAL_PORTRAIT) {
            target_W = THUMBNAIL_WIDTH;
            target_H = (target_W * original_H) / original_W;
            Log.d(LOG_TAG, "createImageThumbnail() :: calculated thumbnail height for landscape mode" + target_H + "and width is" + target_W);
        } else {
            target_H = THUMNAIL_HEIGHT;
            target_W = (target_H * original_W) / original_H;
            Log.d(LOG_TAG, "createImageThumbnail() :: calculated thumbnail width for portrait mode: width " + target_W + "and height " + target_H);
        }

        if (originalOrien == ExifInterface.ORIENTATION_ROTATE_180) {
            degrees = 180;
            Log.d(LOG_TAG, "createImageThumbnail() :: rotating thumbnail by " + degrees);


        } else if (originalOrien == ExifInterface.ORIENTATION_ROTATE_90) {
            degrees = 90;
            Log.d(LOG_TAG, "createImageThumbnail() :: rotating thumbnail by " + degrees);

        } else if (originalOrien == ExifInterface.ORIENTATION_ROTATE_270) {
            degrees = 270;
            Log.d(LOG_TAG, "createImageThumbnail() :: rotating thumbnail by " + degrees);

        }
        Log.d(LOG_TAG, "createImageThumbnail() :: Thumbnail rotation is " + degrees);

        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);

        scaledBitmap = Bitmap.createScaledBitmap(finalMap, target_W, target_H, true);
        extBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, target_W, target_H, matrix, true);
        FileOutputStream out = null;
        try {

            Log.d(LOG_TAG, "createImageThumbnail() :: target width" + target_W);
            Log.d(LOG_TAG, "createImageThumbnail() :: target height" + target_H);

            int ind = filePath.lastIndexOf(".");
            thumbnailFilePath = filePath.substring(0, ind) + "_th.png";

            Log.d(LOG_TAG, "createImageThumbnail() :: thumbNailPath:::::::::::::" + thumbnailFilePath);

            File thumbNailfile = new File(thumbnailFilePath);//MediaUtil.getOutputMediaFile(AppConstants.MEDIA_TYPE_IMAGE);
            Log.d(LOG_TAG, "createImageThumbnail() :: thumbnailFileName & path::::::::::::::" + thumbNailfile.getAbsolutePath());

            out = new FileOutputStream(thumbNailfile);
            extBitmap.compress(Bitmap.CompressFormat.PNG, compressQualityInt, out);
            out.flush();
            extBitmap = rotateIfNecessary(thumbNailfile.getAbsolutePath(), extBitmap);

        } catch (Exception ee) {
            ee.printStackTrace();
            Log.e(LOG_TAG, "Error while create Image Thumbnail" + ee);
        } finally {
            scaledBitmap.recycle();
            if (extBitmap != null) {
                extBitmap.recycle();
            }
            if(out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Bitmap rotateIfNecessary(String fileName, Bitmap bitmap) {
        ExifInterface exif;
        try {
            exif = new ExifInterface(fileName);
            if (Objects.requireNonNull(exif.getAttribute(ExifInterface.TAG_ORIENTATION)).equalsIgnoreCase("6")) {
                bitmap = rotate(bitmap, 90);
            } else if (Objects.requireNonNull(exif.getAttribute(ExifInterface.TAG_ORIENTATION)).equalsIgnoreCase("8")) {
                bitmap = rotate(bitmap, 270);
            } else if (Objects.requireNonNull(exif.getAttribute(ExifInterface.TAG_ORIENTATION)).equalsIgnoreCase("3")) {
                bitmap = rotate(bitmap, 180);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    public static Bitmap rotate(Bitmap bitmap, int degree) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix mtx = new Matrix();
        mtx.postRotate(degree);

        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mOrientationEventListener = new OrientationEventListener(this) {

            @Override
            public void onOrientationChanged(int orientation) {
                if (orientation >= 315 || orientation < 45) {
                    NORMAL_PORTRAIT = true;
                } else if (orientation >= 225) {
                    NORMAL_PORTRAIT = false;
                } else if (orientation >= 135) {
                    NORMAL_PORTRAIT = true; //inverted
                } else if (orientation > 45) {
                    NORMAL_PORTRAIT = false; //inverted land
                }
            }
        };

        if (mOrientationEventListener.canDetectOrientation()) {
            mOrientationEventListener.enable();
        }
    }

    @Override
    public void startMediaRecording() {

    }

    @Override
    public void stopMediaRecording() {

    }

    @Override
    public void createMediaFilePath() {

    }

    @Override
    public void playOrViewMedia() {

    }

    @Override
    public void pauseOrStopMedia() {

    }

    @Override
    public void resumeMedia() {

    }

    @Override
    public void getUserinfo() {
    }

    @Override
    public void getFileTokenAndStartUpload() {

    }

    @Override
    public void onBackPressed() {
        finishAndCancelOperation();
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @author paulburke
     */
    @SuppressLint("NewApi")
    private String getPath(final Context context, final Uri uri) {
        if (DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return KaMediaUtils.KaEnvironment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id;
                id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.parseLong(id));

                return getDataColumn(context, contentUri, null, null);
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
            // MediaStore (and general) - GoogleDrive fetching comes over here
            else if ("content".equalsIgnoreCase(uri.getScheme())) {
                return fetchRemoteContentAddress(context, uri);
            }
        }
        // MediaStore (and general) - Google Photos and normal gallery pics is fetched over here
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return fetchRemoteContentAddress(context, uri);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    private String fetchRemoteContentAddress(Context context, Uri uri){
        // Return the remote address
        String url;
        if (isGooglePhotosUri(uri)) {
            url = getDataColumnWithAuthority(context, uri);
            return getDataColumn(context, Uri.parse(url), null, null);
        }
        return getDataColumn(context, uri, null, null);
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    private boolean isGooglePhotosUri(Uri uri) {
        return uri.getAuthority() != null;
    }

    /**
     * Function for fixing synced folder image picking bug
     *
     * **/
    private String getDataColumnWithAuthority(Context context, Uri uri) {
        InputStream is = null;
        if (uri.getAuthority()!=null){
            try {
                is = context.getContentResolver().openInputStream(uri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap bmp = BitmapFactory.decodeStream(is);
            return getImageUri(context,bmp).toString();
        }
        return null;
    }

    private Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
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
    //===============================Gallery fetch code ends=====================================//
}

