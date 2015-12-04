package com.kickbackapps.ghostcall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.kickbackapps.ghostcall.User.UserData;

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

    ImageView sendTextButton;
    ListView messagesList;
    FrameLayout composeButton;
    RelativeLayout composeLayout;
    AutoCompleteTextView enterNameBar;
    private SmsAdapter smsAdapter;
    Bundle extras;
    private String toNumber, ghostNumberID, apiKey, smsLimit;
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
    private BroadcastReceiver incomingSmsBroadcastReceiver;
    boolean canSendAgain = true;
    Button contactsButton;
    private Uri uriContact;
    private String contactID;
    private String contactNumber;

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

        service.getUserData(new Callback<UserData>() {
            @Override
            public void success(UserData userData, Response response) {
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("userSMS", userData.getBalance().getSms());
                editor.putString("userMins", userData.getBalance().getMinutes());
                editor.apply();
                smsLimit = userData.getBalance().getSms();
            }

            @Override
            public void failure(RetrofitError retrofitError) {

            }
        });

        nDatabaseAdapter = new GhostCallDatabaseAdapter(SMSActivity.this);

        smsObjectArrayList = new ArrayList<>();
        smsAdapter = new SmsAdapter(SMSActivity.this, smsObjectArrayList);

        composeLayout = (RelativeLayout) findViewById(R.id.relativeComposeLayout);

        contactsButton = (Button) findViewById(R.id.smsContactsButton);
        contactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), Constants.REQUEST_CODE_PICK_CONTACTS);
            }
        });

        messagesList = (ListView) findViewById(R.id.conversation);
        messagesList.setAdapter(smsAdapter);
        smsNumber = (TextView) findViewById(R.id.smsNumber);
        enterNameBar = (AutoCompleteTextView) findViewById(R.id.EnterNameBar);
        enterNameBar.addTextChangedListener(new PhoneNumberTextWatcher(enterNameBar));
        enterNameBar.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {

                    try {
                        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
                        Phonenumber.PhoneNumber usaNumber = phoneUtil.parse(enterNameBar.getText().toString(), "US");
                        if (phoneUtil.isValidNumberForRegion(usaNumber, "US")) {
                            hideKeyboard();
                            enterNameBar.setCursorVisible(false);
                            nDatabaseAdapter.open();
                            enterNameBar.setVisibility(View.GONE);
                            contactsButton.setVisibility(View.GONE);
                            smsNumber.setText(enterNameBar.getText().toString());
                            smsNumber.setVisibility(View.VISIBLE);
                            composeLayout.setVisibility(View.VISIBLE);
                            toNumber = phoneUtil.format(usaNumber, PhoneNumberUtil.PhoneNumberFormat.E164);
                            smsObjectArrayList = nDatabaseAdapter.getSmsHistory(ghostNumberID, toNumber);
                            nDatabaseAdapter.close();

                            if (!smsObjectArrayList.isEmpty()) {
                                smsObjectArrayList = removeDuplicates(smsObjectArrayList);
                                smsAdapter = new SmsAdapter(SMSActivity.this, smsObjectArrayList);
                                messagesList.setAdapter(smsAdapter);
                            }
                        } else {
                            Toast.makeText(SMSActivity.this, "Invalid Number", Toast.LENGTH_SHORT).show();
                        }
                    } catch (NumberParseException e) {
                        Toast.makeText(SMSActivity.this, "Invalid Number", Toast.LENGTH_SHORT).show();
                    } catch (SQLException e) {

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
                contactsButton.setVisibility(View.GONE);
                smsNumber.setText(toNumber);
                smsNumber.setVisibility(View.VISIBLE);
                composeLayout.setVisibility(View.VISIBLE);
                try {
                    nDatabaseAdapter.open();
                    PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
                    Phonenumber.PhoneNumber usaNumber = phoneUtil.parse(toNumber, "US");
                    toNumber = phoneUtil.format(usaNumber, PhoneNumberUtil.PhoneNumberFormat.E164);
                    smsObjectArrayList = nDatabaseAdapter.getSmsHistory(ghostNumberID, toNumber);
                    nDatabaseAdapter.close();

                    if (!smsObjectArrayList.isEmpty()) {
                        smsObjectArrayList = removeDuplicates(smsObjectArrayList);
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
        sendTextButton.setClickable(false);
        sendTextButton.setColorFilter(getResources().getColor(R.color.gray), PorterDuff.Mode.SRC_ATOP);

        tempNew = new SmsObject();
        composeEditText = (EditText) findViewById(R.id.compose_reply_text);
        composeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    composeButton.setClickable(true);
                    sendTextButton.setColorFilter(getResources().getColor(R.color.titleblue), PorterDuff.Mode.SRC_ATOP);
                } else {
                    composeButton.setClickable(false);
                    sendTextButton.setColorFilter(getResources().getColor(R.color.gray), PorterDuff.Mode.SRC_ATOP);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        composeButton = (FrameLayout) findViewById(R.id.compose_button);
        composeButton.setClickable(false);
        composeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (InternetDialog.haveNetworkConnection(SMSActivity.this)) {
                    if (smsLimit != null) {
                        if (!smsLimit.equals("0")) {
                            if (canSendAgain) {
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
                                canSendAgain = false;
                                service.sendText(toNumber, ghostNumberID, sendText, new Callback<Response>() {
                                    @Override
                                    public void success(Response response, Response response2) {
                                        new getNumbersTask().execute();

                                    }

                                    @Override
                                    public void failure(RetrofitError retrofitError) {
                                        tempNew.setMessageDate("failed to send");
                                        canSendAgain = true;
                                    }
                                });
                            }
                        } else {
                            Toast.makeText(SMSActivity.this, "Out of Credits", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SMSActivity.this, "There was an error sending your text.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    InternetDialog.showInternetDialog(SMSActivity.this);
                }

            }
        });

        incomingSmsBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                try {
                    if (smsNumber != null || !smsNumber.equals("")) {
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
                                smsObjectArrayList = removeDuplicates(smsObjectArrayList);
                                smsAdapter.getData().clear();
                                smsAdapter.getData().addAll(smsObjectArrayList);
                                smsAdapter.notifyDataSetChanged();
                                messagesList.setSelection(smsAdapter.getCount() - 1);
                            }
                        }
                        lastUpdatedTimestamp = settings.getString("lastUpdatedTimestamp", "");
                    }

                } catch (Exception e) {

                }
            }
        };

    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).registerReceiver(incomingSmsBroadcastReceiver, new IntentFilter("newIncomingSMS"));
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(incomingSmsBroadcastReceiver, new IntentFilter("newIncomingSMS"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent backToStart = new Intent(SMSActivity.this, HomeScreen.class);
            startActivity(backToStart);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE_PICK_CONTACTS && resultCode == RESULT_OK) {
            uriContact = data.getData();
            contactNumber = retrieveContactNumber();
            contactNumber = contactNumber.replaceAll("[^0-9]", "");
            if (contactNumber.startsWith("1")) {
                contactNumber = contactNumber.replaceFirst("1", "");
            }

            try {
                StringBuilder contactNumberBuilder = new StringBuilder(contactNumber);
                contactNumberBuilder.insert(3, '-');
                contactNumberBuilder.insert(7, '-');

                PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
                Phonenumber.PhoneNumber usaNumber = phoneNumberUtil.parse(contactNumberBuilder.toString(), "US");
                if (phoneNumberUtil.isValidNumberForRegion(usaNumber, "US")) {
                    hideKeyboard();
                    enterNameBar.setCursorVisible(false);
                    nDatabaseAdapter.open();
                    enterNameBar.setVisibility(View.GONE);
                    smsNumber.setText(contactNumberBuilder.toString());
                    contactsButton.setVisibility(View.GONE);
                    smsNumber.setVisibility(View.VISIBLE);
                    composeLayout.setVisibility(View.VISIBLE);
                    toNumber = phoneNumberUtil.format(usaNumber, PhoneNumberUtil.PhoneNumberFormat.E164);
                    smsObjectArrayList = nDatabaseAdapter.getSmsHistory(ghostNumberID, toNumber);
                    nDatabaseAdapter.close();

                    if (!smsObjectArrayList.isEmpty()) {
                        smsObjectArrayList = removeDuplicates(smsObjectArrayList);
                        smsAdapter = new SmsAdapter(SMSActivity.this, smsObjectArrayList);
                        messagesList.setAdapter(smsAdapter);
                    }
                }
            } catch (Exception e) {
                Toast.makeText(SMSActivity.this, "Invalid Number", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent backtoStart = new Intent(SMSActivity.this, HomeScreen.class);
        startActivity(backtoStart);
        finish();
    }

    private String retrieveContactNumber() {

        String contactNumber;

        // getting contacts ID
        Cursor cursorID = getContentResolver().query(uriContact,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);

        if (cursorID.moveToFirst()) {

            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }

        cursorID.close();

        // Using the contact ID now we will get contact phone number
        Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},

                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,

                new String[]{contactID},
                null);

        if (cursorPhone.moveToFirst()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            return contactNumber;

        } else {
            cursorPhone.close();
            return "";
        }
    }

    public ArrayList<SmsObject> removeDuplicates(ArrayList<SmsObject> smsList) {
        for (int i = 0; i < smsList.size(); i++) {
            if (i != 0) {
                int previousValue = i - 1;
                SmsObject previousSmsObject = smsList.get(previousValue);
                SmsObject currentSmsObject = smsList.get(i);
                if (previousSmsObject.getMessageDate().equals(currentSmsObject.getMessageDate()) &&
                        (previousSmsObject.getMessageText().equals(currentSmsObject.getMessageText()))) {
                    smsList.remove(i);
                }
            }
        }
        return smsList;
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
                        canSendAgain = true;
                    } else {
                        for (int i = 0; i < smsObjectArrayList.size(); i++) {
                            if (i != 0) {
                                int previousValue = i - 1;
                                SmsObject previousSmsObject = smsObjectArrayList.get(previousValue);
                                SmsObject currentSmsObject = smsObjectArrayList.get(i);
                                if (previousSmsObject.getMessageDate().equals(currentSmsObject.getMessageDate()) &&
                                        (previousSmsObject.getMessageText().equals(currentSmsObject.getMessageText()))) {
                                    smsObjectArrayList.remove(i);
                                }
                            }
                        }
                        smsAdapter.getData().clear();
                        smsAdapter.getData().addAll(smsObjectArrayList);
                        smsAdapter.notifyDataSetChanged();
                        messagesList.setSelection(smsAdapter.getCount() - 1);
                        canSendAgain = true;
                    }
                }

                service.getUserData(new Callback<UserData>() {
                    @Override
                    public void success(UserData userData, Response response) {
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("userSMS", userData.getBalance().getSms());
                        editor.putString("userMins", userData.getBalance().getMinutes());
                        editor.apply();
                        smsLimit = userData.getBalance().getSms();
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {

                    }
                });


            } catch (Exception e) {
                canSendAgain = true;
            }


        }
    }
}
