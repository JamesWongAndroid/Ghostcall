package com.kickbackapps.ghostcall.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.kickbackapps.ghostcall.R;

/**
 * Created by Ynott on 9/2/15.
 */
public class RegistrationIntentService extends IntentService {

    private static final String TAG = "GhostRegIntentService";
    public static final String GHOST_PREF = "GhostPrefFile";
    private SharedPreferences settings;

    public RegistrationIntentService() {
        super(TAG);
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        settings = getSharedPreferences(GHOST_PREF, 0);

        try {
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            settings.edit().putBoolean("sentTokenToServer", true).apply();
            settings.edit().putString("GCMToken", token).apply();
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            settings.edit().putBoolean("sentTokenToServer", false).apply();
        }


        Intent registrationComplete = new Intent("registrationComplete");
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }
}
