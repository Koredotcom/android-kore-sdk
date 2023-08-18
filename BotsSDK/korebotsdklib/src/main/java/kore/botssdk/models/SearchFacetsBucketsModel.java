package kore.botssdk.models;

public class SearchFacetsBucketsModel
{
    private String key;
    private int doc_count;
    private String fieldName;
    private String name;
    private boolean isChecked = false;

    public int getDoc_count() {
        return doc_count;
    }

    public String getKey() {
        return key;
    }

    public void setDoc_count(int doc_count) {
        this.doc_count = doc_count;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean getChecked()
    {
        return isChecked;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
