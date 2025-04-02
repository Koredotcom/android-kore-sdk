package kore.botssdk.models;

import java.io.Serializable;
import java.util.List;

public class TagsModel implements Serializable {
    private List<AlternateTextModel> altText;

    public List<AlternateTextModel> getAltText() {
        return altText;
    }

    public void setAltText(List<AlternateTextModel> altText) {
        this.altText = altText;
    }
}
