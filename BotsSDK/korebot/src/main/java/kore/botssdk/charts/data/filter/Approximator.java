package kore.botssdk.charts.data.filter;

import android.annotation.TargetApi;

import java.util.Arrays;

public class Approximator {
    public Approximator() {
    }

    @TargetApi(9)
    public float[] reduceWithDouglasPeucker(float[] points, float tolerance) {
        int greatestIndex = 0;
        float greatestDistance = 0.0F;
        Line line = new Line(points[0], points[1], points[points.length - 2], points[points.length - 1]);

        for(int i = 2; i < points.length - 2; i += 2) {
            float distance = line.distance(points[i], points[i + 1]);
            if (distance > greatestDistance) {
                greatestDistance = distance;
                greatestIndex = i;
            }
        }

        if (greatestDistance > tolerance) {
            float[] reduced1 = this.reduceWithDouglasPeucker(Arrays.copyOfRange(points, 0, greatestIndex + 2), tolerance);
            float[] reduced2 = this.reduceWithDouglasPeucker(Arrays.copyOfRange(points, greatestIndex, points.length), tolerance);
            float[] result2 = Arrays.copyOfRange(reduced2, 2, reduced2.length);
            return this.concat(reduced1, result2);
        } else {
            return line.getPoints();
        }
    }

    float[] concat(float[]... arrays) {
        int length = 0;
        float[][] var3 = arrays;
        int pos = arrays.length;

        for(int var5 = 0; var5 < pos; ++var5) {
            float[] array = var3[var5];
            length += array.length;
        }

        float[] result = new float[length];
        pos = 0;
        float[][] var14 = arrays;
        int var15 = arrays.length;

        for(int var7 = 0; var7 < var15; ++var7) {
            float[] array = var14[var7];
            float[] var9 = array;
            int var10 = array.length;

            for(int var11 = 0; var11 < var10; ++var11) {
                float element = var9[var11];
                result[pos] = element;
                ++pos;
            }
        }

        return result;
    }

    private class Line {
        private final float[] points;
        private final float sxey;
        private final float exsy;
        private final float dx;
        private final float dy;
        private final float length;

        public Line(float x1, float y1, float x2, float y2) {
            this.dx = x1 - x2;
            this.dy = y1 - y2;
            this.sxey = x1 * y2;
            this.exsy = x2 * y1;
            this.length = (float)Math.sqrt(this.dx * this.dx + this.dy * this.dy);
            this.points = new float[]{x1, y1, x2, y2};
        }

        public float distance(float x, float y) {
            return Math.abs(this.dy * x - this.dx * y + this.sxey - this.exsy) / this.length;
        }

        public float[] getPoints() {
            return this.points;
        }
    }
}
