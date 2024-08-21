package kore.botssdk.view.viewUtils;

import java.util.ArrayList;

import kore.botssdk.R;

public class FileUtils {
    public static final String EXT_VIDEO = "mp4";
    public static final String EXT_JPG = "jpg";
    public static final String EXT_PNG = "png";
    private static final ArrayList<String> videoTypes = new ArrayList<>();
    private static final ArrayList<String> audioTypes = new ArrayList<>();
    private static final ArrayList<String> docTypes = new ArrayList<>();
    private static final ArrayList<String> presentationTypes = new ArrayList<>();
    private static final ArrayList<String> threeDImgTypes = new ArrayList<>();
    private static final ArrayList<String> rasterImageTypes = new ArrayList<>();
    private static final ArrayList<String> vectorImgTypes = new ArrayList<>();
    private static final ArrayList<String> spreadTypes = new ArrayList<>();
    private static final ArrayList<String> dbTypes = new ArrayList<>();
    private static final ArrayList<String> exeTypes = new ArrayList<>();
    private static final ArrayList<String> fontTypes = new ArrayList<>();
    private static final ArrayList<String> systemTypes = new ArrayList<>();
    private static final ArrayList<String> compressedTypes = new ArrayList<>();
    private static final ArrayList<String> devTypes = new ArrayList<>();
    private static final ArrayList<String> msExcel = new ArrayList<>();
    private static final ArrayList<String> msPowerpaint = new ArrayList<>();
    private static final ArrayList<String> msDoc = new ArrayList<>();

    private static final String pdf = "pdf";

    public static ArrayList<String> ImageTypes() {
        return rasterImageTypes;
    }

    public static ArrayList<String> VideoTypes() {
        return videoTypes;
    }

    static {
        loadMsoffice();
        loadVideoTypes();
        loadAudioTypes();
        loadDocTypes();
        loadPresentationTypes();
        loadThreeDImgTypes();
        loadRasterImgTypes();
        loadVectorImgTypes();
        loadSpreadTypes();
        loadDbFileTypes();
        loadExeTypes();
        loadFontTypes();
        loadSystemTypes();
        loadCompressedTypes();
        loadDeveloperTypes();

    }

    private static void loadDeveloperTypes() {
        devTypes.add("c");
        devTypes.add("class");
        devTypes.add("cpp");
        devTypes.add("cs");
        devTypes.add("dtd");
        devTypes.add("fla");
        devTypes.add("h");
        devTypes.add("java");
        devTypes.add("lua");
        devTypes.add("m");
        devTypes.add("pl");
        devTypes.add("py");
        devTypes.add("sh");
        devTypes.add("sln");
        devTypes.add("swift");
        devTypes.add("vb");
        devTypes.add("vcxproj");
        devTypes.add("xcodeproj");
    }

    private static void loadMsoffice() {
        msExcel.add("xls");
        msExcel.add("xlsx");
        msPowerpaint.add("ppt");
        msPowerpaint.add("pptx");
        msDoc.add("doc");
        msDoc.add("docx");
    }

    private static void loadCompressedTypes() {
        compressedTypes.add("7z");
        compressedTypes.add("cbr");
        compressedTypes.add("deb");
        compressedTypes.add("gz");
        compressedTypes.add("pkg");
        compressedTypes.add("rar");
        compressedTypes.add("rpm");
        compressedTypes.add("sitx");
        compressedTypes.add("tar.gz");
        compressedTypes.add("zip");
        compressedTypes.add("zipx");
        compressedTypes.add("");
    }

    private static void loadSystemTypes() {
        systemTypes.add("cab");
        systemTypes.add("cpl");
        systemTypes.add("cur");
        systemTypes.add("deskthemepack");
        systemTypes.add("dll");
        systemTypes.add("dmp");
        systemTypes.add("drv");
        systemTypes.add("icns");
        systemTypes.add("ico");
        systemTypes.add("lnk");
        systemTypes.add("sys");
        systemTypes.add("cnf");
        systemTypes.add("ini");
        systemTypes.add("prf");
    }

    private static void loadFontTypes() {
        fontTypes.add("fnt");
        fontTypes.add("fon");
        fontTypes.add("otf");
        fontTypes.add("ttf");
    }

    private static void loadExeTypes() {
        exeTypes.add("apk");
        exeTypes.add("app");
        exeTypes.add("bat");
        exeTypes.add("cgi");
        exeTypes.add("com");
        exeTypes.add("exe");
        exeTypes.add("gadget");
        exeTypes.add("jar");
        exeTypes.add("wsf");
    }

