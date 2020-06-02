package kore.botssdk.listener;

import kore.botssdk.models.WCalEventsTemplateModel;

public interface AnalyticsListener {
    void userAnalytics(WCalEventsTemplateModel.AnalyticsModel analyticsModel);
}

/*"entity": "Meeting",
        "entityId": "<meeting-id>",
        "action": "DAILIN"*/
