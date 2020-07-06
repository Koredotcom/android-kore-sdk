package com.kore.ai.widgetsdk.events;

/**
 * Created by amit on 7/10/2015.
 */
public interface ComponentPlaybackEvents {

    class UpdateDownloadProgress {

        private String fileName;
        private int percentage;

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public int getPercentage() {
            return percentage;
        }

        public void setPercentage(int percentage) {
            this.percentage = percentage;
        }
    }

    class EnableDisableMediaDownload {

        private String fileName;

        private boolean isEnable;

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public boolean isEnable() {
            return isEnable;
        }

        public void setEnable(boolean isEnable) {
            this.isEnable = isEnable;
        }

    }

    class ShowHideHeaderFooter {
    }

    class StopPlayback {

    }

    class UpdateResourceId {
        private String resourceId;

        public String getResourceId() {
            return resourceId;
        }

        public void setResourceId(String resourceId) {
            this.resourceId = resourceId;
        }
    }

    class DownloadBegin {

    }

    class DownloadAborted {

    }

    class DownloadComplete {

        String mediaType;
        String downloadedFilePath;
        String componentId;
        String status;

        public String getMediaType() {
            return mediaType;
        }

        public void setMediaType(String mediaType) {
            this.mediaType = mediaType;
        }

        public String getDownloadedFilePath() {
            return downloadedFilePath;
        }

        public void setDownloadedFilePath(String downloadedFilePath) {
            this.downloadedFilePath = downloadedFilePath;
        }

        public String getComponentId() {
            return componentId;
        }

        public void setComponentId(String componentId) {
            this.componentId = componentId;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
