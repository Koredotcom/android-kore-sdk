package com.kore.ai.widgetsdk.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.service.autofill.UserData;
import android.view.View;
import android.widget.ImageView;

import com.kora.ai.widgetsdk.R;
import com.kore.ai.widgetsdk.models.KoreMedia;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Shiva Krishna on 4/5/2018.
 */

public class BitmapUtils {

    public static final String ORIENTATION_LS = "landscape";
    public static final String ORIENTATION_PT = "portrait";

    public static final String CSV_MIME_TYPE = "text/csv";
    public static final String DOTX_MIME_TYPE = "application/vnd.openxmlformats-officedocument.wordprocessingml.template";
    public static final String SLDX_MIME_TYPE = "application/vnd.openxmlformats-officedocument.presentationml.slide";
    public static final String DOTM_MIME_TYPE = "application/vnd.ms-word.template.macroEnabled.12";
    public static final String DOCM_MIME_TYPE = "application/vnd.ms-word.document.macroEnabled.12";

    public static final String EXT_DOTX = "dotx";
    public static final String EXT_DOTM = "dotm";
    public static final String EXT_DOCM = "docm";
    public static final String EXT_DOT = "dot";
    public static final String EXT_XLA = "xla";
    public static final String EXT_XLT = "xlt";
    public static final String EXT_XLM = "xlm";
    public static final String EXT_XLL = "xll";
    public static final String EXT_XLW = "xlw";
    public static final String EXT_XLSB = "xlsb";
    public static final String EXT_XLSM = "xlsm";
    public static final String EXT_XLTX = "xltx";
    public static final String EXT_XLTM = "xltm";
    public static final String EXT_XLAM = "xlam";
    public static final String EXT_POTM = "potm";
    public static final String EXT_POTX = "potx";
    public static final String EXT_SLDX = "sldx";
    public static final String EXT_JPEG = "jpeg";
    public static final String EXT_IMAGE = "image";

    public static final String EXT_AUDIO_10 = "m4a";
    public static final String EXT_AUDIO_9 = "amr";
    public static final String EXT_AUDIO_MP3 = "mp3";
    public static final String EXT_AUDIO_MPEG = "mpeg";
    public static final String EXT_AUDIO_MID = "mid";
    public static final String EXT_AUDIO_BASIC = "au";
    public static final String EXT_AUDIO_SND = "snd";
    public static final String EXT_AUDIO_WAV = "wav";
    public static final String EXT_AUDIO_AAC = "aac";
    public static final String EXT_AUDIO_PLAIN = "audio";
    public static final String EXT_AUDIO_RA = "ra";
    public static final String EXT_AUDIO_RM = "rm";
    public static final String EXT_AUDIO_WMA = "wma";
    public static final String EXT_VIDEO_AVI = "avi";
    public static final String EXT_VIDEO_MOV = "mov";
    public static final String EXT_VIDEO_FLV = "flv";
    public static final String EXT_VIDEO_WMV = "wmv";
    public static final String EXT_VIDEO_VOB = "vob";
    public static final String EXT_VIDEO = "mp4";
    public static final String EXT_VIDEO_3GP = "3gp";
    public static final String EXT_VIDEO_3GP2 = "3gp2";
    public static final String EXT_VIDEO_PLAIN = "video";
    public static final String EXT_7ZIP = "7z";
    public static final String EXT_ZIP = "zip";
    public static final String EXT_RAR = "rar";
    public static final String EXT_HTM = "htm";
    public static final String EXT_HTML = "html";
    public static final String EXT_AZW = "azw";

    //3dimageFileFormats
    public static final String EXT_3DM = "3dm";
    public static final String EXT_3DS = "3ds";
    public static final String EXT_MAX = "max";
    public static final String EXT_OBJ = "obj";

