package com.kickbackapps.ghostcall.Services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.kickbackapps.ghostcall.CallScreen;
import com.kickbackapps.ghostcall.Constants;
import com.kickbackapps.ghostcall.GhostCallDatabaseAdapter;
import com.kickbackapps.ghostcall.R;
import com.kickbackapps.ghostcall.StartScreen;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Ynott on 9/2/15.
 */
public class GhostCallListenerService extends GcmListenerService {

    HttpURLConnection urlConnection = null;
    URL url = null;
    String temp, response = "";
    Uri.Builder builderString;
    InputStream inStream = null;
    private SharedPreferences settings;
    private String apiKey, lastUpdatedTimestamp;

    @Override
    public void onMessageReceived(String from, Bundle data) {

        String notificationType = "";
        String smsTitle = "";
        String fromNumber = "";
        String smsMessage = "";

        settings = getSharedPreferences(Constants.GHOST_PREF, 0);
        apiKey = settings.getString("api_key", "");
        lastUpdatedTimestamp = settings.getString("lastUpdatedTimestamp", "");
        SharedPreferences.Editor editor = settings.edit();

        builderString = new Uri.Builder();
        builderString.scheme("http")
                .authority("www.ghostcall.in")
                .appendPath("api")
                .appendPath("numbers");
        if (!lastUpdatedTimestamp.equals("")) {
            builderString.appendQueryParameter("last_timestamp", lastUpdatedTimestamp);
        }

        try {
            url = new URL(builderString.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("X-api-key", apiKey);
            urlConnection.setDoInput(true);
            urlConnection.connect();

            inStream = urlConnection.getInputStream();
            BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));
            while ((temp = bReader.readLine()) != null) {
                response += temp;
            }

            if (response != null) {
                GhostCallDatabaseAdapter numberAdapter = new GhostCallDatabaseAdapter(getApplicationContext());
                numberAdapter.open();
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i <jsonArray.length(); i++) {
                    JSONObject jObject = jsonArray.getJSONObject(i);
                    String ghostID = jObject.getString("id");
                    String ghostNumber = jObject.getString("number");
                    String ghostName = jObject.getString("name");
                    String ghostExpire = jObject.getString("expire_on");
                    String ghostVoicemail = jObject.getString("voicemail");
                    String ghostDisableCalls = jObject.getString("disable_calls");
                    String ghostDisableMessages = jObject.getString("disable_messages");
                    JSONArray callArray = jObject.getJSONArray("calls");

                    if (!(callArray.length() == 0)) {
                        for (int k = 0; k < callArray.length(); k++) {
                            JSONObject jCall = callArray.getJSONObject(k);
                            String callID = jCall.getString("id");
                            String callUserID = jCall.getString("user_id");
                            String callNumberID = jCall.getString("number_id");
                            String callTo = jCall.getString("to");
                            String callFrom = jCall.getString("from");
                            String callDirection = jCall.getString("direction");
                            String callStatus = jCall.getString("status");
                            String callPitch = jCall.getString("pitch");
                            String callBackgroundID = jCall.getString("background_item_id");
                            String callDuration = jCall.getString("duration");
                            String callResourceID = jCall.getString("resource_id");
                            String callRecord = jCall.getString("record");
                            String callCreatedAt = jCall.getString("created_at");
                            String callUpdatedAt = jCall.getString("updated_at");
                            numberAdapter.createCall(callID, callUserID, callNumberID, callTo, callFrom, callDirection, callStatus, callPitch, callBackgroundID, callDuration, callResourceID, callRecord, callCreatedAt, callUpdatedAt);
                        }
                    }
                    JSONArray messageArray = jObject.getJSONArray("messages");
                    if (!(messageArray.length() == 0)) {
                        for (int j = 0; j < messageArray.length(); j++) {
                            JSONObject jMessage = messageArray.getJSONObject(j);
                            String messageID = jMessage.getString("id");
                            String messageUserID = jMessage.getString("user_id");
                            String messageNumberID = jMessage.getString("number_id");
                            String messageTo = jMessage.getString("to");
                            String messageFrom = jMessage.getString("from");
                            String messageDirection = jMessage.getString("direction");
                            String messageStatus = jMessage.getString("status");
                            String messageResourceID = jMessage.getString("resource_id");
                            String messageText = jMessage.getString("text");
                            String messageCreatedAt = jMessage.getString("created_at");
                            String messageupdatedAt = jMessage.getString("updated_at");
                            String messageDeleted = jMessage.getString("deleted");
                            numberAdapter.createMessage(messageID, messageUserID, messageNumberID, messageTo, messageFrom, messageDirection,
                                    messageStatus, messageResourceID, messageText, messageCreatedAt, messageupdatedAt, messageDeleted);


                        }
                    }
                    JSONArray voicemailArray = jObject.getJSONArray("voicemails");
                    if (!(voicemailArray.length() == 0)) {
                        for (int m = 0; m < voicemailArray.length(); m++) {
                            JSONObject jVoicemail = voicemailArray.getJSONObject(m);
                            String voicemailID = jVoicemail.getString("id");
                            String voicemailUserID = jVoicemail.getString("user_id");
                            String voicemailNumberID = jVoicemail.getString("number_id");
                            String voicemailCallID = jVoicemail.getString("call_id");
                            String voicemailTo = jVoicemail.getString("to");
                            String voicemailFrom = jVoicemail.getString("from");
                            String voicemailDuration = jVoicemail.getString("duration");
                            String voicemailResourceID = jVoicemail.getString("resource_id");
                            String voicemailText = jVoicemail.getString("text");
                            String voicemailCreatedAt = jVoicemail.getString("created_at");
                            String voicemailUpdatedAt = jVoicemail.getString("updated_at");
                            numberAdapter.createVoicemail(voicemailID, voicemailUserID, voicemailNumberID, voicemailCallID, voicemailTo, voicemailFrom, voicemailDuration, voicemailResourceID, voicemailText, voicemailCreatedAt, voicemailUpdatedAt);
                        }
                    }
                    if (!numberAdapter.numberExists(ghostID)) {
                        numberAdapter.createNumber(ghostID, ghostNumber, ghostName, ghostVoicemail, ghostDisableCalls, ghostDisableMessages, ghostExpire);
                    }
                }

                String theLatestTimestamp = numberAdapter.getLatestTimestamp();
                editor.putString("lastUpdatedTimestamp", theLatestTimestamp);
                editor.apply();
                lastUpdatedTimestamp = settings.getString("lastUpdatedTimestamp", "");
                numberAdapter.close();
            }
        } catch (Exception e) {
            Log.d("GhostCallListenService", e.toString());
        }

        if (data.getString("message") != null) {
            notificationType = data.getString("message");
            Log.d("notificationType", notificationType);
        } else {
            if (data.getString("body") != null) {
                smsMessage = data.getString("body");
                Log.d("notificationType", smsMessage);
                smsTitle = data.getString("title");
                if (!smsTitle.equals("")) {
                    fromNumber = smsTitle.substring(smsTitle.indexOf("+"));
                }

            }
        }


        if (!notificationType.equals("")) {
            sendCallNotification(notificationType);
        } else if (!fromNumber.equals("")) {
            Intent newSMSincoming = new Intent("newIncomingSMS");
            LocalBroadcastManager.getInstance(this).sendBroadcast(newSMSincoming);
            sendTextNotification(smsMessage, fromNumber);
        }


    }

    private void sendCallNotification(String notificationType) {
        if (notificationType.equals("Incoming call")) {
            Intent intent = new Intent(this, CallScreen.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.gc_notif)
                    .setContentTitle("GhostCall")
                    .setContentText(notificationType)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setColor(R.color.titleblue)
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_HIGH);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notificationBuilder.build());
        }
    }

    private void sendTextNotification(String smsMessage, String fromNumber) {
            Intent intent = new Intent(this, StartScreen.class);
            if (!fromNumber.equals("")) {
                StringBuilder formatNumber = new StringBuilder(fromNumber);
                formatNumber.replace(0, 2, "(");
                formatNumber.insert(4, ")");
                formatNumber.insert(5, " ");
                formatNumber.insert(9, "-");
                intent.putExtra("toNumber", formatNumber.toString());
            }
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.gc_notif)
                    .setContentTitle(fromNumber)
                    .setContentText(smsMessage)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setColor(R.color.titleblue)
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_HIGH);

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notificationBuilder.build());
    }
}
