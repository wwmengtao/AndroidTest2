package com.mt.androidtest2;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
/**
 * Android6.0如果想让ActivityManager.getRunningAppProcesses获取所有应用的信息，必须满足三个条件：
 * 1)app至于system/priv-app下；2)平台签名；3)android:sharedUserId="android.uid.system"
 * 由于ALL_APPS_FILTER过滤了系统应用和自身应用，因此手机刷机之后必须安装带有Service的三方应用，否则ListView为空
 * @author Mengtao1
 *
 */
public class RunningAppProcessesActivity extends Activity {
	ListView mListView;
	AppsListViewAdapter aAdapter;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apps_listview);
		mListView = (ListView)findViewById(R.id.list_runningappprocesses);
		aAdapter = new AppsListViewAdapter(this);
		mListView.setAdapter(aAdapter);
    }

    @Override
    public void onResume(){
    	super.onResume();		
    	aAdapter.getAppsInfo();
    	aAdapter.notifyDataSetChanged();
    }
}
