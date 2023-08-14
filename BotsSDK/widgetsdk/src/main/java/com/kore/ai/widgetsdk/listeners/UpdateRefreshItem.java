package com.kore.ai.widgetsdk.listeners;

public interface UpdateRefreshItem {
    void updateItemToRefresh(int pos);
    void updateWeatherWidgetSummery(int type, String summary);
    void onWidgetMenuButtonClicked();
}
