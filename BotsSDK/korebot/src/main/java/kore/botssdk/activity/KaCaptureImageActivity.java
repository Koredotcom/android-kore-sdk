package kore.botssdk.activity;

import static android.os.Build.VERSION_CODES.TIRAMISU;
import static kore.botssdk.utils.BundleConstants.CAPTURE_IMAGE_BUNDLED_PERMISSION_REQUEST;
import static kore.botssdk.utils.BundleConstants.CAPTURE_VIDEO_BUNDLED_PERMISSION_REQUEST;
import static kore.botssdk.utils.BundleConstants.CHOOSE_IMAGE_BUNDLED_PERMISSION_REQUEST;
import static kore.botssdk.utils.BundleConstants.CHOOSE_VIDEO_BUNDLED_PERMISSION_REQUEST;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.OrientationEventListener;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import kore.botssdk.utils.KoreBotFileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.exifinterface.media.ExifInterface;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import io.reactivex.annotations.NonNull;
import kore.botssdk.R;
import kore.botssdk.exceptions.NoExternalStorageException;
import kore.botssdk.exceptions.NoWriteAccessException;
import kore.botssdk.models.BotResponse;
import kore.botssdk.models.KoreMedia;
import kore.botssdk.utils.BitmapUtils;
import kore.botssdk.utils.BundleConstants;
import kore.botssdk.utils.KaMediaUtils;
import kore.botssdk.utils.KaPermissionsHelper;
import kore.botssdk.utils.LogUtils;
import kore.botssdk.utils.StringUtils;

