package com.kore.ai.widgetsdk.listeners;

public interface UpdateRefreshItem {
    public void updateItemToRefresh(int pos);
    public void updateWeatherWidgetSummery(int type, String summary);
    public void onWidgetMenuButtonClicked();
}
