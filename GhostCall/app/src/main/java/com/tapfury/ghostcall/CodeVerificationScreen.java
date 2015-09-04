package com.tapfury.ghostcall;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CodeVerificationScreen extends AppCompatActivity {

    EditText smsCodeField;
    Button resendButton;
    public static final String GHOST_PREF = "GhostPrefFile";
    CircleProgressBar progressSpinner;
    RelativeLayout spinnerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_verification_screen);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1d375a")));
        actionBar.setDisplayShowTitleEnabled(false);

        spinnerLayout = (RelativeLayout) findViewById(R.id.spinnerLayout);
        progressSpinner = (CircleProgressBar) findViewById(R.id.progressBar);
        progressSpinner.setColorSchemeResources(android.R.color.holo_blue_dark);

        smsCodeField = (EditText) findViewById(R.id.smsCodeInput);
        resendButton = (Button) findViewById(R.id.resendButton);
        resendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        findViewById(R.id.verifyButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerLayout.setVisibility(View.VISIBLE);
                new verifyCodeTask().execute();
            }
        });
    }

    public class verifyCodeTask extends AsyncTask<Void, Void, Void> {

        HttpURLConnection urlConnection = null;
        URL url = null;
        String temp, response = "";
        Uri.Builder builderString;
        InputStream inStream = null;
        String apiKey;
        StringBuilder codeInput;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            builderString = new Uri.Builder();
            builderString.scheme("http")
                    .authority("dev.ghostcall.in")
                    .appendPath("api")
                    .appendPath("verify");
            SharedPreferences settings = getSharedPreferences(GHOST_PREF, 0);
            apiKey = settings.getString("api_key", "");
            codeInput = new StringBuilder();
            codeInput.append("code=");
            codeInput.append(smsCodeField.getText().toString());
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                url = new URL(builderString.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("X-api-key", apiKey);
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
//                urlConnection.connect();
//                responseCode = urlConnection.getResponseCode();

                DataOutputStream wr = new DataOutputStream (urlConnection.getOutputStream());
                wr.writeBytes(codeInput.toString());
                wr.flush();
                wr.close();

                inStream = urlConnection.getInputStream();
                BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));
                while ((temp = bReader.readLine()) != null) {
                    response += temp;
                }
            } catch (Exception e) {

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
                    String verificationString = jObject.getString("verified");
                    if (verificationString.equals("true")) {
                        startActivity(new Intent(CodeVerificationScreen.this, StartScreen.class));
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "An error occurred", Toast.LENGTH_SHORT).show();
                    }
                    spinnerLayout.setVisibility(View.INVISIBLE);
                }  catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "An error occurred", Toast.LENGTH_SHORT).show();
                    spinnerLayout.setVisibility(View.INVISIBLE);
                }
            }
        }
    }
}
