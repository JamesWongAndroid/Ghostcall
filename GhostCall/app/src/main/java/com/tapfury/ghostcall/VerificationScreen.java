package com.tapfury.ghostcall;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class VerificationScreen extends AppCompatActivity {

    private EditText codePhoneInput;
    private String deviceID;
    private String buildNumber;
    private String ePhoneNumber;
    CircleProgressBar progressSpinner;
    RelativeLayout spinnerLayout;
    public static final String GHOST_PREF = "GhostPrefFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_screen);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1d375a")));
        actionBar.setDisplayShowTitleEnabled(false);

        codePhoneInput = (EditText) findViewById(R.id.phoneCodeField);
        codePhoneInput.addTextChangedListener(new PhoneNumberTextWatcher(codePhoneInput));

        TelephonyManager tManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        deviceID = tManager.getDeviceId();
        buildNumber = Integer.toString(Build.VERSION.SDK_INT);

        spinnerLayout = (RelativeLayout) findViewById(R.id.spinnerLayout);
        progressSpinner = (CircleProgressBar) findViewById(R.id.progressBar);
        progressSpinner.setColorSchemeResources(android.R.color.holo_blue_dark);


        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerLayout.setVisibility(View.VISIBLE);
                PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
                try {
                    Phonenumber.PhoneNumber usaNumber = phoneUtil.parse(codePhoneInput.getText().toString(), "US");
                    if (phoneUtil.isValidNumberForRegion(usaNumber, "US")) {
                        ePhoneNumber = phoneUtil.format(usaNumber, PhoneNumberUtil.PhoneNumberFormat.E164);
                        new LoginCredentialsTask().execute();
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid Number", Toast.LENGTH_SHORT).show();
                        spinnerLayout.setVisibility(View.INVISIBLE);
                    }
                } catch (NumberParseException e) {
                    Toast.makeText(getApplicationContext(), "Invalid Number", Toast.LENGTH_SHORT).show();
                    spinnerLayout.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    public class LoginCredentialsTask extends AsyncTask<Void, Void, Void> {

        HttpURLConnection urlConnection = null;
        URL url = null;
        String temp, response = "";
        Uri.Builder builderString;
        InputStream inStream = null;
        StringBuilder phoneNumber;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            phoneNumber = new StringBuilder();
            phoneNumber.append("%2B").append(codePhoneInput.getText().toString());
            builderString = new Uri.Builder();
            builderString.scheme("http")
                    .authority("www.ghostcall.in")
                    .appendPath("api")
                    .appendPath("login")
                    .appendQueryParameter("phone_number", ePhoneNumber)
                    .appendQueryParameter("device_id", deviceID)
                    .appendQueryParameter("platform", "android")
                    .appendQueryParameter("platform_version", buildNumber);
        }

        @Override
        protected Void doInBackground(Void... voids) {
                try {
                    url = new URL(builderString.toString());
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setDoInput(true);
                    urlConnection.connect();
                    inStream = urlConnection.getInputStream();
                    BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));
                    while ((temp = bReader.readLine()) != null) {
                        response += temp;
                    }
                } catch (Exception e) {
                    String error = e.getMessage();
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
            if (response != null) {
                try {
                    JSONObject jObject = new JSONObject(response);
                    String apiKey = jObject.getString("api_key");
                    SharedPreferences settings = getSharedPreferences(GHOST_PREF, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("api_key", apiKey);
                    editor.apply();
                    new sendVerifyCodeTask().execute();
                }  catch (JSONException e) {
                    spinnerLayout.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(), "Fail to connect to server", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public class sendVerifyCodeTask extends AsyncTask<Void, Void, Void> {

        HttpURLConnection urlConnection = null;
        URL url = null;
        String temp, response = "";
        Uri.Builder builderString;
        InputStream inStream = null;
        String apiKey;
        int responseCode;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            builderString = new Uri.Builder();
            builderString.scheme("http")
                    .authority("www.ghostcall.in")
                    .appendPath("api")
                    .appendPath("verify");
            SharedPreferences settings = getSharedPreferences(GHOST_PREF, 0);
            apiKey = settings.getString("api_key", "");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (apiKey.equals("")) {
                // do something
            } else {
                try {
                    url = new URL(builderString.toString());
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("X-api-key", apiKey);
                    urlConnection.setDoInput(true);
                    urlConnection.connect();
                    responseCode = urlConnection.getResponseCode();
                } catch (Exception e) {

                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            spinnerLayout.setVisibility(View.INVISIBLE);
            if (responseCode == 200 || responseCode == 201 || responseCode == 202) {
                startActivity(new Intent(VerificationScreen.this, CodeVerificationScreen.class));
            } else {
                Toast.makeText(getApplicationContext(), "Fail to connect to server", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(aVoid);
        }
    }
}
