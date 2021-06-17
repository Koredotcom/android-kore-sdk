package com.kore.findlysdk.models;

public class SearchInterfaceModel
{
    private String _id;
    private String createdBy;
    private String searchIndexId;
    private String indexPipelineId;
    private String streamId;
    private String createdOn;
    private String modifiedOn;
    private ExperienceConfigModel experienceConfig;
    private WidgetConfigModel widgetConfig;
    private InteractionsConfigModel interactionsConfig;
    private String lModifiedBy;
    private String lModifiedOn;

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public void setIndexPipelineId(String indexPipelineId) {
        this.indexPipelineId = indexPipelineId;
    }

    public void setSearchIndexId(String searchIndexId) {
        this.searchIndexId = searchIndexId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    public void setlModifiedOn(String lModifiedOn) {
        this.lModifiedOn = lModifiedOn;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setExperienceConfig(ExperienceConfigModel experienceConfig) {
        this.experienceConfig = experienceConfig;
    }

    public void setInteractionsConfig(InteractionsConfigModel interactionsConfig) {
        this.interactionsConfig = interactionsConfig;
    }

    public void setlModifiedBy(String lModifiedBy) {
        this.lModifiedBy = lModifiedBy;
    }

    public void setModifiedOn(String modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public void setWidgetConfig(WidgetConfigModel widgetConfig) {
        this.widgetConfig = widgetConfig;
    }

    public String get_id() {
        return _id;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public String getIndexPipelineId() {
        return indexPipelineId;
    }

    public String getSearchIndexId() {
        return searchIndexId;
    }

    public String getStreamId() {
        return streamId;
    }

    public String getlModifiedOn() {
        return lModifiedOn;
    }

    public ExperienceConfigModel getExperienceConfig() {
        return experienceConfig;
    }

    public InteractionsConfigModel getInteractionsConfig() {
        return interactionsConfig;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getlModifiedBy() {
        return lModifiedBy;
    }

    public String getModifiedOn() {
        return modifiedOn;
    }

    public WidgetConfigModel getWidgetConfig() {
        return widgetConfig;
    }
}
