package com.mt.androidtest2;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.android.internal.app.LocalePicker.LocaleInfo;
import com.mt.androidtest2.language.Languages;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemProperties;
import android.view.View;
import android.widget.Button;

import com.example.androidtest2.R;

public class MainActivity extends Activity {
	boolean isLogRun=true;
	private static final String NOTIFICATION_ID = "CaptivePortal.Notification";
	Button btn=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		btn=(Button)findViewById(R.id.btn);
		btn.setOnClickListener(viewListener);
	}

	@Override
	public void onResume(){
		super.onResume();
		if(isLogRun)ALog.Log("====onResume:"+getLocale());
		testFunctions();
	}
	
	@Override
	public void onPause(){
		super.onPause();
		//2、取消通知栏内容的显示
		//cancelNotification(this, 1);
	}
	
	public void testFunctions(){
		//0、文件操作
		//fileOperation();
		//1、读写xml文件
		//ALog.howToWriteToXml(this);
		//ALog.howToReadFromXml(this);
		//2、通知栏显示通知
		//showNotification(this,1,null);
		//3、获取当前手机的所有语言列表
		//showAllLocales(1);
		saveAllLocales(1,false);//保存语言信息，不需要细节内容
		//4、判断当前手机VIBEUI的版本
		//String lvpVersion = getLVPVersion();
		//boolean isVibeUI3_5 = (null!=lvpVersion&&lvpVersion.contains("V3.5"));
		//ALog.Log("isVibeUI3_5:"+isVibeUI3_5);
	}
	
	public void fileOperation(){
		fileOperate mfileOperate = new fileOperate(this);
		//mfileOperate.listDirs();
		//mfileOperate.writeToFile("test.txt","hello\nxixi\nhaha",0);
		//mfileOperate.writeToFile("test.txt","hello\nxixi\nhaha",10);
		//mfileOperate.readFromFile("test.txt",10);
		//mfileOperate.readRawResources();
		//mfileOperate.getResourcesDescription();
		mfileOperate.getFromAssets("test/test.txt");
		//列举assets目录文件
		//mfileOperate.listAssets("");//列举assets根目录下的文件
		//mfileOperate.listAssets("test");//列举assets/test目录下的文件
		//拷贝assets目录下的内容到指定位置
		/**
		 * getFilesDir：/data/data/com.example.androidtest2/files下创建子文件夹
		 * 向上述文件夹写入数据需要WRITE_EXTERNAL_STORAGE权限
		 */
		//mfileOperate.copyFilesFassets(this,"",getFilesDir()+File.separator+"myAssets");
		/**
		 * getExternalFilesDir：storage/emulated/0/Android/data/com.example.androidtest2/files
		 * 向上述文件夹写入数据需要WRITE_EXTERNAL_STORAGE权限
		 */
		//mfileOperate.copyFilesFassets(this,"",getExternalFilesDir(null)+File.separator+"myAssets");
	}
	
	View.OnClickListener viewListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			switch(view.getId()){
				case R.id.btn:
					Intent intent = new Intent();  
					intent.setClass(MainActivity.this, VpnActivity.class);
					startActivity(intent);//跳转到VpnActivity
				break;
			}
		}
	};
    /*
	[persist.sys.first_time_boot]: [false]
	[persist.sys.sd.defaultpath]: [/storage/emulated/0]
	[persist.sys.timezone]: [Europe/Moscow]
	[persist.sys.usb.config]: [mtp,adb]
	
	[ro.lenovo.wificert]: [pass]
	[ro.lenovo.platform]: [mtk]
	[ro.lenovo.region]: [row]
	[ro.lenovo.series]: [Lenovo S1]
	[ro.lenovo.device]: [phone]
	[ro.lenovo.easyimage.code]: [ru]
    */
    public String getLocale(){
			String locale = SystemProperties.get("persist.sys.locale");
			return locale;
    }
	
    public String getLVPVersion(){
    	String lvpVersion = SystemProperties.get("ro.lenovo.lvp.version");
    	return lvpVersion;
    }
	/**
	 * setProvNotificationVisibleIntent：状态栏显示图标，通知栏显示图标及title、details
	 * @param mContext
	 * @param id
	 * @param intent
	 */
	private void showNotification(Context mContext,int id,PendingIntent intent) {
	    NotificationManager notificationManager = (NotificationManager) mContext
	        .getSystemService(Context.NOTIFICATION_SERVICE);
	        CharSequence title = "title";
	        CharSequence details = "details";
	        int icon = com.android.internal.R.drawable.stat_notify_rssi_in_range;
	        Notification notification = new Notification.Builder(mContext)
	                .setWhen(0)
	                .setSmallIcon(icon)
	                .setAutoCancel(true)
	                .setTicker(title)
	                .setColor(mContext.getColor(
	                        com.android.internal.R.color.system_notification_accent_color))
	                .setContentTitle(title)
	                .setContentText(details)
	                .setContentIntent(intent)
	                .setLocalOnly(true)
	                .setPriority(Notification.PRIORITY_DEFAULT)
	                .setDefaults(Notification.DEFAULT_ALL)
	                .setOnlyAlertOnce(true)
	                .build();
	        try {
	            notificationManager.notify(NOTIFICATION_ID, id, notification);
	        } catch (NullPointerException npe) {
	        	ALog.Log("setNotificationVisible: visible notificationManager npe=" + npe);
	            npe.printStackTrace();
	        }
	    }
    
	/**
	 * cancelNotification：取消状态栏内容的显示
	 * @param mContext
	 * @param id
	 */
	public void cancelNotification(Context mContext,int id){
	    NotificationManager notificationManager = (NotificationManager) mContext
	            .getSystemService(Context.NOTIFICATION_SERVICE);
	    try {
	        notificationManager.cancel(NOTIFICATION_ID, id);
	    } catch (NullPointerException npe) {
	        ALog.Log("setNotificationVisible: cancel notificationManager npe=" + npe);
	        npe.printStackTrace();
	    }
	}
	
	/**
	 * 显示当前设备所有语言信息
	 */
	public void showAllLocales(int type){
		List<MergedLocaleInfo>mMergedLocaleInfoList=getMergedLocaleInfoList(type);
		String label=null;
		Locale locale=null;
		for(MergedLocaleInfo mMergedLocaleInfo : mMergedLocaleInfoList){
            label = mMergedLocaleInfo.getLabel();
            locale = mMergedLocaleInfo.getLocale();
            ALog.Log("label:"+label);
            ALog.Log("locale:"+locale.toString());
		}
	}
	
	/**
	 *  saveAllLocales：储存当前设备所有语言信息到fileToSave文件中
	 * @param type：标识存储的是默认列表还是指定列表
	 */
	public void saveAllLocales(int type,boolean needDetailed){
		saveAllLocalesInfo(this,getMergedLocaleInfoList(type),needDetailed);
	}
	
    public void saveAllLocalesInfo(Context mContext,List<MergedLocaleInfo> mLocaleInfoList,boolean needDetailed){
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
		for(MergedLocaleInfo mLocaleInfo : mLocaleInfoList){
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
	
	public List<MergedLocaleInfo> getMergedLocaleInfoList(int type){
		List<MergedLocaleInfo>mMergedLocaleInfoList=new ArrayList<MergedLocaleInfo>();
		if(0==type){
			List<LocaleInfo> mLocaleInfoList = Languages.getAllAssetLocales(this);
			for(LocaleInfo mLocaleInfo:mLocaleInfoList){
				mMergedLocaleInfoList.add(new MergedLocaleInfo(mLocaleInfo));
			}
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
			List<Languages.LocaleInfo> mLanguagesLocaleInfoList =null;
			mLanguagesLocaleInfoList = Languages.getAllAssetLocalesFromStrings(this, locales, true);
			if(null!=mLanguagesLocaleInfoList&&mLanguagesLocaleInfoList.size()>0){
				for(Languages.LocaleInfo mLocaleInfo:mLanguagesLocaleInfoList){
					mMergedLocaleInfoList.add(new MergedLocaleInfo(mLocaleInfo));
				}
			}
		}else if(2==type){
            ArrayList<String>mLanguagesArrayList = new ArrayList<String>();			
			List<Languages.LocaleInfo> mLanguagesLocaleInfoList2 = null;
	        try { 
	        	//languagesIn.txt文件内容格式：values-pt-rPT、./values-pt-rPT，不带后缀(比如values-pt)的不可以
	        	InputStreamReader inputReader = new InputStreamReader(getResources().getAssets().open("locales/languagesIn.txt")); 
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
				mLanguagesLocaleInfoList2 =Languages.getAllAssetLocalesFromStrings(this, languages, true);
				if(null!=mLanguagesLocaleInfoList2&&mLanguagesLocaleInfoList2.size()>0){
					for(Languages.LocaleInfo mLocaleInfo2:mLanguagesLocaleInfoList2){
						mMergedLocaleInfoList.add(new MergedLocaleInfo(mLocaleInfo2));
					}
				}	
	        }        
		}
		return mMergedLocaleInfoList;
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
	
    public class MergedLocaleInfo{
    	LocaleInfo mLocaleInfo=null;
    	Languages.LocaleInfo mLanguagesLocaleInfo=null;
    	String label=null;
    	Locale locale=null;
    	public MergedLocaleInfo(Languages.LocaleInfo mLanguagesLocaleInfo0){
    		mLanguagesLocaleInfo = mLanguagesLocaleInfo0;
            label = mLanguagesLocaleInfo0.getLabel();
            locale = mLanguagesLocaleInfo0.getLocale();
    	}
    	public MergedLocaleInfo(LocaleInfo mLocaleInfo0){
    		mLocaleInfo = mLocaleInfo0;
            label = mLocaleInfo0.getLabel();
            locale = mLocaleInfo0.getLocale();    		
    	}
    	
        public String getLabel() {
            return (null!=mLocaleInfo)?mLocaleInfo.getLabel():mLanguagesLocaleInfo.getLabel();
        }

        public Locale getLocale() {
            return (null!=mLocaleInfo)?mLocaleInfo.getLocale():mLanguagesLocaleInfo.getLocale();
        }    	
    }
}
