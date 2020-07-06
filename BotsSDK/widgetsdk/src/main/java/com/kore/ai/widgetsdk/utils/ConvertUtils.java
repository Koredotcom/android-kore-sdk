package com.kore.ai.widgetsdk.utils;

import com.kore.ai.widgetsdk.models.BotResponse;
import com.kore.ai.widgetsdk.models.KnowledgeDetailModel;
import com.kore.ai.widgetsdk.models.PayloadInner;
import com.kore.ai.widgetsdk.models.PayloadOuter;

import java.util.ArrayList;

/**
 * Created by Ramachandra Pradeep on 04-Dec-18.
 */

public class ConvertUtils {
    public static BotResponse convertToCarousalModal(BotResponse botResponse, ArrayList<KnowledgeDetailModel> knowledgeDetailModels) {
        PayloadOuter payloadOuter = botResponse.getMessage().get(0).getComponent().getPayload();
        payloadOuter.setType("template");
        PayloadInner payloadInner = new PayloadInner();
        payloadInner.setTemplate_type(BotResponse.TEMPLATE_TYPE_KORA_CAROUSAL);
        payloadInner.setElements(knowledgeDetailModels);
        payloadOuter.setPayload(payloadInner);
        payloadOuter.setText("");
        botResponse.getMessage().get(0).getComponent().setPayload(payloadOuter);
//        KoraSocketConnectionManager.getInstance().persistBotMessage(new Gson().toJson(botResponse), false, null);
        return botResponse;
    }
}
