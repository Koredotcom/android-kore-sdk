package kore.botssdk.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import kore.botssdk.R;
import kore.botssdk.net.SDKConfiguration;

@SuppressLint("UnknownNullness")
public class KaFontUtils {
    private static Typeface robotoLight;
    private static Typeface robotoLightItalic;
    private static Typeface robotoRegular;
    private static Typeface robotoRegularItalic;
    private static Typeface robotoMedium;
    private static Typeface robotoMediumItalic;
    private static Typeface robotoBold;
    private static Typeface robotoBoldItalic;
    private static Typeface robotoExtraBold;

    public static final String ROBOTO_LIGHT = "light";
    public static final String ROBOTO_LIGHT_ITALICS = "light-italic";
    public static final String ROBOTO_REGULAR = "regular";
    public static final String ROBOTO_REGULAR_ITALICS = "regular-italic";
    public static final String ROBOTO_MEDIUM = "medium";
    public static final String ROBOTO_MEDIUM_ITALICS = "medium-italic";
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
            } else if (root instanceof TextView) {
                TextView myView = (TextView) root;
                setCustomFont(myView, context);
            }
        }
    }

    private static void setCustomFont(TextView myview, Context context) {
        Object tag = myview.getTag();
        if (tag instanceof String) {
            if (((String) tag).equalsIgnoreCase(ROBOTO_LIGHT)) {
                if (robotoLight == null) {
                    robotoLight = ResourcesCompat.getFont(context,R.font.latoregular);
                    //robotoLight = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
                }
                myview.setTypeface(robotoLight);
            } else if (((String) tag).equalsIgnoreCase(ROBOTO_LIGHT_ITALICS)) {
                if (robotoLightItalic == null) {
                    robotoLightItalic = ResourcesCompat.getFont(context,R.font.latoregular);
                }
                myview.setTypeface(robotoLightItalic);
            } else if (((String) tag).equalsIgnoreCase(ROBOTO_REGULAR)) {
                if (robotoRegular == null) {
                    robotoRegular = ResourcesCompat.getFont(context,R.font.latoregular);
                }
                myview.setTypeface(robotoRegular);
            } else if (((String) tag).equalsIgnoreCase(ROBOTO_REGULAR_ITALICS)) {
                if (robotoRegularItalic == null) {
                    robotoRegularItalic = ResourcesCompat.getFont(context,R.font.latoregular);
                }
                myview.setTypeface(robotoRegularItalic);
            } else if (((String) tag).equalsIgnoreCase(ROBOTO_MEDIUM)) {
                if (robotoMedium == null) {
                    robotoMedium = ResourcesCompat.getFont(context,R.font.latoregular);
                }
                myview.setTypeface(robotoMedium);
            } else if (((String) tag).equalsIgnoreCase(ROBOTO_MEDIUM_ITALICS)) {
                if (robotoMediumItalic == null) {
                    robotoMediumItalic = ResourcesCompat.getFont(context,R.font.latoregular);
                }
                myview.setTypeface(robotoMediumItalic);
            }  else if (((String) tag).equalsIgnoreCase(ROBOTO_BOLD)) {
                if (robotoBold == null) {
                    robotoBold = ResourcesCompat.getFont(context,R.font.latobold);
                }
                myview.setTypeface(robotoBold);
            } else if (((String) tag).equalsIgnoreCase(ROBOTO_BOLD_ITALICS)) {
                if (robotoBoldItalic == null) {
                    robotoBoldItalic = ResourcesCompat.getFont(context,R.font.latobold);
                }
                myview.setTypeface(robotoBoldItalic);
            }else if(((String) tag).equalsIgnoreCase(ROBOTO_EXTRA_BOLD)){
                if (robotoExtraBold == null) {
                    robotoExtraBold = ResourcesCompat.getFont(context,R.font.latobold);
                }
                myview.setTypeface(robotoExtraBold);
            }
        }
    }


    public static void setCustomTypeface(TextView view,String tag,Context context){
        if(SDKConfiguration.isApplyFontStyle()) {
            view.setTypeface(getCustomTypeface(tag, context));
        }
    }

    public static Typeface getCustomTypeface(String tag, Context context) {

        switch (tag) {
            case ROBOTO_LIGHT:
                if (robotoLight == null) {
                    robotoLight = ResourcesCompat.getFont(context,R.font.latoregular);
                }
                return robotoLight;
            case ROBOTO_LIGHT_ITALICS:
                if (robotoLightItalic == null) {
                    robotoLightItalic = ResourcesCompat.getFont(context,R.font.latoregular);
                }
                return robotoLightItalic;
            case ROBOTO_REGULAR_ITALICS:
                if (robotoRegularItalic == null) {
                    robotoRegularItalic = ResourcesCompat.getFont(context,R.font.latoregular);
                }
                return robotoRegularItalic;
            case ROBOTO_MEDIUM:
                if (robotoMedium == null) {
                    robotoMedium = ResourcesCompat.getFont(context,R.font.latoregular);
                }
                return robotoMedium;
            case ROBOTO_MEDIUM_ITALICS:
                if (robotoMediumItalic == null) {
                    robotoMediumItalic = ResourcesCompat.getFont(context,R.font.latoregular);
                }
                return robotoMediumItalic;
            case ROBOTO_BOLD:
                if (robotoBold == null) {
                    robotoBold = ResourcesCompat.getFont(context,R.font.latobold);
                }
                return robotoBold;
            case ROBOTO_BOLD_ITALICS:
                if (robotoBoldItalic == null) {
                    robotoBoldItalic = ResourcesCompat.getFont(context,R.font.latobold);
                }
                return robotoBoldItalic;
            case ROBOTO_EXTRA_BOLD:
                if (robotoExtraBold == null) {
                    robotoExtraBold = ResourcesCompat.getFont(context,R.font.latobold);
                }
                return robotoExtraBold;
            default:
                if (robotoRegular == null) {
                    robotoRegular = ResourcesCompat.getFont(context,R.font.latoregular);
                }
                return robotoRegular;
        }
    }

}