    //audiofileformats
    public static final String EXT_AIF = "aif";
    public static final String EXT_IFF = "iff";
    public static final String EXT_M3U = "m3u";
    public static final String EXT_M4A = "m4a";
    public static final String EXT_MID = "mid";
    public static final String EXT_MP3 = "mp3";
    public static final String EXT_MPA = "mpa";
    public static final String EXT_WAV = "wav";
    public static final String EXT_WMA = "wma";

    //datafileformats
    public static final String EXT_CSV = "csv";
    public static final String EXT_DAT = "dat";
    public static final String EXT_GED = "ged";
    public static final String EXT_KEY = "key";
    public static final String EXT_KEYCHA = "keycha";
    public static final String EXT_PPS = "pps";
    public static final String EXT_PPT = "ppt";
    public static final String EXT_PPTX = "pptx";
    public static final String EXT_SDF = "sdf";
    public static final String EXT_TAR = "tar";
    public static final String EXT_VCF = "vcf";
    public static final String EXT_XML = "xml";


    //executableFileFormats
    public static final String EXT_APK = "apk";
    public static final String EXT_APP = "app";
    public static final String EXT_BAT = "bat";
    public static final String EXT_CGI = "cgi";
    public static final String EXT_COM = "com";
    public static final String EXT_EXE = "exe";
    public static final String EXT_GADGET = "gadget";
    public static final String EXT_JAR = "jar";
    public static final String EXT_WSF = "wsf";


    //otherFileFormats
    public static final String EXT_ACCBD = "accbd";
    public static final String EXT_DB = "db";
    public static final String EXT_DBF = "dbf";
    public static final String EXT_MDB = "mdb";
    public static final String EXT_PDB = "pdb";
    public static final String EXT_SQL = "sql";


    //pagelayoutimageFileFormats
    public static final String EXT_INDD = "indd";
    public static final String EXT_PCT = "pct";
    public static final String EXT_PDF = "pdf";


    //rasterimageFileFormats
    public static final String EXT_BMP = "bmp";
    public static final String EXT_DDS = "dds";
    public static final String EXT_GIF = "gif";
    public static final String EXT_JPG = "jpg";
    public static final String EXT_PNG = "png";
    public static final String EXT_PSD = "psd";
    public static final String EXT_PSPIMA = "pspima";
    public static final String EXT_TGA = "tga";
    public static final String EXT_THM = "thm";
    public static final String EXT_TIF = "tif";
    public static final String EXT_TIFF = "tiff";
    public static final String EXT_YUV = "yuv";


    //spreadsheetFileFormats
    public static final String EXT_XLR = "xlr";
    public static final String EXT_XLS = "xls";
    public static final String EXT_XLSX = "xlsx";


    //textFileFormats
    public static final String EXT_DOC = "doc";
    public static final String EXT_DOCX = "docx";
    public static final String EXT_LOG = "log";
    public static final String EXT_MSG = "msg";
    public static final String EXT_ODT = "odt";
    public static final String EXT_PAGES = "pages";
    public static final String EXT_RTF = "rtf";
    public static final String EXT_TEX = "tex";
    public static final String EXT_TXT = "txt";
    public static final String EXT_WPD = "wpd";
    public static final String EXT_WPS = "wps";


    //vectorimageFileFormats
    public static final String EXT_AI = "ai";
    public static final String EXT_EPS = "eps";
    public static final String EXT_PS = "ps";
    public static final String EXT_SKETCH = "sketch";
    public static final String EXT_SVG = "svg";


    //videofileformats
    public static final String EXT_3G2 = "3g2";
    public static final String EXT_3GP = "3gp";
    public static final String EXT_ASF = "asf";
    public static final String EXT_AVI = "avi";
    public static final String EXT_FLV = "flv";
    public static final String EXT_M4V = "m4v";
    public static final String EXT_MOV = "mov";
    public static final String EXT_MP4 = "mp4";
    public static final String EXT_MPG = "mpg";
    public static final String EXT_RM = "rm";
    public static final String EXT_SRT = "srt";
    public static final String EXT_SWF = "swf";
    public static final String EXT_VOB = "vob";
    public static final String EXT_WMV = "wmv";


