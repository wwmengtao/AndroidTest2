package com.android.settings.applications;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import org.xmlpull.v1.XmlSerializer;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.UserInfo;
import android.util.AtomicFile;
import android.util.Log;
import android.util.SparseArray;
import com.android.internal.util.FastXmlSerializer;
import com.android.settings.applications.RunningState.MergedItem;
import com.android.settings.applications.RunningState.ProcessItem;
import com.android.settings.applications.RunningState.ServiceItem;
import com.android.settings.applications.RunningState.UserState;
/*
	进程服务信息存储方法：
	1、将此SaveProcessServiceInfo.java文件原封不同拷贝到packages/apps/Settings/src/com/android/settings/applications目录下；
	2、在上述目录下的RunningState.java的private boolean update(Context context, ActivityManager am)函数锁同步synchronized (mLock) {}内加上下列内容：
	SaveProcessServiceInfo spsi = new SaveProcessServiceInfo(mApplicationContext,mServiceProcessesByName,
	mServiceProcessesByPid, mRunningProcesses, mInterestingProcesses, mMergedItems);
	spsi.startToSaveProcessServiceInfo();
	3、进入手机系统设置-应用，选择“正在运行”，此时将执行2中所述写入xml操作
	4、将data/data/com.android.settings/files中xml文件拷贝出来
*/
public class SaveProcessServiceInfo{
    static final String TAG_M = "RunningState:M_T";    
    private SparseArray<HashMap<String, ProcessItem>> mServiceProcessesByName;
    private SparseArray<ProcessItem> mServiceProcessesByPid;
    private ArrayList<ProcessItem> mInterestingProcesses;
    private SparseArray<ProcessItem> mRunningProcesses;
    private ArrayList<MergedItem> mMergedItems;
    private ArrayList<String> filenames;
    String dirName=null;
    int mSequence;
    public SaveProcessServiceInfo(Context mContext,SparseArray<HashMap<String, ProcessItem>> mServiceProcessesByName, SparseArray<ProcessItem> mServiceProcessesByPid,
		SparseArray<ProcessItem> mRunningProcesses, ArrayList<ProcessItem> mInterestingProcesses, ArrayList<MergedItem> mMergedItems){
    	dirName=mContext.getFilesDir()+File.separator;
    	this.mServiceProcessesByName=mServiceProcessesByName;
    	this.mServiceProcessesByPid=mServiceProcessesByPid;
    	this.mInterestingProcesses=mInterestingProcesses;
    	this.mRunningProcesses=mRunningProcesses;        	
    	this.mMergedItems=mMergedItems;
    	
    	filenames=new ArrayList<String>();
    	dirName=mContext.getFilesDir()+File.separator;//dirName：data/data/com.android.settings/files
    	filenames.add(dirName+"mServiceProcessesByName.xml");
    	filenames.add(dirName+"mServiceProcessesByPid.xml");    	
    	filenames.add(dirName+"mInterestingProcesses.xml");    
    	filenames.add(dirName+"mRunningProcesses.xml");  
    	filenames.add(dirName+"mMergedItems.xml");     	
    }
    
    public void setValues(int mSequence){
    	this.mSequence=mSequence;
    }
    /*startToSaveProcessServiceInfo：保存所有需要存储的数据内容到指定文件中*/
    public void startToSaveProcessServiceInfo(){
    	for(String mfile : filenames){
    		saveToXml(mfile);
    	}
    	Log.e("SaveProcessServiceInfo","Save to dir:"+dirName);
    }
    
