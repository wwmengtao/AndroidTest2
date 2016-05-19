package com.mt.androidtest2;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

public class FileOperateActivity extends BaseActivity {
	private String [] mMethodNameFT={"FileOperate"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ALog.Log("FileOperateActivity_onCreate");
		initListFTData(mMethodNameFT);
		initListActivityData(null);
	}
	
	@Override
	public void onResume(){
		super.onResume();
		ALog.Log("FileOperateActivity_onResume");
	}
	
	@Override
	public void initListFTData(String [] mMethodNameFT){
		super.initListFTData(mMethodNameFT);
	}
	
	public void initListActivityData(String [] mActivitiesName){
		super.initListActivityData(mActivitiesName);
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View view,	int position, long id) {
		// TODO Auto-generated method stub
	}	
}
