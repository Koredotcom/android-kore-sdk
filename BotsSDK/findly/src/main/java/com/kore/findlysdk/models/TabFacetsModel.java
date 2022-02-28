package com.kore.findlysdk.models;

import java.util.ArrayList;

public class TabFacetsModel
{
    private String _id;
    private boolean multiselect;
    private String active;
    private String showFieldWarning;
    private String searchIndexId;
    private String indexPipelineId;
    private String queryPipelineId;
    private String streamId;
    private String createdBy;
    private String createdOn;
    private String type;
    private String fieldId;
    private ArrayList<TabsModel> tabs;
    private String name;
    private String lMod;
    private int __v;

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public ArrayList<TabsModel> getTabs() {
        return tabs;
    }

    public int get__v() {
        return __v;
    }

    public String get_id() {
        return _id;
    }

    public String getActive() {
        return active;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getFieldId() {
        return fieldId;
    }

    public String getIndexPipelineId() {
        return indexPipelineId;
    }

    public String getlMod() {
        return lMod;
    }

    public String getQueryPipelineId() {
        return queryPipelineId;
    }

    public String getSearchIndexId() {
        return searchIndexId;
    }

    public String getShowFieldWarning() {
        return showFieldWarning;
    }

    public String getStreamId() {
        return streamId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public void set__v(int __v) {
        this.__v = __v;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public void setIndexPipelineId(String indexPipelineId) {
        this.indexPipelineId = indexPipelineId;
    }

    public void setlMod(String lMod) {
        this.lMod = lMod;
    }

    public void setMultiselect(boolean multiselect) {
        this.multiselect = multiselect;
    }

    public void setQueryPipelineId(String queryPipelineId) {
        this.queryPipelineId = queryPipelineId;
    }

    public void setSearchIndexId(String searchIndexId) {
        this.searchIndexId = searchIndexId;
    }

    public void setShowFieldWarning(String showFieldWarning) {
        this.showFieldWarning = showFieldWarning;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    public void setTabs(ArrayList<TabsModel> tabs) {
        this.tabs = tabs;
    }

    public class TabsModel
    {
        private String fieldValue;
        private String bucketName;
        private int bucketCount;

        public void setBucketName(String bucketName) {
            this.bucketName = bucketName;
        }

        public String getBucketName() {
            return bucketName;
        }

        public void setFieldValue(String fieldValue) {
            this.fieldValue = fieldValue;
        }

        public String getFieldValue() {
            return fieldValue;
        }

        public void setBucketCount(int bucketCount) {
            this.bucketCount = bucketCount;
        }

        public int getBucketCount() {
            return bucketCount;
        }
    }
}
