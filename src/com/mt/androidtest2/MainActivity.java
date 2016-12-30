package com.mt.androidtest2;

import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.SystemProperties;
import android.os.storage.StorageManager;
import android.os.storage.VolumeInfo;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends BaseActivity{
	boolean isLogRun=false;	
	private String [] mActivitiesName={"ContentResolverDemoActivity","LanguageActivity","VpnActivity","MultiUserActivity","RunningAppProcessesActivity"};	
	private String [] mMethodNameFT={"howToReadFromXml","howToWriteToXml","getProperty",
			"getVolumeInfo"};
	private int osVersion = android.os.Build.VERSION.SDK_INT;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);
		initListActivityData(mActivitiesName);
		initListFTData(mMethodNameFT);
	}

	@Override
	protected void onResume(){
		super.onResume();
		if(isLogRun)ALog.Log("====onResume");
		ALog.Log("osVersion:"+osVersion);
		getLabels();
	}
	
	private void getLabels(){
		String photoLabel = getPhotoLabel();
		ALog.Log("photoLabel:"+photoLabel);
		photoLabel = getPhotoLabel(this.getPackageManager());
		ALog.Log("photoLabel2:"+photoLabel);
        String packageName = "com.google.android.apps.photos";
        String className = "com.google.android.apps.photos.home.HomeActivity";
		photoLabel = getCertainLabel(packageName, className, this.getPackageManager());
		ALog.Log("photoLabel2:"+photoLabel);
		String pName = "com.google.android.calculator";
		String cName = "com.android.calculator2.Calculator";
		String labelCalcu = getCertainLabel(pName, cName, this.getPackageManager());
		ALog.Log("labelCalcu:"+labelCalcu);
		pName = "com.lenovo.scg";
		cName = "camera.CameraLauncher";
		String labelCamera = getCertainLabel(pName, cName, this.getPackageManager());
		ALog.Log("labelCamera:"+labelCamera);
	}
	
	public String getPhotoLabel(){
    	PackageManager pm = this.getPackageManager();
    	String packageName = "com.google.android.apps.photos";
    	String className = "com.google.android.apps.photos/.home.HomeActivity";
    	ComponentName mCN = new ComponentName(packageName,className);
    	String label = null;
    	ActivityInfo mAI = null;
        try {
        	mAI = pm.getActivityInfo(mCN, 0);
        	label = mAI.loadLabel(pm).toString();
        } catch (PackageManager.NameNotFoundException e) {
        	label = null;
        }
        return label;
	}
	
    public String getPhotoLabel(PackageManager pm){
        String label = null;
        String packageName = "com.google.android.apps.photos";
        String className = "com.google.android.apps.photos.home.HomeActivity";
        PackageInfo mPackageInfo = null;
        try {
            mPackageInfo = pm.getPackageInfo(packageName,PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if(null != mPackageInfo){
            ActivityInfo[] mActivities = mPackageInfo.activities;
            for(ActivityInfo mActivityInfo : mActivities){
                if(mActivityInfo.toString().endsWith(className+"}")) {
                    label = mActivityInfo.loadLabel(pm).toString();
                    break;
                }
            }

        }
        return label;
    }
	
    public String getCertainLabel(String packageName, String className, PackageManager pm){
    	if(null==packageName || null==className || null==pm){
    		return null;
    	}
    	String label = null;
    	PackageInfo mPackageInfo = null;
        try {
            mPackageInfo = pm.getPackageInfo(packageName,PackageManager.GET_ACTIVITIES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if(null != mPackageInfo){
            ActivityInfo[] mActivities = mPackageInfo.activities;
            for(ActivityInfo mActivityInfo : mActivities){
                if(mActivityInfo.toString().endsWith(className+"}")) {
                    label = mActivityInfo.loadLabel(pm).toString();
                    break;
                }
            }

        }
        return label;
    }
    
	@Override
	protected void onPause(){
		super.onPause();
	}
	
	@Override
	public void initListFTData(String [] mMethodNameFT){
		super.initListFTData(mMethodNameFT);
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View view,	int position, long id) {
		// TODO Auto-generated method stub
		String methodName = (String)getListViewAdapterFT().mList.get(position).get("itemText"); 
		switch(methodName){
		case "getProperty":
			getProperty();
			break;		
		case "howToReadFromXml":
			howToReadFromXml();
			break;		
		case "howToWriteToXml":
			howToWriteToXml();
			break;	
		case "getVolumeInfo":
			getVolumeInfo();
			break;
		}
	}	
	
	@Override
	public void initListActivityData(String [] mActivitiesName){
		super.initListActivityData(mActivitiesName);
	}
	
	@Override
	protected void onListItemClick(ListView list, View view, int position, long id) {
		super.onListItemClick(list, view, position, id);	
	}
	
	public void howToWriteToXml(){
		//写xml文件
		ALog.howToWriteToXml(this);
	}
	
	public void howToReadFromXml(){
		//读xml文件
		ALog.howToReadFromXml(this);
	}

    public void getProperty(){
		//1、判断当前手机VIBEUI的版本
		String lvpVersion =  SystemProperties.get("ro.lenovo.lvp.version");
		boolean isVibeUI3_5 = (null!=lvpVersion&&lvpVersion.contains("V3.5"));
		ALog.Log("isVibeUI3_5:"+isVibeUI3_5);
		//2、ota_model
		String ota_model = SystemProperties.get("ro.product.ota.model", "NotSet");
		ALog.Log("ota_model:"+ota_model);
		ALog.Log("ota_model:"+ota_model.toLowerCase().endsWith("b36_row"));
    }
    
    /**
     * getVolumeInfo: Set storage type according to detailed storage, such as internal/otg/sdcard.
     */
	public void getVolumeInfo(){
		StorageManager mStorageManager = (StorageManager) this.getSystemService(Context.STORAGE_SERVICE);
		final List<VolumeInfo> volumes = mStorageManager.getVolumes();
		/**
		 * log信息如下：
		 * 07-27 10:01:25.518 11785 11785 D M_T_AT2 : INT getId:private getFsUuid:null
			07-27 10:01:25.518 11785 11785 D M_T_AT2 : SDC getId:public:179,65 getFsUuid:5456-0914
			07-27 10:01:25.518 11785 11785 D M_T_AT2 : SDC getId:emulated getFsUuid:null
			07-27 10:01:25.518 11785 11785 D M_T_AT2 : OTG getId:public:8,1 getFsUuid:3698-1116
		 */
        for (VolumeInfo mVolume : volumes) {
            if (VolumeInfo.ID_PRIVATE_INTERNAL.equals(mVolume.getId())) {//Internal Storage
            	ALog.Log("INT getId:"+mVolume.getId()+" getFsUuid:"+mVolume.getFsUuid());
            } else {
                if (mVolume != null && mVolume.getDisk() != null && mVolume.getDisk().isUsb()) {//OTG Storage
                	ALog.Log("OTG getId:"+mVolume.getId()+" getFsUuid:"+mVolume.getFsUuid());
                } else {//SDCard
                	ALog.Log("SDC getId:"+mVolume.getId()+" getFsUuid:"+mVolume.getFsUuid());
                }
            }
        }
	}
}
