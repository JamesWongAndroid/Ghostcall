package com.kickbackapps.ghostcall.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kickbackapps.ghostcall.Constants;
import com.kickbackapps.ghostcall.GetUserInfo;
import com.kickbackapps.ghostcall.GhostCallDatabaseAdapter;
import com.kickbackapps.ghostcall.InternetDialog;
import com.kickbackapps.ghostcall.R;
import com.kickbackapps.ghostcall.adapters.GhostNumbersAdapter;
import com.kickbackapps.ghostcall.objects.GhostNumbers;
import com.kickbackapps.ghostcall.pjsip.MyPJSIPService;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class HomeScreen extends AppCompatActivity {

    ListView ghostNumberListView;
    GhostNumbersAdapter gNumberAdapter;
    GhostCallDatabaseAdapter nDatabaseAdapter;
    TextView userNumber;
    ImageView purchaseButton;
    public static final String GHOST_PREF = "GhostPrefFile";
    TextView userRemainingText;
    String ownNumber = "";
    private String lastUpdatedTimestamp, apiKey;
    ArrayList<GhostNumbers> gNumberList;
    SharedPreferences.Editor editor;
    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1d375a")));
        actionBar.setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.home_actionbar_layout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.titleblue));
        }

        userRemainingText = (TextView) findViewById(R.id.remainingText);
        SharedPreferences settings = getSharedPreferences(GHOST_PREF, 0);
        String userSMS = settings.getString("userSMS", "0");
        String userMins = settings.getString("userMins", "0");

        startService(new Intent(this, MyPJSIPService.class));

        userRemainingText.setText(userSMS + " sms / " + userMins + " mins left");

        userNumber = (TextView) findViewById(R.id.user_number);

        purchaseButton = (ImageView) findViewById(R.id.purchaseButton);
        purchaseButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    purchaseButton.setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_ATOP);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    purchaseButton.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
                }
                return false;
            }
        });
        purchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toPurchase = new Intent(HomeScreen.this, SelectPackageScreen.class);
                toPurchase.putExtra(Constants.PACKAGE_TYPE, "credits");
                startActivity(toPurchase);
            }
        });

        ghostNumberListView = (ListView) findViewById(R.id.ghostNumberList);

        nDatabaseAdapter = new GhostCallDatabaseAdapter(HomeScreen.this);
        try {
            nDatabaseAdapter.open();
            ownNumber = nDatabaseAdapter.getUserNumber();
            userNumber.setText(ownNumber);
            gNumberList = nDatabaseAdapter.getUserNumbers();
            nDatabaseAdapter.close();
            if (gNumberList.size() != 0) {
                for (int i = 0; i < gNumberList.size(); i++ ) {
                    try {
                        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                        DateTime dateTime = formatter.parseDateTime(gNumberList.get(i).getExpirationDate());
                        DateTime now = new DateTime();
                        LocalDate today = now.toLocalDate();
                        DateTime startOfToday = today.toDateTimeAtCurrentTime(now.getZone());
                        Days day = Days.daysBetween(startOfToday, dateTime);
                        int difference = day.getDays();
                        if (difference <= 0) {
                            Minutes minutes = Minutes.minutesBetween(startOfToday, dateTime);
                            difference = minutes.getMinutes();
                            if (difference <= 0) {
                                gNumberList.remove(i);
                            }
                        }
                    } catch (Exception e) {

                    }
                }
            }
            gNumberAdapter = new GhostNumbersAdapter(this, gNumberList);
            ghostNumberListView.setAdapter(gNumberAdapter);
        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
        }

        ImageView yourSMSButton = (ImageView) findViewById(R.id.yourSMS);
        yourSMSButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse("sms:"));
                startActivity(sendIntent);
            }
        });

        ImageView yourCallButton = (ImageView) findViewById(R.id.yourCall);
        yourCallButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent callIntent = new Intent(HomeScreen.this, CallScreen.class);
                callIntent.putExtra("callName", "Own Number");
                callIntent.putExtra("ghostIDExtra", "0");
                startActivity(callIntent);
            }
        });

        ghostNumberListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                GhostNumbers ghostItem = (GhostNumbers) adapterView.getItemAtPosition(position);
                Intent intent = new Intent(HomeScreen.this, HistoryScreen.class);
                intent.putExtra("ghostNumberExtra", ghostItem.getGhostNumber());
                intent.putExtra("ghostIDExtra", ghostItem.getGhostID());
                intent.putExtra("ghostExpiration", ghostItem.getExpirationDate());
                intent.putExtra("ghostName", ghostItem.getGhostTitle());
                startActivity(intent);
            }
        });

        Button getGhostNumbers = (Button) findViewById(R.id.getGhostButton);
        getGhostNumbers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreen.this, GetGhostNumberScreen.class));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GetUserInfo userInfo = new GetUserInfo(this);
        userInfo.getUserData();

        if (InternetDialog.haveNetworkConnection(HomeScreen.this)) {
            new getNumbersTask().execute();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public class getNumbersTask extends AsyncTask<Void, Integer, Void> {

        HttpURLConnection urlConnection = null;
        URL url = null;
        String temp, response = "";
        Uri.Builder builderString;
        InputStream inStream = null;
        int progress_status;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            settings = getSharedPreferences(GHOST_PREF, 0);
            apiKey = settings.getString("api_key", "");
            lastUpdatedTimestamp = settings.getString("lastUpdatedTimestamp", "");
            editor = settings.edit();

            builderString = new Uri.Builder();
            builderString.scheme("http")
                    .authority("www.ghostcall.in")
                    .appendPath("api")
                    .appendPath("numbers");
            if (!lastUpdatedTimestamp.equals("")) {
                builderString.appendQueryParameter("last_timestamp", lastUpdatedTimestamp);
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                progress_status = 0;
                publishProgress(progress_status);
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
                    try {
                        GhostCallDatabaseAdapter numberAdapter = new GhostCallDatabaseAdapter(HomeScreen.this);
                        numberAdapter.open();
                        JSONArray jsonArray = new JSONArray(response);
                        progress_status = 10;
                        publishProgress(progress_status);
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
                            progress_status = 20;
                            publishProgress(progress_status);
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
                            progress_status = 30;
                            publishProgress(progress_status);
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
                            progress_status = 40;
                            publishProgress(progress_status);
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

                    } catch (JSONException e) {

                    } catch (SQLException e) {

                    }
                }
            } catch (Exception e) {
                Log.d("Something fucked up", e.getMessage().toString());
            } finally {
                if (inStream != null) {
                    try {
                        inStream.close();
                    } catch (IOException ignored) {
                    }
                }
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d("I'm running", "running..");
            try {
                nDatabaseAdapter.open();
                gNumberList = nDatabaseAdapter.getUserNumbers();
                nDatabaseAdapter.close();

                if (!gNumberList.isEmpty()) {
                    if (gNumberList.size() != 0) {
                        for (int i = 0; i < gNumberList.size(); i++ ) {
                            try {
                                DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                                DateTime dateTime = formatter.parseDateTime(gNumberList.get(i).getExpirationDate());
                                DateTime now = new DateTime();
                                LocalDate today = now.toLocalDate();
                                DateTime startOfToday = today.toDateTimeAtCurrentTime(now.getZone());
                                Days day = Days.daysBetween(startOfToday, dateTime);
                                int difference = day.getDays();
                                if (difference <= 0) {
                                    Minutes minutes = Minutes.minutesBetween(startOfToday, dateTime);
                                    difference = minutes.getMinutes();
                                    if (difference <= 0) {
                                        gNumberList.remove(i);
                                    }
                                }
                            } catch (Exception e) {

                            }
                        }
                    }
                    if (gNumberList.size() != 1) {
                        gNumberAdapter.getData().clear();
                        gNumberAdapter.getData().addAll(gNumberList);
                        gNumberAdapter.notifyDataSetChanged();
                    } else {
                        gNumberAdapter.getData().clear();
                        gNumberAdapter.getData().addAll(gNumberList);
                        gNumberAdapter.notifyDataSetChanged();
                    }

                }


            } catch (Exception e) {
                Log.d("error in home screen", e.getMessage());
            }


        }
    }


}
