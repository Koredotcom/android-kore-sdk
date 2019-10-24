
package kore.botssdk.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class KnowledgeDetailModelResponseNew {

    @SerializedName("buttons")
    @Expose
    private List<Button> buttons = null;
    @SerializedName("preview_length")
    @Expose
    private Long previewLength;
    @SerializedName("hasMore")
    @Expose
    private Boolean hasMore;
    @SerializedName("elements")
    @Expose
    private List<KnowledgeDetailModel> elements = null;
    @SerializedName("placeholder")
    @Expose
    private String placeholder;

    public List<Button> getButtons() {
        return buttons;
    }

    public void setButtons(List<Button> buttons) {
        this.buttons = buttons;
    }

    public Long getPreviewLength() {
        return previewLength;
    }

    public void setPreviewLength(Long previewLength) {
        this.previewLength = previewLength;
    }

    public Boolean getHasMore() {
        return hasMore;
    }

    public void setHasMore(Boolean hasMore) {
        this.hasMore = hasMore;
    }

    public List<KnowledgeDetailModel> getElements() {
        return elements;
    }

    public void setElements(List<KnowledgeDetailModel> elements) {
        this.elements = elements;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public class Button {

        @SerializedName("template_type")
        @Expose
        private String templateType;
        @SerializedName("action_type")
        @Expose
        private String actionType;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("hasMore")
        @Expose
        private Boolean hasMore;

        public String getTemplateType() {
            return templateType;
        }

        public void setTemplateType(String templateType) {
            this.templateType = templateType;
        }

        public String getActionType() {
            return actionType;
        }

        public void setActionType(String actionType) {
            this.actionType = actionType;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Boolean getHasMore() {
            return hasMore;
        }

        public void setHasMore(Boolean hasMore) {
            this.hasMore = hasMore;
        }

    }








}
