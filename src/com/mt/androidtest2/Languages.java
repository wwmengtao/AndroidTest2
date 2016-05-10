package com.mt.androidtest2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.res.Resources;
import android.os.SystemProperties;

import com.example.androidtest2.R;

public class Languages {
    static boolean DEBUG = false;    
    Context mContext=null;
	public Languages(Context mContext){
		this.mContext = mContext;
	}
	/**
	 * 显示当前设备所有语言信息
	 */
	public void showAllLocales(int type){
		List<LocaleInfo>mLocaleInfoList=getLocaleInfoList(type);
		String label=null;
		Locale locale=null;
		for(LocaleInfo mLocaleInfo : mLocaleInfoList){
            label = mLocaleInfo.getLabel();
            locale = mLocaleInfo.getLocale();
            ALog.Log("label:"+label);
            ALog.Log("locale:"+locale.toString());
		}
	}
	
	/**
	 *  saveAllLocales：储存当前设备所有语言信息到fileToSave文件中
	 * @param type：标识存储的是默认列表还是指定列表
	 */
	public void saveAllLocales(int type,boolean needDetailed){
		saveAllLocalesInfo(mContext,getLocaleInfoList(type),needDetailed);
	}
	
    public void saveAllLocalesInfo(Context mContext,List<LocaleInfo> mLocaleInfoList,boolean needDetailed){
    	if(null==mLocaleInfoList){
    		return;
    	}else if(0==mLocaleInfoList.size()){
    		return;
    	}
		String fileToSave = "Languages.xml";
		String docTag = "Languages";
		ALog.startSaving(mContext,fileToSave,docTag);
        String label = null;
        Locale locale = null;
        String tagName="LocaleInfo";
        String tagNameGet="get:";
        String tagNameGetDisplay="getDisplay:";
        /*
         手机语音为英文时香港繁体描述如下：
		<LocaleInfo locale="zh_HK" label="中文 (香港)" getDisplayName="Chinese (Hong Kong)">
	        <get: getLanguage="zh" getCountry="HK" />
	        <getDisplay: getDisplayLanguage="Chinese" getDisplayCountry="Hong Kong" />
	    </LocaleInfo>
         手机语言为中文简体时香港繁体描述如下：
	    <LocaleInfo locale="zh_HK" label="中文 (香港)" getDisplayName="中文 (香港)">
	        <get: getLanguage="zh" getCountry="HK" />
	        <getDisplay: getDisplayLanguage="中文" getDisplayCountry="香港" />
	    </LocaleInfo>
        */
		for(LocaleInfo mLocaleInfo : mLocaleInfoList){
			label = mLocaleInfo.getLabel();
			locale = mLocaleInfo.getLocale();
			ALog.stag(tagName);
			ALog.attr("locale",locale.toString());
			ALog.attr("label",label);
			ALog.attr("getDisplayName",locale.getDisplayName());
			if(needDetailed){
				//以下细分每个函数内容
				ALog.stag(tagNameGet);
				ALog.attr("getLanguage",locale.getLanguage());
				ALog.attr("getCountry",locale.getCountry());			
				ALog.etag(tagNameGet);
				//
				ALog.stag(tagNameGetDisplay);
				ALog.attr("getDisplayLanguage",locale.getDisplayLanguage());
				ALog.attr("getDisplayCountry",locale.getDisplayCountry());
				ALog.etag(tagNameGetDisplay);
			}
			ALog.etag(tagName);
		}
		ALog.endSaving();
    }
	
