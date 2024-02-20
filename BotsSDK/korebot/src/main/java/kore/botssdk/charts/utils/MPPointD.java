package kore.botssdk.charts.utils;

import java.util.List;

public class MPPointD extends ObjectPool.Poolable {
    private static final ObjectPool<MPPointD> pool = ObjectPool.create(64, new MPPointD(0.0D, 0.0D));
    public double x;
    public double y;

    public static MPPointD getInstance(double x, double y) {
        MPPointD result = pool.get();
        result.x = x;
        result.y = y;
        return result;
    }

    public static void recycleInstance(MPPointD instance) {
        pool.recycle(instance);
    }

    public static void recycleInstances(List<MPPointD> instances) {
        pool.recycle(instances);
    }

    protected ObjectPool.Poolable instantiate() {
        return new MPPointD(0.0D, 0.0D);
    }

    private MPPointD(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return "MPPointD, x: " + this.x + ", y: " + this.y;
    }

    static {
        pool.setReplenishPercentage(0.5F);
    }
}
