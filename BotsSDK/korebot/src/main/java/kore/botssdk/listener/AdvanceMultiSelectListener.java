package kore.botssdk.listener;

import android.annotation.SuppressLint;

import java.util.ArrayList;

import kore.botssdk.models.AdvanceMultiSelectCollectionModel;

@SuppressLint("UnknownNullness")
public interface AdvanceMultiSelectListener {
    public void itemSelected(AdvanceMultiSelectCollectionModel checkedItems);

    public void allItemsSelected(boolean addRemove, ArrayList<AdvanceMultiSelectCollectionModel> allCheckedItems);
}