	public void saveToXml(String fileName) {
     final FileOutputStream os;
		AtomicFile mAtomicFile = new AtomicFile(new File(fileName));
        try {
            os = mAtomicFile.startWrite();
            boolean success = false;
            try {
                XmlSerializer serializer = new FastXmlSerializer();
                serializer.setOutput(new BufferedOutputStream(os), "utf-8");
                startToSave(serializer,fileName);
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

    public void startToSave(XmlSerializer serializer,String fileName) throws IOException {
        serializer.startDocument(null, true);
        serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
        if(fileName.contains("mServiceProcessesByName")){
        	SaveContainerInfo_SparseArray_HashMap_String_ProcessItem(serializer,mServiceProcessesByName,"mServiceProcessesByName","si_uid",true);
        }
        if(fileName.contains("mServiceProcessesByPid")){
        	SaveContainerInfo_SparseArray_ProcessItem(serializer,mServiceProcessesByPid,"mServiceProcessesByPid","si_pid",true);
        }
        if(fileName.contains("mRunningProcesses")){
        	SaveContainerInfo_SparseArray_ProcessItem(serializer,mRunningProcesses,"mRunningProcesses","pi_pid",true);
        }
        if(fileName.contains("mInterestingProcesses")){
        	SaveContainerInfo_ArrayList_ProcessItem(serializer,mInterestingProcesses,"mInterestingProcesses",true);
        }        
        if(fileName.contains("mMergedItems")){
           	SaveContainerInfo_ArrayList_MergedItem(serializer,mMergedItems,"mMergedItems");
        }        
        serializer.endDocument();        
    }	
    
    public void SaveContainerInfo_SparseArray_HashMap_String_ProcessItem(XmlSerializer serializer,SparseArray<HashMap<String, ProcessItem>>container,String Tag_Doc,String Tag_first,boolean writeDependentProcesses) throws IOException{
        int sizeOfContainer = container.size();
        if(0 == sizeOfContainer)return;
    	serializer.startTag(null, Tag_Doc);
        serializer.attribute(null, "count", toString(sizeOfContainer));
        serializer.attribute(null, "mSequence", toString(mSequence));
        for (int i=0; i<container.size(); i++) {
        	int key = container.keyAt(i);
	        serializer.startTag(null, Tag_first);
	        serializer.attribute(null, "value", toString(key));
            HashMap<String, ProcessItem> values = container.valueAt(i);
            Set<String> setOfValue = values.keySet();
            for(@SuppressWarnings("rawtypes")
			Iterator iter = setOfValue.iterator(); iter.hasNext();)
            {
	            String keyOfValues = (String)iter.next();
	            ProcessItem valueOfValues = (ProcessItem)values.get(keyOfValues);
            	serializer.startTag(null, "si_process");
            	serializer.attribute(null, "value", keyOfValues);
            	SaveProcessItem(serializer, valueOfValues,true);
            	serializer.endTag(null, "si_process");
            }
	        serializer.endTag(null, Tag_first);
        }
        serializer.endTag(null, Tag_Doc);
    }
    
    public void SaveContainerInfo_SparseArray_ProcessItem(XmlSerializer serializer, SparseArray<ProcessItem> container,String Tag_Doc,String Tag_first,boolean writeDependentProcesses) throws IOException{
        int sizeOfContainer = container.size();
        if(0 == sizeOfContainer)return;
    	serializer.startTag(null, Tag_Doc);
        serializer.attribute(null, "count", toString(sizeOfContainer));
        serializer.attribute(null, "mSequence", toString(mSequence));
        for (int i=0; i<sizeOfContainer; i++) {
        	int key = container.keyAt(i);
	        ProcessItem value = container.valueAt(i);
	        serializer.startTag(null, Tag_first);
	        serializer.attribute(null, "value", toString(key));
	        SaveProcessItem(serializer, value,writeDependentProcesses);
	        serializer.endTag(null, Tag_first);
        }
        serializer.endTag(null, Tag_Doc);    	
    }
    
    public void SaveContainerInfo_ArrayList_ProcessItem(XmlSerializer serializer, ArrayList<ProcessItem> container,String Tag_Doc, boolean writeDependentProcesses)  throws IOException{
        int sizeOfContainer = container.size();
        if(0 == sizeOfContainer)return;
    	serializer.startTag(null, Tag_Doc);
        serializer.attribute(null, "count", toString(sizeOfContainer));
        serializer.attribute(null, "mSequence", toString(mSequence));
        for (int i=0; i<sizeOfContainer; i++) {
	        ProcessItem value = container.get(i);
	        SaveProcessItem(serializer, value,writeDependentProcesses);
        }
        serializer.endTag(null, Tag_Doc);    
    }    
    
    public void SaveContainerInfo_ArrayList_MergedItem(XmlSerializer serializer, ArrayList<MergedItem> container,String Tag_Doc)  throws IOException{
        int sizeOfContainer = container.size();
        if(0 == sizeOfContainer)return;
    	serializer.startTag(null, Tag_Doc);
        serializer.attribute(null, "count", toString(sizeOfContainer));
        serializer.attribute(null, "mSequence", toString(mSequence));
        for (int i=0; i<sizeOfContainer; i++) {
         	MergedItem value = container.get(i);
    	        SaveMergedItem(serializer, value);
        }
        serializer.endTag(null, Tag_Doc);    
    }               
    
    public void SaveContainerInfo_HashMap_ComponentName_ServiceItem(XmlSerializer serializer, HashMap<ComponentName, ServiceItem> container,String Tag_Doc)  throws IOException{
        int sizeOfContainer = container.size();
        if(0 == sizeOfContainer)return;
    	serializer.startTag(null, Tag_Doc);
        serializer.attribute(null, "count", toString(sizeOfContainer));
        serializer.attribute(null, "mSequence", toString(mSequence));
        Set<ComponentName> setOfValue = container.keySet();
        for(@SuppressWarnings("rawtypes")
		Iterator iter = setOfValue.iterator(); iter.hasNext();)
        {
        	ComponentName key = (ComponentName)iter.next();
            ServiceItem value = (ServiceItem)container.get(key);
        	serializer.startTag(null, "ComponentName");
        	serializer.attribute(null, "mPackage", toString(key.getPackageName()));
        	serializer.attribute(null, "mClass", toString(key.getClassName()));
        	SaveServiceItem(serializer,value);
        	serializer.endTag(null, "ComponentName");
        }
        serializer.endTag(null, Tag_Doc);
    }
    //Save information of ProcessItem, for example, mServices and mDependentProcesses
    public void SaveProcessItem(XmlSerializer serializer, ProcessItem value, boolean writeDependentProcesses) throws IOException{
    	if(null == value)return;
    	/*
    	{"parameter",toString(value.parameter},
    	*/
    	String [][] tags_values ={
    			/*------------------------------------ProcessItem--------------------------------------*/
    			{"mUid", toString(value.mUid)},
				{"mProcessName", toString(value.mProcessName)},
				{"mPid", toString(value.mPid)},
				{"mLastNumDependentProcesses",toString(value.mLastNumDependentProcesses)},
				{"mRunningSeq", toString(value.mRunningSeq)},
				{"mInteresting", toString(value.mInteresting)},		
		    	{"mIsSystem",toString(value.mIsSystem)},
		    	{"mIsStarted",toString(value.mIsStarted)},
		    	{"mActiveSince",toString(value.mActiveSince)},
    			/*------------------------------------BaseItem--------------------------------------*/		    	
		    	{"mIsProcess",toString(value.mIsProcess)},		 
		    	{"mUserId",toString(value.mUserId)},		    
		    	{"mDisplayLabel",toString(value.mDisplayLabel)},
		    	{"mLabel",toString(value.mLabel)},
		    	{"mDescription",toString(value.mDescription)},		   
				{"mCurSeq", toString(value.mCurSeq)},
		    	{"mSize",toString(value.mSize)},
		    	{"mSizeStr",toString(value.mSizeStr)},
		    	{"mCurSizeStr",toString(value.mCurSizeStr)},
		    	{"mNeedDivider",toString(value.mNeedDivider)},
		    	{"mBackground",toString(value.mBackground)},
				{"mRunningProcessInfo.importanceReasonPid", toString(value.mRunningProcessInfo.importanceReasonPid)},
    	};
    	serializer.startTag(null, "ProcessItem"); 
    	//1. Save members info
    	for(int i=0;i<tags_values.length;i++){//tags_values.length: the columns of the array
    		serializer.startTag(null, tags_values[i][0]);
    		serializer.attribute(null, "value", toString(tags_values[i][1]));
    		serializer.endTag(null, tags_values[i][0]);
    	}
    	//2. Save ProcessItem.mServices info
    	SaveContainerInfo_HashMap_ComponentName_ServiceItem(serializer, value.mServices, "mServices");
    	//3. Save ProcessItem.mDependentProcesses info
    	if(writeDependentProcesses)
    		SaveContainerInfo_SparseArray_ProcessItem(serializer, value.mDependentProcesses, "mDependentProcesses","mPid", !writeDependentProcesses);
    	serializer.endTag(null, "ProcessItem");	
    }

    public void SaveServiceItem(XmlSerializer serializer, ServiceItem value) throws IOException{
    	if(null == value)return;
    	/*
    	{"parameter",toString(value.parameter},
    	*/
    	serializer.startTag(null, "ServiceItem"); 
    	ActivityManager.RunningServiceInfo rs = value.mRunningService;
    	if(rs!=null){
    		String [][] tags_values_ServiceItem_mRunningService ={
    			/*-------------------------ServiceItem$ActivityManager.RunningServiceInfo------------------*/
				{"pid", toString(rs.pid)},
				{"uid",toString(rs.uid)},
				{"process", toString(rs.process)},				
				{"foreground", toString(rs.foreground)},		
				{"activeSince", toString(rs.activeSince)},			
				{"started", toString(rs.started)},	
				{"clientCount", toString(rs.clientCount)},	
				{"crashCount", toString(rs.crashCount)},	
				{"lastActivityTime", toString(rs.lastActivityTime)},	
				{"restarting", toString(rs.restarting)},	
				{"flags", toString(rs.flags)},	
				{"clientPackage", toString(rs.clientPackage)},	
    			{"clientLabel", toString(rs.clientLabel)},	
    		};
    		serializer.startTag(null, "mRunningService"); 
    		saveArrayInfo(tags_values_ServiceItem_mRunningService,serializer);
    		serializer.endTag(null, "mRunningService"); 
    	}
    	String [][] tags_values_ServiceItem ={
				/*------------------------------------ServiceItem$BaseItem--------------------------------------*/
    			{"mShownAsStarted", toString(value.mShownAsStarted)},
		    	{"mIsProcess",toString(value.mIsProcess)},		 
		    	{"mUserId",toString(value.mUserId)},		    
		    	{"mDisplayLabel",toString(value.mDisplayLabel)},
		    	{"mLabel",toString(value.mLabel)},
		    	{"mDescription",toString(value.mDescription)},		   
				{"mCurSeq", toString(value.mCurSeq)},
		    	{"mActiveSince",toString(value.mActiveSince)},				
		    	{"mSize",toString(value.mSize)},
		    	{"mSizeStr",toString(value.mSizeStr)},
		    	{"mCurSizeStr",toString(value.mCurSizeStr)},
		    	{"mNeedDivider",toString(value.mNeedDivider)},
		    	{"mBackground",toString(value.mBackground)},
    	};
    	saveArrayInfo(tags_values_ServiceItem,serializer);
    	serializer.endTag(null, "ServiceItem");	
    }
    public void SaveMergedItem(XmlSerializer serializer, MergedItem value) throws IOException{
    	if(null==value)return;
       	/*
       	{"parameter",toString(value.parameter},
       	*/
       	serializer.startTag(null, "MergedItem"); 
       	//1. save MergedItem.mProcess info, only save mUid, mProcessName and mPid
       	ProcessItem pi = value.mProcess;
       	if(null!=pi){
	       	String [][] tags_values_MergedItem_mProcess ={
	       	       	{"mUid",toString(pi.mUid)},       	
	       	       	{"mProcessName",toString(pi.mProcessName)},      
	       	       	{"mPid",toString(pi.mPid)} 
	       	};
       		serializer.startTag(null, "mProcess"); 
	       	saveArrayInfo(tags_values_MergedItem_mProcess,serializer);
	       	serializer.endTag(null, "mProcess"); 
       	}
       	String [][] tags_values_MergedItem ={
				/*------------------------------------MergedItem$BaseItem--------------------------------------*/
		    	{"mIsProcess",toString(value.mIsProcess)},		 
		    	{"mUserId",toString(value.mUserId)},
		    	{"mDisplayLabel",toString(value.mDisplayLabel)},
		    	{"mLabel",toString(value.mLabel)},
		    	{"mDescription",toString(value.mDescription)},		   
				{"mCurSeq", toString(value.mCurSeq)},
		    	{"mActiveSince",toString(value.mActiveSince)},				
		    	{"mSize",toString(value.mSize)},
		    	{"mSizeStr",toString(value.mSizeStr)},
		    	{"mCurSizeStr",toString(value.mCurSizeStr)},
		    	{"mNeedDivider",toString(value.mNeedDivider)},
		    	{"mBackground",toString(value.mBackground)}
       	};
       	saveArrayInfo(tags_values_MergedItem,serializer);
    	//2. save MergedItem.UserState info, only save mLabel info
       	UserState mUser = value.mUser;
       	if(null!=mUser){
	       	serializer.startTag(null, "mUser"); 
	       	String [][] tags_values_UserLabel ={
	       	       	{"mLabel",toString(mUser.mLabel)},          	        
	       	}; 	
	       	saveArrayInfo(tags_values_UserLabel,serializer);
	       	//Save info of mUserInfo
	       	UserInfo mUserInfo = null;
	       	if(null!=mUser){
	       		mUserInfo = mUser.mInfo;
	       		if(null!=mUserInfo){
		       		String [][] tags_values_UserInfo ={
		       			{"id",toString(mUserInfo.id)}, 
		       	        {"name",toString(mUserInfo.name)}, 
		       	        {"iconPath",toString(mUserInfo.iconPath)},    
		       	        {"flags",toString(mUserInfo.flags)},  
		       	        {"toString",mUserInfo.toString()}
		       	    };
	       			serializer.startTag(null, "mInfo");
		       		saveArrayInfo(tags_values_UserInfo,serializer);
		       		serializer.endTag(null, "mInfo");
	       		}
	       	}
	       	serializer.endTag(null, "mUser");
       	}
       	//3. Save MergeItem.mOtherProcesses info
       	SaveContainerInfo_ArrayList_ProcessItem(serializer,value.mOtherProcesses,"mOtherProcesses",true);
       	//4. Save MergeItem.mServices info
       	if(value.mServices.size()>0){
	       	serializer.startTag(null, "mServices");
	       	serializer.attribute(null, "count", toString(value.mServices.size()));
	       	for(ServiceItem si : value.mServices){
	           	SaveServiceItem(serializer,si);
	       	}
	       	serializer.endTag(null, "mServices");
       	}
       	//5. Save MergeItem.mChildren info
       	if(value.mChildren.size()>0){
	       	serializer.startTag(null, "mChildren");
	       	serializer.attribute(null, "count", toString(value.mChildren.size()));
	       	for(MergedItem mi : value.mChildren){
	       		SaveMergedItem(serializer,mi);
	       	}
	       	serializer.endTag(null, "mChildren");
       	}
       	serializer.endTag(null, "MergedItem");
    }
    
    public void saveArrayInfo(String [][]tags_values,XmlSerializer serializer) throws IOException{
    	for(int i=0;i<tags_values.length;i++){//tags_values.length: the columns of the array
    		serializer.startTag(null, tags_values[i][0]);
    		serializer.attribute(null, "value", toString(tags_values[i][1]));
    		serializer.endTag(null, tags_values[i][0]);
    	}	
    }
    
    public String toString(Object member){
    	return ""+member;
    }
}
