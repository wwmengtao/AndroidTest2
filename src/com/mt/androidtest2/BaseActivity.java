package com.mt.androidtest2;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.app.ListActivity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class BaseActivity extends ListActivity implements Handler.Callback,AdapterView.OnItemClickListener,View.OnClickListener{
	boolean isLogRun=true;
    private int AndroidVersion=-1;
    private static Handler mHandler=null;
	private LinearLayout mLinearlayout_listview_android=null;
	private LinearLayout mLinearlayout_listview_functions=null;
	private ListView mListViewFT=null;
	private ListViewAdapter mListViewAdapterFT = null;	
	private static WeakReference<BaseActivity>mBaseActivityWR=null;	
    private ArrayList<String>mActivitiesName=null;
	private Intent mIntent = null;
	private String packageName = null;
	private String className = null;		    
	private String selectedItem=null;
	//
	private static final int REQUEST_PERMISSION_CODE = 0x001;
    protected String []permissionsRequiredBase = null;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		AndroidVersion =Build.VERSION.SDK_INT;
		requestPermissions(permissionsRequiredBase);
		super.onCreate(savedInstanceState);
		//ALog.Log("BaseActivity_onCreate");
		packageName = this.getPackageName();
		mBaseActivityWR=new WeakReference<BaseActivity>(this);
		getActivities(this);
	}
	
	@Override
	protected void onResume(){
		super.onResume();
	}
	
	@Override
	protected void onPause(){
		if(null!=mHandler){
			mHandler.removeCallbacksAndMessages(null);//避免内存泄露
		}
		super.onPause();
	}
	
	
	/**
	 * getActivities：获取当前应用AndroidManifest.xml文件中所有<activity>节点信息
	 * @param mContext
	 */
	public void getActivities(Context mContext) {
		ActivityInfo[] activities=null;
		try {
			activities = mContext.getPackageManager().getPackageInfo(this.getPackageName(),PackageManager.GET_ACTIVITIES).activities;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			finish();
		}
		if(null==activities)finish();
		mActivitiesName = new ArrayList<String>();
		ActivityInfo mActivityInfo=null;
	      for (int i=0;i<activities.length;i++) {
	    	  mActivityInfo=activities[i];
	    	  if(null!=mActivityInfo){
	    		  //ALog.Log(""+mActivityInfo.name);
	    		  mActivitiesName.add(mActivityInfo.name);
	    	  }
	      }
	  }
	
	public String getActivityName(String str){
		if(null==str||null==mActivitiesName)return null;
		for(String mStr : mActivitiesName){
			if(mStr.endsWith(str)){
				return mStr;
			}
		}
		return null;
	}	
	
	public static Handler getHandler(){
        if (mHandler == null) {
        	BaseActivity mBaseActivity=mBaseActivityWR.get();
        	if(null!=mBaseActivity){
        		mHandler = new Handler(mBaseActivity);
        	}
        }
		return mHandler;
	}
	
	@Override
	public boolean handleMessage(Message msg) {
		return true;
	}
	
	public ListViewAdapter getListViewAdapterFT(){
		return mListViewAdapterFT;
	}
	
	public void initListFTData(String [] mMethodNameFT){
		if(null==mMethodNameFT){
			mLinearlayout_listview_functions=(LinearLayout)findViewById(R.id.linearlayout_listview_functions);
			mLinearlayout_listview_functions.setVisibility(View.GONE);
			return;
		}
		mListViewAdapterFT = new ListViewAdapter(this);
		mListViewAdapterFT.setMode(2);
		mListViewAdapterFT.setupList(mMethodNameFT);
		mListViewFT=(ListView)findViewById(R.id.listview_functions);	
		mListViewFT.setOnItemClickListener(this);		
		mListViewFT.setAdapter(mListViewAdapterFT);
		ListViewAdapter.setListViewHeightBasedOnChildren(mListViewFT);
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View view,	int position, long id) {
		// TODO Auto-generated method stub
	}	
	
	@Override
	protected void onListItemClick(ListView list, View view, int position, long id) {
		super.onListItemClick(list, view, position, id);
		//以下操作为打开本应用内部的Activity
		mIntent = new Intent();
		selectedItem = (String) list.getItemAtPosition(position);
		if(null==(className = getActivityName(selectedItem)))return;
		mIntent.setComponent(new ComponentName(packageName, className));
		try{
			startActivity(mIntent);
		}catch(ActivityNotFoundException e){
			e.printStackTrace();
		}
	}
	
	public void initListActivityData(String [] mActivitiesName){
		if(null==mActivitiesName){
			mLinearlayout_listview_android=(LinearLayout)findViewById(R.id.linearlayout_listview_android);
			mLinearlayout_listview_android.setVisibility(View.GONE);
			return;
		}
		ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this, R.layout.item_getview_android, R.id.listText, mActivitiesName);
        setListAdapter(myAdapter);
	}	
	
	@Override
	public void onClick(View view){
	}
	
	//以下申请权限
	public void requestPermissions(String [] permissionsRequired){
		if(AndroidVersion<=22)return;
		if(null!=permissionsRequired && permissionsRequired.length>0){
			this.requestPermissions(permissionsRequired,REQUEST_PERMISSION_CODE);
		}
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
		switch (requestCode){
			case REQUEST_PERMISSION_CODE:
				if (permissions.length != 0 && isAllGranted(grantResults)){
					Toast.makeText(this, "Get all Permissions!", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(this, "Not get all Permissions!", Toast.LENGTH_SHORT).show();
				}
				break;
			default:
	            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	            break;
			}
	  }
	
	public boolean isAllGranted(int[] grantResults){
		if(null==grantResults)return false;
		for(int i=0;i<grantResults.length;i++){
			if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
				return false;
			}
		}
		return true;
	}
}
