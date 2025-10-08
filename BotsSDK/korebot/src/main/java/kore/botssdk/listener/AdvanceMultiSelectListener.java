package kore.botssdk.listener;

import android.annotation.SuppressLint;

import java.util.ArrayList;

import kore.botssdk.models.AdvanceMultiSelectCollectionModel;

@SuppressLint("UnknownNullness")
public interface AdvanceMultiSelectListener {
    void itemSelected(AdvanceMultiSelectCollectionModel checkedItems);
    void allItemsSelected(boolean addRemove, ArrayList<AdvanceMultiSelectCollectionModel> allCheckedItems);
}
