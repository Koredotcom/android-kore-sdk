package com.kore.ai.widgetsdk.events;

/**
 * Created by amit on 7/13/2015.
 */
public class ComponentPlaybackPublisher {

    public static void publishDownloadBeginEvent() {
        ComponentPlaybackEvents.DownloadBegin event = new ComponentPlaybackEvents.DownloadBegin();
        KoreEventCenter.post(event);
    }

    public static void publishDownloadAbortedEvent() {
        ComponentPlaybackEvents.DownloadAborted event = new ComponentPlaybackEvents.DownloadAborted();
        KoreEventCenter.post(event);
    }

    public static void publishDownloadCompleteEvent(String mediaType, String downloadedFilePath, String componentId, String status) {
        ComponentPlaybackEvents.DownloadComplete event = new ComponentPlaybackEvents.DownloadComplete();
        event.setMediaType(mediaType);
        event.setDownloadedFilePath(downloadedFilePath);
        event.setComponentId(componentId);
        event.setStatus(status);
        KoreEventCenter.post(event);
    }

    public static void publishUpdateDownloadProgressEvent(String fileName, int percentage) {
        ComponentPlaybackEvents.UpdateDownloadProgress updateDownloadProgressEvent = new ComponentPlaybackEvents.UpdateDownloadProgress();
        updateDownloadProgressEvent.setFileName(fileName);
        updateDownloadProgressEvent.setPercentage(percentage);
        KoreEventCenter.post(updateDownloadProgressEvent);
    }

    public static void publishEnableDisableMediaDownloadEvent(String fileName, boolean isEnable) {
        ComponentPlaybackEvents.EnableDisableMediaDownload enableDisableMediaDownloadEvent = new ComponentPlaybackEvents.EnableDisableMediaDownload();
        enableDisableMediaDownloadEvent.setFileName(fileName);
        enableDisableMediaDownloadEvent.setEnable(isEnable);
        KoreEventCenter.post(enableDisableMediaDownloadEvent);
    }

    public static void publishShowHideHeaderFooterEvent() {
        ComponentPlaybackEvents.ShowHideHeaderFooter showHideHeaderFooterEvent = new ComponentPlaybackEvents.ShowHideHeaderFooter();
        KoreEventCenter.post(showHideHeaderFooterEvent);
    }

    public static void publishStopPlaybackEvent() {
        ComponentPlaybackEvents.StopPlayback stopPlaybackEvent = new ComponentPlaybackEvents.StopPlayback();
        KoreEventCenter.post(stopPlaybackEvent);
    }

    public static void publishUpdateResourceIdEvent(String resourceId) {
        ComponentPlaybackEvents.UpdateResourceId updateResourceIdEvent = new ComponentPlaybackEvents.UpdateResourceId();
        updateResourceIdEvent.setResourceId(resourceId);
        KoreEventCenter.post(updateResourceIdEvent);
    }

}
