package com.mt.androidtest2;

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
		//1、读写xml文件
		ALog.howToWriteToXml(this);
		ALog.howToReadFromXml(this);
		//2、通知栏显示通知
		//showNotification(this,1,null);
	}
	
	@Override
	public void onPause(){
		super.onPause();
		//2、取消通知栏内容的显示
		//cancelNotification(this, 1);
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

}
