package kore.botssdk.listener;

import android.content.Context;
import android.os.Bundle;

import java.util.HashMap;

import kore.botssdk.models.BaseCalenderTemplateModel;
import kore.botssdk.models.BotCaourselButtonModel;
import kore.botssdk.models.ContactViewListModel;
import kore.botssdk.models.KnowledgeCollectionModel;
import kore.botssdk.models.WelcomeChatSummaryModel;

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
