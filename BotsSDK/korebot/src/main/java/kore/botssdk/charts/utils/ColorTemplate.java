package kore.botssdk.charts.utils;

import android.content.res.Resources;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

public class ColorTemplate {
    public static final int[] MATERIAL_COLORS = new int[]{rgb("#4A9AF2"), rgb("#5BC8C4"), rgb("#e74c3c"), rgb("#3498db")};

    public ColorTemplate() {
    }

    public static int rgb(String hex) {
        int color = (int) Long.parseLong(hex.replace("#", ""), 16);
        int r = color >> 16 & 255;
        int g = color >> 8 & 255;
        int b = color & 255;
        return Color.rgb(r, g, b);
    }

    public static int getHoloBlue() {
        return Color.rgb(51, 181, 229);
    }

    public static int colorWithAlpha(int color, int alpha) {
        return color & 16777215 | (alpha & 255) << 24;
    }

    public static List<Integer> createColors(Resources r, int[] colors) {
        List<Integer> result = new ArrayList();
        int[] var3 = colors;
        int var4 = colors.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            int i = var3[var5];
            result.add(r.getColor(i));
        }

        return result;
    }

    public static List<Integer> createColors(int[] colors) {
        List<Integer> result = new ArrayList();
        int[] var2 = colors;
        int var3 = colors.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            int i = var2[var4];
            result.add(i);
        }

        return result;
    }
}
