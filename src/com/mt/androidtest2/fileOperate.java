package com.mt.androidtest2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.util.EncodingUtils;

import android.content.Context;
import android.os.Environment;

import com.example.androidtest2.R;

public class fileOperate {
	Context mContext = null;
	public fileOperate(Context mContext){
		this.mContext = mContext;
	}
	public void listDirs(){
		File mDataDirectory = Environment.getDataDirectory();// /data
		File mDownloadCacheDirectory = Environment.getDownloadCacheDirectory();// /cache
		File mExternalStorageDirectory = Environment.getExternalStorageDirectory();//  /mnt/sdcard
		File mExternalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory("test");// /mnt/sdcard/test
		File mRootDirectory = Environment.getRootDirectory();// /system
		//����*�������
		String mPackageCodePath = mContext.getPackageCodePath();// /data/app/*.apk
		String mPackageResourcePath = mContext.getPackageResourcePath();//  /data/app/*.apk
		File mCacheDir = mContext.getCacheDir();// /data/data/com.my.app/cache
		File mDatabasePath = mContext.getDatabasePath("test");// /data/data/*/test
		File mDir = mContext.getDir("", Context.MODE_PRIVATE);// /data/data/*/app_test
		File FilesDir = mContext.getFilesDir();
		//
		ALog.Log("mDataDirectory:"+mDataDirectory);
		ALog.Log("mDownloadCacheDirectory:"+mDownloadCacheDirectory);
		ALog.Log("mExternalStorageDirectory:"+mExternalStorageDirectory);		
		ALog.Log("mExternalStoragePublicDirectory:"+mExternalStoragePublicDirectory);
		ALog.Log("mRootDirectory:"+mRootDirectory);
		ALog.Log("mPackageCodePath:"+mPackageCodePath);			
		ALog.Log("mPackageResourcePath:"+mPackageResourcePath);		
		ALog.Log("mCacheDir:"+mCacheDir);
		ALog.Log("mDatabasePath:"+mDatabasePath);
		ALog.Log("mDir:"+mDir);				
	}
	
	public FileOutputStream getFileOutputStream(String fileName,int type) throws FileNotFoundException{
		File mDir = null;		
		File outFile = null;
	    FileOutputStream osw = null;
    	switch(type){
    	case 0:
    		mDir = mContext.getFilesDir();//�洢·��Ϊ�� /data/data/[package.name]/files/
    		outFile = new File(mDir, fileName);
    		osw = new FileOutputStream(outFile);
    		break;
    	case 1:
	        osw = mContext.openFileOutput(fileName, Context.MODE_PRIVATE);//openFileOutputЧ����case 0�ȼ�
	        break;
    	case 10:
    		mDir = mContext.getCacheDir();//�洢·��Ϊ�� /data/data/[package.name]/cache/
    		outFile = new File(mDir, fileName);
    		osw = new FileOutputStream(outFile);	    
    		break;
    	}
    	return osw;
	}
	
	public void writeToFile(String fileName, String data, int type) {
	    FileOutputStream osw = null;
	    try {
	    	osw = getFileOutputStream(fileName, type);
	        osw.write(data.getBytes());
	        osw.flush();
	    } catch (Exception e) {
	    	e.printStackTrace();
	    } finally {
	        try {
	            osw.close();
	        } catch (Exception e) {
	           e.printStackTrace();
	        }
	    }
	}
	
	public void readFromFile(String fileName,int type) {
		File fin = null;
	    StringBuilder data = new StringBuilder();
	    BufferedReader reader = null;
		File mDir = null;
    	switch(type){
    	case 0:
    	case 1:
    		mDir = mContext.getFilesDir();//�洢·��Ϊ�� /data/data/[package.name]/files/
	        break;
    	case 10:
    		mDir = mContext.getCacheDir();//�洢·��Ϊ�� /data/data/[package.name]/cache/
    		break;
    	}
    	fin = new File(mDir, fileName);
	    try {
	        reader = new BufferedReader(new InputStreamReader(new FileInputStream(fin), "utf-8"));
	        String line;
	        while ((line = reader.readLine()) != null) {
	            data.append(line);
	            ALog.Log("��������:"+line.toString());
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            reader.close();
	        } catch (Exception e) {
	            ;
	        }
	    }
	}	
	
	//ԭ����Դ�Ķ�ȡres/raw�ļ��������ݣ���ȡasset������Ҫ�õ�AssetManager
	public void readRawResources(){
		String res = null; 
		try{
		    //�õ���Դ�е�Raw������
		    InputStream in = mContext.getResources().openRawResource(R.raw.taido); 
		    //�õ����ݵĴ�С
		    int length = in.available();       
		    byte [] buffer = new byte[length];        
		    //��ȡ����
		    in.read(buffer);         
		    //�����ļ���������ѡ����ʵı��룬��������������� 
		    res = EncodingUtils.getString(buffer, "UTF-8"); 
		    ALog.Log(res);
		    //�ر�
		    in.close();            
		}catch(Exception e){ 
		    e.printStackTrace();         
		}
	}
	
}
