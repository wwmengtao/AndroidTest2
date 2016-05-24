package com.mt.androidtest2;

import java.lang.reflect.Method;
import java.util.HashMap;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserHandle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

public class MultiUserActivity extends BaseActivity{
	boolean isLogRun = true;
	boolean ismLogRun = true;
	int userID=-10;
	boolean mUriSet=false;
	int [] ids_btn = {R.id.btn_torchlight,
			   R.id.btn_widetouch,
			   R.id.btn_none,
			   R.id.btn_USER_OWNER,
			   R.id.btn_USER_CURRENT,
			   R.id.btn_USER_CURRENT_OR_SELF,
			   R.id.btn_USER_ALL};
	final String ACTION_CLICK_FLASHLIGHT = "action_click_flashlight";
	Uri mUri= null;
	HashMap<Integer,String> mHashMap=new HashMap<Integer,String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_multi_user);
		super.initListActivityData(null);
		for(int i=0; i<ids_btn.length; i++){
			((Button)findViewById(ids_btn[i])).setOnClickListener(this);
		}
		/*
		public static final int USER_OWNER = 0;
	    public static final int USER_ALL = -1;
	    public static final int USER_CURRENT = -2;
	    public static final int USER_CURRENT_OR_SELF = -3;
		*/
		mHashMap.put(99, "USER_NOTSET");
		mHashMap.put(0, "USER_OWNER");
		mHashMap.put(-1, "USER_ALL");
		mHashMap.put(-2, "USER_CURRENT");
		mHashMap.put(-3, "USER_CURRENT_OR_SELF");
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		getContentResolver().unregisterContentObserver(mContentObserver);
	}
	/**
	 * 下列torch_on和total_wide_touch都是通知中心里面写入数据库的
	 * SwitchWideTouch.java有 setWideTouch函数，内容如下：
		Settings.System.putIntForUser(
		        mContext.getContentResolver(),
				WIDE_TOUCH ,
				enable ? 1 : 0 ,
				UserHandle.USER_CURRENT);//只修改当前手机用户对应的数据库内容，如当前为New user，如果监听owner用户则监听不到
	* torch_on的读写为Settings.Global.putInt(getContentResolver(), "torch_on", enable?1:0);
	* Settings.Global.putInt最终会调用到putStringForUser(resolver, name, value, UserHandle.myUserId())->NameValueCache.putStringForUser->
	* SettingsProvider.update，从而调用updateGlobalSetting->mutateGlobalSetting如下内容：
	* case MUTATION_OPERATION_UPDATE: {
       	return mSettingsRegistry
                   		.updateSettingLocked(SettingsRegistry.SETTINGS_TYPE_GLOBAL,
                        		UserHandle.USER_OWNER, name, value, getCallingPackage());
       可见，Settings.Global.putInt对应的UserID为写死的UserHandle.USER_OWNER，这和上述Settings.System.putIntForUser情况不同
	*/
	public void onClick(View view){
		switch(view.getId()){
			case R.id.btn_torchlight:
				mUriSet=true;
				mUri= Settings.Global.getUriFor("torch_on");
				if(isTorchServiceExist()){
					openTorchLightService();
				}else{
					openTorchLightBroadcast();
				}
			break;
			case R.id.btn_widetouch://通过通知中心开关WideTouch，观察监听结果
				mUriSet=true;
				mUri= Settings.System.getUriFor("total_wide_touch");
			break;
			case R.id.btn_none:
				userID= 99;
			break;
			case R.id.btn_USER_OWNER:
				userID = UserHandle.USER_OWNER;
			break;
			case R.id.btn_USER_ALL:
				userID = UserHandle.USER_ALL;
			break;			
			case R.id.btn_USER_CURRENT:
				userID = UserHandle.USER_CURRENT;
			break;
			case R.id.btn_USER_CURRENT_OR_SELF:
				userID = UserHandle.USER_CURRENT_OR_SELF;
			break;
		}

		if(mUriSet){
			getContentResolver().unregisterContentObserver(mContentObserver);
			if(userID == 99){
				//getContentResolver().registerContentObserver(mUri, false, mContentObserver);
				reflect_registerContentObserver();
			}else{
				//getContentResolver().registerContentObserver(mUri, false, mContentObserver, userID);
				reflect_registerContentObserver(userID);
			}
		}

	}

	private ContentObserver mContentObserver = new ContentObserver(
			new Handler()) {
		@Override
		public void onChange(boolean selfChange) {
			if(isLogRun)ALog.Log("/-------------------mContentObserver_onChange-------------------/");
			if(ismLogRun)ALog.mLog("userID:"+userID+" userInfo:"+mHashMap.get(userID)+" mUri:"+mUri.toString());
			if(isLogRun)ALog.Log("/-------------------mContentObserver_onChange-------------------/");			
		}
	};
	
	public void reflect_registerContentObserver(){		
		try {
			ContentResolver cr = getContentResolver();
			Class<?>  class_t = cr.getClass();
			Method registerContentObserver = class_t.getMethod("registerContentObserver",new Class[]{Uri.class,boolean.class,ContentObserver.class});
			registerContentObserver.setAccessible(true);
			registerContentObserver.invoke(cr, mUri, false, mContentObserver);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void reflect_registerContentObserver(int userId){		
		try {
			ContentResolver cr = getContentResolver();
			Class<?>  class_t = cr.getClass();
			Method registerContentObserver = class_t.getMethod("registerContentObserver",new Class[]{Uri.class,boolean.class,ContentObserver.class,int.class});
			registerContentObserver.setAccessible(true);
			registerContentObserver.invoke(cr, mUri, false, mContentObserver,userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void openTorchLightBroadcast(){
		//sendBroadcastAsUser(intent_torchlight, new UserHandle(user));
		Intent intent_torchlight;
		intent_torchlight = new Intent(ACTION_CLICK_FLASHLIGHT);
		sendBroadcast(intent_torchlight);
	}
	
	public void openTorchLightService(){
		Intent intent_torchlight = new Intent("com.lenovo.systemuiplus.flashlight.normal_click");
		intent_torchlight	.setComponent(new ComponentName("com.android.systemui",
							"com.android.systemui.lenovo.flashlight.LenovoHdLightService"));
		startService(intent_torchlight);
	}
	
    public boolean isTorchServiceExist() {
    	String packageName = "com.android.systemui";
    	String className = "com.android.systemui.lenovo.flashlight.LenovoHdLightService";
        ComponentName mComponentName = new ComponentName(packageName, className);
        PackageManager pm = getPackageManager();
        try {
            ServiceInfo info = pm.getServiceInfo(mComponentName, 0);
            if (info != null) {
                return true;
            }
            ALog.Log("ServiceInfo equals null!");
            return false;
        } catch (NameNotFoundException e) {
        	e.printStackTrace();
        	//ALog.Log("NameNotFoundException");
            return false;
        }
    }		
}
