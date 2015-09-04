package com.tapfury.ghostcall;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class HistoryScreen extends AppCompatActivity {

    ListView historyList;
    HistoryAdapter historyAdapter;
    TextView userNumber, expireTimer;
    ImageView purchaseButton;
    GhostCallDatabaseAdapter ghostDatabaseAdapter;
    TextView userRemainingText;
    Bundle extras;
    ArrayList<HistoryObject> gHistoryList;
    MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    private String apiKey;
    private SharedPreferences settings;
    RequestInterceptor requestInterceptor;
    RestAdapter restAdapter;
    GhostCallAPIInterface service;
    private static final String GHOST_PREF = "GhostPrefFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_screen);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1d375a")));
        actionBar.setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.home_actionbar_layout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.titleblue));
        }

        settings = getSharedPreferences(GHOST_PREF, 0);
        apiKey = settings.getString("api_key", "");

        requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("X-api-key", apiKey);
            }
        };

        restAdapter = new RestAdapter.Builder().setEndpoint("http://dev.ghostcall.in/api")
                .setRequestInterceptor(requestInterceptor).build();
        service = restAdapter.create(GhostCallAPIInterface.class);

        userRemainingText = (TextView) findViewById(R.id.remainingText);
        SharedPreferences settings = getSharedPreferences(GHOST_PREF, 0);
        String userSMS = settings.getString("userSMS", "0");
        String userMins = settings.getString("userMins", "0");
        userRemainingText.setText(userSMS + " sms / " + userMins + " mins left");

        userNumber = (TextView) findViewById(R.id.user_number);
        expireTimer = (TextView) findViewById(R.id.expire_timer);

        extras = getIntent().getExtras();
        if (!(extras == null)) {
            userNumber.setText(extras.getString("ghostNumberExtra"));
            try {
                DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
                DateTime dateTime = formatter.parseDateTime(extras.getString("ghostExpiration"));
                DateTime now = new DateTime();
                LocalDate today = now.toLocalDate();
                DateTime startOfToday = today.toDateTimeAtStartOfDay(now.getZone());
                Days day = Days.daysBetween(startOfToday, dateTime);
                int difference = day.getDays();
                if (difference > 1) {
                    expireTimer.setText("expires in " + Integer.toString(difference) + " days");
                } else {
                    expireTimer.setText("expires in " + Integer.toString(difference) + " day");
                }

            } catch (IllegalArgumentException e) {
                expireTimer.setText("");
            }
        }

        purchaseButton = (ImageView) findViewById(R.id.purchaseButton);
        purchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toPurchase = new Intent(HistoryScreen.this, SelectPackageScreen.class);
                startActivity(toPurchase);
            }
        });

        historyList = (ListView) findViewById(R.id.historyList);

        ImageView extendButton = (ImageView) findViewById(R.id.extendButton);
        extendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toPurchaseScreen = new Intent(getApplicationContext(), SelectPackageScreen.class);
                startActivity(toPurchaseScreen);
            }
        });

        ghostDatabaseAdapter = new GhostCallDatabaseAdapter(HistoryScreen.this);
        try {
            ghostDatabaseAdapter.open();
            gHistoryList = ghostDatabaseAdapter.getCallHistory(extras.getString("ghostIDExtra"));
            historyAdapter = new HistoryAdapter(this, gHistoryList);
            historyList.setAdapter(historyAdapter);
            ghostDatabaseAdapter.close();
        } catch (SQLException e) {
            // TODO DO SOMETHING
        }

        historyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long id) {
                HistoryObject historyObject = (HistoryObject) adapterView.getItemAtPosition(position);
                if (historyObject.getHistoryType().equals("call")) {
                    if (historyObject.getHistoryRecord().equals("1")) {

                        for (int i = 0; i < gHistoryList.size(); i++) {
                            if (i != position) {
                                if (gHistoryList.get(i).getHistoryRecord().equals("1")) {
                                    gHistoryList.get(i).setHistoryState("not_playing");
                                    ensureMediaPlayerDeath();
                                }
                            }
                        }
                                if (!gHistoryList.get(position).getHistoryState().equals("playing")) {
                                    gHistoryList.get(position).setHistoryState("playing");
                                    mediaPlayer = new MediaPlayer();

                                    service.getMP3(historyObject.getHistoryID(), new Callback<Response>() {
                                        @Override
                                        public void success(Response response, Response response2) {
                                            Log.d("TESTING MP3 API", "SUCCESS");
                                            Uri url = Uri.parse("http://dev.ghostcall.in/api/playback/call/" + gHistoryList.get(position).getHistoryID() + "/mp3");
                                            Map<String, String> headers = new HashMap<String, String>();
                                            headers.put("X-api-key", apiKey);

                                            try {
                                                mediaPlayer.setDataSource(getApplicationContext(), url, headers);
                                                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                                                mediaPlayer.prepareAsync();
                                            } catch (IOException e) {
                                                Toast.makeText(getApplicationContext(), "Fail to load recording", Toast.LENGTH_SHORT).show();
                                            }

                                            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                                                @Override
                                                public void onPrepared(MediaPlayer mp) {
                                                    mp.start();
                                                }
                                            });
                                            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                                @Override
                                                public void onCompletion(MediaPlayer mp) {
                                                    gHistoryList.get(position).setHistoryState("not_playing");
                                                    ensureMediaPlayerDeath();
                                                    historyAdapter.notifyDataSetChanged();
                                                }
                                            });
                                        }

                                        @Override
                                        public void failure(RetrofitError retrofitError) {
                                            Log.d("TESTING MP3 API", "FAIL");
                                        }
                                    });
                                    historyAdapter.notifyDataSetChanged();
                                } else {
                                    gHistoryList.get(position).setHistoryState("not_playing");
                                    ensureMediaPlayerDeath();
                                    historyAdapter.notifyDataSetChanged();
                                }
                    } else {
                        Intent intent = new Intent(HistoryScreen.this, CallScreen.class);
                        intent.putExtra("callName", extras.getString("ghostName"));
                        StringBuilder formatNumber = new StringBuilder(historyObject.getHistoryNumber());
                        formatNumber.delete(0, 1);
                        formatNumber.replace(3, 5, "-");
                        intent.putExtra("toNumber", formatNumber.toString());
                        intent.putExtra("ghostIDExtra", extras.getString("ghostIDExtra"));
                        startActivity(intent);
                    }
                } else if (historyObject.getHistoryType().equals("message")) {
                    Intent intent = new Intent(HistoryScreen.this, SMSActivity.class);
                    intent.putExtra("callName", extras.getString("ghostName"));
                    if (historyObject.getHistoryNumber().equals(extras.getString("ghostNumberExtra"))) {
                        StringBuilder formatNumber = new StringBuilder(historyObject.getHistoryOutNumber());
                        formatNumber.delete(0, 1);
                        formatNumber.replace(3, 5, "-");
                        intent.putExtra("toNumber", formatNumber.toString());
                        intent.putExtra("ghostIDExtra", extras.getString("ghostIDExtra"));
                    } else {
                        StringBuilder formatNumber = new StringBuilder(historyObject.getHistoryNumber());
                        formatNumber.delete(0, 1);
                        formatNumber.replace(3, 5, "-");
                        intent.putExtra("toNumber", formatNumber.toString());
                        intent.putExtra("ghostIDExtra", extras.getString("ghostIDExtra"));
                    }
                    startActivity(intent);
                }
            }
        });

        Button sendTextButton = (Button) findViewById(R.id.sendTextButton);
        sendTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(HistoryScreen.this, SMSActivity.class);
                sendIntent.putExtra("ghostIDExtra", extras.getString("ghostIDExtra"));
                startActivity(sendIntent);
            }
        });

        Button sendCallButton = (Button) findViewById(R.id.sendCallButton);
        sendCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HistoryScreen.this, CallScreen.class);
                intent.putExtra("callName", extras.getString("ghostName"));
                intent.putExtra("ghostIDExtra", extras.getString("ghostIDExtra"));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        for (int i = 0; i < gHistoryList.size(); i++) {
                if (gHistoryList.get(i).getHistoryRecord().equals("1")) {
                    gHistoryList.get(i).setHistoryState("not_playing");
                    ensureMediaPlayerDeath();
                    historyAdapter.notifyDataSetChanged();
                }
            }
    }

    @Override
    protected void onResume() {
        super.onResume();
        GetUserInfo userInfo = new GetUserInfo(this);
        userInfo.getUserData();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void ensureMediaPlayerDeath() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
