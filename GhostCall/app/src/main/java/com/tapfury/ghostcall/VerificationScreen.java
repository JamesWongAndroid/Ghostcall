package com.tapfury.ghostcall;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class VerificationScreen extends AppCompatActivity {

    private EditText codePhoneInput;
    PhoneNumberFormattingTextWatcher mPhoneWatcher = new PhoneNumberFormattingTextWatcher();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_screen);

        codePhoneInput = (EditText) findViewById(R.id.phoneCodeField);
        codePhoneInput.addTextChangedListener(mPhoneWatcher);
        TelephonyManager tManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String uuid = tManager.getDeviceId();
        Log.d("UUID", uuid);

        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VerificationScreen.this, CodeVerificationScreen.class));
              //  new LoginCredentialsTask().execute();
            }
        });
    }

    public class LoginCredentialsTask extends AsyncTask<Void, Void, Void> {

        HttpURLConnection urlConnection = null;
        URL url = null;
        JSONObject object = null;
        InputStream inStream = null;
        String urlString = "https://dev.ghostcall.in/login?phone_number=%2B12019831916&device_id=354982058226540&platform=android&platform_version=4.4";

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                url = new URL(urlString.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoInput(true);
                urlConnection.connect();
          //      urlConnection.setRequestProperty("X-api-key", "edce0e48b3c5e99d6fa3867e3cff221a10d682712d981efaf345ab670bdc3799");
                inStream = urlConnection.getInputStream();
                BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));
                String temp, response = "";
                while ((temp = bReader.readLine()) != null) {
                    response += temp;
                }
                object = (JSONObject) new JSONTokener(response).nextValue();
            } catch (Exception e) {
                Log.d("WTF MAAAAAAN", e.getMessage());
            } finally {
                if (inStream != null) {
                    try {
                        // this will close the bReader as well
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
    }
}