    private static final int FREE_MEMORY_FRACTION = 16;

    private static final int MAX_IMAGE_SIZE = 800;

    public static final int TYPE_DOC_ATTACHMENT = 101;
    public static final int TYPE_PPT_ATTACHMENT = 102;
    public static final int TYPE_EXCEL_ATTACHMENT = 103;
    public static final int TYPE_PDF_ATTACHMENT = 104;
    public static final int TYPE_IMAGE_ATTACHMENT = 105;
    public static final int TYPE_VIDEO_ATTACHMENT = 106;
    public static final int TYPE_AUDIO_ATTACHMENT = 107;
    public static final int TYPE_ZIP_ATTACHMENT = 108;
    public static final int TYPE_AI_ATTACHMENT = 110;
    public static final int TYPE_PS_ATTACHMENT = 111;
    public static final int TYPE_EPS_ATTACHMENT = 112;

    public static final int TYPE_OTHER_ATTACHMENT = 109;
    public static final int TYPE_TEXT = 113;

    public static final String DOC_TEXT = "Document";
    public static final String PPT_TEXT = "Presentation";
    public static final String XLX_TEXT = "Excel";
    public static final String PDF_TEXT = "Pdf";
    public static final String AUDIO_TEXT = "Audio";
    public static final String VIDEO_TEXT = "Video";
    public static final String IMAGE_TEXT = "Image";
    public static final String ZIP_TEXT = "Zip";
    public static final String AI_TEXT = "Adobe Illustrator Artwork";
    public static final String PS_TEXT = "PostScript";
    public static final String EPS_TEXT = "Encapsulated PostScript";
    public static final String OTHER_TEXT = "Other";


    private static Bitmap decodeBitmap(File file, BitmapFactory.Options options, float rotationAngle){
        Bitmap bitmap = null, bm = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            options.inJustDecodeBounds = false;
            bm = BitmapFactory.decodeStream(fis,null, options);
            Matrix matrix = new Matrix();
            matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
            bitmap = Bitmap.createBitmap(bm, 0, 0, options.outWidth, options.outHeight, matrix, true);
        } catch (OutOfMemoryError | FileNotFoundException oom){
            options.inSampleSize *= 2;
            bitmap = decodeBitmap(file, options, rotationAngle);
        } finally {
            if(bm != null && !bm.equals(bitmap)) bm.recycle();
            if(fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return bitmap;
    }

    public static Bitmap decodeBitmapFromFile(String filename, int reqWidth, int reqHeight, boolean isCapturedFile) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filename, options);


        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap resultBitmap = null;
        resultBitmap = BitmapFactory.decodeFile(filename, options);

