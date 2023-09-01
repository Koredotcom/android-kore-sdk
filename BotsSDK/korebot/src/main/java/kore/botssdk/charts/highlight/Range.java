package kore.botssdk.charts.highlight;

public final class Range {
    public final float from;
    public final float to;

    public Range(float from, float to) {
        this.from = from;
        this.to = to;
    }

    public boolean contains(float value) {
        return value > this.from && value <= this.to;
    }

    public boolean isLarger(float value) {
        return value > this.to;
    }

    public boolean isSmaller(float value) {
        return value < this.from;
    }
}
