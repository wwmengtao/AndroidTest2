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
		//下列*代表包名
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
    		mDir = mContext.getFilesDir();//存储路径为： /data/data/[package.name]/files/
    		outFile = new File(mDir, fileName);
    		osw = new FileOutputStream(outFile);
    		break;
    	case 1:
	        osw = mContext.openFileOutput(fileName, Context.MODE_PRIVATE);//openFileOutput效果和case 0等价
	        break;
    	case 10:
    		mDir = mContext.getCacheDir();//存储路径为： /data/data/[package.name]/cache/
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
    		mDir = mContext.getFilesDir();//存储路径为： /data/data/[package.name]/files/
	        break;
    	case 10:
    		mDir = mContext.getCacheDir();//存储路径为： /data/data/[package.name]/cache/
    		break;
    	}
    	fin = new File(mDir, fileName);
	    try {
	        reader = new BufferedReader(new InputStreamReader(new FileInputStream(fin), "utf-8"));
	        String line;
	        while ((line = reader.readLine()) != null) {
	            data.append(line);
	            ALog.Log("读入数据:"+line.toString());
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
	
	//原生资源的读取res/raw文件夹下内容，读取asset内容需要用到AssetManager
	public void readRawResources(){
		String res = null; 
		try{
		    //得到资源中的Raw数据流
		    InputStream in = mContext.getResources().openRawResource(R.raw.taido); 
		    //得到数据的大小
		    int length = in.available();       
		    byte [] buffer = new byte[length];        
		    //读取数据
		    in.read(buffer);         
		    //按照文件编码类型选择合适的编码，如果不调整会乱码 
		    res = EncodingUtils.getString(buffer, "UTF-8"); 
		    ALog.Log(res);
		    //关闭
		    in.close();            
		}catch(Exception e){ 
		    e.printStackTrace();         
		}
	}
	
}
