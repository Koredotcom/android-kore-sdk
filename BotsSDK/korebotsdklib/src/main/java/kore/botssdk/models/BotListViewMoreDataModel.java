package kore.botssdk.models;

import java.util.ArrayList;

public class BotListViewMoreDataModel
{
    private ArrayList<BotListModel> Tab1;
    private ArrayList<BotListModel> Tab2;

    public void setTab1(ArrayList<BotListModel> Tab1)
    {
        this.Tab1 = Tab1;
    }

    public ArrayList<BotListModel> getTab1()
    {
        return Tab1;
    }

    public void setTab2(ArrayList<BotListModel> Tab2)
    {
        this.Tab2 = Tab2;
    }

    public ArrayList<BotListModel> getTab2()
    {
        return Tab2;
    }
}
