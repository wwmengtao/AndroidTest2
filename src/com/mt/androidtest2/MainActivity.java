package com.mt.androidtest2;

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

public class MainActivity extends Activity implements View.OnClickListener{
	boolean isLogRun=true;
	private static final String NOTIFICATION_ID = "CaptivePortal.Notification";
	Button btn=null;
	int [] buttonID = {R.id.btn};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		for(int i=0;i<buttonID.length;i++){
			btn = (Button)findViewById(buttonID[i]);
			btn.setOnClickListener(this);
		}
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
		Languages mLanguages = new Languages(this);
		mLanguages.showAllLocales(0);
		mLanguages.saveAllLocales(2,false,true);//保存语言信息，(不)需要细节内容，(不)需要中文注解
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
}
