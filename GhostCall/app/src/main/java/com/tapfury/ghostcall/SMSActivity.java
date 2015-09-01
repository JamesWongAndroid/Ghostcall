package com.tapfury.ghostcall;

import android.content.Context;
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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

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

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Ynott on 8/21/15.
 */
public class SMSActivity extends AppCompatActivity {

    ImageView sendTextButton, sendBarOne, sendBarTwo;
    ListView messagesList;
    FrameLayout composeButton;
    AutoCompleteTextView enterNameBar;
    private SmsAdapter smsAdapter;
    Bundle extras;
    private String toNumber, ghostNumberID, apiKey;
    TextView smsNumber;
    GhostCallDatabaseAdapter nDatabaseAdapter;
    ArrayList<SmsObject> smsObjectArrayList;
    EditText composeEditText;
    private static final String GHOST_PREF = "GhostPrefFile";
    private SharedPreferences settings;
    RequestInterceptor requestInterceptor;
    RestAdapter restAdapter;
    GhostCallAPIInterface service;
    private String lastUpdatedTimestamp;
    SmsObject tempNew;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1d375a")));
        actionBar.setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.sms_actionbar_layout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.titleblue));
        }

        settings = getSharedPreferences(GHOST_PREF, 0);
        apiKey = settings.getString("api_key", "");
        lastUpdatedTimestamp = settings.getString("lastUpdatedTimestamp", "");

        requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("X-api-key", apiKey);
            }
        };

        restAdapter = new RestAdapter.Builder().setEndpoint("http://www.ghostcall.in/api")
                .setRequestInterceptor(requestInterceptor).build();
        service = restAdapter.create(GhostCallAPIInterface.class);

        nDatabaseAdapter = new GhostCallDatabaseAdapter(SMSActivity.this);

        smsObjectArrayList = new ArrayList<>();
        smsAdapter = new SmsAdapter(SMSActivity.this, smsObjectArrayList);


        messagesList = (ListView) findViewById(R.id.conversation);
        messagesList.setAdapter(smsAdapter);
        smsNumber = (TextView) findViewById(R.id.smsNumber);
        enterNameBar = (AutoCompleteTextView) findViewById(R.id.EnterNameBar);
        enterNameBar.addTextChangedListener(new PhoneNumberTextWatcher(enterNameBar));
        enterNameBar.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    enterNameBar.setVisibility(View.GONE);
                    smsNumber.setText(enterNameBar.getText().toString());
                    smsNumber.setVisibility(View.VISIBLE);
                    try {
                        nDatabaseAdapter.open();
                        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
                        Phonenumber.PhoneNumber usaNumber = phoneUtil.parse(enterNameBar.getText().toString(), "US");
                        toNumber = phoneUtil.format(usaNumber, PhoneNumberUtil.PhoneNumberFormat.E164);
                        smsObjectArrayList = nDatabaseAdapter.getSmsHistory(ghostNumberID, toNumber);
                        nDatabaseAdapter.close();

                        if (!smsObjectArrayList.isEmpty()) {

                            smsAdapter = new SmsAdapter(SMSActivity.this, smsObjectArrayList);
                            messagesList.setAdapter(smsAdapter);
                        }

                    } catch (SQLException e) {

                    } catch (NumberParseException e) {

                    }
                }
                return false;
            }
        });

        extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.getString("toNumber") != null) {
                toNumber = extras.getString("toNumber");
                ghostNumberID = extras.getString("ghostIDExtra");
                smsNumber.setText(toNumber);
                smsNumber.setVisibility(View.VISIBLE);
                try {
                    nDatabaseAdapter.open();
                    PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
                    Phonenumber.PhoneNumber usaNumber = phoneUtil.parse(toNumber, "US");
                    toNumber = phoneUtil.format(usaNumber, PhoneNumberUtil.PhoneNumberFormat.E164);
                    smsObjectArrayList = nDatabaseAdapter.getSmsHistory(ghostNumberID, toNumber);
                    nDatabaseAdapter.close();

                    if (!smsObjectArrayList.isEmpty()) {

                        smsAdapter = new SmsAdapter(SMSActivity.this, smsObjectArrayList);
                        messagesList.setAdapter(smsAdapter);
                    }

                } catch (SQLException e) {

                } catch (NumberParseException e) {

                }
            } else {
                enterNameBar.setVisibility(View.VISIBLE);
                ghostNumberID = extras.getString("ghostIDExtra");
            }
        }

        sendTextButton = (ImageView) findViewById(R.id.sendTextCircleButton);
        sendTextButton.setColorFilter(getResources().getColor(R.color.titleblue), PorterDuff.Mode.SRC_ATOP);

        tempNew = new SmsObject();
        composeEditText = (EditText) findViewById(R.id.compose_reply_text);
        composeButton = (FrameLayout) findViewById(R.id.compose_button);
        composeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendTextButton.setColorFilter(getResources().getColor(R.color.gray), PorterDuff.Mode.SRC_ATOP);
                final String sendText = composeEditText.getText().toString();
                composeEditText.setText("");

                tempNew.setMessageText(sendText);
                tempNew.setMessageDirection("out");
                tempNew.setMessageDate("sending...");
                smsAdapter.getData().add(tempNew);
                smsAdapter.notifyDataSetChanged();
                messagesList.setSelection(smsAdapter.getCount() - 1);

                composeButton.setClickable(false);
                service.sendText(toNumber, ghostNumberID, sendText, new Callback<Response>() {
                    @Override
                    public void success(Response response, Response response2) {
                        new getNumbersTask().execute();
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        tempNew.setMessageDate("failed to send");
                    }
                });
            }
        });


        sendBarOne = (ImageView) findViewById(R.id.compose_button_bar_1);
        sendBarTwo = (ImageView) findViewById(R.id.compose_button_bar_2);
        float translation = dpToPx(this, 14) / 3;
        sendBarTwo.setTranslationY(translation);
        sendBarOne.setTranslationY(-translation);

    }

    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:

                return true;
        }
        return super.onOptionsItemSelected(item);
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
            progress_status = 0;
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

            SharedPreferences settings = getSharedPreferences(GHOST_PREF, 0);
            SharedPreferences.Editor editor = settings.edit();

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
                        GhostCallDatabaseAdapter numberAdapter = new GhostCallDatabaseAdapter(SMSActivity.this);
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
            sendTextButton.setColorFilter(getResources().getColor(R.color.titleblue), PorterDuff.Mode.SRC_ATOP);
            composeButton.setClickable(true);
            try {
                nDatabaseAdapter.open();
                PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
                Phonenumber.PhoneNumber usaNumber = phoneUtil.parse(smsNumber.getText().toString(), "US");
                toNumber = phoneUtil.format(usaNumber, PhoneNumberUtil.PhoneNumberFormat.E164);
                smsObjectArrayList = nDatabaseAdapter.getSmsHistory(ghostNumberID, toNumber);
                nDatabaseAdapter.close();

                if (!smsObjectArrayList.isEmpty()) {
                    if (smsObjectArrayList.size() != 1) {
                        smsAdapter.getData().clear();
                        smsAdapter.getData().addAll(smsObjectArrayList);
                        smsAdapter.notifyDataSetChanged();
                        messagesList.setSelection(smsAdapter.getCount() - 1);
                    } else {
                        smsAdapter.getData().clear();
                        smsAdapter.getData().addAll(smsObjectArrayList);
                        smsAdapter.notifyDataSetChanged();
                        messagesList.setSelection(smsAdapter.getCount() - 1);
                    }

                }


            } catch (SQLException e) {

            } catch (NumberParseException e) {

            }


        }
    }
}