package com.kore.common.utils

import com.kore.botclient.R

class FileUtils {
    companion object {
        val pdf = "pdf"

        @JvmStatic
        fun imageTypes(): ArrayList<String> {
            val imageTypes = ArrayList<String>()
            imageTypes.add("bmp")
            imageTypes.add("dds")
            imageTypes.add("gif")
            imageTypes.add("heic")
            imageTypes.add("jpg")
            imageTypes.add("png")
            imageTypes.add("psd")
            imageTypes.add("pspimage")
            imageTypes.add("tga")
            imageTypes.add("thm")
            imageTypes.add("tif")
            imageTypes.add("tiff")
            imageTypes.add("yuv")
            imageTypes.add("jpeg")
            return imageTypes
        }

        @JvmStatic
        fun videoTypes(): ArrayList<String> {
            val videoTypes = ArrayList<String>()
            videoTypes.add("3g2")
            videoTypes.add("3gp")
            videoTypes.add("asf")
            videoTypes.add("avi")
            videoTypes.add("flv")
            videoTypes.add("m4v")
            videoTypes.add("mov")
            videoTypes.add("mp4")
            videoTypes.add("mpg")
            videoTypes.add("rm")
            videoTypes.add("srt")
            videoTypes.add("swf")
            videoTypes.add("vob")
            videoTypes.add("wmv")
            return videoTypes
        }

        @JvmStatic
        fun docType(): ArrayList<String> {
            val docTypes = ArrayList<String>()
            docTypes.add("log")
            docTypes.add("msg")
            docTypes.add("odt")
            docTypes.add("pages")
            docTypes.add("rtf")
            docTypes.add("tex")
            docTypes.add("txt")
            docTypes.add("wpd")
            docTypes.add("wps")
            docTypes.add("gdoc")
            docTypes.add("doc")
            docTypes.add("docx")
            return docTypes
        }

        @JvmStatic
        fun spreadTypes(): ArrayList<String> {
            val spreadTypes = ArrayList<String>()
            spreadTypes.add("xlr")
            spreadTypes.add("csv")
            spreadTypes.add("gsheet")
            spreadTypes.add("ods")
            spreadTypes.add("xls")
            spreadTypes.add("xlsx")
            return spreadTypes
        }

    }

    fun getDrawableByExt(ext: String): Int {
        return if (videoTypes().contains(ext)) {
            R.drawable.ic_video
        } else if (spreadTypes().contains(ext)) {
            R.drawable.ic_sheet_old
        } else if (docType().contains(ext)) {
            R.drawable.ic_document_old
        } else if (pdf == ext) {
            R.drawable.ic_pdf
        } else {
            R.drawable.ic_file_general
        }
    }

}