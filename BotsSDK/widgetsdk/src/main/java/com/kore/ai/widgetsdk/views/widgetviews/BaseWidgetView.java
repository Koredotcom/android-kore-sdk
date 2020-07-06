package com.kore.ai.widgetsdk.views.widgetviews;

import android.content.Context;
import android.view.ViewGroup;

import com.kore.ai.widgetsdk.application.AppControl;
import com.kore.ai.widgetsdk.listeners.VerticalListViewActionHelper;
import com.kore.ai.widgetsdk.models.KnowledgeCollectionModel;
import com.kore.ai.widgetsdk.utils.Utility;

public abstract class BaseWidgetView extends ViewGroup implements VerticalListViewActionHelper {

    protected float dp1;
//    protected final int viewHeight;
    public BaseWidgetView(Context context) {
        super(context);
        dp1 = (int) Utility.convertDpToPixel(context, 1);
//        viewHeight = (int)(277*dp1);
    }

    @Override
    public void knowledgeCollectionItemClick(KnowledgeCollectionModel.DataElements elements, String id) {

    }
}
