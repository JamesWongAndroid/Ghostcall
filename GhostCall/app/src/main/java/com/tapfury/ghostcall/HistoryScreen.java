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

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class HistoryScreen extends AppCompatActivity {

    ListView historyList;
    HistoryAdapter historyAdapter;
    TextView userNumber;
    ImageView purchaseButton;

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

        Bundle extras = getIntent().getExtras();
        if (!(extras == null)) {
            userNumber.setText(extras.getString("ghostNumberExtra"));

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


        HistoryObject historyObject = new HistoryObject();
        historyObject.setHistoryIcon("sms");
        historyObject.setHistoryNumber("(201) 241-2897");
        historyObject.setHistoryDescription("You got me dude");
        historyObject.setHistoryDate("Jun 23");
        historyObject.setHistoryTime("05:17pm");

        HistoryObject historyObjectOne = new HistoryObject();
        historyObjectOne.setHistoryIcon("sms");
        historyObjectOne.setHistoryNumber("(201) 241-2897");
        historyObjectOne.setHistoryDescription("ghostcall for the win");
        historyObjectOne.setHistoryDate("Jun 23");
        historyObjectOne.setHistoryTime("03:10pm");

        HistoryObject historyObjectTwo = new HistoryObject();
        historyObjectTwo.setHistoryIcon("sent");
        historyObjectTwo.setHistoryNumber("(212) 125-4971");
        historyObjectTwo.setHistoryDescription("test post");
        historyObjectTwo.setHistoryDate("Jun 23");
        historyObjectTwo.setHistoryTime("01:14pm");

        HistoryObject historyObjectThree = new HistoryObject();
        historyObjectThree.setHistoryIcon("sent");
        historyObjectThree.setHistoryNumber("(201) 241-2897");
        historyObjectThree.setHistoryDescription("scary ghostcall");
        historyObjectThree.setHistoryDate("Jun 23");
        historyObjectThree.setHistoryTime("11:17am");

        HistoryObject historyObjectFour = new HistoryObject();
        historyObjectFour.setHistoryIcon("smss");
        historyObjectFour.setHistoryNumber("(201) 241-2897");
        historyObjectFour.setHistoryDescription("outgoing call");
        historyObjectFour.setHistoryDate("Jun 22");
        historyObjectFour.setHistoryTime("12:17pm");

        List<HistoryObject> historyArrayList = new ArrayList<>();
        historyArrayList.add(historyObject);
        historyArrayList.add(historyObjectTwo);
        historyArrayList.add(historyObjectThree);
        historyArrayList.add(historyObjectFour);

        historyAdapter = new HistoryAdapter(this, historyArrayList);
        historyList.setAdapter(historyAdapter);

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
