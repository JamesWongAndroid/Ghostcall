package com.tapfury.ghostcall;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
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

public class HomeScreen extends AppCompatActivity {

    ListView ghostNumberListView;
    GhostNumbersAdapter gNumberAdapter;
    GhostCallDatabaseAdapter nDatabaseAdapter;
    TextView userNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1d375a")));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.titleblue));
        }

        userNumber = (TextView) findViewById(R.id.user_number);

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
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(HomeScreen.this, HistoryScreen.class);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
