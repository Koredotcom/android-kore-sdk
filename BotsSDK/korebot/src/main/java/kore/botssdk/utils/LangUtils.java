package kore.botssdk.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.util.ArrayMap;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.LinkedHashSet;
import java.util.Locale;

public class LangUtils {
    public static final String LANG_EN = "en";
    public static final String LANG_ES = "es";

    private static ArrayMap<String, Locale> sLocaleMap;

    public static void setAppLanguages(@NonNull Context context, String lang) {
        if (sLocaleMap == null) sLocaleMap = new ArrayMap<>();
        // Assume that there is an array called language_key which contains all the supported language tags
        Locale appDefaultLocale = Locale.forLanguageTag(lang);
        updateResources(context, appDefaultLocale);
    }

    private static void updateResources(@NonNull Context context, @NonNull Locale locale) {
        Locale.setDefault(locale);

        Resources res = context.getResources();
        Configuration conf = res.getConfiguration();
        //noinspection
        Locale current = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ? conf.getLocales().get(0) : conf.locale;

        if (current == locale) {
            return;
        }

        conf = new Configuration(conf);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            setLocaleApi24(conf, locale);
        } else {
            conf.setLocale(locale);
        }
        //noinspection
        res.updateConfiguration(conf, res.getDisplayMetrics());
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private static void setLocaleApi24(@NonNull Configuration config, @NonNull Locale locale) {
        LocaleList defaultLocales = LocaleList.getDefault();
        LinkedHashSet<Locale> locales = new LinkedHashSet<>(defaultLocales.size() + 1);
        // Bring the target locale to the front of the list
        // There's a hidden API, but it's not currently used here.
        locales.add(locale);
        for (int i = 0; i < defaultLocales.size(); ++i) {
            locales.add(defaultLocales.get(i));
        }
        config.setLocales(new LocaleList(locales.toArray(new Locale[0])));
    }
}
