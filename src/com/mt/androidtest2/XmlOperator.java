package com.mt.androidtest2;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;
import android.content.Context;
import android.content.res.AssetManager;
import android.util.Xml;
import com.mt.androidtest2.ALog;

public class XmlOperator {
	private XmlPullParser mXmlPullParser = null;//读取xml文件
	private XmlSerializer mXmlSerializer = null;//生成xml文件
	private String ioEncoding=null;
	private String namespace = null;
	private InputStream mInputStream=null;
	private FileOutputStream mFileOutputStream = null;
	private Context mContext=null;
	//
	private String fileName=null;
	private String tagOfDoc=null;
	private String eleName=null;
	private String attrName=null;
	//
	private ArrayList<String>mAttrAL=null;
	private ArrayList<String>mTextAL=null;
	//
	public XmlOperator(Context context){
		mContext=context.getApplicationContext();
		ioEncoding = StandardCharsets.UTF_8.name();
		mXmlPullParser = Xml.newPullParser();
		mXmlSerializer  = Xml.newSerializer();
	}
	
	public void howToWriteAndReadXml(Context context){
		String fileName="writeXml.xml";
		String tagOfDoc="tagOfDoc";
		String eleName="eleName";
		String attr="attr";
		XmlOperator mXmlOperator=new XmlOperator(context);
		mXmlOperator.setInfomation(fileName, tagOfDoc, eleName, attr);
		mXmlOperator.writeToXml(0);
		mXmlOperator.readFromXml(0);
	}
	
	public void setInfomation(String fileName,String tagOfDoc,String eleName,String attrName){
		this.fileName=fileName;
		this.tagOfDoc=tagOfDoc;
		this.eleName=eleName;
		this.attrName=attrName;
	}
	
	//检查文件读写所需信息是否完整
	public void checkInfomation(){
		if(null==fileName||null==tagOfDoc||null==eleName||null==attrName){
			throw new IllegalArgumentException("File info incomplete!");
		}
	}
	
	public void writeToXml(int type){
		checkInfomation();
		startWrite(fileName,tagOfDoc,type);
		writeContents();
		endWrite(tagOfDoc);
	}
	
	public void startWrite(String fileName,String tag_Doc,int type){
		try {
			setOutputStream(type);
			mXmlSerializer.setOutput(new BufferedOutputStream(mFileOutputStream), ioEncoding);
			mXmlSerializer.startDocument(ioEncoding, true);
			mXmlSerializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
			mXmlSerializer.startTag(namespace, tag_Doc);
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public void setOutputStream(int type) throws FileNotFoundException{
		switch(type){
		case 0:
			mFileOutputStream=mContext.openFileOutput(fileName, Context.MODE_PRIVATE);
			break;
		}
	}
	
	public void writeContents(){
		for(int i=0;i<5;i++){
			stag(eleName);
			attr(attrName, Integer.toString(i));
			etag(eleName);
		}
	}
	
	public void endWrite(String tag_Doc){
		try {
	    	mXmlSerializer.endTag(namespace, tag_Doc);
	    	mXmlSerializer.endDocument();
	    	mXmlSerializer.flush();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
	    	if(null!=mFileOutputStream){
	            try {
	            	mFileOutputStream.close();
	            	mFileOutputStream=null;
	            } catch (Exception e) {
	            	e.printStackTrace();
	            }
	    	}
		}
	}
	
	public void stag(String tag){
		try {
			mXmlSerializer.startTag(namespace, tag);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void attr(String attrName,String attrValue){
		try {
			mXmlSerializer.attribute(namespace,attrName,attrValue);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public  void etag(String tag){
		try {
			mXmlSerializer.endTag(namespace, tag);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
	public void readFromXml(int type){
		checkInfomation();
		try {
			setInputStream(type);
			mXmlPullParser.setInput(new BufferedInputStream(mInputStream), ioEncoding);
			filterBeforeFirstElement(mXmlPullParser, tagOfDoc);
			readContents(mXmlPullParser,eleName,attrName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
	    	if(null!=mInputStream){
	            try {
	            	mInputStream.close();
	            	mInputStream=null;
	            } catch (Exception e) {
	            	e.printStackTrace();
	            }
	    	}
		}
	}
	
	public void setInputStream(int type) throws Exception{
		switch(type){
		case 0:
			mInputStream=mContext.openFileInput(fileName);
			break;
		case 1:
			AssetManager mAssetManager = mContext.getAssets();
			mInputStream = mAssetManager.open(fileName); 
			break;			
		}
	}
	
    private void readContents(XmlPullParser parser,String tag_name,String attr) throws IOException, XmlPullParserException {
        int outerDepth = parser.getDepth();
        int type=0;
        String attrValue=null;
        mAttrAL=new ArrayList<String>();
        mTextAL=new ArrayList<String>();
        String str_Text=null;
        /**
         * int START_DOCUMENT = 0;
         * int END_DOCUMENT = 1;
         * int START_TAG = 2;
         * int END_TAG = 3;
         * int TEXT = 4;
         */
        while ((type= parser.next())!=XmlPullParser.END_DOCUMENT) {
        	if(XmlPullParser.START_TAG==type&&parser.getName().equals(tag_name)&&(parser.getDepth() == outerDepth+1)){
	 			attrValue = parser.getAttributeValue(namespace, attr);
	 			mAttrAL.add(attrValue);
	    		//ALog.Log("attr:"+attrValue);
	    		while ((type= parser.next())==XmlPullParser.TEXT){
	    			str_Text=parser.getText();
	    			mTextAL.add(str_Text);
	    			//ALog.Log("text:"+str_Text);
	    			break;
	    		}//while
        	}//if
        }//while
    }
	
    public ArrayList<String> getAttrArrayList(){
    	if(null!=mAttrAL&&mAttrAL.size()>0){
    		return mAttrAL;
    	}
    	return null;
    }
    
    public ArrayList<String> getTextArrayList(){
    	if(null!=mTextAL&&mTextAL.size()>0){
    		return mTextAL;
    	}
    	return null;
    }
    
    //filterBeforefirstElement：过滤掉达到指定标签之前的所有内容
    public void filterBeforeFirstElement(XmlPullParser parser, String firstElementName) throws XmlPullParserException, IOException
    {
        int type;
        //首先过滤掉非标签类事件
        while ((type=parser.next()) != XmlPullParser.START_TAG && type != XmlPullParser.END_DOCUMENT) {
        	;
        }
        //已经到了END_DOCUMENT
        if (type != XmlPullParser.START_TAG) {
            throw new XmlPullParserException("No start tag");
        }
        //已经到了START_TAG
        if (!parser.getName().equals(firstElementName)) {
            throw new XmlPullParserException("Unexpected start tag: "+parser.getName()+", expected: " + firstElementName);
        }
    }
}
