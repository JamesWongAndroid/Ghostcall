package com.tapfury.ghostcall;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class GetGhostNumberScreen extends AppCompatActivity {

    TextView userRemainingText, getGhostLabel;
    EditText nickNameField, areaCodeField;
    View rootView;
    private SharedPreferences settings;
    private String apiKey;
    RequestInterceptor requestInterceptor;
    RestAdapter restAdapter;
    GhostCallAPIInterface service;
    RelativeLayout spinnerLayout;
    CircleProgressBar progressSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_ghost_number_screen);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1d375a")));
        actionBar.setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.home_actionbar_layout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.titleblue));
        }

        GetUserInfo userInfo = new GetUserInfo(this);
        userInfo.getUserData();

        spinnerLayout = (RelativeLayout) findViewById(R.id.spinnerLayout);
        progressSpinner = (CircleProgressBar) findViewById(R.id.progressBar);
        progressSpinner.setColorSchemeResources(android.R.color.holo_blue_dark);

        userRemainingText = (TextView) findViewById(R.id.remainingText);
        getGhostLabel = (TextView) findViewById(R.id.getGhostLabel);
        SharedPreferences settings = getSharedPreferences(Constants.GHOST_PREF, 0);
        String userSMS = settings.getString("userSMS", "0");
        String userMins = settings.getString("userMins", "0");
        userRemainingText.setText(userSMS + " sms / " + userMins + " mins left");

        rootView = findViewById(R.id.rootView);
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = rootView.getRootView().getHeight() - rootView.getHeight();
                if (heightDiff > 300) {
                    getGhostLabel.setVisibility(View.GONE);
                } else {
                    getGhostLabel.setVisibility(View.VISIBLE);
                }
            }
        });

        nickNameField = (EditText) findViewById(R.id.nicknameEditText);
        areaCodeField = (EditText) findViewById(R.id.areaCodeEditText);

        Button continueButton = (Button) findViewById(R.id.continueButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nickNameField.getText().toString().length() == 0) {
                    Toast.makeText(GetGhostNumberScreen.this, "Please insert a nickname for your new number", Toast.LENGTH_SHORT).show();
                } else if (areaCodeField.getText().toString().length() < 3) {
                    Toast.makeText(GetGhostNumberScreen.this, "Please insert a valid area code", Toast.LENGTH_SHORT).show();
                } else {
                    new checkAreaCodeTask().execute();
                }

            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public class checkAreaCodeTask extends AsyncTask<Void, Void, Void> {
        String areaCode, nickName;
        AreaCodeObject areaCodeObject;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            areaCode = areaCodeField.getText().toString();
            nickName = nickNameField.getText().toString();
            spinnerLayout.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {

            settings = getSharedPreferences(Constants.GHOST_PREF, 0);
            apiKey = settings.getString("api_key", "");

            requestInterceptor = new RequestInterceptor() {
                @Override
                public void intercept(RequestFacade request) {
                    request.addHeader("X-api-key", apiKey);
                }
            };

            restAdapter = new RestAdapter.Builder().setEndpoint("http://www.ghostcall.in/api")
                    .setRequestInterceptor(requestInterceptor).build();
            service = restAdapter.create(GhostCallAPIInterface.class);

            try {
                areaCodeObject = service.getAreaCodeStatus(areaCode);
            } catch (RetrofitError e) {
                Log.d("GhostCall-Retrofit", e.getMessage());
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (areaCodeObject != null) {
                if (areaCodeObject.getAvailable() != null) {
                    if (areaCodeObject.getAvailable()) {
                        spinnerLayout.setVisibility(View.INVISIBLE);
                        Intent toGetNumberScreen = new Intent(GetGhostNumberScreen.this, SelectPackageScreen.class);
                        toGetNumberScreen.putExtra("nickName", nickName);
                        toGetNumberScreen.putExtra(Constants.PACKAGE_TYPE, "new");
                        toGetNumberScreen.putExtra("areacode", areaCode);
                        startActivity(toGetNumberScreen);
                    }
                } else if (areaCodeObject.getCategory() != null) {
                    if (areaCodeObject.getCategory().equals("area_code_not_available")) {
                        Toast.makeText(GetGhostNumberScreen.this, "This area code is not available, please try again", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(GetGhostNumberScreen.this, "This area code is not available, please try again", Toast.LENGTH_SHORT).show();
                spinnerLayout.setVisibility(View.INVISIBLE);
            }
        }
    }
}
