package com.mt.androidtest2;

import java.util.ArrayList;
import java.util.List;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AppsListViewAdapter extends BaseAdapter{
	Context mContext;
	LayoutInflater inflater;
	ActivityManager mAM;
	PackageManager mPM;
	ActivityManager.RunningAppProcessInfo mItem;
	AppFilter mAppFilter;
    final ArrayList<ActivityManager.RunningAppProcessInfo> mItems =
    		new ArrayList<ActivityManager.RunningAppProcessInfo>();
	List<ApplicationInfo> mApplications =null;
    public AppsListViewAdapter(Context mContext){
    	this.mContext = mContext;
		mAM =  (ActivityManager)mContext.getSystemService(Context.ACTIVITY_SERVICE);
		mPM =  mContext.getPackageManager();
    	inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public void getAppsInfo(){
    	ArrayList<ActivityManager.RunningAppProcessInfo> mNewItems =(ArrayList<RunningAppProcessInfo>) mAM.getRunningAppProcesses();
    	mApplications = mPM.getInstalledApplications(PackageManager.GET_DISABLED_COMPONENTS);
    	filterRunningPackagesFromRAPI(mNewItems);
    	//ALog.Log("mNewItems.size:"+mNewItems.size());
    }
    
    /**
     * filterRunningPackagesFromRAPI：过滤掉根据进程名称无法获取ApplicationInfo的ActivityManager.RunningAppProcessInfo元素
     * @param mNewItems
     */
    public void filterRunningPackagesFromRAPI(ArrayList<ActivityManager.RunningAppProcessInfo> mNewItems){
    	if(null==mNewItems){
    		return;
    	}else if(0==mNewItems.size()){
    		return;
    	}
    	mItems.clear();
    	ApplicationInfo info = null;
    	ActivityManager.RunningAppProcessInfo mRAPI = null;
    	ArrayList<ActivityManager.RunningAppProcessInfo> mContainer;
    	for(int i=0; i<mNewItems.size();i++){
    		mRAPI = mNewItems.get(i);
    	    try{	
    	    	info = mPM.getApplicationInfo(mRAPI.processName, PackageManager.GET_DISABLED_COMPONENTS);
    	    	if(null!=info&&ALL_APPS_FILTER.filterApp(mContext,info)){
    	    		mItems.add(mRAPI);
    	    	}
    	    } catch (NameNotFoundException e) {
    	    	ALog.Log("ApplicationInfo can not be found!");
    	    }
    	}
    	return;
    }
    
    public Object getItem(int position) {
        return mItems.get(position);
    }
    
    public long getItemId(int position) {
        return mItems.get(position).hashCode();
    }
    
	public int getCount(){
		return mItems.size();
	}
	
	public View getView(int position, View convertView, ViewGroup parent){
		mItem = mItems.get(position);
		View mView=null;
		if(null==convertView){
			mView = inflater.inflate(R.layout.running_apps_layout, parent,false);
		}else{
			mView = convertView;
		}
		return getItemView(mView,mItem);
	}
	
	public View getItemView(View mView,ActivityManager.RunningAppProcessInfo mItem){
		ImageView icon = (ImageView)mView.findViewById(R.id.icon);
		TextView name = (TextView)mView.findViewById(R.id.name);
		TextView size = (TextView)mView.findViewById(R.id.size);
		TextView packageName = (TextView)mView.findViewById(R.id.packageName);
		TextView uptime = (TextView)mView.findViewById(R.id.uptime);
		ApplicationInfo info = null;
		try {
			info = mPM.getApplicationInfo(mItem.processName, PackageManager.GET_DISABLED_COMPONENTS);
			Drawable dra_icon = info.loadIcon(mPM);
			if(null!=dra_icon){
				icon.setImageDrawable(dra_icon);
			}else{
				ALog.Log("null!=info:"+mItem.processName);
				icon.setImageResource(R.drawable.ic_launcher);
			}
			CharSequence cs = (CharSequence) mPM.getApplicationLabel(info);
			String str = (null!=cs)?cs.toString():mItem.processName;
			name.setText(str);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			ALog.Log("AppInfo not found！");
		}
		packageName.setText(mItem.processName);
        size.setText("size");
        uptime.setText("uptime");
        return mView;
	}
    public static interface AppFilter {
        public void init(Context context);

        public boolean filterApp(Context context, ApplicationInfo info);
    }
    
    /**
     * ALL_APPS_FILTER：过滤掉本应用以及系统应用后剩下的所有应用
     */
    public static final AppFilter ALL_APPS_FILTER = new AppFilter() {
        public void init(Context context) {
        }

        @Override
        public boolean filterApp(Context context, ApplicationInfo info) {
			if (context.getPackageName().equals(info.packageName)) {
			    // Skip myself.
			    return false;
			}
	         if (0 != (info.flags & ApplicationInfo.FLAG_SYSTEM)) {
	        	 return false;
	         }
	        return true;
        }
    };
}
