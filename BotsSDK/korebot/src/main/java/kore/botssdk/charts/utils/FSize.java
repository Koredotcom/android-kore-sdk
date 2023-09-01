package kore.botssdk.charts.utils;

import java.util.List;

public final class FSize extends ObjectPool.Poolable {
    public float width;
    public float height;
    private static final ObjectPool<FSize> pool = ObjectPool.create(256, new FSize(0.0F, 0.0F));

    protected ObjectPool.Poolable instantiate() {
        return new FSize(0.0F, 0.0F);
    }

    public static FSize getInstance(float width, float height) {
        FSize result = pool.get();
        result.width = width;
        result.height = height;
        return result;
    }

    public static void recycleInstance(FSize instance) {
        pool.recycle(instance);
    }

    public static void recycleInstances(List<FSize> instances) {
        pool.recycle(instances);
    }

    public FSize() {
    }

    public FSize(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (this == obj) {
            return true;
        } else if (!(obj instanceof FSize)) {
            return false;
        } else {
            FSize other = (FSize)obj;
            return this.width == other.width && this.height == other.height;
        }
    }

    public String toString() {
        return this.width + "x" + this.height;
    }

    public int hashCode() {
        return Float.floatToIntBits(this.width) ^ Float.floatToIntBits(this.height);
    }

    static {
        pool.setReplenishPercentage(0.5F);
    }
}

