package com.android.settings.applications;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageItemInfo;
import android.content.pm.ServiceInfo;
import android.content.pm.UserInfo;
import android.graphics.drawable.Drawable;
import android.util.SparseArray;

public class RunningState {
    static class BaseItem {
        final boolean mIsProcess;
        final int mUserId;

        PackageItemInfo mPackageInfo;
        CharSequence mDisplayLabel;
        String mLabel;
        String mDescription;

        int mCurSeq;

        long mActiveSince;
        long mSize;
        String mSizeStr;
        String mCurSizeStr;
        boolean mNeedDivider;
        boolean mBackground;
        
        public BaseItem(boolean isProcess, int userId) {
            mIsProcess = isProcess;
            mUserId = userId;
        }        
    }
    
    static class MergedItem extends BaseItem {
        ProcessItem mProcess;
        UserState mUser;
        final ArrayList<ProcessItem> mOtherProcesses = new ArrayList<ProcessItem>();
        final ArrayList<ServiceItem> mServices = new ArrayList<ServiceItem>();
        final ArrayList<MergedItem> mChildren = new ArrayList<MergedItem>();
        
        private int mLastNumProcesses = -1, mLastNumServices = -1;

        MergedItem(int userId) {
            super(false, userId);
        }
    }
    
    static class ServiceItem extends BaseItem {
        ActivityManager.RunningServiceInfo mRunningService;
        ServiceInfo mServiceInfo;
        boolean mShownAsStarted;
        
        MergedItem mMergedItem;
        
        public ServiceItem(int userId) {
            super(false, userId);
        }
    }
    
    static class ProcessItem extends BaseItem {
        final HashMap<ComponentName, ServiceItem> mServices
        = new HashMap<ComponentName, ServiceItem>();
		final SparseArray<ProcessItem> mDependentProcesses
		        = new SparseArray<ProcessItem>();
		
		final int mUid;
		final String mProcessName;
		int mPid;
		
		ProcessItem mClient;
		int mLastNumDependentProcesses;
		
		int mRunningSeq;
		ActivityManager.RunningAppProcessInfo mRunningProcessInfo;
		
		MergedItem mMergedItem;
		
		boolean mInteresting;
		
		// Purely for sorting.
		boolean mIsSystem;
		boolean mIsStarted;
		long mActiveSince;
		
		public ProcessItem(Context context, int uid, String processName) {
		    super(true,0);
		    mDescription = null;
		    mUid = uid;
		    mProcessName = processName;
		}
    }
    static class UserState {
        UserInfo mInfo;
        String mLabel;
        Drawable mIcon;
    }
}
