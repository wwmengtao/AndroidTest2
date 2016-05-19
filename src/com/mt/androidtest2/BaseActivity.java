package com.mt.androidtest2;

import android.app.ListActivity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class BaseActivity extends ListActivity implements AdapterView.OnItemClickListener{
	boolean isLogRun=true;
	private ListView mListViewFT=null;
	private ListViewAdapter mListViewAdapterFT = null;	
	private String [] mActivitiesName={"No activity"};;
	private String [] mMethodNameFT={"No method"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);
		ALog.Log("BaseActivity_onCreate");
	}
	
	@Override
	public void onPause(){
		super.onPause();
	}
	
	public ListViewAdapter getListViewAdapterFT(){
		return mListViewAdapterFT;
	}
	
	public void initListFTData(String [] mMethodNameFT){
		if(null==mMethodNameFT){
			mMethodNameFT=this.mMethodNameFT;
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
		String selectedItem = (String) list.getItemAtPosition(position);
		Intent mIntent = null;
		String packname = null;
		String classname = null;
		switch(selectedItem){
			default://打开本应用的Activity
				mIntent=new Intent();
				packname = this.getPackageName();
				classname = packname+"."+selectedItem;
				ALog.Log("packname:"+packname);
				ALog.Log("classname:"+classname);
				mIntent.setComponent(new ComponentName(packname, classname));
				break;
		}
		try{
			startActivity(mIntent);
		}catch(ActivityNotFoundException e){
			e.printStackTrace();
		}
	}
	
	public void initListActivityData(String [] mActivitiesName){
		if(null==mActivitiesName){
			mActivitiesName=this.mActivitiesName;
		}
		ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(this, R.layout.item_getview_android, R.id.listText, mActivitiesName);
        setListAdapter(myAdapter);
	}	
}
