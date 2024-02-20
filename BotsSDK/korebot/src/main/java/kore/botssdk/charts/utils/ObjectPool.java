package kore.botssdk.charts.utils;

import java.util.List;

public class ObjectPool<T extends ObjectPool.Poolable> {
    private static int ids = 0;
    private int poolId;
    private int desiredCapacity;
    private Object[] objects;
    private int objectsPointer;
    private final T modelObject;
    private float replenishPercentage;

    public int getPoolId() {
        return this.poolId;
    }

    public static synchronized ObjectPool create(int withCapacity, ObjectPool.Poolable object) {
        ObjectPool result = new ObjectPool(withCapacity, object);
        result.poolId = ids++;
        return result;
    }

    private ObjectPool(int withCapacity, T object) {
        if (withCapacity <= 0) {
            throw new IllegalArgumentException("Object Pool must be instantiated with a capacity greater than 0!");
        } else {
            this.desiredCapacity = withCapacity;
            this.objects = new Object[this.desiredCapacity];
            this.objectsPointer = 0;
            this.modelObject = object;
            this.replenishPercentage = 1.0F;
            this.refillPool();
        }
    }

    public void setReplenishPercentage(float percentage) {
        float p = percentage;
        if (percentage > 1.0F) {
            p = 1.0F;
        } else if (percentage < 0.0F) {
            p = 0.0F;
        }

        this.replenishPercentage = p;
    }

    public float getReplenishPercentage() {
        return this.replenishPercentage;
    }

    private void refillPool() {
        this.refillPool(this.replenishPercentage);
    }

    private void refillPool(float percentage) {
        int portionOfCapacity = (int)((float)this.desiredCapacity * percentage);
        if (portionOfCapacity < 1) {
            portionOfCapacity = 1;
        } else if (portionOfCapacity > this.desiredCapacity) {
            portionOfCapacity = this.desiredCapacity;
        }

        for(int i = 0; i < portionOfCapacity; ++i) {
            this.objects[i] = this.modelObject.instantiate();
        }

        this.objectsPointer = portionOfCapacity - 1;
    }

    public synchronized T get() {
        if (this.objectsPointer == -1 && this.replenishPercentage > 0.0F) {
            this.refillPool();
        }

        T result = (T) this.objects[this.objectsPointer];
        result.currentOwnerId = ObjectPool.Poolable.NO_OWNER;
        --this.objectsPointer;
        return result;
    }

    public synchronized void recycle(T object) {
        if (object.currentOwnerId != ObjectPool.Poolable.NO_OWNER) {
            if (object.currentOwnerId == this.poolId) {
                throw new IllegalArgumentException("The object passed is already stored in this pool!");
            } else {
                throw new IllegalArgumentException("The object to recycle already belongs to poolId " + object.currentOwnerId + ".  Object cannot belong to two different pool instances simultaneously!");
            }
        } else {
            ++this.objectsPointer;
            if (this.objectsPointer >= this.objects.length) {
                this.resizePool();
            }

            object.currentOwnerId = this.poolId;
            this.objects[this.objectsPointer] = object;
        }
    }

    public synchronized void recycle(List<T> objects) {
        while(objects.size() + this.objectsPointer + 1 > this.desiredCapacity) {
            this.resizePool();
        }

        int objectsListSize = objects.size();

        for(int i = 0; i < objectsListSize; ++i) {
            T object = objects.get(i);
            if (object.currentOwnerId != ObjectPool.Poolable.NO_OWNER) {
                if (object.currentOwnerId == this.poolId) {
                    throw new IllegalArgumentException("The object passed is already stored in this pool!");
                }

                throw new IllegalArgumentException("The object to recycle already belongs to poolId " + object.currentOwnerId + ".  Object cannot belong to two different pool instances simultaneously!");
            }

            object.currentOwnerId = this.poolId;
            this.objects[this.objectsPointer + 1 + i] = object;
        }

        this.objectsPointer += objectsListSize;
    }

    private void resizePool() {
        int oldCapacity = this.desiredCapacity;
        this.desiredCapacity *= 2;
        Object[] temp = new Object[this.desiredCapacity];

        if (oldCapacity >= 0) System.arraycopy(this.objects, 0, temp, 0, oldCapacity);

        this.objects = temp;
    }

    public int getPoolCapacity() {
        return this.objects.length;
    }

    public int getPoolCount() {
        return this.objectsPointer + 1;
    }

    public abstract static class Poolable {
        public static final int NO_OWNER = -1;
        int currentOwnerId;

        public Poolable() {
            this.currentOwnerId = NO_OWNER;
        }

        protected abstract ObjectPool.Poolable instantiate();
    }
}
