package com.mt.androidtest2;

import com.example.androidtest2.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemProperties;

public class MainActivity extends Activity {
	boolean isLogRun=true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public void onResume(){
		super.onResume();
		if(isLogRun)ALog.Log("====onResume:"+getLocale());
		writeToXml(this);//或者调用ALog.howToWriteToXml(this);
	}
	
    /**
     * writeToXml：Android环境下调用ALog中的方法写xml
     */
    public void writeToXml(Context mContext){
		String fileToSave = "1.xml";
		String docTag = "Document";
		ALog.startSaving(mContext,fileToSave,docTag);
		for(int i=0;i<5;i++){
			ALog.stag("name"+i);
			ALog.attr("attr1", "123");
			ALog.etag("name"+i);
		}
		ALog.endSaving();
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

}
