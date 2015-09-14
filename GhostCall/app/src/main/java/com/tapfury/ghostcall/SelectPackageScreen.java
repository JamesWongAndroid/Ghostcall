package com.tapfury.ghostcall;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SelectPackageScreen extends AppCompatActivity {

    ListView listView;
    PackageAdapter packageAdapter;
    TextView userRemainingText, selectLabel;
    GhostCallDatabaseAdapter nDatabaseAdapter;
    Bundle extras;
    String packagesType;
    ImageView purchaseButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_package_screen);
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

        purchaseButton = (ImageView) findViewById(R.id.purchaseButton);
        purchaseButton.setVisibility(View.GONE);

        userRemainingText = (TextView) findViewById(R.id.remainingText);
        SharedPreferences settings = getSharedPreferences(Constants.GHOST_PREF, 0);
        String userSMS = settings.getString("userSMS", "0");
        String userMins = settings.getString("userMins", "0");
        userRemainingText.setText(userSMS + " sms / " + userMins + " mins left");

        selectLabel = (TextView) findViewById(R.id.selectLabel);

        listView = (ListView) findViewById(R.id.packageListView);
        nDatabaseAdapter = new GhostCallDatabaseAdapter(SelectPackageScreen.this);
        extras = getIntent().getExtras();
        if (extras != null) {
            packagesType = extras.getString(Constants.PACKAGE_TYPE);
            if (packagesType.equals("new") || packagesType.equals("extend")) {
                if (packagesType.equals("new")) {
                    selectLabel.setText("Select a number package");
                } else {
                    selectLabel.setText("Select an extend package");
                }
                try {
                    nDatabaseAdapter.open();
                    List<GhostPackage> ghostPackageList = nDatabaseAdapter.getPackages(packagesType);
                    nDatabaseAdapter.close();
                    packageAdapter = new PackageAdapter(this, ghostPackageList);
                    listView.setAdapter(packageAdapter);
                } catch (Exception e) {
                    Log.d("Select Package - Error", e.getMessage());
                    nDatabaseAdapter.close();
                }
            } else if (packagesType.equals("credits")) {
                selectLabel.setText("Select a credits package");
                try {
                    nDatabaseAdapter.open();
                    List<GhostPackage> ghostPackageList = nDatabaseAdapter.getCreditPackages();
                    nDatabaseAdapter.close();
                    packageAdapter = new PackageAdapter(this, ghostPackageList);
                    listView.setAdapter(packageAdapter);

                } catch (Exception e) {
                    Log.d("Select Package - Error", e.getMessage());
                }
            }
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
