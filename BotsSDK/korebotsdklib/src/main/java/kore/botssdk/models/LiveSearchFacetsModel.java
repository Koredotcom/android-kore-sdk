package kore.botssdk.models;

import java.util.ArrayList;

public class LiveSearchFacetsModel
{
    private ArrayList<SearchFacetsBucketsModel> buckets;
    private String fieldName;
    private boolean multiselect;
    private String name;
    private String subtype;

    public String getFieldName() {
        return fieldName;
    }

    public String getName() {
        return name;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void setMultiselect(boolean multiselect) {
        this.multiselect = multiselect;
    }

    public boolean getMultiSelect()
    {
        return multiselect;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public ArrayList<SearchFacetsBucketsModel> getBuckets() {
        return buckets;
    }

    public void setBuckets(ArrayList<SearchFacetsBucketsModel> buckets) {
        this.buckets = buckets;
    }

    public class BucketsModel
    {
        private String key;
        private int doc_count;
        private boolean isChecked = false;

        public void setDoc_count(int doc_count) {
            this.doc_count = doc_count;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public int getDoc_count() {
            return doc_count;
        }

        public String getKey() {
            return key;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public boolean getChecked()
        {
            return isChecked;
        }
    }
}
