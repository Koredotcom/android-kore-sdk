package kore.botssdk.models;

public class FreemiumData {
    public String message,/* rightBtnStr, leftBtnStr,*/timeStr;
    public final FreemiumType freemiumType;
    private boolean isAppendingNewText;

    public FreemiumData( FreemiumType freemiumType) {
        this.freemiumType = freemiumType;
    }

    public boolean isAppendingNewText() {
        return isAppendingNewText;
    }

    public void setAppendingNewText(boolean appendingNewText) {
        isAppendingNewText = appendingNewText;
    }
}
