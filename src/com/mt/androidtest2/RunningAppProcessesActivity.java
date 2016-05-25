package com.mt.androidtest2;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
/**
 * Android6.0�������ActivityManager.getRunningAppProcesses��ȡ����Ӧ�õ���Ϣ��������������������
 * 1)app����system/priv-app�£�2)ƽ̨ǩ����3)android:sharedUserId="android.uid.system"
 * ����ALL_APPS_FILTER������ϵͳӦ�ú�����Ӧ�ã�����ֻ�ˢ��֮����밲װ����Service������Ӧ�ã�����ListViewΪ��
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
