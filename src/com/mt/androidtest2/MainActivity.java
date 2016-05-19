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
		//1、文件操作
		//fileOperation();
		//2、读写xml文件
		//ALog.howToWriteToXml(this);
		//ALog.howToReadFromXml(this);

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

    public String getLVPVersion(){
    	String lvpVersion = SystemProperties.get("ro.lenovo.lvp.version");
    	return lvpVersion;
    }
}
