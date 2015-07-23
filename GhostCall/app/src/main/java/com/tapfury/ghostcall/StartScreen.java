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

import com.tapfury.ghostcall.SoundEffects.SoundEffectsData;

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
import java.util.List;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.http.GET;


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
                RequestInterceptor requestInterceptor = new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addHeader("X-api-key", apiKey);
                    }
                };

                RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("http://www.ghostcall.in/api")
                        .setRequestInterceptor(requestInterceptor).build();
                GhostCallAPIInterface service = restAdapter.create(GhostCallAPIInterface.class);



                try {
                    NumbersDatabaseAdapter numberAdapter = new NumbersDatabaseAdapter(StartScreen.this);
                    numberAdapter.open();

                    if (!numberAdapter.numberPackageExists("1")) {
                        List<NumberPackagesData> newNumberPackageList = service.getNewNumberPackages();
                        if (!newNumberPackageList.isEmpty()) {
                            for (int i = 0; i < newNumberPackageList.size(); i++) {
                                numberAdapter.createNumbersPackage(newNumberPackageList.get(i).getId(), newNumberPackageList.get(i).getName(), newNumberPackageList.get(i).getType(),
                                        newNumberPackageList.get(i).getCredits(), newNumberPackageList.get(i).getCost(), newNumberPackageList.get(i).getIosProductId(),
                                        newNumberPackageList.get(i).getAndroidProductId(), newNumberPackageList.get(i).getExpiration(), newNumberPackageList.get(i).getCreatedOn());
                            }
                        }
                    }

                    if (!numberAdapter.numberPackageExists("4")) {
                        List<NumberPackagesData> extendNumberPackageList = service.getExtendNumberPackages();
                        if (!extendNumberPackageList.isEmpty()) {
                            for (int i = 0; i <extendNumberPackageList.size(); i++) {
                                numberAdapter.createNumbersPackage(extendNumberPackageList.get(i).getId(), extendNumberPackageList.get(i).getName(), extendNumberPackageList.get(i).getType(),
                                        extendNumberPackageList.get(i).getCredits(), extendNumberPackageList.get(i).getCost(), extendNumberPackageList.get(i).getIosProductId(),
                                        extendNumberPackageList.get(i).getAndroidProductId(), extendNumberPackageList.get(i).getExpiration(), extendNumberPackageList.get(i).getCreatedOn());
                            }
                        }
                    }

                    if (!numberAdapter.soundEffectExists("2")) {
                        List<SoundEffectsData> soundEffectsDataList = service.getsoundEffectsList();
                        if (!soundEffectsDataList.isEmpty()) {
                            for (int i = 0; i < soundEffectsDataList.size(); i++) {
                                List<com.tapfury.ghostcall.SoundEffects.Item> effectItems = soundEffectsDataList.get(i).getItems();
                                for (int j = 0; j < effectItems.size(); j++) {
                                    numberAdapter.createSoundEffect(effectItems.get(j).getId(), effectItems.get(j).getEffectId(), effectItems.get(j).getName(),
                                            effectItems.get(j).getAudioName(), effectItems.get(j).getVolume(), effectItems.get(j).getImageActive(), effectItems.get(j).getImageOn(),
                                            effectItems.get(j).getImageOff(), effectItems.get(j).getCreatedAt(), effectItems.get(j).getUpdatedAt(), effectItems.get(j).getAudioUrl());
                                }
                            }
                        }
                    }
                    numberAdapter.close();
                } catch (SQLException e) {

                }

                service.creditPackagesList(new Callback<List<CreditPackagesData>>() {
                    @Override
                    public void success(List<CreditPackagesData> creditPackagesDatas, Response response) {
                        try {
                            NumbersDatabaseAdapter numberAdapter = new NumbersDatabaseAdapter(StartScreen.this);
                            numberAdapter.open();
                            List<CreditPackagesData> creditPackagesDataList = new ArrayList();
                            creditPackagesDataList.addAll(creditPackagesDatas);
                            if (!creditPackagesDataList.isEmpty()) {
                                for (int i = 0; i < creditPackagesDataList.size(); i++) {
                                    if (!numberAdapter.creditPackageExists(creditPackagesDataList.get(i).getId())) {
                                        numberAdapter.createCreditsNumber(creditPackagesDataList.get(i).getId(),
                                                creditPackagesDataList.get(i).getName(),
                                                creditPackagesDataList.get(i).getDescription(),
                                                creditPackagesDataList.get(i).getCost(),
                                                creditPackagesDataList.get(i).getCredits(),
                                                creditPackagesDataList.get(i).getIosProductId(),
                                                creditPackagesDataList.get(i).getAndroidProductId(),
                                                creditPackagesDataList.get(i).getCreatedAt(),
                                                creditPackagesDataList.get(i).getUpdatedAt(),
                                                creditPackagesDataList.get(i).getDeleted()
                                        );
                                    }
                                }
                            }
                            numberAdapter.close();
                        } catch (SQLException e) {

                        }
                    }

                    @Override
                    public void failure(RetrofitError retrofitError) {
                        Log.d("wtf", "fuck");
                    }
                });



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
                    }

                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (SQLException e) {
                    Toast.makeText(getApplicationContext(), "An error occurred", Toast.LENGTH_SHORT).show();
                }
            }
            Intent toHomeScreen = new Intent(getApplicationContext(), HomeScreen.class);
            startActivity(toHomeScreen);
            finish();
        }
    }

    public interface GhostCallAPIInterface {
        @GET("/credit_packages")
        void creditPackagesList(Callback<List<CreditPackagesData>> callBack);

        @GET("/effects")
        List<SoundEffectsData> getsoundEffectsList();

        @GET("/packages?type=new")
        List<NumberPackagesData> getNewNumberPackages();

        @GET("/packages?type=extend")
        List<NumberPackagesData> getExtendNumberPackages();
    }
}
