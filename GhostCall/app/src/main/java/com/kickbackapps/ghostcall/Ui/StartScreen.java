package com.kickbackapps.ghostcall.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.net.sip.SipAudioCall;
import android.net.sip.SipManager;
import android.net.sip.SipProfile;
import android.net.sip.SipRegistrationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.kickbackapps.ghostcall.objects.BackgroundEffects.BackgroundEffectsData;
import com.kickbackapps.ghostcall.objects.CreditPackagesData;
import com.kickbackapps.ghostcall.objects.NumberPackagesData;
import com.kickbackapps.ghostcall.objects.SoundEffects.SoundEffectsData;
import com.kickbackapps.ghostcall.Constants;
import com.kickbackapps.ghostcall.GhostCallAPIInterface;
import com.kickbackapps.ghostcall.GhostCallDatabaseAdapter;
import com.kickbackapps.ghostcall.R;
import com.kickbackapps.ghostcall.user.UserData;

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
import java.util.List;

import io.fabric.sdk.android.Fabric;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class StartScreen extends Activity {

    public static final String GHOST_PREF = "GhostPrefFile";
    Button tourButton;
    Button startButton;
    TextView progressText;
    NumberProgressBar progressBar;
    private String apiKey, lastUpdatedTimestamp;
    private boolean isUserLoaded, isNumberPackageLoaded, isExtendLoaded, isCreditsLoaded, isEffectsLoaded, isBackgroundLoaded;
    RelativeLayout startRelativeLayout;
    Boolean verified;
    String sipUsername;
    String sipPassword;
    SipManager sipManager = null;
    SipProfile sipProfile = null;
    SipAudioCall.Listener listener;
    SipAudioCall call = null;
    SipRegistrationListener registrationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_start_screen);

        tourButton = (Button) findViewById(R.id.tourButton);
        startButton = (Button) findViewById(R.id.startButton);

        progressBar = (NumberProgressBar) findViewById(R.id.preloaderBar);
        progressText = (TextView) findViewById(R.id.progressText);

        SharedPreferences settings = getSharedPreferences(GHOST_PREF, 0);
        apiKey = settings.getString("api_key", "");
        verified = settings.getBoolean("verified", false);
        lastUpdatedTimestamp = settings.getString("lastUpdatedTimestamp", "");
        isUserLoaded = settings.getBoolean("userDataLoaded", false);
        isNumberPackageLoaded = settings.getBoolean("numberPackageLoaded", false);
        isExtendLoaded = settings.getBoolean("extendLoaded", false);
        isCreditsLoaded = settings.getBoolean("creditsLoaded", false);
        isEffectsLoaded = settings.getBoolean("effectsLoaded", false);
        isBackgroundLoaded = settings.getBoolean("backgroundsLoaded", false);
        sipUsername = settings.getString(Constants.SIP_NAME, "");
        sipPassword = settings.getString(Constants.SIP_PASSWORD, "");

        startRelativeLayout = (RelativeLayout) findViewById(R.id.startRelativeLayout);

        if (apiKey.equals("") || verified == false) {


        } else {
            tourButton.setVisibility(View.GONE);
            startButton.setVisibility(View.GONE);
            new getNumbersTask().execute();
        }

        tourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toTour = new Intent(getApplicationContext(), TourScreen.class);
                startActivity(toTour);
                finish();

            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartScreen.this, VerificationScreen.class));
                finish();
            }
        });
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
            progressBar.setVisibility(View.VISIBLE);
            progressText.setVisibility(View.VISIBLE);
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
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            progressBar.setProgress(values[0]);


            switch (values[0]) {
                case 10:
                    progressText.setText("Loading Server Data...");
                    break;
                case 20:
                    progressText.setText("Loading Numbers...");
                    break;
                case 30:
                    progressText.setText("Loading Calls...");
                    break;
                case 40:
                    progressText.setText("Loading Messages...");
                    break;
                case 50:
                    progressText.setText("Loading Voicemails...");
                    break;
                case 60:
                    progressText.setText("Loading Credits...");
                    break;
                case 70:
                    progressText.setText("Loading Number Packages...");
                    break;
                case 80:
                    progressText.setText("Loading Sound Effects...");
                    break;
                case 90:
                    progressText.setText("Loading Background Effects");
                    break;
                case 100:
                    progressText.setText("Done!");
                    break;
            }

        }

        @Override
        protected Void doInBackground(Void... voids) {

            SharedPreferences settings = getSharedPreferences(GHOST_PREF, 0);
            SharedPreferences.Editor editor = settings.edit();

            try {
                RequestInterceptor requestInterceptor = new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addHeader("X-api-key", apiKey);
                    }
                };

                RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("http://www.ghostcall.in/api")
                        .setRequestInterceptor(requestInterceptor).build();
                GhostCallAPIInterface service = restAdapter.create(GhostCallAPIInterface.class);

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
                        GhostCallDatabaseAdapter numberAdapter = new GhostCallDatabaseAdapter(StartScreen.this);
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
                        numberAdapter.close();

                    } catch (JSONException e) {

                    } catch (SQLException e) {

                    }
                }


                try {
                    GhostCallDatabaseAdapter numberAdapter = new GhostCallDatabaseAdapter(StartScreen.this);
                    numberAdapter.open();

                    if (!isUserLoaded) {
                        UserData userData = service.getUserData();
                        if (!(userData == null)) {
                            numberAdapter.createUser(userData.getId(), userData.getPhoneNumber(), userData.getDeviceToken(),
                                    userData.getAppVersion(), userData.getPlatform(), userData.getPlatformVersion(),
                                    userData.getApiKeyId(), userData.getName(), userData.getEmail(), userData.getCredits(),
                                    userData.getCreatedAt(), userData.getUpdatedAt(), userData.getDeleted(), userData.getBalance().getSms(), userData.getBalance().getMinutes(), userData.getBalance().getCredits());
                            editor.putBoolean("userDataLoaded", true);
                            editor.putString(Constants.SIP_NAME, userData.getSip().getUsername());
                            editor.putString(Constants.SIP_PASSWORD, userData.getSip().getPassword());
                            editor.apply();
                        }
                        progress_status = 50;
                        publishProgress(progress_status);
                    }



                    if (!isNumberPackageLoaded) {
                        List<NumberPackagesData> newNumberPackageList = service.getNewNumberPackages();
                        if (!newNumberPackageList.isEmpty()) {
                            for (int i = 0; i < newNumberPackageList.size(); i++) {
                                numberAdapter.createNumbersPackage(newNumberPackageList.get(i).getId(), newNumberPackageList.get(i).getName(), newNumberPackageList.get(i).getType(),
                                        newNumberPackageList.get(i).getCredits(), newNumberPackageList.get(i).getCost(), newNumberPackageList.get(i).getIosProductId(),
                                        newNumberPackageList.get(i).getAndroidProductId(), newNumberPackageList.get(i).getExpiration(), newNumberPackageList.get(i).getCreatedOn());
                            }
                            editor.putBoolean("numberPackageLoaded", true);
                            editor.apply();
                        }
                        progress_status = 60;
                        publishProgress(progress_status);
                    }

                    if (!isExtendLoaded) {
                        List<NumberPackagesData> extendNumberPackageList = service.getExtendNumberPackages();
                        if (!extendNumberPackageList.isEmpty()) {
                            for (int i = 0; i <extendNumberPackageList.size(); i++) {
                                numberAdapter.createNumbersPackage(extendNumberPackageList.get(i).getId(), extendNumberPackageList.get(i).getName(), extendNumberPackageList.get(i).getType(),
                                        extendNumberPackageList.get(i).getCredits(), extendNumberPackageList.get(i).getCost(), extendNumberPackageList.get(i).getIosProductId(),
                                        extendNumberPackageList.get(i).getAndroidProductId(), extendNumberPackageList.get(i).getExpiration(), extendNumberPackageList.get(i).getCreatedOn());
                            }
                            editor.putBoolean("extendLoaded", true);
                            editor.apply();
                        }
                        progress_status = 70;
                        publishProgress(progress_status);
                    }

                    if (!isEffectsLoaded) {
                        List<SoundEffectsData> soundEffectsDataList = service.getsoundEffectsList();
                        if (!soundEffectsDataList.isEmpty()) {
                            for (int i = 0; i < soundEffectsDataList.size(); i++) {
                                List<com.kickbackapps.ghostcall.objects.SoundEffects.Item> effectItems = soundEffectsDataList.get(i).getItems();
                                for (int j = 0; j < effectItems.size(); j++) {
                                    numberAdapter.createSoundEffect(effectItems.get(j).getId(), effectItems.get(j).getEffectId(), effectItems.get(j).getName(),
                                            effectItems.get(j).getAudioName(), effectItems.get(j).getVolume(), effectItems.get(j).getImageActive(), effectItems.get(j).getImageOn(),
                                            effectItems.get(j).getImageOff(), effectItems.get(j).getCreatedAt(), effectItems.get(j).getUpdatedAt(), effectItems.get(j).getAudioUrl());
                                }
                            }
                            editor.putBoolean("effectsLoaded", true);
                            editor.apply();
                        }
                        progress_status = 80;
                        publishProgress(progress_status);
                    }

                    if (!isBackgroundLoaded) {
                        List<BackgroundEffectsData> backgroundEffectsDataList = service.getBackgroundEffectsList();
                        if (!backgroundEffectsDataList.isEmpty()) {
                            for (int i = 0; i < backgroundEffectsDataList.size(); i++) {
                                List<com.kickbackapps.ghostcall.objects.BackgroundEffects.Item> backgroundItems = backgroundEffectsDataList.get(i).getItems();
                                for (int j = 0; j < backgroundItems.size(); j++) {
                                    numberAdapter.createBackgroundEffects(backgroundItems.get(j).getId(), backgroundItems.get(j).getBackgroundId(), backgroundItems.get(j).getName(),
                                            backgroundItems.get(j).getAudioName(), backgroundItems.get(j).getVolume(), backgroundItems.get(j).getAudioUrl());
                                }
                            }
                            editor.putBoolean("backgroundsLoaded", true);
                            editor.apply();
                        }
                        progress_status = 90;
                        publishProgress(progress_status);
                    }

                    if (!isCreditsLoaded) {
                        List<CreditPackagesData> creditPackagesDataList = service.getCreditPackagesData();
                        if (!creditPackagesDataList.isEmpty()) {
                            for (int i = 0; i < creditPackagesDataList.size(); i++) {
                                numberAdapter.createCreditsNumber(creditPackagesDataList.get(i).getId(), creditPackagesDataList.get(i).getName(), creditPackagesDataList.get(i).getDescription(),
                                        creditPackagesDataList.get(i).getCost(), creditPackagesDataList.get(i).getCredits(), creditPackagesDataList.get(i).getIosProductId(),
                                        creditPackagesDataList.get(i).getAndroidProductId(), creditPackagesDataList.get(i).getCreatedAt(), creditPackagesDataList.get(i).getUpdatedAt(), creditPackagesDataList.get(i).getDeleted());
                            }
                            editor.putBoolean("creditsLoaded", true);
                            editor.apply();
                        }
                        progress_status = 100;
                        publishProgress(progress_status);
                    }

                    numberAdapter.close();
                } catch (SQLException e) {
                    // TODO: DO SOMETHING HERE
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

            Intent toHomeScreen = new Intent(getApplicationContext(), HomeScreen.class);
            startActivity(toHomeScreen);
            finish();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