    private static void loadDbFileTypes() {
        dbTypes.add("db");
        dbTypes.add("accdb");
        dbTypes.add("dbf");
        dbTypes.add("mdb");
        dbTypes.add("pdb");
        dbTypes.add("sql");

    }

    private static void loadSpreadTypes() {
        spreadTypes.add("xlr");

        spreadTypes.add("csv");
        spreadTypes.add("gsheet");
        spreadTypes.add("ods");
    }


    private static void loadVideoTypes() {
        videoTypes.add("3g2");
        videoTypes.add("3gp");
        videoTypes.add("asf");
        videoTypes.add("avi");
        videoTypes.add("flv");
        videoTypes.add("m4v");
        videoTypes.add("mov");
        videoTypes.add("mp4");
        videoTypes.add("mpg");
        videoTypes.add("rm");
        videoTypes.add("srt");
        videoTypes.add("swf");
        videoTypes.add("vob");
        videoTypes.add("wmv");
    }


    private static void loadAudioTypes() {
        audioTypes.add("aif");
        audioTypes.add("iff");
        audioTypes.add("m3u");
        audioTypes.add("m4a");
        audioTypes.add("mid");
        audioTypes.add("mp3");
        audioTypes.add("mpa");
        audioTypes.add("wav");
        audioTypes.add("wma");
    }

    private static void loadDocTypes() {

        docTypes.add("log");
        docTypes.add("msg");
        docTypes.add("odt");
        docTypes.add("pages");
        docTypes.add("rtf");
        docTypes.add("tex");
        docTypes.add("txt");
        docTypes.add("wpd");
        docTypes.add("wps");
        docTypes.add("gdoc");
    }

    private static void loadPresentationTypes() {
        presentationTypes.add("odp");
        presentationTypes.add("ged");
        presentationTypes.add("key");
        presentationTypes.add("pps");
        presentationTypes.add("gslide");
    }

    private static void loadThreeDImgTypes() {
        threeDImgTypes.add("3dm");
        threeDImgTypes.add("3ds");
        threeDImgTypes.add("max");
        threeDImgTypes.add("obj");
    }

    private static void loadRasterImgTypes() {
        rasterImageTypes.add("bmp");
        rasterImageTypes.add("dds");
        rasterImageTypes.add("gif");
        rasterImageTypes.add("heic");
        rasterImageTypes.add("jpg");
        rasterImageTypes.add("png");
        rasterImageTypes.add("psd");
        rasterImageTypes.add("pspimage");
        rasterImageTypes.add("tga");
        rasterImageTypes.add("thm");
        rasterImageTypes.add("tif");
        rasterImageTypes.add("tiff");
        rasterImageTypes.add("yuv");
    }

    private static void loadVectorImgTypes() {
        vectorImgTypes.add("ai");
        vectorImgTypes.add("eps");
        vectorImgTypes.add("ps");
        vectorImgTypes.add("svg");
    }

    public static int getDrawableByExt(String ext) {
        if (videoTypes.contains(ext)) {
            return R.drawable.ic_video;
        } else if (msDoc.contains(ext)) {
            return R.drawable.ic_document;
        } else if (msPowerpaint.contains(ext)) {
            return R.drawable.ic_slides;
        } else if (msExcel.contains(ext)) {
            return R.drawable.ic_sheet;
        } else if (audioTypes.contains(ext)) {
            return R.drawable.ic_music;
        } else if (spreadTypes.contains(ext)) {
            return R.drawable.ic_sheet_old;
        } else if (docTypes.contains(ext)) {
            return R.drawable.ic_document_old;
        } else if (threeDImgTypes.contains(ext)) {
            return R.drawable.ic_3d_object;
        } else if (rasterImageTypes.contains(ext)) {
            return R.drawable.ic_raster_image;
        } else if (vectorImgTypes.contains(ext)) {
            return R.drawable.ic_vector_type;
        } else if (dbTypes.contains(ext)) {
            return R.drawable.ic_database_type;
        } else if (devTypes.contains(ext)) {
            return R.drawable.ic_developer;
        } else if (exeTypes.contains(ext)) {
            return R.drawable.ic_executable;
        } else if (systemTypes.contains(ext)) {
            return R.drawable.ic_settings;
        } else if (compressedTypes.contains(ext)) {
            return R.drawable.ic_zip;
        } else if (presentationTypes.contains(ext)) {
            return R.drawable.ic_slides_old;
        } else if (fontTypes.contains(ext)) {
            return R.drawable.ic_font;
        } else if (pdf.equals(ext)) {
            return R.drawable.ic_pdf;
        } else {
            return R.drawable.ic_file_general;
        }
    }

}
