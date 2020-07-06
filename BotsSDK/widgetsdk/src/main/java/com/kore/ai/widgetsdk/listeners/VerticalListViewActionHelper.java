package com.kore.ai.widgetsdk.listeners;

import android.content.Context;
import android.os.Bundle;

import com.kore.ai.widgetsdk.models.BaseCalenderTemplateModel;
import com.kore.ai.widgetsdk.models.BotCaourselButtonModel;
import com.kore.ai.widgetsdk.models.ContactViewListModel;
import com.kore.ai.widgetsdk.models.KnowledgeCollectionModel;
import com.kore.ai.widgetsdk.models.WelcomeChatSummaryModel;

import java.util.HashMap;

public interface VerticalListViewActionHelper {
    void knowledgeItemClicked(Bundle extras, boolean isKnowledge);
    void driveItemClicked(BotCaourselButtonModel botCaourselButtonModel);
    void emailItemClicked(String action, HashMap customData);
    void calendarItemClicked(String action, BaseCalenderTemplateModel model);
    void tasksSelectedOrDeselected(boolean selecetd);
    void widgetItemSelected(boolean isSelected, int count);
    void navigationToDialAndJoin(String actiontype, String actionLink);
    void takeNotesNavigation(BaseCalenderTemplateModel baseCalenderTemplateModel);
    void meetingNotesNavigation(Context context, String mId, String eId);
    void meetingWidgetViewMoreVisibility(boolean visible);
    void calendarContactItemClick(ContactViewListModel model);
    void welcomeSummaryItemClick(WelcomeChatSummaryModel model);
    void knowledgeCollectionItemClick(KnowledgeCollectionModel.DataElements elements, String id);
}
