package kore.botssdk.charts.utils;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class MPPointF extends ObjectPool.Poolable {
    private static final ObjectPool<kore.botssdk.charts.utils.MPPointF> pool = ObjectPool.create(32, new MPPointF(0.0F, 0.0F));
    public float x;
    public float y;
    public static final Parcelable.Creator<MPPointF> CREATOR;

    public MPPointF() {
    }

    public MPPointF(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public static MPPointF getInstance(float x, float y) {
        MPPointF result = pool.get();
        result.x = x;
        result.y = y;
        return result;
    }

    public static MPPointF getInstance() {
        return pool.get();
    }

    public static MPPointF getInstance(MPPointF copy) {
        MPPointF result = pool.get();
        result.x = copy.x;
        result.y = copy.y;
        return result;
    }

    public static void recycleInstance(MPPointF instance) {
        pool.recycle(instance);
    }

    public static void recycleInstances(List<MPPointF> instances) {
        pool.recycle(instances);
    }

    public void my_readFromParcel(Parcel in) {
        this.x = in.readFloat();
        this.y = in.readFloat();
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    protected ObjectPool.Poolable instantiate() {
        return new MPPointF(0.0F, 0.0F);
    }

    static {
        pool.setReplenishPercentage(0.5F);
        CREATOR = new Parcelable.Creator<MPPointF>() {
            public MPPointF createFromParcel(Parcel in) {
                MPPointF r = new MPPointF(0.0F, 0.0F);
                r.my_readFromParcel(in);
                return r;
            }

            public MPPointF[] newArray(int size) {
                return new MPPointF[size];
            }
        };
    }
}
