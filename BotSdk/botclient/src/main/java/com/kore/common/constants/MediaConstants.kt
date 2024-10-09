package com.kore.common.constants

class MediaConstants {
    companion object{
        const val MEDIA_TYPE_AUDIO = "audio"
        const val MEDIA_TYPE_VIDEO = "video"
        const val MEDIA_TYPE_IMAGE = "image"
        const val MEDIA_TYPE_NONE = "text"
        const val MEDIA_TYPE_ATTACHMENT = "attachment"
        const val MEDIA_TYPE_FILELINK = "filelink"
        const val MEDIA_TYPE_DOCUMENT = "docs"
        const val MEDIA_TYPE_ARCHIVE = "archieve"
        const val MEDIA_TYPE_CONTACT = "contact"
        const val MEDIA_TYPE_LINK = "link"

        const val CHOOSE_TYPE_IMAGE_PICK = "choose"
        const val CHOOSE_TYPE_VIDEO_PICK = "choose_video"
        const val CHOOSE_TYPE_DOCUMENT_PICK = "choose_file"
        const val CHOOSE_TYPE_CAPTURE_IMAGE = "capture_image"
        const val CHOOSE_TYPE_CAPTURE_VIDEO = "capture_video"
        const val FOR_MESSAGE = "message"
        const val FOR_PROFILE = "profile"

        const val BUFFER_SIZE_AUDIO = 20 * 1024
        const val BUFFER_SIZE_VIDEO = 256 * 1024
        const val BUFFER_SIZE_IMAGE = 256 * 1024

        const val EXTRA_PICK_TYPE = "pickType"
        const val EXTRA_FILE_CONTEXT = "fileContext"
        const val EXTRA_MEDIA_TYPE = "mediaType"
        const val EXTRA_DOCUMENT_MIME = "documentMime"
        const val EXTRA_THUMBNAIL_FILE_PATH = "thumbnailFilePath"
        const val EXTRA_FILE_NAME = "fileName"
        const val EXTRA_FILE_PATH = "filePath"
        const val EXTRA_FILE_TYPE = "fileType"
        const val EXTRA_ACTION = "action"
        const val EXTRA_FILE_EXT = "fileExtension"
        const val EXTRA_FILE_ID = "fileId"
        const val EXTRA_FILE_SIZE = "fileSize"
        const val EXTRA_LOCAL_FILE_PATH = "localFilePath"
        const val EXTRA_FILE_URI = "fileUri"
        const val EXTRA_COMPONENT_TYPE = "componentType"
        const val EXTRA_THUMBNAIL_URL = "thumbnailURL"
        const val EXTRA_ORIENTATION = "orientation"

        const val THUMBNAIL_TYPE_IMAGE = -1
        const val THUMBNAIL_TYPE_VIDEO = -2

        const val COMPONENT_COUNT = 6
        const val CAPTURE_IMAGE_BUNDLED_PERMISSION_REQUEST = 1234
        const val CAPTURE_IMAGE_CHOOSE_FILES_BUNDLED_PERMISSION_REQUEST = 1434
        const val CAPTURE_IMAGE_CHOOSE_FILES_RECORD_BUNDLED_PERMISSION_REQUEST = 3453
    }
}