package kore.botssdk.listener;

import java.util.ArrayList;

import kore.botssdk.models.AdvanceOptionsModel;

public interface AdvanceButtonClickListner
{
    void advanceButtonClick(ArrayList<AdvanceOptionsModel> viewType);

    void closeWindow();
}
