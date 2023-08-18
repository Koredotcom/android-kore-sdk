package kore.botssdk.models;

import java.io.Serializable;

public class ResultsViewTemplate implements Serializable
{
    private String _id;
    private String type;
    private String lModifiedOn;
    private String createdOn;
    private String createdBy;
    private String streamId;
    private String searchIndexId;
    private String indexPipelineId;
    private ResultsViewlayout layout;
    private ResultsViewMapping mapping;

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public void setIndexPipelineId(String indexPipelineId) {
        this.indexPipelineId = indexPipelineId;
    }

    public void setLayout(ResultsViewlayout layout) {
        this.layout = layout;
    }

    public void setlModifiedOn(String lModifiedOn) {
        this.lModifiedOn = lModifiedOn;
    }

    public void setMapping(ResultsViewMapping mapping) {
        this.mapping = mapping;
    }

    public void setSearchIndexId(String searchIndexId) {
        this.searchIndexId = searchIndexId;
    }

    public void setStreamId(String streamId) {
        this.streamId = streamId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ResultsViewlayout getLayout() {
        return layout;
    }

    public ResultsViewMapping getMapping() {
        return mapping;
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

    public String getlModifiedOn() {
        return lModifiedOn;
    }

    public String getSearchIndexId() {
        return searchIndexId;
    }

    public String getStreamId() {
        return streamId;
    }

    public String getType() {
        return type;
    }
}