	public List<LocaleInfo> getLocaleInfoList(int type){
		List<LocaleInfo>mLocaleInfoList=new ArrayList<LocaleInfo>();
		if(0==type){
			mLocaleInfoList = getAllAssetLocales(mContext,null,true);
		}else if(1==type){
			/**下列字符串数组内容必须带后缀，下列形式是不可以的
			 * <locales>ar,bg,cs,el,es-rUS,fa,fr,hr,hu,in,ms,pt-rBR,pt-rPT,ro,ru,sk,sl,sr-rRS,ur-rPK,th,tr,uk,vi,zh-rTW,zh-rCN,zh-rHK,hi</locales>
			 * 下列形式可以：
			 * en_US en_GB en_IN zh_CN zh_HK zh_TW in_ID vi_VN ru_RU ms_MY ar_EG th_TH uk_UA fr_FR ro_RO el_GR hu_HU bg_BG hr_HR sl_SI sk_SK es_US sr_RS tr_TR cs_CZ pt_PT pt_BR fa_IR hi_IN ur_PK bn_BD my_MM my_ZG en_ZG
			 * 
			 */
			//final String[] locales = {"en_US","en_AU","en_IN","fr_FR","it_IT","es_ES","et_EE","de_DE","nl_NL","cs_CZ","pl_PL","ja_JP","zh_TW","zh_CN","zh_HK","ru_RU","ko_KR","nb_NO","es_US","da_DK","el_GR","tr_TR","pt_PT","pt_BR","rm_CH","sv_SE","bg_BG","ca_ES","en_GB","fi_FI","hi_IN","hr_HR","hu_HU","in_ID","iw_IL","lt_LT","lv_LV","ro_RO","sk_SK","sl_SI","sr_RS","uk_UA","vi_VN","tl_PH","ar_EG","fa_IR","th_TH","sw_TZ","ms_MY","af_ZA","zu_ZA","am_ET","hi_IN","en_XA","ar_XB","fr_CA","km_KH","lo_LA","ne_NP","si_LK","mn_MN","hy_AM","az_AZ","ka_GE","my_MM","mr_IN","ml_IN","is_IS","mk_MK","ky_KG","eu_ES","gl_ES","bn_BD","ta_IN","kn_IN","te_IN","uz_UZ","ur_PK","kk_KZ","sq_AL","gu_IN","pa_IN"};
			String localesStr="en_US en_GB en_IN zh_CN zh_HK zh_TW in_ID vi_VN ru_RU ms_MY ar_EG th_TH uk_UA fr_FR ro_RO el_GR hu_HU bg_BG hr_HR sl_SI sk_SK es_US sr_RS tr_TR cs_CZ pt_PT pt_BR fa_IR hi_IN ur_PK bn_BD my_MM my_ZG en_ZG";
			String [] locales=null;
			locales=localesStr.split(" ");
			mLocaleInfoList = getAllAssetLocales(mContext, locales, true);
		}else if(2==type){
            ArrayList<String>mLanguagesArrayList = new ArrayList<String>();			
	        try { 
	        	//languagesIn.txt文件内容格式：values-pt-rPT、./values-pt-rPT，不带后缀(比如values-pt)的不可以
	        	InputStreamReader inputReader = new InputStreamReader(mContext.getResources().getAssets().open("locales/languagesIn.txt")); 
	            BufferedReader bufReader = new BufferedReader(inputReader);
	            String line=null;
	            String str=null;
	            while((line = bufReader.readLine()) != null){
	            	str = refine(line);
	            	mLanguagesArrayList.add(str);
	                //ALog.Log("str:"+str);
	            }
	        } catch (Exception e) {
	            e.printStackTrace(); 
	        }
	        if(mLanguagesArrayList.size()>0){
				String [] languages = new String[mLanguagesArrayList.size()]; 
				for(int i=0;i<mLanguagesArrayList.size();i++){  
					languages[i]=mLanguagesArrayList.get(i);  
		        }
				mLocaleInfoList =getAllAssetLocales(mContext, languages, true);
	        }        
		}
		return mLocaleInfoList;
	}
	
	public String refine(String line){
		String str=line;
		String [][]tags_values={{"./values-",""},
											   {"values-",""},
											   {"-r","-"}};
    	for(int i=0;i<tags_values.length;i++){//tags_values.length: the columns of the array
    		if(line.contains(tags_values[i][0])){
    			str=str.replace(tags_values[i][0], tags_values[i][1]);
    		}
    	}
    	return str;
	}
    
    /**
     * getAllAssetLocalesFromStrings：获取指定语言代码的语言信息
     * @param context
     * @param isInDeveloperMode
     * @return
     */
    public static List<LocaleInfo> getAllAssetLocales(Context context, String[] localesAno, boolean isInDeveloperMode) {
        final Resources resources = context.getResources();
        String[] locales = null;
        if(null==localesAno){
        	//获取当前设备预置的语言
        	locales = Resources.getSystem().getAssets().getLocales();
        }else{
        	//获取用户给定的语言
        	locales = localesAno;
        }
        if(null==locales){
        	return null;
        }else if(0==locales.length){
        	return null;
        }
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
