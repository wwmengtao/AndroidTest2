package com.mt.androidtest2;

import android.os.Bundle;
import android.os.SystemProperties;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends BaseActivity{
	boolean isLogRun=false;
	private String [] mActivitiesName={"FileOperateActivity","LanguageActivity","VpnActivity"};	
	private String [] mMethodNameFT={"getLVPVersion"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initListActivityData(mActivitiesName);
		initListFTData(mMethodNameFT);
	}

	@Override
	public void onResume(){
		super.onResume();
		if(isLogRun)ALog.Log("====onResume");
		testFunctions();
	}
	
	@Override
	public void onPause(){
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
		case "getLVPVersion":
			//判断当前手机VIBEUI的版本
			String lvpVersion = getLVPVersion();
			boolean isVibeUI3_5 = (null!=lvpVersion&&lvpVersion.contains("V3.5"));
			ALog.Log("isVibeUI3_5:"+isVibeUI3_5);
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
	
	public void testFunctions(){
		//2、读写xml文件
		//ALog.howToWriteToXml(this);
		//ALog.howToReadFromXml(this);

	}
	


    public String getLVPVersion(){
    	String lvpVersion = SystemProperties.get("ro.lenovo.lvp.version");
    	return lvpVersion;
    }
}
