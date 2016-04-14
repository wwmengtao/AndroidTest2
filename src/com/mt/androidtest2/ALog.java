package com.mt.androidtest2;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import com.android.internal.util.FastXmlSerializer;
import com.android.internal.app.LocalePicker.LocaleInfo;
import android.app.ActivityManager;
import android.content.Context;
import android.os.UserHandle;
import android.util.AtomicFile;
import android.util.Log;
import android.util.Xml;

public class ALog {
	public  static String TAG_M = "M_T_AT2";
	/*---------------------------------------------*/
	static Context mContext_write=null;
	static String namespace=null;
	static XmlSerializer serializer = null;
	static AtomicFile mAtomicFile = null;
	static FileOutputStream os = null;
	static String tag_Doc = null;
	static boolean ifStartSaving = false;
	/*---------------------------------------------*/
	static Context mContext_read=null;
    static XmlPullParser parser = null;
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
			ALog.stag("name");
			ALog.attr("attr", "123");
			ALog.etag("name");
		}
		ALog.endSaving();
    }
	
	public static void startSaving(Context mContext ,String fileName,String tagOfDoc){
			mContext_write = mContext;
			ifStartSaving = true;
			tag_Doc = tagOfDoc;
			mAtomicFile = new AtomicFile(new File(mContext.getFilesDir(),fileName));
			Log("File to be saved:"+mAtomicFile.getBaseFile().getAbsolutePath());
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
	
	//saveToXmlDemo：一次性写入xml，实际应用较少，作为代码模板供阅读修改使用
	public static class saveToXmlDemo{
		/**
		 * saveToXml：此方法适合一次性写入的情况，相比较howToWriteToXml方法使用起来不灵活
		 * @param mContext
		 * @param fileName
		 */
		public  static void saveToXml(Context mContext ,String fileName) {
		     final FileOutputStream os;
				AtomicFile mAtomicFile = new AtomicFile(new File(mContext.getFilesDir(),fileName));
				Log("File saved:"+mAtomicFile.getBaseFile().getAbsolutePath());
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
	
    /**
     * howToReadFromXml：此函数使用时，注意把filesToReadWrite/taido.xml从eclipse工程目录下adb push到
     * /data/user/0/com.example.androidtest2/files目录下面
     * @param mContext
     */
    public static void howToReadFromXml(Context mContext){
		String fileToSave = "taido.xml";
		String docTag = "manifest";
		ALog.readFromXml(mContext, fileToSave, docTag);
    }
    
	public static void readFromXml(Context mContext, String fileName,String tagOfDoc) {
		mContext_read = mContext;
		final InputStream is;
        try {
			mAtomicFile = new AtomicFile(new File(mContext.getFilesDir(),fileName));
			Log("File to load:"+mAtomicFile.getBaseFile().getAbsolutePath());
            is = mAtomicFile.openRead();
        } catch (FileNotFoundException ex) {
        	Log("File:"+mAtomicFile.getBaseFile().getAbsolutePath()+" not found!");
            return;
        }
        try {
            parser = Xml.newPullParser();
            parser.setInput(new BufferedInputStream(is), StandardCharsets.UTF_8.name());
            startToLoad(parser,tagOfDoc);
        } catch (IOException ex) {
           Log("IOException:Failed to load xml File!");
        } catch (XmlPullParserException ex) {
            Log("XmlPullParserException to load xml File!");
        } finally {
        	closeQuietly(is);
        }
    }
	
    public static void startToLoad(XmlPullParser parser,String tagOfDoc)
            throws IOException, XmlPullParserException {
        beginDocument(parser, tagOfDoc);
        //Log("startToLoad_outerDepth:"+outerDepth);
        loadElementsFromXml(parser);
    }
	
    /**
     * loadElementsFromXml：读取节点后的细节处理
     * @param parser
     * @throws IOException
     * @throws XmlPullParserException
     */
    public static void loadElementsFromXml(XmlPullParser parser)
            throws IOException, XmlPullParserException {
        final int outerDepth = parser.getDepth();
        String tag_name = "project";
        String attr1 = "name";
        String attr2 = "path";
        String attrValue=null;
        /*--------------------------读xml的时候顺便写入特定内容------------------------*/
		String fileToSave = "taido_m.xml";
		String docTag = "Document";
		ALog.startSaving(mContext_read,fileToSave,docTag);
        /*--------------------------读xml的时候顺便写入特定内容------------------------*/
        while (nextElementWithin(parser, outerDepth)) {
            if (parser.getName().equals(tag_name)) {
                attrValue = parser.getAttributeValue(null, attr1);
                if(null!=attrValue&&isAttrValueOK(attrValue)){
                	ALog.stag(tag_name);
                	ALog.attr(attr1, attrValue);
                    attrValue = parser.getAttributeValue(null, attr2);
                    if(null!=attrValue){
                    	ALog.attr(attr2, attrValue);
                    }
                    ALog.etag(tag_name);
                }
            }
        }
        /*--------------------------读xml的时候顺便写入特定内容------------------------*/
        ALog.endSaving();
        /*--------------------------读xml的时候顺便写入特定内容------------------------*/
    }
    public static String[] excludeNames={
    "translation",
    "abi",
    "art",
    "bionic",
    "bootable",
    "build",
    "cts",
    "dalvik",
    "developers",
    "development",
    "device",
    "docs",
    "hardware",
    "kernel",
    "lib",
    "libcore",
    "libnativehelper",
    "ndk",
    "out",
    "pdk",
    "prebuilts",
    "sdk",
    "system",
    "tools",
    "repo",
    "test",
    "support",
    "sample",
    "doc",
    "gdk",
    "sdk",
    "include",
    "develop",
    "cts",};

    public static boolean isAttrValueOK(String tagValue){
    	if(null==tagValue)return false;
    	for(int i = 0;i<excludeNames.length;i++){
    		if(tagValue.toLowerCase().contains(excludeNames[i].toLowerCase())){
    			return false;
    		}
    	}
    	return true;
    }
    
    public static boolean nextElementWithin(XmlPullParser parser, int outerDepth)
            throws IOException, XmlPullParserException {
    	/**
    	 * int END_DOCUMENT = 1;
    	 * int START_TAG = 2;
    	 * int END_TAG = 3;
    	 * int TEXT = 4;
    	 */
        for (;;) {
            int type = parser.next();
            //Log("nextElementWithin_type1:"+type);
            //下列条件表明已经到文件末尾或者到某个特定标签的结尾
            if (type == XmlPullParser.END_DOCUMENT
                    || (type == XmlPullParser.END_TAG && parser.getDepth() == outerDepth)) {
                return false;
            }
            //Log("nextElementWithin_type2:"+type);
            //下列条件表明已经到文件特定标签的开始位置
            if (type == XmlPullParser.START_TAG
                    && parser.getDepth() == outerDepth + 1) {
                return true;
            }
        }
    }
    
    public static void beginDocument(XmlPullParser parser, String firstElementName) throws XmlPullParserException, IOException
    {
        int type;
        while ((type=parser.next()) != parser.START_TAG && type != parser.END_DOCUMENT) {
        	;
        }
        if (type != parser.START_TAG) {
            throw new XmlPullParserException("No start tag found");
        }
        if (!parser.getName().equals(firstElementName)) {
            throw new XmlPullParserException("Unexpected start tag: found " + parser.getName() +
                    ", expected " + firstElementName);
        }
    }
    
    public static void closeQuietly(AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (RuntimeException rethrown) {
                throw rethrown;
            } catch (Exception ignored) {
            }
        }
    }

    public static List<LocaleInfo> getAllAssetLocales(Context mContext){
    	return com.android.internal.app.LocalePicker.getAllAssetLocales(mContext,true);
    }
}