@SuppressLint("UnknownNullness")
public class KaCaptureImageActivity extends KaAppCompatActivity implements KoreMedia, ActivityResultCallback<ActivityResult> {
    public static final int THUMBNAIL_WIDTH = 320;
    public static final int THUMBNAIL_HEIGHT = 240;
    public static final String THUMBNAIL_FILE_PATH = "filePathThumbnail";
    private static Uri cameraMediaUri;
    private static File videoMediaFilePath;
    static String LOG_TAG = KaCaptureImageActivity.class.getName();
    private String imagePickType = null;
    String fileContext = null;
    static boolean NORMAL_PORTRAIT;
    private final int compressQualityInt = 100;
    private String MEDIA_TYPE = MEDIA_TYPE_IMAGE;
    String MEDIA_FILENAME;
    String MEDIA_FILE_PATH;
    String MEDIA_EXTENSION;
    String thumbnailFilePath;
    Intent resultIntent = null;
    String mCurrentMediaPath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ka_capture_image);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_capture_image), (view, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(insets.left, insets.top, insets.right, insets.bottom);
            return WindowInsetsCompat.CONSUMED;
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            imagePickType = extras.getString(BundleConstants.PICK_TYPE);
            LogUtils.d(LOG_TAG, "onCreate() :: extras :: imagePickType ::::" + imagePickType);
            fileContext = extras.getString(BundleConstants.FILE_CONTEXT);
            LogUtils.d(LOG_TAG, "onCreate() :: extras :: fileContext ::::" + fileContext);
            MEDIA_TYPE = extras.containsKey("mediaType") ? extras.getString("mediaType") : MEDIA_TYPE_IMAGE;
        }

        checkForPermissionAccessAndRequest();

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finishAndCancelOperation();
            }
        });
    }

    private void checkForPermissionAccessAndRequest() {
        if (CHOOSE_TYPE_CAPTURE_IMAGE.equalsIgnoreCase(imagePickType)) {
            if (KaPermissionsHelper.hasPermission(this, Manifest.permission.CAMERA)) {
                openImageIntent(imagePickType);
            } else {
                KaPermissionsHelper.requestForPermission(this, CAPTURE_IMAGE_BUNDLED_PERMISSION_REQUEST, Manifest.permission.CAMERA);
            }
        } else if (CHOOSE_TYPE_VIDEO_PICK.equalsIgnoreCase(imagePickType)) {
            if (Build.VERSION.SDK_INT >= TIRAMISU || KaPermissionsHelper.hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                openImageIntent(imagePickType);
            } else {
                KaPermissionsHelper.requestForPermission(this, CHOOSE_VIDEO_BUNDLED_PERMISSION_REQUEST,
                        Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        } else if (CHOOSE_TYPE_IMAGE_PICK.equalsIgnoreCase(imagePickType)) {
            if (Build.VERSION.SDK_INT >= TIRAMISU || KaPermissionsHelper.hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                openImageIntent(imagePickType);
            } else {
                KaPermissionsHelper.requestForPermission(this, CHOOSE_IMAGE_BUNDLED_PERMISSION_REQUEST, Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        } else if (CHOOSE_TYPE_CAPTURE_VIDEO.equalsIgnoreCase(imagePickType)) {
            if (KaPermissionsHelper.hasPermission(this, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)) {
                openImageIntent(imagePickType);
            } else {
                KaPermissionsHelper.requestForPermission(this, CAPTURE_VIDEO_BUNDLED_PERMISSION_REQUEST,
                        Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO);
            }
        } else if (CHOOSE_TYPE_DOCUMENT_PICK.equalsIgnoreCase(imagePickType)) {
            openImageIntent(imagePickType);
        } else {
            LogUtils.e(LOG_TAG, "no pickType found. Please assign one and invoke this activity, again.");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @androidx.annotation.NonNull String[] permissions, @androidx.annotation.NonNull int[] grantResults) {
        if (requestCode == CAPTURE_IMAGE_BUNDLED_PERMISSION_REQUEST) {
            if (KaPermissionsHelper.hasPermission(this, Manifest.permission.CAMERA)) {
                openImageIntent(imagePickType);
            }
        } else if (requestCode == CHOOSE_IMAGE_BUNDLED_PERMISSION_REQUEST) {
            if (Build.VERSION.SDK_INT >= TIRAMISU || KaPermissionsHelper.hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                openImageIntent(imagePickType);
            }
        } else if (requestCode == CHOOSE_VIDEO_BUNDLED_PERMISSION_REQUEST) {
            if (Build.VERSION.SDK_INT >= TIRAMISU || KaPermissionsHelper.hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                openImageIntent(imagePickType);
            }
        } else if (requestCode == CAPTURE_VIDEO_BUNDLED_PERMISSION_REQUEST) {
            if (KaPermissionsHelper.hasPermission(this, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)) {
                openImageIntent(imagePickType);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void openImageIntent(String mediaPickType) {
        try {
            getUserinfo();
            KaMediaUtils.setupAppDir(this, MEDIA_TYPE);
            // in case of picture taken from camera capture
            if (CHOOSE_TYPE_CAPTURE_IMAGE.equals(mediaPickType)) {
                //use standard intent to capture an image
                Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                captureIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
                captureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                captureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                try {
                    Uri photoURI = KoreBotFileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".korebot.provider", createImageFile());
                    captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    cameraMediaUri = photoURI;
                } catch (IOException e) {
                    LogUtils.e(LOG_TAG, e + " Error");
                }
                //we will handle the returned data in onActivityResult
                captureActivityResultLauncher.launch(captureIntent);

            } else if (CHOOSE_TYPE_CAPTURE_VIDEO.equals(mediaPickType)) {
                // in case picture is chosen from gallery
                //use standard intent to pick an image from gallery
                File videoFile = createVideoFile();
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

                Uri videoURI = KoreBotFileProvider.getUriForFile(this, getPackageName() + ".korebot.provider",
                        videoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, videoURI);
                videoMediaFilePath = videoFile.getAbsoluteFile();
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                //we will handle the returned data in onActivityResult
                captureVideoActivityResultLauncher.launch(intent);

            } else if (CHOOSE_TYPE_IMAGE_PICK.equals(mediaPickType)) {
                // in case picture is chosen from gallery
                //use standard intent to pick an image from gallery
                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                photoPickerIntent.setType("image/*");
                photoPickerIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                photoPickerIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                //we will handle the returned data in onActivityResult
                photoPickerActivityResultLauncher.launch(photoPickerIntent);

            } else if (mediaPickType.equals(CHOOSE_TYPE_VIDEO_PICK)) {
                //use standard intent to pick a video from gallery
                Intent videoPickerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                videoPickerIntent.addCategory(Intent.CATEGORY_OPENABLE);
                videoPickerIntent.setType("video/*");
                videoPickerIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION |
                        Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
                );
                videoActivityResultLauncher.launch(videoPickerIntent);

            } else if (mediaPickType.equals(CHOOSE_TYPE_DOCUMENT_PICK)) {
                Intent documentPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                documentPickerIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                documentPickerIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                String[] mime = {"text/plain",
                        "application/pdf", "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                        "application/vnd.ms-excel", "application/vnd.ms-excel.sheet.binary.macroenabled.12", "application/rtf",
                        "application/vnd.ms-excel.sheet.macroenabled.12", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.template", "application/vnd.ms-excel.template.macroEnabled.12",
                        "application/vnd.ms-excel.addin.macroEnabled.12", "application/vnd.ms-powerpoint", "application/vnd.oasis.opendocument.text",
                        "application/vnd.openxmlformats-officedocument.presentationml.presentation", "audio/*"};
                documentPickerIntent.setType("text/*");
                documentPickerIntent.putExtra(Intent.EXTRA_MIME_TYPES, mime);
                documentPickerIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                documentPickerActivityResultLauncher.launch(documentPickerIntent);
            }

        } catch (ActivityNotFoundException | IOException anfe) {
            //display an error message
            String errorMessage = "Your device doesn't support capturing images!";
            Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private File createVideoFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String videoFileName = "VIDEO_" + timeStamp;
        File mediaStorageDir = getExternalFilesDir(Environment.DIRECTORY_MOVIES);

        File videoFile = File.createTempFile(
                videoFileName,  /* prefix */
                ".mp4",         /* suffix */
                mediaStorageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentMediaPath = videoFile.getAbsolutePath();
        return videoFile;
    }

    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    final ActivityResultLauncher<Intent> documentPickerActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        if (data != null) {
                            Uri selectedFile = data.getData();
                            String fileExtn;
                            if (selectedFile == null) {
                                Toast.makeText(KaCaptureImageActivity.this, "Could not attach a file there was a problem", Toast.LENGTH_SHORT).show();
                                finish();
                            }

                            if (selectedFile != null) {
                                String realPath = getFileNameByUri(KaCaptureImageActivity.this, selectedFile);
                                String realFileName;
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
                                        file = KaMediaUtils.getOutputMediaFile(BitmapUtils.obtainMediaTypeOfExtn(fileExtn), realFileName, fileExtn);
                                    } catch (NoExternalStorageException |
                                             NoWriteAccessException e) {
                                        LogUtils.e(LOG_TAG, e + " Error");
                                    }
                                    if (file != null) {
                                        MEDIA_FILE_PATH = file.getAbsolutePath();
                                    }

                                    if (!fileExtn.equals(BitmapUtils.EXT_VIDEO))
                                        KaMediaUtils.saveFileToKorePath(realPath, MEDIA_FILE_PATH);

                                    MEDIA_FILENAME = MEDIA_FILE_PATH.substring(MEDIA_FILE_PATH.lastIndexOf("/") + 1);
                                    finishOperation(selectedFile, fileExtn);

                                } else {
                                    Cursor returnCursor =
                                            getContentResolver().query(selectedFile, null, null, null, null);
                                    if (returnCursor != null) {
                                        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                                        returnCursor.moveToFirst();
                                        MEDIA_FILENAME = returnCursor.getString(nameIndex);
                                        fileExtn = BitmapUtils.getExtensionFromFileName(MEDIA_FILENAME);  // will not be null but "" and will return as not whitelisted
                                        MEDIA_FILE_PATH = KaMediaUtils.saveFileToKoreWithStream(KaCaptureImageActivity.this, selectedFile, MEDIA_FILENAME, "."+fileExtn);
                                        returnCursor.close();

                                        if (MEDIA_FILE_PATH != null) {
                                            MEDIA_FILENAME = MEDIA_FILE_PATH.substring(MEDIA_FILE_PATH.lastIndexOf("/") + 1);
                                        }
                                        finishOperation(null, fileExtn);
                                    }
                                }
                            }
                        } else {
                            finish();
                        }
                    } else {
                        finish();
                    }
                }
            });
    final ActivityResultLauncher<Intent> captureActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (fileContext.equalsIgnoreCase(FOR_MESSAGE) || fileContext.equalsIgnoreCase(FOR_PROFILE)) {
                            getFullImage();
                            if (resultIntent == null) resultIntent = new Intent();
                            resultIntent.putExtra("action", "IMAGE_DONE");
                            resultIntent.putExtra("fileName", MEDIA_FILENAME);
                            resultIntent.putExtra("filePath", MEDIA_FILE_PATH);
                            resultIntent.putExtra(THUMBNAIL_FILE_PATH, thumbnailFilePath);
                            //keep track of camera capture intent
                            setResult(RESULT_OK, resultIntent);
                            finish();
                        } else {
                            finish();
                        }
                    } else {
                        finish();
                    }
                }
            });
    final ActivityResultLauncher<Intent> captureVideoActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    if (fileContext.equalsIgnoreCase(FOR_MESSAGE)) {
                        processCapturedVideo(videoMediaFilePath);
                    } else {
                        finishAndCancelOperation();
                    }
                } else {
                    finish();
                }
            });
    final ActivityResultLauncher<Intent> photoPickerActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    if (result.getData() != null) {
                        Uri selectedImage = result.getData().getData();
                        if ((fileContext.equalsIgnoreCase(FOR_MESSAGE) || fileContext.equalsIgnoreCase(FOR_PROFILE)) && selectedImage != null) {
                            // keep track of choose image intent
                            processCapturedImage(selectedImage);
                        } else if (selectedImage == null) {
                            finishAndCancelOperation();
                        }
                    } else {
                        finish();
                    }
                } else {
                    finish();
                }
            });
    final ActivityResultLauncher<Intent> videoActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if (result.getData() != null && result.getData().getData() != null) {
                            Uri selectedImage = result.getData().getData();

                            getContentResolver().takePersistableUriPermission(selectedImage, Intent.FLAG_GRANT_READ_URI_PERMISSION);

                            if (fileContext.equalsIgnoreCase(FOR_MESSAGE)) {

                                File file = null;
                                try {
                                    file = KaMediaUtils.getOutputMediaFile(KoreMedia.MEDIA_TYPE_VIDEO, null, KaMediaUtils.getMediaExtension(KoreMedia.MEDIA_TYPE_VIDEO));
                                } catch (NoExternalStorageException e) {
                                    LogUtils.e(LOG_TAG, e + " Error");
                                } catch (NoWriteAccessException e) {
                                    throw new RuntimeException(e);
                                }
                                if (file != null) MEDIA_FILE_PATH = file.getAbsolutePath();

                                MEDIA_FILENAME = MEDIA_FILE_PATH.substring(MEDIA_FILE_PATH.lastIndexOf("/") + 1);
                                MEDIA_FILENAME = MEDIA_FILENAME.substring(0, MEDIA_FILENAME.lastIndexOf("."));
                                MEDIA_EXTENSION = MEDIA_FILE_PATH.substring(MEDIA_FILE_PATH.lastIndexOf(".") + 1);
                                //display the returned video
                                finishOperation(selectedImage, BitmapUtils.getExtensionFromFileName(MEDIA_FILE_PATH));
                            }
                        } else {
                            finish();
                        }
                    } else {
                        finish();
                    }
                }
            });

    @SuppressLint("SimpleDateFormat")
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentMediaPath = image.getAbsolutePath();
        return image;
    }

    String getFileNameByUri(Context context, Uri uri) {
        String filepath = "";
        File file;
        if (Objects.requireNonNull(uri.getScheme()).compareTo("content") == 0) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{android.provider.MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.ORIENTATION}, null, null, null);
            if (cursor != null) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();

                String mImagePath = cursor.getString(column_index);
                cursor.close();
                filepath = mImagePath;
            }

        } else if (uri.getScheme().compareTo("file") == 0) {
            try {
                file = new File(new URI(uri.toString()));
                if (file.exists())
                    filepath = file.getAbsolutePath();

            } catch (URISyntaxException e) {
                LogUtils.e(LOG_TAG, e+" Error");
            }
        } else {
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
            LogUtils.e("FileUtility", "Some Error Occurred : " + result);
        }
    }

    void finishOperation(Uri uri, String fileExtn) {
        resultIntent = new Intent();
        resultIntent.putExtra("action", KoreMedia.CHOOSE_TYPE_DOCUMENT_PICK);
        resultIntent.putExtra("fileName", MEDIA_FILENAME);
        resultIntent.putExtra("filePath", MEDIA_FILE_PATH);
        resultIntent.putExtra("fileUri", uri);
        resultIntent.putExtra("fileExtn", fileExtn);

        setResult(RESULT_OK, resultIntent);
        finish();
    }

    void finishAndCancelOperation() {
        if (resultIntent == null) resultIntent = new Intent();
        resultIntent.putExtra("action", "IMAGE_CANCEL");
        resultIntent.putExtra("fileName", "");
        setResult(RESULT_CANCELED, resultIntent);
        this.finish();
    }

    private void processCapturedVideo(File videoMediaFilePath) {
        try
        {
            // Use the already created video file
            if (videoMediaFilePath == null || !videoMediaFilePath.exists()) {
                LogUtils.e(LOG_TAG, "Video file does not exist!");
                return;
            }

            MEDIA_FILE_PATH = videoMediaFilePath.getAbsolutePath();
            String fileName = videoMediaFilePath.getName();
            MEDIA_FILENAME = fileName.substring(0, fileName.lastIndexOf("."));
            MEDIA_EXTENSION = "mp4";

        } catch (Exception e) {
            LogUtils.e(LOG_TAG, e + " Video Error");
        }

        returnResult();
    }

    private void returnResult() {

        if (resultIntent == null)
            resultIntent = new Intent();

        resultIntent.putExtra("fileName", MEDIA_FILENAME);
        resultIntent.putExtra("filePath", MEDIA_FILE_PATH);
        resultIntent.putExtra("filePathThumbnail", thumbnailFilePath);
        resultIntent.putExtra("fileExtension", MEDIA_EXTENSION);

        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void processCapturedImage(Uri mediaUri) {
        OutputStream fOut = null;
        try
        {
            Bitmap highResBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), mediaUri);
            Bitmap bitmapPic = BitmapUtils.getScaledBitmap(highResBitmap);
            String fileName = "IMG_" + System.currentTimeMillis() + ".jpg";
            String fileExt = getFileExtension(KaCaptureImageActivity.this, mediaUri);

            if(!StringUtils.isNullOrEmpty(fileExt))
            {
                fileName = "IMG_" + System.currentTimeMillis() +"."+fileExt;
            }
            else fileExt = ".jpg";

            File file = KaMediaUtils.getOutputMediaFile(MEDIA_TYPE, fileName, fileExt);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                fOut = Files.newOutputStream(file.toPath());
            } else {
                fOut = new FileOutputStream(file);
            }

            bitmapPic.compress(Bitmap.CompressFormat.JPEG, compressQualityInt, fOut);
            fOut.flush();

            MEDIA_FILE_PATH = file.getAbsolutePath();
            MEDIA_FILENAME = fileName.substring(0, fileName.lastIndexOf("."));
            MEDIA_EXTENSION = "jpg";

            bitmapPic = rotateIfNecessary(MEDIA_FILE_PATH, bitmapPic);
            createImageThumbnail(MEDIA_FILE_PATH, bitmapPic);
        } catch (Exception e) {
            LogUtils.e(LOG_TAG, e + " Image Error");
        } finally {
            try {
                if (fOut != null)
                    fOut.close();
            } catch (IOException ignored) {}
        }

        returnResult();
    }


    public static String getFileExtension(Context context, Uri uri) {
        ContentResolver cr = context.getContentResolver();
        String mimeType = cr.getType(uri);

        if (mimeType != null) {
            return MimeTypeMap.getSingleton()
                    .getExtensionFromMimeType(mimeType);
        }
        return null;
    }

    void getFullImage() {

        String path = mCurrentMediaPath;
        File file = new File(path);

        LogUtils.d(LOG_TAG, "real image path  " + path);

        MEDIA_FILE_PATH = file.getAbsolutePath();
        LogUtils.d(LOG_TAG, "getFullImage() :: full image file absolute path::" + MEDIA_FILE_PATH);

        Bitmap thePic = BitmapUtils.decodeBitmapFromFile(file, 800, 600);
        OutputStream fOut = null;

        try {
            // compress the image
            File _file = new File(MEDIA_FILE_PATH);

            LogUtils.d(LOG_TAG, "getFullImage() :: file.exists() ---------------------------------------- " + _file.exists());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                fOut = Files.newOutputStream(_file.toPath());
            } else fOut = new FileOutputStream(_file);

            thePic.compress(Bitmap.CompressFormat.JPEG, compressQualityInt, fOut);
            fOut.flush();
            thePic = rotateIfNecessary(file.getAbsolutePath(), thePic);
        } catch (Exception e) {
            LogUtils.e(LOG_TAG, e.toString());
        } finally {
            try {
                assert fOut != null;
                fOut.close();
            } catch (Exception e) {
                LogUtils.e(LOG_TAG, e+" Error");
            }
        }

        MEDIA_FILENAME = MEDIA_FILE_PATH.substring(MEDIA_FILE_PATH.lastIndexOf("/") + 1);
        MEDIA_FILENAME = MEDIA_FILENAME.substring(0, MEDIA_FILENAME.lastIndexOf("."));
        MEDIA_EXTENSION = MEDIA_FILE_PATH.substring(MEDIA_FILE_PATH.lastIndexOf(".") + 1);

        getFileTokenAndStartUpload();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            LogUtils.e(LOG_TAG, e+" Error");
        }
        try {
            createImageThumbnail(MEDIA_FILE_PATH, thePic);
        } catch (Exception e) {
            LogUtils.e(LOG_TAG, e+" Error");
        }
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        if (cameraMediaUri != null) {
            outState.putParcelable("uri", cameraMediaUri);
            LogUtils.d(LOG_TAG, "onSaveInstanceState() :: +++++++On Save Instance " + cameraMediaUri.getPath());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        cameraMediaUri = savedInstanceState.getParcelable("uri");
    }

    private void createImageThumbnail(String filePath, Bitmap finalMap) {
        Bitmap scaledBitmap;
        Bitmap extBitmap;
        int originalOrien = 0;
        int degrees = 0;
        int original_H = finalMap.getHeight();
        int original_W = finalMap.getWidth();
        int target_W;
        int target_H;
        LogUtils.d(LOG_TAG, "createImageThumbnail() :: original height" + original_H);
        LogUtils.d(LOG_TAG, "createImageThumbnail() :: original width" + original_W);

        try {
            ExifInterface fullImageExif = new ExifInterface(filePath);
            originalOrien = fullImageExif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);

            LogUtils.d(LOG_TAG, "createImageThumbnail() :: Original image orientation" + originalOrien);
        } catch (IOException e) {
            LogUtils.e(LOG_TAG, e+" Error");
        }

        if (!NORMAL_PORTRAIT) {
            target_W = THUMBNAIL_WIDTH;
            target_H = (target_W * original_H) / original_W;
            LogUtils.d(LOG_TAG, "createImageThumbnail() :: calculated thumbnail height for landscape mode" + target_H + "and width is" + target_W);
        } else {
            target_H = THUMBNAIL_HEIGHT;
            target_W = (target_H * original_W) / original_H;
            LogUtils.d(LOG_TAG, "createImageThumbnail() :: calculated thumbnail width for portrait mode: width " + target_W + "and height " + target_H);
        }

        if (originalOrien == ExifInterface.ORIENTATION_ROTATE_180) {
            degrees = 180;
            LogUtils.d(LOG_TAG, "createImageThumbnail() :: rotating thumbnail by " + degrees);


        } else if (originalOrien == ExifInterface.ORIENTATION_ROTATE_90) {
            degrees = 90;
            LogUtils.d(LOG_TAG, "createImageThumbnail() :: rotating thumbnail by " + degrees);

        } else if (originalOrien == ExifInterface.ORIENTATION_ROTATE_270) {
            degrees = 270;
            LogUtils.d(LOG_TAG, "createImageThumbnail() :: rotating thumbnail by " + degrees);

        }
        LogUtils.d(LOG_TAG, "createImageThumbnail() :: Thumbnail rotation is " + degrees);

        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);

        scaledBitmap = Bitmap.createScaledBitmap(finalMap, target_W, target_H, true);
        extBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, target_W, target_H, matrix, true);
        OutputStream out = null;
        try {

            LogUtils.d(LOG_TAG, "createImageThumbnail() :: target width" + target_W);
            LogUtils.d(LOG_TAG, "createImageThumbnail() :: target height" + target_H);

            int ind = filePath.lastIndexOf(".");
            thumbnailFilePath = filePath.substring(0, ind) + "_th.png";

            LogUtils.d(LOG_TAG, "createImageThumbnail() :: thumbNailPath:::::::::::::" + thumbnailFilePath);

            File thumbNailfile = new File(thumbnailFilePath);//MediaUtil.getOutputMediaFile(AppConstants.MEDIA_TYPE_IMAGE);
            LogUtils.d(LOG_TAG, "createImageThumbnail() :: thumbnailFileName & path::::::::::::::" + thumbNailfile.getAbsolutePath());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                out = Files.newOutputStream(thumbNailfile.toPath());
            } else out = new FileOutputStream(thumbNailfile);
            extBitmap.compress(Bitmap.CompressFormat.PNG, compressQualityInt, out);
            out.flush();
            extBitmap = rotateIfNecessary(thumbNailfile.getAbsolutePath(), extBitmap);

        } catch (Exception ee) {
            LogUtils.e(LOG_TAG, "Error while create Image Thumbnail" + ee);
        } finally {
            scaledBitmap.recycle();
            if (extBitmap != null) {
                extBitmap.recycle();
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    LogUtils.e(LOG_TAG, e+" Error");
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
            LogUtils.e(LOG_TAG, e+" Error");
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
        //inverted
        //inverted land
        OrientationEventListener mOrientationEventListener = new OrientationEventListener(this) {

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
}