        return resultBitmap;
    }



    public static Bitmap decodeBitmapFromFile(File file, int reqWidth, int reqHeight) {


        ExifInterface exif;
        String orientString = null;
        try {
            exif = new ExifInterface(file.getAbsolutePath());
            orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);

        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;

        int rotationAngle = 0;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return decodeBitmap(file, options, rotationAngle);
    }


    /**
     * Calculate an inSampleSize for use in a {@link BitmapFactory.Options} object when decoding
     * bitmaps using the decode* methods from {@link BitmapFactory}. This implementation calculates
     * the closest inSampleSize that is a power of 2 and will result in the final decoded bitmap
     * having a width and height equal to or larger than the requested width and height.
     *
     * @param options   An options object with out* params already populated (run through a decode*
     *                  method with inJustDecodeBounds==true
     * @param reqWidth  The requested width of the resulting bitmap
     * @param reqHeight The requested height of the resulting bitmap
     * @return The value to be used for inSampleSize
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger inSampleSize).

            long totalPixels = width * height / inSampleSize;

            // Anything more than 2x the requested pixels we'll sample down further
            final long totalReqPixelsCap = reqWidth * reqHeight * 6;

            while (totalPixels > totalReqPixelsCap) {
                inSampleSize *= 2;
                totalPixels /= 2;
            }
        }
        return inSampleSize;
    }


    public static Bitmap rotate(Bitmap bitmap, int degree) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix mtx = new Matrix();
        mtx.postRotate(degree);

        return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
    }

    public static Bitmap rotateIfNecessary(String fileName, Bitmap bitmap) {
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(fileName);
//            KoreLogger.debugLog("EXIF value", exif.getAttribute(ExifInterface.TAG_ORIENTATION));
            if (exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("6")) {

                bitmap = rotate(bitmap, 90);
            } else if (exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("8")) {
                bitmap = rotate(bitmap, 270);
            } else if (exif.getAttribute(ExifInterface.TAG_ORIENTATION).equalsIgnoreCase("3")) {
                bitmap = rotate(bitmap, 180);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }
    public static String getExtensionFromFileName(String fileName) {
        String extn = "";
        if (fileName != null && fileName.contains(".")) {
            extn = fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        return extn;
    }




    public static int categorisedAttachmentType(String extn) {
        switch(extn.toLowerCase()) {
            case EXT_PDF:
                return TYPE_PDF_ATTACHMENT;

            case EXT_DOC:
            case EXT_DOCX:
            case EXT_DOTX:
            case EXT_DOTM:
            case EXT_DOCM:
            case EXT_DOT:
                return TYPE_DOC_ATTACHMENT;

            case EXT_SLDX:
            case EXT_PPT:
            case EXT_PPTX:
            case EXT_POTM:
            case EXT_POTX:
                return TYPE_PPT_ATTACHMENT;

            case EXT_XLSX:
            case EXT_XLSM:
            case EXT_XLSB:
            case EXT_XLS:
            case EXT_XLL:
            case EXT_XLA:
            case EXT_XLAM:
            case EXT_XLT:
            case EXT_XLTM:
            case EXT_XLW:
            case EXT_XLTX:
                return TYPE_EXCEL_ATTACHMENT;

            case EXT_PNG:
            case EXT_GIF:
            case EXT_BMP:
            case EXT_JPG:
            case EXT_JPEG:
            case EXT_IMAGE:
                return TYPE_IMAGE_ATTACHMENT;

            case EXT_VIDEO:
            case EXT_VIDEO_MOV:
            case EXT_VIDEO_FLV:
            case EXT_VIDEO_VOB:
            case EXT_VIDEO_WMV:
            case EXT_MPG:
            case EXT_VIDEO_3GP:
            case EXT_VIDEO_3GP2:
            case EXT_VIDEO_PLAIN:
                return TYPE_VIDEO_ATTACHMENT;

            case EXT_AUDIO_WAV:
            case EXT_AUDIO_MP3:
            case EXT_AUDIO_MPEG:
            case EXT_AUDIO_MID:
            case EXT_AUDIO_BASIC:
            case EXT_AUDIO_SND:
            case EXT_AUDIO_10:
            case EXT_AUDIO_9:
            case EXT_AUDIO_AAC:
            case EXT_AUDIO_PLAIN:
                return TYPE_AUDIO_ATTACHMENT;

            case EXT_ZIP:
            case EXT_7ZIP:
            case EXT_RAR:
                return TYPE_ZIP_ATTACHMENT;

            case EXT_AI:
                return TYPE_AI_ATTACHMENT;

            case EXT_PS:
                return TYPE_PS_ATTACHMENT;

            case EXT_EPS:
                return TYPE_EPS_ATTACHMENT;
            case EXT_TXT:
                return TYPE_TEXT;
            default:
                return TYPE_OTHER_ATTACHMENT;
        }
    }



    
    public static String getFileAssetOnAttachmentType(String fileType) {
        switch (fileType.toLowerCase()) {

            //3dimageFileFormats
            case EXT_3DM:
                return "images/attachment_3dm.png";
            case EXT_3DS:
                return "images/attachment_3ds.png";
            case EXT_MAX:
                return "images/attachment_max.png";
            case EXT_OBJ:
                return "images/attachment_obj.png";


            //audiofileformats
            case EXT_AIF:
                return "images/attachment_aif.png";
            case EXT_IFF:
                return "images/attachment_iff.png";
            case EXT_M3U:
                return "images/attachment_m3u.png";
            case EXT_M4A:
                return "images/attachment_m4a.png";
            case EXT_MID:
                return"images/attachment_mid.png";
            case EXT_MP3:
                return "images/attachment_mp3.png";
            case EXT_MPA:
                return "images/attachment_mpa.png";
            case EXT_WAV:
                return "images/attachment_wav.png";
            case EXT_WMA:
                return "images/attachment_wma.png";


            //datafileformats
            case EXT_CSV:
                return "images/attachment_csv.png";
            case EXT_DAT:
                return "images/attachment_dat.png";
            case EXT_GED:
                return "images/attachment_ged.png";
            case EXT_KEY:
                return "images/attachment_key.png";
            case EXT_KEYCHA:
                return "images/attachment_keycha.png";
            case EXT_PPS:
                return "images/attachment_pps.png";
            case EXT_PPT:
                return "images/attachment_ppt.png";
            case EXT_PPTX:
                return "images/attachment_pptx_irc.png";
            case EXT_SDF:
                return "images/attachment_sdf.png";
            case EXT_TAR:
                return "images/attachment_tar.png";
            case EXT_VCF:
                return "images/attachment_vcf.png";
            case EXT_XML:
                return "images/attachment_xml.png";


            //executableFileFormats
            case EXT_APK:
                return "images/attachment_apk.png";
            case EXT_APP:
                return "images/attachment_app.png";
            case EXT_BAT:
                return "images/attachment_bat.png";
            case EXT_CGI:
                return "images/attachment_cgi.png";
            case EXT_COM:
                return "images/attachment_com.png";
            case EXT_EXE:
                return "images/attachment_exe.png";
            case EXT_GADGET:
                return "images/attachment_gadget.png";
            case EXT_JAR:
                return "images/attachment_jar.png";
            case EXT_WSF:
                return "images/attachment_wsf.png";


            //otherFileFormats
            case EXT_ACCBD:
                return "images/attachment_accbd.png";
            case EXT_DB:
                return "images/attachment_db.png";
            case EXT_DBF:
                return "images/attachment_dbf.png";
            case EXT_MDB:
                return "images/attachment_mdb.png";
            case EXT_PDB:
                return "images/attachment_pdb.png";
            case EXT_SQL:
                return "images/attachment_sql.png";


            //pagelayoutimageFileFormats
            case EXT_INDD:
                return "images/attachment_indd.png";
            case EXT_PCT:
                return "images/attachment_pct.png";
            case EXT_PDF:
                return "images/attachment_pdf_irc.png";


            //rasterimageFileFormats
            case EXT_BMP:
                return "images/attachment_bmp.png";
            case EXT_DDS:
                return "images/attachment_dds.png";
            case EXT_GIF:
                return "images/attachment_gif.png";
            case EXT_JPG:
                return "images/attachment_jpg.png";
            case EXT_PNG:
                return "images/attachment_png.png";
            case EXT_PSD:
                return "images/attachment_psd.png";
            case EXT_PSPIMA:
                return "images/attachment_pspima.png";
            case EXT_TGA:
                return "images/attachment_tga.png";
            case EXT_THM:
                return "images/attachment_thm.png";
            case EXT_TIF:
                return "images/attachment_tif.png";
            case EXT_TIFF:
                return "images/attachment_tiff.png";
            case EXT_YUV:
                return "images/attachment_yuv.png";


            //spreadsheetFileFormats
            case EXT_XLR:
                return "images/attachment_xlr.png";
            case EXT_XLS:
                return "images/attachment_xls.png";
            case EXT_XLSX:
                return "images/attachment_xlsx.png";


            //textFileFormats
            case EXT_DOC:
                return "images/attachment_doc_irc.png";
            case EXT_DOCX:
                return "images/attachment_docx.png";
            case EXT_LOG:
                return "images/attachment_log.png";
            case EXT_MSG:
                return "images/attachment_msg.png";
            case EXT_ODT:
                return "images/attachment_odt.png";
            case EXT_PAGES:
                return "images/attachment_pages.png";
            case EXT_RTF:
                return "images/attachment_rtf.png";
            case EXT_TEX:
                return "images/attachment_tex.png";
            case EXT_TXT:
                return "images/attachment_txt.png";
            case EXT_WPD:
                return "images/attachment_wpd.png";
            case EXT_WPS:
                return "images/attachment_wps.png";


            //vectorimageFileFormats
            case EXT_AI:
                return "images/attachment_ai_irc.png";
            case EXT_EPS:
                return "images/attachment_eps_irc.png";
            case EXT_PS:
                return "images/attachment_ps_irc.png";
            case EXT_SKETCH:
                return "images/attachment_sketch.png";
            case EXT_SVG:
                return "images/attachment_svg.png";


            //videofileformats
            case EXT_3G2:
                return "images/attachment_3g2.png";
            case EXT_3GP:
                return "images/attachment_3gp.png";
            case EXT_ASF:
                return "images/attachment_asf.png";
            case EXT_AVI:
                return "images/attachment_avi.png";
            case EXT_FLV:
                return "images/attachment_flv.png";
            case EXT_M4V:
                return "images/attachment_m4v.png";
            case EXT_MOV:
                return "images/attachment_mov_irc.png";
            case EXT_MP4:
                return "images/attachment_mp4.png";
            case EXT_MPG:
                return "images/attachment_mpg.png";
            case EXT_RM:
                return "images/attachment_rm.png";
            case EXT_SRT:
                return "images/attachment_srt.png";
            case EXT_SWF:
                return "images/attachment_swf.png";
            case EXT_VOB:
                return "images/attachment_vob.png";
            case EXT_WMV:
                return "images/attachment_wmv.png";

            default:
                return "images/attachment_accbd.png";
        }
    }


    public static int getFileResourceOnAttachmentType(String fileType) {
        switch (fileType.toLowerCase()) {

            //3dimageFileFormats
            case EXT_3DM:
                return R.mipmap.attachment_3dm;
            case EXT_3DS:
                return R.mipmap.attachment_3ds;
            case EXT_MAX:
                return R.mipmap.attachment_max;
            case EXT_OBJ:
                return R.mipmap.attachment_obj;


            //audiofileformats
            case EXT_AIF:
                return R.mipmap.attachment_aif;
            case EXT_IFF:
                return R.mipmap.attachment_iff;
            case EXT_M3U:
                return R.mipmap.attachment_m3u;
            case EXT_M4A:
                return R.mipmap.attachment_m4a;
            case EXT_MID:
                return R.mipmap.attachment_mid;
            case EXT_MP3:
                return R.mipmap.attachment_mp3;
            case EXT_MPA:
                return R.mipmap.attachment_mpa;
            case EXT_WAV:
                return R.mipmap.attachment_wav;
            case EXT_WMA:
                return R.mipmap.attachment_wma;


            //datafileformats
            case EXT_CSV:
                return R.mipmap.attachment_csv;
            case EXT_DAT:
                return R.mipmap.attachment_dat;
            case EXT_GED:
                return R.mipmap.attachment_ged;
            case EXT_KEY:
                return R.mipmap.attachment_key;
            case EXT_KEYCHA:
                return R.mipmap.attachment_keycha;
            case EXT_PPS:
                return R.mipmap.attachment_pps;
            case EXT_PPT:
                return R.mipmap.attachment_ppt;
            case EXT_PPTX:
                return R.mipmap.attachment_pptx_irc;
            case EXT_SDF:
                return R.mipmap.attachment_sdf;
            case EXT_TAR:
                return R.mipmap.attachment_tar;
            case EXT_VCF:
                return R.mipmap.attachment_vcf;
            case EXT_XML:
                return R.mipmap.attachment_xml;


            //executableFileFormats
            case EXT_APK:
                return R.mipmap.attachment_apk;
            case EXT_APP:
                return R.mipmap.attachment_app;
            case EXT_BAT:
                return R.mipmap.attachment_bat;
            case EXT_CGI:
                return R.mipmap.attachment_cgi;
            case EXT_COM:
                return R.mipmap.attachment_com;
            case EXT_EXE:
                return R.mipmap.attachment_exe;
            case EXT_GADGET:
                return R.mipmap.attachment_gadget;
            case EXT_JAR:
                return R.mipmap.attachment_jar;
            case EXT_WSF:
                return R.mipmap.attachment_wsf;


            //otherFileFormats
            case EXT_ACCBD:
                return R.mipmap.attachment_accbd;
            case EXT_DB:
                return R.mipmap.attachment_db;
            case EXT_DBF:
                return R.mipmap.attachment_dbf;
            case EXT_MDB:
                return R.mipmap.attachment_mdb;
            case EXT_PDB:
                return R.mipmap.attachment_pdb;
            case EXT_SQL:
                return R.mipmap.attachment_sql;


            //pagelayoutimageFileFormats
            case EXT_INDD:
                return R.mipmap.attachment_indd;
            case EXT_PCT:
                return R.mipmap.attachment_pct;
            case EXT_PDF:
                return R.mipmap.attachment_pdf_irc;


            //rasterimageFileFormats
            case EXT_BMP:
                return R.mipmap.attachment_bmp;
            case EXT_DDS:
                return R.mipmap.attachment_dds;
            case EXT_GIF:
                return R.mipmap.attachment_gif;
            case EXT_JPG:
                return R.mipmap.attachment_jpg;
            case EXT_PNG:
                return R.mipmap.attachment_png;
            case EXT_PSD:
                return R.mipmap.attachment_psd;
            case EXT_PSPIMA:
                return R.mipmap.attachment_pspima;
            case EXT_TGA:
                return R.mipmap.attachment_tga;
            case EXT_THM:
                return R.mipmap.attachment_thm;
            case EXT_TIF:
                return R.mipmap.attachment_tif;
            case EXT_TIFF:
                return R.mipmap.attachment_tiff;
            case EXT_YUV:
                return R.mipmap.attachment_yuv;


            //spreadsheetFileFormats
            case EXT_XLR:
                return R.mipmap.attachment_xlr;
            case EXT_XLS:
                return R.mipmap.attachment_xls;
            case EXT_XLSX:
                return R.mipmap.attachment_xlsx;


            //textFileFormats
            case EXT_DOC:
                return R.mipmap.attachment_doc_irc;
            case EXT_DOCX:
                return R.mipmap.attachment_docx;
            case EXT_LOG:
                return R.mipmap.attachment_log;
            case EXT_MSG:
                return R.mipmap.attachment_msg;
            case EXT_ODT:
                return R.mipmap.attachment_odt;
            case EXT_PAGES:
                return R.mipmap.attachment_pages;
            case EXT_RTF:
                return R.mipmap.attachment_rtf;
            case EXT_TEX:
                return R.mipmap.attachment_tex;
            case EXT_TXT:
                return R.mipmap.attachment_txt;
            case EXT_WPD:
                return R.mipmap.attachment_wpd;
            case EXT_WPS:
                return R.mipmap.attachment_wps;


            //vectorimageFileFormats
            case EXT_AI:
                return R.mipmap.attachment_ai_irc;
            case EXT_EPS:
                return R.mipmap.attachment_eps_irc;
            case EXT_PS:
                return R.mipmap.attachment_ps_irc;
            case EXT_SKETCH:
                return R.mipmap.attachment_sketch;
            case EXT_SVG:
                return R.mipmap.attachment_svg;


            //videofileformats
            case EXT_3G2:
                return R.mipmap.attachment_3g2;
            case EXT_3GP:
                return R.mipmap.attachment_3gp;
            case EXT_ASF:
                return R.mipmap.attachment_asf;
            case EXT_AVI:
                return R.mipmap.attachment_avi;
            case EXT_FLV:
                return R.mipmap.attachment_flv;
            case EXT_M4V:
                return R.mipmap.attachment_m4v;
            case EXT_MOV:
                return R.mipmap.attachment_mov_irc;
            case EXT_MP4:
                return R.mipmap.attachment_mp4;
            case EXT_MPG:
                return R.mipmap.attachment_mpg;
            case EXT_RM:
                return R.mipmap.attachment_rm;
            case EXT_SRT:
                return R.mipmap.attachment_srt;
            case EXT_SWF:
                return R.mipmap.attachment_swf;
            case EXT_VOB:
                return R.mipmap.attachment_vob;
            case EXT_WMV:
                return R.mipmap.attachment_wmv;

            default:
                return R.mipmap.attachment_accbd;
        }
    }
    

    // determines extn media type. If type !image && !video && !audio then return extn
    // Method generally used with creating dir
    public static String obtainMediaTypeOfExtn(String extn) {
        switch(categorisedAttachmentType(extn)) {
            case TYPE_IMAGE_ATTACHMENT:
                return KoreMedia.MEDIA_TYPE_IMAGE;
            case TYPE_VIDEO_ATTACHMENT:
                return KoreMedia.MEDIA_TYPE_VIDEO;
            case TYPE_AUDIO_ATTACHMENT:
                return KoreMedia.MEDIA_TYPE_AUDIO;
            default:
                return extn;
        }
    }

    public static String getAttachmentType(String extn) {
        switch(categorisedAttachmentType(extn)) {
            case TYPE_IMAGE_ATTACHMENT:
                return KoreMedia.MEDIA_TYPE_IMAGE;
            case TYPE_VIDEO_ATTACHMENT:
                return KoreMedia.MEDIA_TYPE_VIDEO;
            case TYPE_AUDIO_ATTACHMENT:
                return KoreMedia.MEDIA_TYPE_AUDIO;
            default:
                return KoreMedia.MEDIA_TYPE_ATTACHMENT;
        }
    }

    public static Bitmap getScaledBitmap(Bitmap image){
        float actualHeight = image.getHeight();
        float actualWidth = image.getWidth();
        float maxHeight = 600f;
        float maxWidth = 800f;
        float imgRatio = actualWidth/actualHeight;
        float maxRatio = maxWidth/maxHeight;
//        int compressionQuality = 50;//50 percent compression
//        KoreLogger.debugLog(LOG_TAG, "#############@@@@@@@@@@@@@@@@ Image size before compress ::" + sizeOf(image));
        if (actualHeight > maxHeight || actualWidth > maxWidth){
            if(imgRatio < maxRatio){
                //adjust width according to maxHeight
                imgRatio = maxHeight / actualHeight;
                actualWidth = imgRatio * actualWidth;
                actualHeight = maxHeight;
            }
            else if(imgRatio > maxRatio){
                //adjust height according to maxWidth
                imgRatio = maxWidth / actualWidth;
                actualHeight = imgRatio * actualHeight;
                actualWidth = maxWidth;
            }
            else{
                actualHeight = maxHeight;
                actualWidth = maxWidth;
            }
        }
        return Bitmap.createScaledBitmap(image, (int)actualWidth, (int)actualHeight, true);//.compress(Bitmap.CompressFormat.JPEG, compressionQuality, stream);
    }

    private static Bitmap loadBitmapFromView(View v, int height, int width) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.layout(0, 0, width, height);
        v.draw(c);
        return b;
    }
}
