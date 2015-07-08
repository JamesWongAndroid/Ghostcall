package com.tapfury.ghostcall;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class HomeScreen extends AppCompatActivity {

    ListView ghostNumberListView;
    GhostNumbersAdapter gNumberAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        ghostNumberListView = (ListView) findViewById(R.id.ghostNumberList);

        GhostNumbers gNumber = new GhostNumbers();
        gNumber.setGhostNumber("201-124-0984");
        gNumber.setGhostTitle("Trial Number");

        GhostNumbers gNumberTwo = new GhostNumbers();
        gNumberTwo.setGhostNumber("123-234-5678");
        gNumberTwo.setGhostTitle("Awesome Number");

        GhostNumbers gNumberThree = new GhostNumbers();
        gNumberThree.setGhostNumber("212-842-1122");
        gNumberThree.setGhostTitle("Manhattan Number");

        List<GhostNumbers> numbersList = new ArrayList<>();
        numbersList.add(gNumber);
        numbersList.add(gNumberTwo);
        numbersList.add(gNumberThree);

        gNumberAdapter = new GhostNumbersAdapter(this, numbersList);
        ghostNumberListView.setAdapter(gNumberAdapter);

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
