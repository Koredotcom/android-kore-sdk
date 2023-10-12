package kore.botssdk.models;

import android.annotation.SuppressLint;
@SuppressLint("UnknownNullness")
public class AdvanceMultiSelectCollectionModel
{
    private final String title;
    private final String description;
    private final String value;
    private final String image_url;

    public AdvanceMultiSelectCollectionModel(String title, String description, String value, String image_url, boolean isSelected) {
        this.title = title;
        this.description = description;
        this.value = value;
        this.image_url = image_url;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getValue() {
        return value;
    }

    public String getImage_url() {
        return image_url;
    }
}
