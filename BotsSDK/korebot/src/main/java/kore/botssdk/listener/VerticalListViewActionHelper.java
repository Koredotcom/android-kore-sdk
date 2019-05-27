package kore.botssdk.listener;

import android.os.Bundle;

import java.util.HashMap;

import kore.botssdk.models.BaseCalenderTemplateModel;
import kore.botssdk.models.BotCaourselButtonModel;

public interface VerticalListViewActionHelper {
    void knowledgeItemClicked(Bundle extras,boolean isKnowledge);
    void driveItemClicked(BotCaourselButtonModel botCaourselButtonModel);
    void emailItemClicked(String action, HashMap customData);
    void calendarItemClicked(String action, BaseCalenderTemplateModel model);
    void tasksSelectedOrDeselected(boolean selecetd);
    void widgetItemSelected(boolean isSelected,int count);
    void navigationToDialAndJoin(String actiontype,String actionLink);

}
