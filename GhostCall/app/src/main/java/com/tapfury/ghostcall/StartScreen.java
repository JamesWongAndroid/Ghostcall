package com.tapfury.ghostcall;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class StartScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

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
