package com.mt.androidtest2;

import java.util.List;
import java.util.Locale;

import com.android.internal.app.LocalePicker.LocaleInfo;
import com.example.androidtest2.R;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemProperties;
import android.widget.ImageView;

public class MainActivity extends Activity {
	boolean isLogRun=true;
	private static final String NOTIFICATION_ID = "CaptivePortal.Notification";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
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
		//1、读写xml文件
		//ALog.howToWriteToXml(this);
		//ALog.howToReadFromXml(this);
		//2、通知栏显示通知
		//showNotification(this,1,null);
		//3、获取当前手机的所有语言列表
		//showAllLocales();
		//saveAllLocales(this);
		//4、判断当前手机VIBEUI的版本
		String lvpVersion = getLVPVersion();
		boolean isVibeUI3_5 = (null!=lvpVersion&&lvpVersion.contains("V3.5"));
		ALog.Log("isVibeUI3_5:"+isVibeUI3_5);
	}
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
	public void showAllLocales(){
		List<LocaleInfo> mLocaleInfoList = ALog.getAllAssetLocales(this);
		if(null==mLocaleInfoList)return;
        String label = null;
        Locale locale = null;
		for(LocaleInfo mLocaleInfo : mLocaleInfoList){
			label = mLocaleInfo.getLabel();
			locale = mLocaleInfo.getLocale();
			ALog.Log("label:"+label);
			ALog.Log("locale:"+locale.toString());
			
		}
	}
	
	/**
	 * 储存当前设备所有语言信息到fileToSave文件中
	 */
    public void saveAllLocales(Context mContext){
		String fileToSave = "Languages.xml";
		String docTag = "Languages";
		ALog.startSaving(mContext,fileToSave,docTag);
		List<LocaleInfo> mLocaleInfoList = ALog.getAllAssetLocales(this);
		if(null==mLocaleInfoList)return;
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
			//
			ALog.etag(tagName);
		}
		ALog.endSaving();
    }
}
