package com.tapfury.ghostcall;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class HistoryScreen extends AppCompatActivity {

    ListView historyList;
    HistoryAdapter historyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_screen);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_history_screen, menu);
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
