package com.kore.findlysdk.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class ResultsViewSetting implements Serializable
{
    private String _id;
    private FacetSettingsModel facetsSetting;
    private String view;
    private boolean groupResults;
    @SerializedName("interface")
    @Expose
    private String myInterface;
    private GroupSettingsModel groupSetting;
    private String defaultTemplateId;
    private String streamId;
    private String searchIndexId;
    private String indexPipelineId;
    private String createdBy;
    private String createdOn;
    private String lModifiedOn;
    private String __v;
    private ResultsViewTemplate defaultTemplate;

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void set__v(String __v) {
        this.__v = __v;
    }

    public void setDefaultTemplate(ResultsViewTemplate defaultTemplate) {
        this.defaultTemplate = defaultTemplate;
    }

    public void setDefaultTemplateId(String defaultTemplateId) {
        this.defaultTemplateId = defaultTemplateId;
    }

    public void setFacetsSetting(FacetSettingsModel facetsSetting) {
        this.facetsSetting = facetsSetting;
    }

    public void setGroupResults(boolean groupResults) {
        this.groupResults = groupResults;
    }

    public void setGroupSetting(GroupSettingsModel groupSetting) {
        this.groupSetting = groupSetting;
    }

    public void setlModifiedOn(String lModifiedOn) {
        this.lModifiedOn = lModifiedOn;
    }

    public void setMyInterface(String myInterface) {
        this.myInterface = myInterface;
    }

    public FacetSettingsModel getFacetsSetting() {
        return facetsSetting;
    }

    public GroupSettingsModel getGroupSetting() {
        return groupSetting;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public ResultsViewTemplate getDefaultTemplate() {
        return defaultTemplate;
    }

    public String get__v() {
        return __v;
    }

    public String getDefaultTemplateId() {
        return defaultTemplateId;
    }

    public String getlModifiedOn() {
        return lModifiedOn;
    }

    public String getMyInterface() {
        return myInterface;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    public void setSearchIndexId(String searchIndexId) {
        this.searchIndexId = searchIndexId;
    }

    public void setIndexPipelineId(String indexPipelineId) {
        this.indexPipelineId = indexPipelineId;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setInterfaces(String interfaces) {
        this.myInterface = interfaces;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getStreamId() {
        return streamId;
    }

    public String getSearchIndexId() {
        return searchIndexId;
    }

    public String getIndexPipelineId() {
        return indexPipelineId;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public String get_id() {
        return _id;
    }

    public String getView() {
        return view;
    }

    public class FacetSettingsModel implements Serializable
    {
        private boolean enabled;
        private String aligned;

        public String getAligned() {
            return aligned;
        }

        public void setAligned(String aligned) {
            this.aligned = aligned;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public boolean getEnabled()
        {
            return enabled;
        }
    }

    public class GroupSettingsModel implements Serializable
    {
        private String fieldId;
        private ArrayList<ResultsViewAppearance> conditions;
        private String fieldName;

        public String getFieldId() {
            return fieldId;
        }

        public ArrayList<ResultsViewAppearance> getConditions() {
            return conditions;
        }

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldId(String fieldId) {
            this.fieldId = fieldId;
        }

        public void setConditions(ArrayList<ResultsViewAppearance> conditions) {
            this.conditions = conditions;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }
    }
}
