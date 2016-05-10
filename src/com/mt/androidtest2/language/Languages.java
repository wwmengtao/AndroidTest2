package com.mt.androidtest2.language;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import android.content.Context;
import android.content.res.Resources;
import android.os.SystemProperties;

import com.example.androidtest2.R;
import com.mt.androidtest2.ALog;

public class Languages {
    static boolean DEBUG = false;    
    /**
     * 获取当前设备预置的语言
     * @param mContext
     * @return
     */
    public static List<com.android.internal.app.LocalePicker.LocaleInfo> getAllAssetLocales(Context mContext){
    	return com.android.internal.app.LocalePicker.getAllAssetLocales(mContext,true);
    }
    /**
     * getAllAssetLocalesFromStrings：获取指定语言代码的语言信息
     * @param context
     * @param isInDeveloperMode
     * @return
     */
    public static List<LocaleInfo> getAllAssetLocalesFromStrings(Context context, String[] locales, boolean isInDeveloperMode) {
        final Resources resources = context.getResources();
        //替换掉下列获取系统语言信息的代码
        //final String[] locales = Resources.getSystem().getAssets().getLocales();
        List<String> localeList = new ArrayList<String>(locales.length);
        Collections.addAll(localeList, locales);
        // Don't show the pseudolocales unless we're in developer mode. http://b/17190407.
        if (!isInDeveloperMode) {
            localeList.remove("ar-XB");
            localeList.remove("en-XA");
        }
        
        String country_code = SystemProperties.get("ro.lenovo.easyimage.code", "");
        if (DEBUG) ALog.Log("hj dbg: country_code=" + country_code);
        if (!country_code.equals("sg")) {
            if (DEBUG) ALog.Log("hj dbg: remove en-ZG");
            localeList.remove("en-ZG");
        }

        Collections.sort(localeList);
        final String[] specialLocaleCodes = resources.getStringArray(R.array.special_locale_codes);
        final String[] specialLocaleNames = resources.getStringArray(R.array.special_locale_names);

        final ArrayList<LocaleInfo> localeInfos = new ArrayList<LocaleInfo>(localeList.size());
        for (String locale : localeList) {
            final Locale l = Locale.forLanguageTag(locale.replace('_', '-'));
            if (l == null || "und".equals(l.getLanguage())
                    || l.getLanguage().isEmpty() || l.getCountry().isEmpty()) {
                continue;
            }

            if (localeInfos.isEmpty()) {
                if (DEBUG) {
                    ALog.Log("adding initial "+ toTitleCase(l.getDisplayLanguage(l)));
                }
                localeInfos.add(new LocaleInfo(toTitleCase(l.getDisplayLanguage(l)), l));
            } else {
                // check previous entry:
                //  same lang and a country -> upgrade to full name and
                //    insert ours with full name
                //  diff lang -> insert ours with lang-only name
                final LocaleInfo previous = localeInfos.get(localeInfos.size() - 1);
                if (previous.locale.getLanguage().equals(l.getLanguage()) &&
                        !previous.locale.getLanguage().equals("zz")) {
                    if (DEBUG) {
                        ALog.Log("backing up and fixing " + previous.label + " to " +
                                getDisplayName(previous.locale, specialLocaleCodes, specialLocaleNames));
                    }
                    previous.label = toTitleCase(getDisplayName(
                            previous.locale, specialLocaleCodes, specialLocaleNames));
                    if (DEBUG) {
                        ALog.Log("  and adding "+ toTitleCase(
                                getDisplayName(l, specialLocaleCodes, specialLocaleNames)));
                    }
                    localeInfos.add(new LocaleInfo(toTitleCase(
                            getDisplayName(l, specialLocaleCodes, specialLocaleNames)), l));
                } else {
                    String displayName = toTitleCase(l.getDisplayLanguage(l));
                    if (DEBUG) {
                        ALog.Log("adding "+displayName);
                    }
                    localeInfos.add(new LocaleInfo(displayName, l));
                }
            }
        }

        Collections.sort(localeInfos);
        return localeInfos;
    }
    private static String toTitleCase(String s) {
        if (s.length() == 0) {
            return s;
        }

        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    public static class LocaleInfo implements Comparable<LocaleInfo> {
        static final Collator sCollator = Collator.getInstance();

        String label;
        Locale locale;

        public LocaleInfo(String label, Locale locale) {
            this.label = label;
            this.locale = locale;
        }

        public String getLabel() {
            return label;
        }

        public Locale getLocale() {
            return locale;
        }

        @Override
        public String toString() {
            return this.label;
        }

        @Override
        public int compareTo(LocaleInfo another) {
            return sCollator.compare(this.label, another.label);
        }
    }
    private static String getDisplayName(
            Locale l, String[] specialLocaleCodes, String[] specialLocaleNames) {
        String code = l.toString();

        for (int i = 0; i < specialLocaleCodes.length; i++) {
            if (specialLocaleCodes[i].equals(code)) {
                return specialLocaleNames[i];
            }
        }

        return l.getDisplayName(l);
    }
}
