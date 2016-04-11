package com.mt.androidtest2;

import com.example.androidtest2.R;

import android.Manifest;
import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onResume(){
		super.onResume();
    	String permissionDes = Manifest.permission.READ_PHONE_STATE;
    	checkPermissionGranted(permissionDes);
	}
	
	/**
	 * checkPermissionGranted：判断是否支持对应权限
	 * @param permissionDes
	 * @return
	 */
	public boolean checkPermissionGranted(String permissionDes){
		boolean isGranted = false;
		Context context = getApplicationContext();  
		if (context.getPackageManager().checkPermission(permissionDes,
		        context.getPackageName()) == PackageManager.PERMISSION_GRANTED){  
			isGranted = true;
		    ALog.Log("Granted!");
		}
		else{  
			isGranted = false;
		    ALog.Log("Not granted");  
		}  
		return isGranted;
	}
	
	public static class ALog {
		public  static String TAG_M = "M_T_AT";
		public static void Log(String info){
			Log.e(TAG_M,info);
		}
	}
}
