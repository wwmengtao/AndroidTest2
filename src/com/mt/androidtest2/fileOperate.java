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
		//下列/data/user/0是链接，指向/data/data
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
    		mDir = mContext.getFilesDir();//存储路径为： /data/data/[package.name]/files/
    		outFile = new File(mDir, fileName);
    		osw = new FileOutputStream(outFile);
    		break;
    	case 1://openFileOutput：在 /data/data/[package.name]/files/目录下操作
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
	
	public void getResourcesDescription(){
		Resources mResources=mContext.getResources();
		Uri uri =  Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
			    + mResources.getResourcePackageName(R.drawable.ic_launcher) + "/"
			    + mResources.getResourceTypeName(R.drawable.ic_launcher) + "/"
			    + mResources.getResourceEntryName(R.drawable.ic_launcher));
		ALog.Log("uri:"+uri.toString());
	}
	
	/**
	 * getFromAssets：从assets中文件读取数据
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
    
    /*assets目录下存放的原生资源文件。因为系统在编译的时候不会编译assets下的资源文件，所以我们
     * 不能通过R.XXX.ID的方式访问它们。那能不能通过该资源的绝对路径去访问它们呢？因为apk安装
     * 之后会放在/data/app/**.apk目录下，以apk形式存在，asset/res和被绑定在apk里，并不会解压到
     * /data/data/YourApp目录下去，所以我们无法直接获取到assets的绝对路径，因为它们根本就没有。
     */
    /**
     * listAssets：列举assets目录或者子目录下所有文件(夹)内容
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
     *  copyFilesFassets：从assets目录中复制整个文件夹内容  
     *  @param  context  Context 使用CopyFiles类的Activity 
     *  @param  oldPath  String  原文件路径
     *  @param  newPath  String  复制后路径
     */   
    public void copyFilesFassets(Context context,String oldPath,String newPath) {     
    	//下面所以要去掉文件(夹)开头的"/"是因为getAssets().list函数的参数是不允许开头有File.separator的，否则文件无法找到
		if(oldPath.startsWith(File.separator)){
			oldPath=oldPath.substring(1, oldPath.length());
		}
    	try {  
    		String fileNames[] = context.getAssets().list(oldPath);//获取assets目录下的所有文件及目录名  
            if (fileNames.length > 0) {//如果是目录  
                File file = new File(newPath);  
                if(file.exists()){
                	deleteDir(file);
                }
                file.mkdirs();//如果文件夹不存在，则递归
                for (String fileName : fileNames) {
                	copyFilesFassets(context,oldPath+File.separator+fileName,newPath+File.separator+fileName);  
                }
            } else{//如果是文件  
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
                while((byteCount=is.read(buffer))!=-1) {//循环从输入流读取 buffer字节       
                    fos.write(buffer, 0, byteCount);//将读取的输入流写入到输出流  
                }
                fos.flush();//刷新缓冲区  
                is.close();  
                fos.close();  
            }  
        } catch (Exception e) {  
            // TODO Auto-generated catch block  
            ALog.Log("Exception");
        }                             
    }   

    /**
     * deleteDir:删除指定文件夹下所有内容
     * @param file
     */
	public void deleteDir(File file){
		if(!file.exists()){//文件夹不存在
			ALog.Log("指定目录不存在:"+file.getName());
			return;
		}
		boolean rslt=true;//保存中间结果
		if(!(rslt=file.delete())){//先尝试直接删除
			//若文件夹非空。枚举、递归删除里面内容
			File subs[] = file.listFiles();
			for (File afile : subs) {
				if (afile.isDirectory()){
					deleteDir(afile);//递归删除子文件夹内容
				}
				rslt = afile.delete();//删除子文件夹本身
			}
			rslt = file.delete();//删除此文件夹本身
		}
		if(!rslt){
			ALog.Log("无法删除:"+file.getName());
			return;
		}
	}    

}
