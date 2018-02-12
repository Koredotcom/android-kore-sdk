package kore.botssdk.utils;

/**
 * Created by Shiva Krishna on 10/23/2017.
 */


import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class KaFontUtils {

    private static final String LOG_TAG = KaFontUtils.class.getSimpleName();

    private static Typeface robotoLight;
    private static Typeface robotoLightItalic;
    private static Typeface robotoRegular;
    private static Typeface robotoRegularItalic;
    private static Typeface robotoMedium;
    private static Typeface robotoMediumItalic;
    private static Typeface robotoSemiBold;
    private static Typeface robotoSemiBoldItalic;
    private static Typeface robotoBold;
    private static Typeface robotoBoldItalic;

    public static final String ROBOTO_LIGHT = "light";
    public static final String ROBOTO_LIGHT_ITALICS = "light-italic";
    public static final String ROBOTO_REGULAR = "regular";
    public static final String ROBOTO_REGULAR_ITALICS = "regular-italic";
    public static final String ROBOTO_MEDIUM = "medium";
    public static final String ROBOTO_MEDIUM_ITALICS = "medium-italic";
    public static final String ROBOTO_SEMI_BOLD = "semi-bold";
    public static final String ROBOTO_SEMI_BOLD_ITALICS = "semi-bold-italic";
    public static final String ROBOTO_BOLD = "bold";
    public static final String ROBOTO_BOLD_ITALICS = "bold-italic";

    public static void applyCustomFont(Context context, View root) {
        if (root instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) root;
            for (int count = 0; count <= viewGroup.getChildCount(); count++) {
                applyCustomFont(context, viewGroup.getChildAt(count));
            }
        } else if (root instanceof TextView || root instanceof Button || root instanceof EditText) {
            TextView myview = (TextView) root;
            setCustomFont(myview, context);

        }
    }

    private static void setCustomFont(TextView myview, Context context) {
        Object tag = myview.getTag();
        if (tag instanceof String) {
            if (((String) tag).equalsIgnoreCase(ROBOTO_LIGHT)) {
                if (robotoLight == null) {
                    robotoLight = Typeface.createFromAsset(context.getAssets(), "fonts/Lato-Light.ttf");
                }
                myview.setTypeface(robotoLight);
            } else if (((String) tag).equalsIgnoreCase(ROBOTO_LIGHT_ITALICS)) {
                if (robotoLightItalic == null) {
                    robotoLightItalic = Typeface.createFromAsset(context.getAssets(), "fonts/Lato-LightItalic.ttf");
                }
                myview.setTypeface(robotoLightItalic);
            } else if (((String) tag).equalsIgnoreCase(ROBOTO_REGULAR)) {
                if (robotoRegular == null) {
                    robotoRegular = Typeface.createFromAsset(context.getAssets(), "fonts/Lato-Regular.ttf");
                }
                myview.setTypeface(robotoRegular);
            } else if (((String) tag).equalsIgnoreCase(ROBOTO_REGULAR_ITALICS)) {
                if (robotoRegularItalic == null) {
                    robotoRegularItalic = Typeface.createFromAsset(context.getAssets(), "fonts/Lato-Italic.ttf");
                }
                myview.setTypeface(robotoRegularItalic);
            } else if (((String) tag).equalsIgnoreCase(ROBOTO_MEDIUM)) {
                if (robotoMedium == null) {
                    robotoMedium = Typeface.createFromAsset(context.getAssets(), "fonts/Lato-Medium.ttf");
                }
                myview.setTypeface(robotoMedium);
            } else if (((String) tag).equalsIgnoreCase(ROBOTO_MEDIUM_ITALICS)) {
                if (robotoMediumItalic == null) {
                    robotoMediumItalic = Typeface.createFromAsset(context.getAssets(), "fonts/Lato-MediumItalic.ttf");
                }
                myview.setTypeface(robotoMediumItalic);
            } else if (((String) tag).equalsIgnoreCase(ROBOTO_SEMI_BOLD)) {
                if (robotoSemiBold == null) {
                    robotoSemiBold = Typeface.createFromAsset(context.getAssets(), "fonts/Lato-Semibold.ttf");
                }
                myview.setTypeface(robotoSemiBold);
            } else if (((String) tag).equalsIgnoreCase(ROBOTO_SEMI_BOLD_ITALICS)) {
                if (robotoSemiBoldItalic == null) {
                    robotoSemiBoldItalic = Typeface.createFromAsset(context.getAssets(), "fonts/Lato-SemiboldItalic.ttf");
                }
                myview.setTypeface(robotoSemiBoldItalic);
            } else if (((String) tag).equalsIgnoreCase(ROBOTO_BOLD)) {
                if (robotoBold == null) {
                    robotoBold = Typeface.createFromAsset(context.getAssets(), "fonts/Lato-Bold.ttf");
                }
                myview.setTypeface(robotoBold);
            } else if (((String) tag).equalsIgnoreCase(ROBOTO_BOLD_ITALICS)) {
                if (robotoBoldItalic == null) {
                    robotoBoldItalic = Typeface.createFromAsset(context.getAssets(), "fonts/Lato-BoldItalic.ttf");
                }
                myview.setTypeface(robotoBoldItalic);
            }
        }
    }

    public static Typeface getCustomTypeface(String tag, Context context) {

        switch (tag) {
            case ROBOTO_LIGHT:
                if (robotoLight == null) {
                    robotoLight = Typeface.createFromAsset(context.getAssets(), "fonts/Lato-Light.ttf");
                }
                return robotoLight;
            case ROBOTO_LIGHT_ITALICS:
                if (robotoLightItalic == null) {
                    robotoLightItalic = Typeface.createFromAsset(context.getAssets(), "fonts/Lato-LightItalic.ttf");
                }
                return robotoLightItalic;
            case ROBOTO_REGULAR:
                if (robotoRegular == null) {
                    robotoRegular = Typeface.createFromAsset(context.getAssets(), "fonts/Lato-Regular.ttf");
                }
                return robotoRegular;
            case ROBOTO_REGULAR_ITALICS:
                if (robotoRegularItalic == null) {
                    robotoRegularItalic = Typeface.createFromAsset(context.getAssets(), "fonts/Lato-Italic.ttf");
                }
                return robotoRegularItalic;
            case ROBOTO_MEDIUM:
                if (robotoMedium == null) {
                    robotoMedium = Typeface.createFromAsset(context.getAssets(), "fonts/Lato-Medium.ttf");
                }
                return robotoMedium;
            case ROBOTO_MEDIUM_ITALICS:
                if (robotoMediumItalic == null) {
                    robotoMediumItalic = Typeface.createFromAsset(context.getAssets(), "fonts/Lato-MediumItalic.ttf");
                }
                return robotoMediumItalic;
            case ROBOTO_SEMI_BOLD:
                if (robotoSemiBold == null) {
                    robotoSemiBold = Typeface.createFromAsset(context.getAssets(), "fonts/Lato-Semibold.ttf");
                }
                return robotoSemiBold;
            case ROBOTO_SEMI_BOLD_ITALICS:
                if (robotoSemiBoldItalic == null) {
                    robotoSemiBoldItalic = Typeface.createFromAsset(context.getAssets(), "fonts/Lato-SemiboldItalic.ttf");
                }
                return robotoSemiBoldItalic;
            case ROBOTO_BOLD:
                if (robotoBold == null) {
                    robotoBold = Typeface.createFromAsset(context.getAssets(), "fonts/Lato-Bold.ttf");
                }
                return robotoBold;
            case ROBOTO_BOLD_ITALICS:
                if (robotoBoldItalic == null) {
                    robotoBoldItalic = Typeface.createFromAsset(context.getAssets(), "fonts/Lato-BoldItalic.ttf");
                }
                return robotoBoldItalic;
            default:
                if (robotoRegular == null) {
                    robotoRegular = Typeface.createFromAsset(context.getAssets(), "fonts/Lato-Regular.ttf");
                }
                return robotoRegular;
        }
    }

    public static Typeface resolveTypeface(int typefaceValue, Context context) {
        String typefaceTag = null;
        switch (typefaceValue) {
            case 0:
                typefaceTag = KaFontUtils.ROBOTO_LIGHT;
                break;
            case 1:
                typefaceTag = KaFontUtils.ROBOTO_LIGHT_ITALICS;
                break;
            case 2:
                typefaceTag = KaFontUtils.ROBOTO_REGULAR;
                break;
            case 3:
                typefaceTag = KaFontUtils.ROBOTO_REGULAR_ITALICS;
                break;
            case 4:
                typefaceTag = KaFontUtils.ROBOTO_MEDIUM;
                break;
            case 5:
                typefaceTag = KaFontUtils.ROBOTO_MEDIUM_ITALICS;
                break;
            case 6:
                typefaceTag = KaFontUtils.ROBOTO_BOLD;
                break;
            case 7:
                typefaceTag = KaFontUtils.ROBOTO_BOLD_ITALICS;
                break;
            default:
                typefaceTag = KaFontUtils.ROBOTO_REGULAR;
                break;
        }
        return KaFontUtils.getCustomTypeface(typefaceTag, context);
    }
}
