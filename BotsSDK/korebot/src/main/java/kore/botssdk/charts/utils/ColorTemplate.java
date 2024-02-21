package kore.botssdk.charts.utils;

import android.content.res.Resources;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

public class ColorTemplate {
    public static final int COLOR_NONE = 1122867;
    public static final int COLOR_SKIP = 1122868;
    public static final int[] LIBERTY_COLORS = new int[]{Color.rgb(207, 248, 246), Color.rgb(148, 212, 212), Color.rgb(136, 180, 187), Color.rgb(118, 174, 175), Color.rgb(42, 109, 130)};
    public static final int[] JOYFUL_COLORS = new int[]{Color.rgb(217, 80, 138), Color.rgb(254, 149, 7), Color.rgb(254, 247, 120), Color.rgb(106, 167, 134), Color.rgb(53, 194, 209)};
    public static final int[] PASTEL_COLORS = new int[]{Color.rgb(64, 89, 128), Color.rgb(149, 165, 124), Color.rgb(217, 184, 162), Color.rgb(191, 134, 134), Color.rgb(179, 48, 80)};
    public static final int[] COLORFUL_COLORS = new int[]{Color.rgb(193, 37, 82), Color.rgb(255, 102, 0), Color.rgb(245, 199, 0), Color.rgb(106, 150, 31), Color.rgb(179, 100, 53)};
    public static final int[] VORDIPLOM_COLORS = new int[]{Color.rgb(192, 255, 140), Color.rgb(255, 247, 140), Color.rgb(255, 208, 140), Color.rgb(140, 234, 255), Color.rgb(255, 140, 157)};
    public static final int[] MATERIAL_COLORS = new int[]{rgb("#2ecc71"), rgb("#f1c40f"), rgb("#e74c3c"), rgb("#3498db")};

    public ColorTemplate() {
    }

    public static int rgb(String hex) {
        int color = (int)Long.parseLong(hex.replace("#", ""), 16);
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

        for(int var5 = 0; var5 < var4; ++var5) {
            int i = var3[var5];
            result.add(r.getColor(i));
        }

        return result;
    }

    public static List<Integer> createColors(int[] colors) {
        List<Integer> result = new ArrayList();
        int[] var2 = colors;
        int var3 = colors.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            int i = var2[var4];
            result.add(i);
        }

        return result;
    }
}
