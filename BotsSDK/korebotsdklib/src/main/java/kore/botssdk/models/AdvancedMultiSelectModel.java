package kore.botssdk.models;

import android.annotation.SuppressLint;

import java.util.ArrayList;

@SuppressLint("UnknownNullness")
public class AdvancedMultiSelectModel
{
    private final String collectionTitle;
    private final ArrayList<AdvanceMultiSelectCollectionModel> collection;

    public AdvancedMultiSelectModel(String collectionTitle, ArrayList<AdvanceMultiSelectCollectionModel> collection) {
        this.collectionTitle = collectionTitle;
        this.collection = collection;
    }

    public ArrayList<AdvanceMultiSelectCollectionModel> getCollection() {
        return collection;
    }

    public String getCollectionTitle() {
        return collectionTitle;
    }
}
