package kore.botssdk.listener;

import android.os.Bundle;

import java.util.HashMap;

import kore.botssdk.models.BotCaourselButtonModel;

public interface VerticalListViewActionHelper {
    void knowledgeItemClicked(Bundle extras);
    void driveItemClicked(BotCaourselButtonModel botCaourselButtonModel);
    void emailItemClicked(String action, HashMap customData);
    void tasksSelectedOrDeselected(boolean selecetd);

}
