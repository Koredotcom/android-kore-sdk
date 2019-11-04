
package kore.botssdk.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class KnowledgeDetailModelResponseNew extends WidgetBaseDataModel{

    @SerializedName("buttons")
    @Expose
    private List<Button> buttons = null;

    @SerializedName("elements")
    @Expose
    private List<KnowledgeDetailModel> elements = null;


    public List<Button> getButtons() {
        return buttons;
    }

    public void setButtons(List<Button> buttons) {
        this.buttons = buttons;
    }

    public int getPreviewLength() {
        return preview_length;
    }

    public void setPreviewLength(int preview_length) {
        this.preview_length = preview_length;
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

    @Override
    protected void setWidgetType(int WIDGET_TYPE) {
    }


}
