package kore.botssdk.listener;

import android.annotation.SuppressLint;

import java.util.ArrayList;

import kore.botssdk.models.AdvanceMultiSelectCollectionModel;

@SuppressLint("UnknownNullness")
public interface MultiSelectAllListener {
    public void isSelectAll(boolean selectAll, ArrayList<AdvanceMultiSelectCollectionModel> checkedItems);
}
