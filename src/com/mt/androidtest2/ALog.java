package com.mt.androidtest2;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.xmlpull.v1.XmlSerializer;

import com.android.internal.util.FastXmlSerializer;

import android.app.ActivityManager;
import android.content.Context;
import android.os.UserHandle;
import android.util.AtomicFile;
import android.util.Log;

public class ALog {
	public  static String TAG_M = "M_T_AT2";
	/*---------------------------------------------*/
	static String namespace=null;
	static XmlSerializer serializer = null;
	static AtomicFile mAtomicFile = null;
	static FileOutputStream os = null;
	static String tag_Doc = null;
	static boolean ifStartSaving = false;
	/*---------------------------------------------*/
	
	public static void Log(String info){
		Log.e(TAG_M,info);
	}
	public static void mLog(String info){
		Log.e(TAG_M,info+" Uid:"+getUid()+" Pid:"+getPid()+" ActivityManager.getCurrentUser:"+amGetCurrentUser()+
				" UserHandle.myUserId:"+uhMyUserId());
	}
	public static void printStackTrace(String info){
		RuntimeException RTE = new RuntimeException(info);
		RTE.fillInStackTrace();
		Log.e(TAG_M, "Pid:"+getPid()+" "+"Called:", RTE);
	}
	public static int getPid(){
		return android.os.Process.myPid();
	}
	public static int getUid(){
		return android.os.Process.myUid();
	}
	public static int amGetCurrentUser(){
		return ActivityManager.getCurrentUser();
	}
	public static int uhMyUserId(){
		return UserHandle.myUserId();
	}
	
    /**
     * howToWriteToXml：Android环境下调用ALog中的方法写xml
     */
    public static void howToWriteToXml(Context mContext){
		String fileToSave = "1.xml";
		String docTag = "Document";
		ALog.startSaving(mContext,fileToSave,docTag);
		for(int i=0;i<5;i++){
			ALog.stag("name"+i);
			ALog.attr("attr"+i, "123");
			ALog.etag("name"+i);
		}
		ALog.endSaving();
    }
	
	public static void startSaving(Context mContext ,String fileName,String tagOfDoc){
			ifStartSaving = true;
			tag_Doc = tagOfDoc;
			mAtomicFile = new AtomicFile(new File(mContext.getFilesDir(),fileName));
			Log("mAtomicFile:"+mAtomicFile.getBaseFile().getAbsolutePath());
	        try {
	            os = mAtomicFile.startWrite();
                serializer = new FastXmlSerializer();
                serializer.setOutput(new BufferedOutputStream(os), "utf-8");
                serializer.startDocument(null, true);
                serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
                serializer.startTag(namespace, tag_Doc);
	        } catch (IOException ex) {
	            Log.e(TAG_M, "Failed to start saving...", ex);
	        }
	}
	
	public static void stag(String tag){
		if(!ifStartSaving){
			Log("stag error : startSaving is not called!");
			return;
		}
		try {
			serializer.startTag(namespace, tag);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void attr(String attrName,String attrValue){
		if(!ifStartSaving){
			Log("attr error : startSaving is not called!");
			return;
		}
		try {
			serializer.attribute(namespace,attrName,attrValue);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void etag(String tag){
		if(!ifStartSaving){
			Log("etag error : startSaving is not called!");
			return;
		}
		try {
			serializer.endTag(namespace, tag);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void endSaving(){
		if(!ifStartSaving){
			Log("endSaving error : startSaving is not called!");
			return;
		}
		ifStartSaving = false;
		boolean success = false;
		try{
	        try {
	        	serializer.endTag(namespace, tag_Doc);
				serializer.endDocument();
	            serializer.flush();
	            success = true;
	        } finally {
	            if (success) {
	                mAtomicFile.finishWrite(os);
	            } else {
	                mAtomicFile.failWrite(os);
	            }
	        } 
        } catch (IOException ex) {
            Log.e(TAG_M, "Failed to save mAtomicFile:"+mAtomicFile.toString(), ex);
        }
	}
	
	/**
	 * saveToXml：此方法适合一次性写入的情况，相比较howToWriteToXml方法使用起来不灵活
	 * @param mContext
	 * @param fileName
	 */
	public  static void saveToXml(Context mContext ,String fileName) {
	     final FileOutputStream os;
			AtomicFile mAtomicFile = new AtomicFile(new File(mContext.getFilesDir(),fileName));
			Log("The name of file saved:"+mAtomicFile.getBaseFile().getAbsolutePath());
	        try {
	            os = mAtomicFile.startWrite();
	            boolean success = false;
	            try {
	                XmlSerializer serializer = new FastXmlSerializer();
	                serializer.setOutput(new BufferedOutputStream(os), "utf-8");
	                serializer.startDocument(null, true);
	                serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
	                //start to save ..........
	                serializer.endDocument();        
	                serializer.flush();
	                success = true;
	            } finally {
	                if (success) {
	                    mAtomicFile.finishWrite(os);
	                } else {
	                    mAtomicFile.failWrite(os);
	                }
	            }
	        } catch (IOException ex) {
	            Log.e(TAG_M, "Failed to save mServiceProcessesByxx", ex);
	        }
	    }
}
