package com.kickbackapps.ghostcall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetworkReceiver extends BroadcastReceiver {

    private static boolean firstConnect = true;

    public NetworkReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
       final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        boolean isConnected = wifi != null && wifi.isConnectedOrConnecting() || mobile != null && mobile.isConnectedOrConnecting();
        if (isConnected) {
            if (firstConnect) {
                firstConnect = false;
                context.stopService(new Intent(context, MyPJSIPService.class));
                context.startService(new Intent(context, MyPJSIPService.class));
                Log.d("Network Available ", "YES");
            } else {
                firstConnect = true;
                Log.d("Network Available ", "first connect reset");
            }
        } else {
            Log.d("Network Available ", "NO");
        }
    }
}
