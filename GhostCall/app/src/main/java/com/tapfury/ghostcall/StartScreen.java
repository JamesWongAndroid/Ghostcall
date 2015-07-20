package com.tapfury.ghostcall;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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


public class StartScreen extends Activity {

    public static final String GHOST_PREF = "GhostPrefFile";
    Button tourButton;
    Button startButton;
    private String apiKey;
    NumbersDatabaseAdapter numberAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        tourButton = (Button) findViewById(R.id.tourButton);
        startButton = (Button) findViewById(R.id.startButton);

        SharedPreferences settings = getSharedPreferences(GHOST_PREF, 0);
        apiKey = settings.getString("api_key", "");
        if (apiKey.equals("")) {

            Toast.makeText(getApplicationContext(), "No API key", Toast.LENGTH_SHORT).show();
        } else {

            Toast.makeText(getApplicationContext(), apiKey, Toast.LENGTH_SHORT).show();
            tourButton.setVisibility(View.GONE);
            startButton.setVisibility(View.GONE);
            new getNumbersTask().execute();
        }

        tourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toTour = new Intent(getApplicationContext(), TourScreen.class);
                startActivity(toTour);

            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartScreen.this, VerificationScreen.class));
            }
        });
    }

    public class getNumbersTask extends AsyncTask<Void, Void, Void> {

        HttpURLConnection urlConnection = null;
        URL url = null;
        String temp, response = "";
        Uri.Builder builderString;
        InputStream inStream = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            builderString = new Uri.Builder();
            builderString.scheme("http")
                    .authority("www.ghostcall.in")
                    .appendPath("api")
                    .appendPath("numbers");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                url = new URL(builderString.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("X-api-key", apiKey);
                urlConnection.setDoInput(true);
                urlConnection.connect();
                int responseCode = urlConnection.getResponseCode();

                inStream = urlConnection.getInputStream();
                BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));
                while ((temp = bReader.readLine()) != null) {
                    response += temp;
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
            if (response != null) {
                try {

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


                        NumbersDatabaseAdapter numberAdapter = new NumbersDatabaseAdapter(StartScreen.this);
                        numberAdapter.open();
                        if (!numberAdapter.numberExists(ghostID)) {
                            numberAdapter.createNumber(ghostID, ghostNumber, ghostName, ghostVoicemail, ghostDisableCalls, ghostDisableMessages, ghostExpire);
                        }
                        numberAdapter.close();

                        Intent toHomeScreen = new Intent(getApplicationContext(), HomeScreen.class);
                        startActivity(toHomeScreen);
                        finish();
                    }

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (SQLException e) {
                    Toast.makeText(getApplicationContext(), "An error occurred", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
