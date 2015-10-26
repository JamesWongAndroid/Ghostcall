package com.kickbackapps.ghostcall;

import android.app.Activity;
import android.content.SharedPreferences;
import android.widget.TextView;

import com.kickbackapps.ghostcall.User.UserData;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Ynott on 7/28/15.
 */
public class GetUserInfo {

    private final Activity activity;
    private String apiKey;
    private static final String GHOST_PREF = "GhostPrefFile";
    UserData userInfo;
    private final SharedPreferences settings;
    TextView userRemainingText;

    public GetUserInfo(Activity activity) {
        this.activity = activity;
        settings = activity.getSharedPreferences(GHOST_PREF, 0);
    }

    public void getUserData() {

        apiKey = settings.getString("api_key", "");
        userInfo = new UserData();
        userRemainingText = (TextView) activity.findViewById(R.id.remainingText);

            RequestInterceptor requestInterceptor = new RequestInterceptor() {
                @Override
                public void intercept(RequestFacade request) {
                    request.addHeader("X-api-key", apiKey);
                }
            };

            RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("http://www.ghostcall.in/api")
                    .setRequestInterceptor(requestInterceptor).build();
            GhostCallAPIInterface service = restAdapter.create(GhostCallAPIInterface.class);

            service.getUserData(new Callback<UserData>() {
                @Override
                public void success(UserData userData, Response response) {
                    userInfo.setBalance(userData.getBalance());
                    userRemainingText.setText(userData.getBalance().getSms() + " sms / " + userData.getBalance().getMinutes() + " mins left");
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("userSMS", userData.getBalance().getSms());
                    editor.putString("userMins", userData.getBalance().getMinutes());
                    editor.apply();
                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    userRemainingText.setText("No Connection");
                }
            });

    }

    public void getUserDataForCall() {

        apiKey = settings.getString("api_key", "");
        userInfo = new UserData();
        userRemainingText = (TextView) activity.findViewById(R.id.remainingText);

        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("X-api-key", apiKey);
            }
        };

        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("http://www.ghostcall.in/api")
                .setRequestInterceptor(requestInterceptor).build();
        GhostCallAPIInterface service = restAdapter.create(GhostCallAPIInterface.class);

        service.getUserData(new Callback<UserData>() {
            @Override
            public void success(UserData userData, Response response) {
                userInfo.setBalance(userData.getBalance());
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("userSMS", userData.getBalance().getSms());
                editor.putString("userMins", userData.getBalance().getMinutes());
                editor.apply();
            }

            @Override
            public void failure(RetrofitError retrofitError) {

            }
        });

    }
}
