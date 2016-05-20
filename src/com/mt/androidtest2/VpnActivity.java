package com.mt.androidtest2;

import com.android.internal.net.LegacyVpnInfo;
import com.mt.androidtest2.R;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.IConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.ConnectivityManager.NetworkCallback;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.widget.TextView;

public class VpnActivity extends Activity  implements Handler.Callback{
    private Handler mUpdater;
    private static final int RESCAN_MESSAGE = 0;
    private static final int RESCAN_INTERVAL_MS = 1000;
    private TextView mTV=null;
	private ConnectivityManager mConnectivityManager;
    private final IConnectivityManager mConnectivityService = IConnectivityManager.Stub
            .asInterface(ServiceManager.getService(Context.CONNECTIVITY_SERVICE));
    private static final NetworkRequest VPN_REQUEST = new NetworkRequest.Builder()
    .removeCapability(NetworkCapabilities.NET_CAPABILITY_NOT_VPN)
    .removeCapability(NetworkCapabilities.NET_CAPABILITY_NOT_RESTRICTED)
    .removeCapability(NetworkCapabilities.NET_CAPABILITY_TRUSTED)
    .build();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vpn);
		mTV=(TextView)findViewById(R.id.vpn);
		mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	}
	@Override
	protected void onResume(){
		super.onResume();
		testFunctionsRegister();
        // Trigger a refresh
        if (mUpdater == null) {
            mUpdater = new Handler(this);
        }
        mUpdater.sendEmptyMessage(RESCAN_MESSAGE);
	}
    @Override
    public void onPause() {
        testFunctionsUnRegister();
        if (mUpdater != null) {
            mUpdater.removeCallbacksAndMessages(null);
        }
        super.onPause();
    }
	public void testFunctionsRegister(){
        //1、注册VPN监控状态
        mConnectivityManager.registerNetworkCallback(VPN_REQUEST, mNetworkCallback);
	}
	
	public void testFunctionsUnRegister(){
		 //2、注销VPN监控状态
        mConnectivityManager.unregisterNetworkCallback(mNetworkCallback);
	}
	@Override
	public boolean handleMessage(Message arg0) {
		// TODO Auto-generated method stub
		//如果在多个地方发送RESCAN_MESSAGE消息，那么极有可能在下列RESCAN_INTERVAL_MS到来前，有多个RESCAN_MESSAGE
		//消息在消息队列中，因此此时问了防止多次执行同样的功能，需要把消息队列中RESCAN_MESSAGE统统删除
		mUpdater.removeMessages(RESCAN_MESSAGE);
		 // Mark out connections with a subtitle
        try {
            // Legacy VPNs
            LegacyVpnInfo info = mConnectivityService.getLegacyVpnInfo(UserHandle.myUserId());
            if (info != null) {
                String[] states = this.getResources().getStringArray(R.array.vpn_states);
            	mTV.setText(states[info.state]);//Indicate the VPN state
            }else{
            	mTV.setText("LegacyVpnInfo为null");
            }
        }catch (RemoteException e) {
        	mTV.setText("RemoteException");
        }
        mUpdater.sendEmptyMessageDelayed(RESCAN_MESSAGE, RESCAN_INTERVAL_MS);//每隔一秒钟发送消息
        return true;
	}
    private NetworkCallback mNetworkCallback = new NetworkCallback() {
        @Override
        public void onAvailable(Network network) {
        	ALog.Log("NetworkCallback.onAvailable");
        }

        @Override
        public void onLost(Network network) {
        	ALog.Log("NetworkCallback.onLost");
        }
    };
}
