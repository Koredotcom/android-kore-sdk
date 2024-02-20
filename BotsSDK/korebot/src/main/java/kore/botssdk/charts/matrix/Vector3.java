package kore.botssdk.charts.matrix;

public final class Vector3 {
    public float x;
    public float y;
    public float z;
    public static final Vector3 ZERO = new Vector3(0.0F, 0.0F, 0.0F);
    public static final Vector3 UNIT_X = new Vector3(1.0F, 0.0F, 0.0F);
    public static final Vector3 UNIT_Y = new Vector3(0.0F, 1.0F, 0.0F);
    public static final Vector3 UNIT_Z = new Vector3(0.0F, 0.0F, 1.0F);

    public Vector3() {
    }

    public Vector3(float[] array) {
        this.set(array[0], array[1], array[2]);
    }

    public Vector3(float xValue, float yValue, float zValue) {
        this.set(xValue, yValue, zValue);
    }

    public Vector3(Vector3 other) {
        this.set(other);
    }

    public void add(Vector3 other) {
        this.x += other.x;
        this.y += other.y;
        this.z += other.z;
    }

    public void add(float otherX, float otherY, float otherZ) {
        this.x += otherX;
        this.y += otherY;
        this.z += otherZ;
    }

    public void subtract(Vector3 other) {
        this.x -= other.x;
        this.y -= other.y;
        this.z -= other.z;
    }

    public void subtractMultiple(Vector3 other, float multiplicator) {
        this.x -= other.x * multiplicator;
        this.y -= other.y * multiplicator;
        this.z -= other.z * multiplicator;
    }

    public void multiply(float magnitude) {
        this.x *= magnitude;
        this.y *= magnitude;
        this.z *= magnitude;
    }

    public void multiply(Vector3 other) {
        this.x *= other.x;
        this.y *= other.y;
        this.z *= other.z;
    }

    public void divide(float magnitude) {
        if (magnitude != 0.0F) {
            this.x /= magnitude;
            this.y /= magnitude;
            this.z /= magnitude;
        }

    }

    public void set(Vector3 other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
    }

    public void set(float xValue, float yValue, float zValue) {
        this.x = xValue;
        this.y = yValue;
        this.z = zValue;
    }

    public float dot(Vector3 other) {
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    public Vector3 cross(Vector3 other) {
        return new Vector3(this.y * other.z - this.z * other.y, this.z * other.x - this.x * other.z, this.x * other.y - this.y * other.x);
    }

    public float length() {
        return (float)Math.sqrt(this.length2());
    }

    public float length2() {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    public float distance2(Vector3 other) {
        float dx = this.x - other.x;
        float dy = this.y - other.y;
        float dz = this.z - other.z;
        return dx * dx + dy * dy + dz * dz;
    }

    public float normalize() {
        float magnitude = this.length();
        if (magnitude != 0.0F) {
            this.x /= magnitude;
            this.y /= magnitude;
            this.z /= magnitude;
        }

        return magnitude;
    }

    public void zero() {
        this.set(0.0F, 0.0F, 0.0F);
    }

    public boolean pointsInSameDirection(Vector3 other) {
        return this.dot(other) > 0.0F;
    }
}
