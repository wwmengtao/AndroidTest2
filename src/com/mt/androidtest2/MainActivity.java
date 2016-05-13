package com.mt.androidtest2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemProperties;
import android.view.View;
import android.widget.Button;

import com.mt.androidtest2.R;

public class MainActivity extends Activity implements View.OnClickListener{
	boolean isLogRun=true;
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
	}
	
	public void testFunctions(){
		//1、文件操作
		//fileOperation();
		//2、读写xml文件
		//ALog.howToWriteToXml(this);
		//ALog.howToReadFromXml(this);
		//3、获取当前手机的所有语言列表
		Languages mLanguages = new Languages(this);
		mLanguages.showCurrentLocale();
		//mLanguages.showAllLocales(0);
		//mLanguages.saveAllLocales(2,false,true);//保存语言信息，(不)需要细节内容，(不)需要中文注解
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
}
