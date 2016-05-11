package com.mt.androidtest2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.util.EncodingUtils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
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
		File mExternalStorageDirectory = Environment.getExternalStorageDirectory();//  /storage/emulated/0
		File mExternalStoragePublicDirectory = Environment.getExternalStoragePublicDirectory("test");// /storage/emulated/0/test
		File mRootDirectory = Environment.getRootDirectory();// /system
		//����/data/user/0�����ӣ�ָ��/data/data
		String mPackageCodePath = mContext.getPackageCodePath();// /data/app/com.example.androidtest2-2/base.apk
		String mPackageResourcePath = mContext.getPackageResourcePath();// /data/app/com.example.androidtest2-2/base.apk
		File mCacheDir = mContext.getCacheDir();// /data/user/0/com.example.androidtest2/cache
		File mDatabasePath = mContext.getDatabasePath("test");// /data/user/0/com.example.androidtest2/databases/test
		File mDir = mContext.getDir("", Context.MODE_PRIVATE);// /data/user/0/com.example.androidtest2/app_
		File mFilesDir = mContext.getFilesDir();// /data/user/0/com.example.androidtest2/files
		File mExternalFilesDir = mContext.getExternalFilesDir(null);// /storage/emulated/0/Android/data/com.example.androidtest2/files
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
		ALog.Log("mFilesDir:"+mFilesDir);	
		ALog.Log("mExternalFilesDir:"+mExternalFilesDir);			
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
    	case 1://openFileOutput���� /data/data/[package.name]/files/Ŀ¼�²���
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
	
	public void getResourcesDescription(){
		Resources mResources=mContext.getResources();
		Uri uri =  Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
			    + mResources.getResourcePackageName(R.drawable.ic_launcher) + "/"
			    + mResources.getResourceTypeName(R.drawable.ic_launcher) + "/"
			    + mResources.getResourceEntryName(R.drawable.ic_launcher));
		ALog.Log("uri:"+uri.toString());
	}
	
	/**
	 * getFromAssets����assets���ļ���ȡ����
	 * @param fileName
	 */
    public void getFromAssets(String fileName){ 
        try { 
             InputStreamReader inputReader = new InputStreamReader(mContext.getResources().getAssets().open(fileName) ); 
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line=null;
            while((line = bufReader.readLine()) != null)
                ALog.Log("line:"+line);
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
    } 
    
    /*assetsĿ¼�´�ŵ�ԭ����Դ�ļ�����Ϊϵͳ�ڱ����ʱ�򲻻����assets�µ���Դ�ļ�����������
     * ����ͨ��R.XXX.ID�ķ�ʽ�������ǡ����ܲ���ͨ������Դ�ľ���·��ȥ���������أ���Ϊapk��װ
     * ֮������/data/app/**.apkĿ¼�£���apk��ʽ���ڣ�asset/res�ͱ�����apk��������ѹ��
     * /data/data/YourAppĿ¼��ȥ�����������޷�ֱ�ӻ�ȡ��assets�ľ���·������Ϊ���Ǹ�����û�С�
     */
    /**
     * listAssets���о�assetsĿ¼������Ŀ¼�������ļ�(��)����
     */
    public void listAssets(String fileName){
       	String fileNames[];
    		try {
    			fileNames = mContext.getAssets().list(fileName);
    	    	for(int i=0;i<fileNames.length;i++){
    	    		ALog.Log("fileName:"+fileNames[i]);
    	    	}
    		} catch (IOException e1) {
    			// TODO Auto-generated catch block
    			e1.printStackTrace();
    		} 
    }
    
    /**  
     *  copyFilesFassets����assetsĿ¼�и��������ļ�������  
     *  @param  context  Context ʹ��CopyFiles���Activity 
     *  @param  oldPath  String  ԭ�ļ�·��
     *  @param  newPath  String  ���ƺ�·��
     */   
    public void copyFilesFassets(Context context,String oldPath,String newPath) {     
    	//��������Ҫȥ���ļ�(��)��ͷ��"/"����ΪgetAssets().list�����Ĳ����ǲ�����ͷ��File.separator�ģ������ļ��޷��ҵ�
		if(oldPath.startsWith(File.separator)){
			oldPath=oldPath.substring(1, oldPath.length());
		}
    	try {  
    		String fileNames[] = context.getAssets().list(oldPath);//��ȡassetsĿ¼�µ������ļ���Ŀ¼��  
            if (fileNames.length > 0) {//�����Ŀ¼  
                File file = new File(newPath);  
                if(file.exists()){
                	deleteDir(file);
                }
                file.mkdirs();//����ļ��в����ڣ���ݹ�
                for (String fileName : fileNames) {
                	copyFilesFassets(context,oldPath+File.separator+fileName,newPath+File.separator+fileName);  
                }
            } else{//������ļ�  
            	File newFile = new File(newPath);
        		if (newFile.exists()) {
        			newFile.delete();
        		}
        		newFile.createNewFile();
                InputStream is = context.getAssets().open(oldPath);  
                ALog.Log("oldPath:"+oldPath+" newPath:"+newPath);
                FileOutputStream fos = new FileOutputStream(newFile);  
                byte[] buffer = new byte[1024];
                int byteCount=0;
                while((byteCount=is.read(buffer))!=-1) {//ѭ������������ȡ buffer�ֽ�       
                    fos.write(buffer, 0, byteCount);//����ȡ��������д�뵽�����  
                }
                fos.flush();//ˢ�»�����  
                is.close();  
                fos.close();  
            }  
        } catch (Exception e) {  
            // TODO Auto-generated catch block  
            ALog.Log("Exception");
        }                             
    }   

    /**
     * deleteDir:ɾ��ָ���ļ�������������
     * @param file
     */
	public void deleteDir(File file){
		if(!file.exists()){//�ļ��в�����
			ALog.Log("ָ��Ŀ¼������:"+file.getName());
			return;
		}
		boolean rslt=true;//�����м���
		if(!(rslt=file.delete())){//�ȳ���ֱ��ɾ��
			//���ļ��зǿա�ö�١��ݹ�ɾ����������
			File subs[] = file.listFiles();
			for (File afile : subs) {
				if (afile.isDirectory()){
					deleteDir(afile);//�ݹ�ɾ�����ļ�������
				}
				rslt = afile.delete();//ɾ�����ļ��б���
			}
			rslt = file.delete();//ɾ�����ļ��б���
		}
		if(!rslt){
			ALog.Log("�޷�ɾ��:"+file.getName());
			return;
		}
	}    

}
