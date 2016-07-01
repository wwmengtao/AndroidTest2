package com.mt.androidtest2.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.mt.androidtest2.ALog;
import com.mt.androidtest2.BaseActivity;
import com.mt.androidtest2.R;
import com.mt.androidtest2.XmlOperator;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;


public class ContentResolverDemoActivity extends BaseActivity {
	private boolean isLogRun = true;
	private String ContProvider_URI = "content://";
	private String [] mMethodNameFT={
			"readContentProviderFile",
			"insert","update","query","delete"};
	private ContentResolver mContentResolver=null;
	private ArrayList<Uri>uriCPFile=null;
	//
	private ArrayList<String>mAttrAL=null;
	private ArrayList<String>mTextAL=null;
	//
	private Uri sqliteUri=null;
	private String sqlitekey=null;
	private String sqliteValue=null;
	//
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);
		initListFTData(mMethodNameFT);
		initListActivityData(null);
		//
		ContProvider_URI += ContentProviderDemo.authority;
		mContentResolver = getContentResolver();
		initUriCPFile();
		//
		initSqliteOperator();
		//确认是否具有EXTERNAL_STORAGE的读写权限
		requestPermissions();
	}
	
	@Override
	public void onResume(){
		super.onResume();
	}
	
	public void requestPermissions(){
		this.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
				Manifest.permission.WRITE_EXTERNAL_STORAGE},REQUEST_EXTERNAL_STORAGE);
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
		switch (requestCode){
			case REQUEST_EXTERNAL_STORAGE:
				if (permissions.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
					Toast.makeText(this, "Permission already got!", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
				}
				break;
			default:
	            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
	            break;
			}
	  }
	
	/**
	 * 以下将内部/外部存储中的共享文件对应的Uri加入uriCPFile中
	 */
	public void initUriCPFile(){
		uriCPFile=new ArrayList<Uri>();
		uriCPFile.add(Uri.parse(ContProvider_URI+"/myAssets_FilesDir/test/test.txt"));
		uriCPFile.add(Uri.parse(ContProvider_URI+"/test.txt"));
		uriCPFile.add(Uri.parse(ContProvider_URI+"/Documents/mt.txt"));
		uriCPFile.add(Uri.parse(ContProvider_URI+"/Download/mt.txt"));
		uriCPFile.add(Uri.parse(ContProvider_URI+"/mt.txt"));
	}
	
	/**
	 * 以下为写入数据库做数据准备
	 */
	public void initSqliteOperator(){
		readXmlForSqlite(getApplicationContext());
		sqliteUri=Uri.parse(ContProvider_URI+ContentProviderDemo.SqliteURI_sqlite);
		sqlitekey=DataBaseHelper.getKeyName();
		sqliteValue=DataBaseHelper.getValueName();
	}
	
	public void readXmlForSqlite(Context context){
		String fileName="locales/strings.xml";
		String tagOfDoc="resources";
		String eleName="string";
		String attr="name";
		XmlOperator mXmlOperator=new XmlOperator(context);
		mXmlOperator.setInfomation(fileName, tagOfDoc, eleName, attr);
		mXmlOperator.readFromXml(1);
		mAttrAL=mXmlOperator.getAttrArrayList();
		mTextAL=mXmlOperator.getTextArrayList();
		if(null==mAttrAL||null==mTextAL){
			throw new IllegalArgumentException("mAttrAL or mTextAL null!");
		}else if(mAttrAL.size()!=mTextAL.size()){
			throw new IllegalArgumentException("mAttrAL and mTextAL size not equal!");
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View view,	int position, long id) {
		// TODO Auto-generated method stub
		String methodName = (String)getListViewAdapterFT().mList.get(position).get("itemText"); 
		switch(methodName){
			case "readContentProviderFile":
				for(Uri mUri : uriCPFile){
					readContentProviderFile(mUri);
				}
				break;
			case "insert":
				insert();
				break;
			case "update":
				update();
				break;
			case "query":
				query();
				break;
			case "delete":
				delete();
				break;						
		}
	}
	
	/**
	 * readContentProviderFile：从ContentProvider标识的文件中读取内容
	 * @return
	 */
	private boolean readContentProviderFile(Uri mUri) {
		InputStream inputStream = null;
		BufferedReader reader = null;
		StringBuffer builder = null;
		String line = null;
		try {
			inputStream = mContentResolver.openInputStream(mUri);
			if (inputStream != null) {
		        reader = new BufferedReader(new InputStreamReader(inputStream));
		        builder = new StringBuffer();
	            while ((line = reader.readLine()) != null) {
	                builder.append(line);
	                builder.append("\n");
	            }
				ALog.Log("/---File content---/");
				ALog.Log(builder.toString());
				ALog.Log("/---File content---/");
			}
			return true;			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
					inputStream = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return false;
	}
	
	/*
	 * 避免在主线程中执行getWritableDatabase()或者getWritableDatabase()这两个耗时操作，可以在ContentProvider.onCreate()中开启。
    */
	public void insert() {
		if(isLogRun)ALog.Log2("CRDemoActivity_insert");
		ContentValues values = null;
		for(int i=0;i<mAttrAL.size();i++){
			values = new ContentValues();
			values.put(sqlitekey,mAttrAL.get(i));
			values.put(sqliteValue, mTextAL.get(i));
			mContentResolver.insert(sqliteUri, values);
		}
	}

	public void update() {
		if(isLogRun)ALog.Log2("CRDemoActivity_update");
		// 创建一个ContentValues对象
		ContentValues values = new ContentValues();
		values.put(sqliteValue, "mt");
		mContentResolver.update(sqliteUri, values, sqlitekey+" = ?", new String[]{"string_name5"});
	}

	public void query() {
		if(isLogRun)ALog.Log2("CRDemoActivity_query");
		String id,name=null;
		Cursor cursor = mContentResolver.query(sqliteUri, null, null, null, null);
		// 将光标移动到下一行，从而判断该结果集是否还有下一条数据，如果有则返回true，没有则返回false
		while (cursor.moveToNext()) {
			id = cursor.getString(cursor.getColumnIndex(sqlitekey));
			name = cursor.getString(cursor.getColumnIndex(sqliteValue));
			ALog.Log("sqlitekey: "+id+" sqliteValue: "+name);
		}
		cursor.close();
	}

	public void delete() {
		if(isLogRun)ALog.Log2("CRDemoActivity_delete");
		//调用SQLiteDatabase对象的delete方法进行删除操作
		//第一个参数String：表名
		//第二个参数String：条件语句
		//第三个参数String[]：条件值
		mContentResolver.delete(sqliteUri	, sqlitekey+" = ?", new String[]{"string_name4"});
	}
}
