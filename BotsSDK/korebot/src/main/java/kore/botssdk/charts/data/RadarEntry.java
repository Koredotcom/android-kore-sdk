package kore.botssdk.charts.data;


import android.annotation.SuppressLint;

@SuppressLint({"ParcelCreator"})
public class RadarEntry extends Entry {
    public RadarEntry(float value) {
        super(0.0F, value);
    }

    public RadarEntry(float value, Object data) {
        super(0.0F, value, data);
    }

    public float getValue() {
        return this.getY();
    }

    public RadarEntry copy() {
        RadarEntry e = new RadarEntry(this.getY(), this.getData());
        return e;
    }

    /** @deprecated */
    @Deprecated
    public void setX(float x) {
        super.setX(x);
    }

    /** @deprecated */
    @Deprecated
    public float getX() {
        return super.getX();
    }
}
