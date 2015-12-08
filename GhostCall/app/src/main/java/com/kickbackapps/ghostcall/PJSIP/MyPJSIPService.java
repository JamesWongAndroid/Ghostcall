package com.kickbackapps.ghostcall.PJSIP;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

import com.kickbackapps.ghostcall.Constants;
import com.kickbackapps.ghostcall.PJSIP.MyPJSIP;

public class MyPJSIPService extends Service {
    public static MyPJSIP pjsipObject = null;
    String sipUsername;
    String sipPassword;
    SharedPreferences.Editor editor;
    private SharedPreferences settings;
    public static final String GHOST_PREF = "GhostPrefFile";


    public MyPJSIPService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        settings = getSharedPreferences(GHOST_PREF, 0);
        editor = settings.edit();
        sipUsername = settings.getString(Constants.SIP_NAME, "");
        sipPassword = settings.getString(Constants.SIP_PASSWORD, "");
        pjsipObject = new MyPJSIP();
        pjsipObject.init(sipUsername, sipPassword);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (pjsipObject != null) {
            MyPJSIP.endPJSIP();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
