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

import kore.botssdk.net.SDKConfiguration;

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
    private static Typeface robotoExtraBold;


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
    public static final String ROBOTO_EXTRA_BOLD = "extra-bold";

    public static void applyCustomFont(Context context, View root) {
        if(SDKConfiguration.isApplyFontStyle()) {
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
    }

    private static void setCustomFont(TextView myview, Context context) {
        Object tag = myview.getTag();
        if (tag instanceof String) {
            myview.setTypeface(getCustomTypeface((String) tag, context));
        }
    }


    public static void setCustomTypeface(TextView view,String tag,Context context){
        if(SDKConfiguration.isApplyFontStyle()) {
            view.setTypeface(getCustomTypeface(tag, context));
        }
    }

    private static Typeface getCustomTypeface(String tag, Context context) {
        if(SDKConfiguration.getFontType().equals(SDKConfiguration.FONT_TYPES.RELATIVE)){
           if(tag.equalsIgnoreCase(ROBOTO_BOLD)) {
               if (robotoBold == null) {
                   robotoBold = Typeface.createFromAsset(context.getAssets(), "fonts/Relative-Bold.ttf");
               }
               return robotoBold;
           }else {
               if (robotoRegular == null) {
                   robotoRegular = Typeface.createFromAsset(context.getAssets(), "fonts/Relative-Normal.ttf");
               }
               return robotoRegular;
           }

        }else {

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
                case ROBOTO_EXTRA_BOLD:
                    if (robotoExtraBold == null) {
                        robotoExtraBold = Typeface.createFromAsset(context.getAssets(), "fonts/Lato-ExtraBold.ttf");
                    }
                    return robotoExtraBold;
                default:
                    if (robotoRegular == null) {
                        robotoRegular = Typeface.createFromAsset(context.getAssets(), "fonts/Lato-Regular.ttf");
                    }
                    return robotoRegular;
            }
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
            case 8:
                typefaceTag = KaFontUtils.ROBOTO_EXTRA_BOLD;
                break;
            default:
                typefaceTag = KaFontUtils.ROBOTO_REGULAR;
                break;
        }
        return KaFontUtils.getCustomTypeface(typefaceTag, context);
    }
}
