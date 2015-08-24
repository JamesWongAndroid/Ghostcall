package com.tapfury.ghostcall;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Ynott on 8/21/15.
 */
public class SMSActivity extends AppCompatActivity {

    ImageView sendTextButton, sendBarOne, sendBarTwo;
    ListView messagesList;
    private SmsAdapter smsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1d375a")));
        actionBar.setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.home_actionbar_layout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.titleblue));
        }

        messagesList = (ListView) findViewById(R.id.conversation);

        ArrayList<SmsObject> smsObjectArrayList = new ArrayList<>();
        SmsObject smsObject1 = new SmsObject();
        smsObject1.setMessageText("Hi, I'm another test message");
        smsObject1.setMessageDirection("to");
        smsObject1.setMessageDate("Aug 02, 2:33 PM");
        smsObject1.setMessageName("Dave");
        smsObjectArrayList.add(smsObject1);

        SmsObject smsObject2 = new SmsObject();
        smsObject2.setMessageText("Hi, I'm another test message");
        smsObject2.setMessageDirection("from");
        smsObject2.setMessageDate("Aug 02, 5:33 PM");
        smsObject2.setMessageName("Dave");
        smsObjectArrayList.add(smsObject2);

        SmsObject smsObject3 = new SmsObject();
        smsObject3.setMessageText("Hi, I'm a much longer, stronger, bigger, faster, test message");
        smsObject3.setMessageDirection("to");
        smsObject3.setMessageDate("Aug 02, 2:33 PM");
        smsObject3.setMessageName("Dave");
        smsObjectArrayList.add(smsObject3);

        smsAdapter = new SmsAdapter(this, smsObjectArrayList);

        messagesList.setAdapter(smsAdapter);

        sendTextButton = (ImageView) findViewById(R.id.sendTextCircleButton);
        sendTextButton.setColorFilter(getResources().getColor(R.color.titleblue), PorterDuff.Mode.SRC_ATOP);
        sendBarOne = (ImageView) findViewById(R.id.compose_button_bar_1);
        sendBarTwo = (ImageView) findViewById(R.id.compose_button_bar_2);
        float translation = dpToPx(this, 14) / 3;
        sendBarTwo.setTranslationY(translation);
        sendBarOne.setTranslationY(-translation);
    }

    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }
}
