package com.kore.ai.widgetsdk.models;

public class ButtonLayoutModel {

    private DisplayLimit displayLimit;
    private String style = "float";

    public DisplayLimit getDisplayLimit() {
        return displayLimit;
    }

    public void setDisplayLimit(DisplayLimit displayLimit) {
        this.displayLimit = displayLimit;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public class DisplayLimit {

        private String count;

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

    }

}
