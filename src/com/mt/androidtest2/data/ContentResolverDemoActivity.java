package com.mt.androidtest2.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.mt.androidtest2.ALog;
import com.mt.androidtest2.BaseActivity;
import com.mt.androidtest2.R;
import com.mt.androidtest2.tool.XmlOperator;

public class ContentResolverDemoActivity extends BaseActivity {
	private boolean isLogRun = true;
	private String ContProvider_URI = "content://";
	private String [] mMethodNameFT={
			"readContentProviderFile",
			"com.mt.androidtest.cpdemo/sqlite","insert","update","query","delete",
			"com.mt.androidtest.cpdemo/grant","insert2","update2","query2","delete2"};
	private ContentResolver mContentResolver=null;
	//
	public static final String Provider_Authority="com.mt.androidtest.cpdemo";
	public static final String SqliteURI_sqlite="/sqlite";
	public static final String GrantURI_grant="/grant";
	//
	private ArrayList<Uri>uriCPFile=null;
	//
	private ArrayList<String>mAttrAL=null;
	private ArrayList<String>mTextAL=null;
	//
	private Uri sqliteUri=null;
	private Uri grantUri=null;	
	private String sqlitekey=null;
	private String sqliteValue=null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		//permissionsRequiredBase = permissionsRequired;
		ALog.Log("CRDA_onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base);
		initListFTData(mMethodNameFT);
		initListActivityData(null);
		//
		ContProvider_URI += Provider_Authority;
		mContentResolver = getContentResolver();
		initUriCPFile();
		//
		initSqliteOperator();
	}
	
	@Override
	public void onResume(){
		super.onResume();
	}

	/**
	 * ���½��ڲ�/�ⲿ�洢�еĹ����ļ���Ӧ��Uri����uriCPFile��
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
	 * ����Ϊд�����ݿ�������׼��
	 */
	public void initSqliteOperator(){
		readXmlForSqlite(getApplicationContext());
		sqliteUri=Uri.parse(ContProvider_URI+SqliteURI_sqlite);
		sqlitekey=DataBaseHelper.getKeyName();
		sqliteValue=DataBaseHelper.getValueName();
		//
		grantUri=Uri.parse(ContProvider_URI+GrantURI_grant);
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
			//����xx2������Ҫ��ʱȨ����Ȩ��ֱ��ִ�лᱨ��
			case "insert2":
				insert2();
				break;
			case "update2":
				update2();
				break;
			case "query2":
				query2();
				break;
			case "delete2":
				delete2();
				break;	
		}
	}
	
	/**
	 * readContentProviderFile����ContentProvider��ʶ���ļ��ж�ȡ���ݡ�����com.mt.androidtest��AndroidManifest.xml
	 * �ļ��ж�provider android:name=".data.ContentProviderDemo"�����˶�дȨ�����£�
	 * android:readPermission="android.permission.READ_CALENDAR"
	 * android:writePermission="android.permission.WRITE_CALENDAR" 
	 * ���com.mt.androidtest2��ContentResolverDemoActivity�ڶ�дcom.mt.androidtest��provider����ʱ��߱�������дȨ�ޡ�
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
	 * ���������߳���ִ��getWritableDatabase()����getWritableDatabase()��������ʱ������������ContentProvider.onCreate()�п�����
    */
	public void insert() {
		if(isLogRun)ALog.Log2("CRDemoActivity_insert_sqlite");
		try{
			mContentResolver.insert(sqliteUri, new ContentValues());
		}catch(SecurityException se){
			ALog.Log("SecurityException_insert");
			se.printStackTrace();
			Toast.makeText(this, "SecurityException\ninsert", Toast.LENGTH_SHORT).show();
		}
	}

	public void insert2() {
		if(isLogRun)ALog.Log2("CRDemoActivity_insert_grant");
		try{
			mContentResolver.insert(grantUri, new ContentValues());
		}catch(SecurityException se){
			ALog.Log("SecurityException_insert2");
			se.printStackTrace();
			Toast.makeText(this, "SecurityException\ninsert2", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void update() {
		if(isLogRun)ALog.Log2("CRDemoActivity_update_sqlite");
		// ����һ��ContentValues����
		ContentValues values = new ContentValues();
		values.put(sqliteValue, "mt");
		try{
			mContentResolver.update(sqliteUri, values, sqlitekey+" = ?", new String[]{"string_name5"});
		}catch(SecurityException se){
			ALog.Log("SecurityException_update");
			se.printStackTrace();
			Toast.makeText(this, "SecurityException\nupdate", Toast.LENGTH_SHORT).show();
		}
	}

	public void update2() {
		if(isLogRun)ALog.Log2("CRDemoActivity_update_grant");
		// ����һ��ContentValues����
		ContentValues values = new ContentValues();
		values.put(sqliteValue, "mt");
		try{
			mContentResolver.update(grantUri, values, sqlitekey+" = ?", new String[]{"string_name5"});
		}catch(SecurityException se){
			ALog.Log("SecurityException_update2");
			se.printStackTrace();
			Toast.makeText(this, "SecurityException\nupdate2", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void query() {
		if(isLogRun)ALog.Log2("CRDemoActivity_query_sqlite");
		Cursor cursor = null;
		try{
			cursor = mContentResolver.query(sqliteUri, null, null, null, null);
		}catch(SecurityException se){
			ALog.Log("SecurityException_query");
			se.printStackTrace();
			Toast.makeText(this, "SecurityException\nquery", Toast.LENGTH_SHORT).show();
		}finally{
			if(null != cursor)cursor.close();
		}
	}

	public void query2() {
		if(isLogRun)ALog.Log2("CRDemoActivity_query_grant");
		try{
			mContentResolver.query(grantUri, null, null, null, null);
		}catch(SecurityException se){
			ALog.Log("SecurityException_query2");
			se.printStackTrace();
			Toast.makeText(this, "SecurityException\nquery2", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void delete() {
		if(isLogRun)ALog.Log2("CRDemoActivity_delete_sqlite");
		//����SQLiteDatabase�����delete��������ɾ������
		//��һ������String������
		//�ڶ�������String���������
		//����������String[]������ֵ
		try{
			mContentResolver.delete(sqliteUri	, sqlitekey+" = ?", new String[]{"string_name4"});
		}catch(SecurityException se){
			ALog.Log("SecurityException_delete");
			se.printStackTrace();
			Toast.makeText(this, "SecurityException\ndelete", Toast.LENGTH_SHORT).show();
		}
	}

	public void delete2() {
		if(isLogRun)ALog.Log2("CRDemoActivity_delete_grant");
		try{
			mContentResolver.delete(grantUri, null, null);
		}catch(SecurityException se){
			ALog.Log("SecurityException_delete2");
			se.printStackTrace();
			Toast.makeText(this, "SecurityException\ndelete2", Toast.LENGTH_SHORT).show();
		}
	}
}
