package com.tapfury.ghostcall;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class StartScreen extends Activity {

    public static final String GHOST_PREF = "GhostPrefFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        SharedPreferences settings = getSharedPreferences(GHOST_PREF, 0);
        String apiKey = settings.getString("api_key", "");
        if (apiKey.equals("")) {
            Toast.makeText(getApplicationContext(), "No API key", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), apiKey, Toast.LENGTH_SHORT).show();
        }

        Button tourButton = (Button) findViewById(R.id.tourButton);
        tourButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toTour = new Intent(getApplicationContext(), TourScreen.class);
                startActivity(toTour);

            }
        });
        findViewById(R.id.tourButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartScreen.this, TourScreen.class));
            }
        });
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartScreen.this, VerificationScreen.class));
            }
        });
    }
}
