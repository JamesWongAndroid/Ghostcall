package com.tapfury.ghostcall;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.sql.SQLException;
import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class HistoryScreen extends AppCompatActivity {

    ListView historyList;
    HistoryAdapter historyAdapter;
    TextView userNumber, expireTimer;
    ImageView purchaseButton;
    GhostCallDatabaseAdapter ghostDatabaseAdapter;

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

        GetUserInfo userInfo = new GetUserInfo(this);
        userInfo.getUserData();

        userNumber = (TextView) findViewById(R.id.user_number);
        expireTimer = (TextView) findViewById(R.id.expire_timer);

        Bundle extras = getIntent().getExtras();
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
            ArrayList<HistoryObject> gHistoryList = ghostDatabaseAdapter.getCallHistory();
            historyAdapter = new HistoryAdapter(this, gHistoryList);
            historyList.setAdapter(historyAdapter);
            ghostDatabaseAdapter.close();
        } catch (SQLException e) {

        }

        Button sendTextButton = (Button) findViewById(R.id.sendTextButton);
        sendTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse("sms:"));
                startActivity(sendIntent);
            }
        });

        Button sendCallButton = (Button) findViewById(R.id.sendCallButton);
        sendCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(getApplicationContext(), CallScreen.class);
                startActivity(callIntent);
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
