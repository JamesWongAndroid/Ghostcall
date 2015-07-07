package com.tapfury.ghostcall;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class SelectPackageScreen extends AppCompatActivity {

    ListView listView;
    PackageAdapter packageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_package_screen);
        listView = (ListView) findViewById(R.id.packageListView);

        GhostPackage gPackage = new GhostPackage();
        gPackage.setPackageName("Lite Pack");
        gPackage.setPackageTime("Topping off a bit - 20 mins / 40 sms");
        gPackage.setPackagePrice("$1.99");

        GhostPackage gPackageTwo = new GhostPackage();
        gPackageTwo.setPackageName("Power Pack");
        gPackageTwo.setPackageTime("Down to business - 60 mins / 120 sms");
        gPackageTwo.setPackagePrice("$5.99");

        GhostPackage gPackageThree = new GhostPackage();
        gPackageThree.setPackageName("Super Pack");
        gPackageThree.setPackageTime("Chattin - 100 mins / 200 sms");
        gPackageThree.setPackagePrice("$9.99");

        List<GhostPackage> ghostPackageList = new ArrayList<>();
        ghostPackageList.add(gPackage);
        ghostPackageList.add(gPackageTwo);
        ghostPackageList.add(gPackageThree);
        packageAdapter = new PackageAdapter(this, ghostPackageList);
        listView.setAdapter(packageAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select_package_screen, menu);
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
