package kore.botssdk.models;

import java.util.List;

public class BaseChartModel {

    private List<ChartElementModel> elements = null;
    private boolean hasMore;
    private String placeholder;



    public List<ChartElementModel> getElements() {
        return elements;
    }

    public void setElements(List<ChartElementModel> elements) {
        this.elements = elements;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

}
