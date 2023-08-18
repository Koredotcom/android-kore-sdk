package kore.botssdk.charts.model;

public class GradientColor {
    private int startColor;
    private int endColor;

    public GradientColor(int startColor, int endColor) {
        this.startColor = startColor;
        this.endColor = endColor;
    }

    public int getStartColor() {
        return this.startColor;
    }

    public void setStartColor(int startColor) {
        this.startColor = startColor;
    }

    public int getEndColor() {
        return this.endColor;
    }

    public void setEndColor(int endColor) {
        this.endColor = endColor;
    }
}
