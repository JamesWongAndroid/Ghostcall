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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class HomeScreen extends AppCompatActivity {

    ListView ghostNumberListView;
    GhostNumbersAdapter gNumberAdapter;
    GhostCallDatabaseAdapter nDatabaseAdapter;
    TextView userNumber;
    ImageView purchaseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
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

        purchaseButton = (ImageView) findViewById(R.id.purchaseButton);
        purchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toPurchase = new Intent(HomeScreen.this, SelectPackageScreen.class);
                startActivity(toPurchase);
            }
        });

        ghostNumberListView = (ListView) findViewById(R.id.ghostNumberList);

        nDatabaseAdapter = new GhostCallDatabaseAdapter(HomeScreen.this);
        try {
            nDatabaseAdapter.open();
            userNumber.setText(nDatabaseAdapter.getUserNumber());
            ArrayList<GhostNumbers> gNumberList = nDatabaseAdapter.getUserNumbers();
            nDatabaseAdapter.close();
            gNumberAdapter = new GhostNumbersAdapter(this, gNumberList);
            ghostNumberListView.setAdapter(gNumberAdapter);
        } catch (SQLException e) {
            Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
        }

        ImageView yourSMSButton = (ImageView) findViewById(R.id.yourSMS);
        yourSMSButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse("sms:"));
                startActivity(sendIntent);
            }
        });

        ImageView yourCallButton = (ImageView) findViewById(R.id.yourCall);
        yourCallButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent callIntent = new Intent(HomeScreen.this, CallScreen.class);
                startActivity(callIntent);
            }
        });

        ghostNumberListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                GhostNumbers ghostItem = (GhostNumbers) adapterView.getItemAtPosition(position);
                Intent intent = new Intent(HomeScreen.this, HistoryScreen.class);
                intent.putExtra("ghostNumberExtra", ghostItem.getGhostNumber());
                intent.putExtra("ghostIDExtra", ghostItem.getGhostID());
                intent.putExtra("ghostExpiration", ghostItem.getExpirationDate());
                startActivity(intent);
            }
        });

        Button getGhostNumbers = (Button) findViewById(R.id.getGhostButton);
        getGhostNumbers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreen.this, GetGhostNumberScreen.class));
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
