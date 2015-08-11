package com.tapfury.ghostcall;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.skyfishjy.library.RippleBackground;
import com.tapfury.ghostcall.BackgroundEffects.BackgroundObject;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CallScreen extends AppCompatActivity implements View.OnClickListener {

    LinearLayout dialpadOne, dialpadTwo, dialpadThree, dialpadFour, dialpadFive, dialpadSix, dialpadSeven, dialpadEight, dialpadNine,
    dialpadContacts, dialpadDelete, dialpadZero, recordButton;
    LinearLayout rowNumberOne, rowNumberTwo, rowNumberThree, rowNumberFour;
    LinearLayout vcHolder, vcLayout, bgHolder;
    RelativeLayout dialpadHolder;
    DiscreteSeekBar voiceChangeBar;
    ImageView recordImage;
    GridView bgGrid;
    BackgroundAdapter backgroundAdapter;
    ImageView previousView = null;

    private static final int REQUEST_CODE_PICK_CONTACTS = 1;
    private Uri uriContact;
    private String contactID;
    private String contactNumber;
    private String apiKey;
    private String toNumber;
    private String numberID;
    private String verified;
    private String voiceChangerNumber ="0";
    private String backgroundID = "0";
    private static final String GHOST_PREF = "GhostPrefFile";
    private static final String TAG = CallScreen.class.getSimpleName();
    private boolean isRecording = false;
    private boolean isChangingVoice = false;
    private boolean isChangingBG = false;
    private ImageView vcIcon;
    private ImageView bgIcon;
    private ArrayList<BackgroundObject> backgroundList;
    TextView numberName, bgText, vcText;
    EditText numberBox;
    Bundle extras;
    private SharedPreferences settings;
    Button makeCallButton;
    Button closeButton;
    RippleBackground rippleBackground;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_screen);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1d375a")));
        actionBar.setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.home_actionbar_layout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.titleblue));
        }

        settings = getSharedPreferences(GHOST_PREF, 0);
        apiKey = settings.getString("api_key", "");

        numberName = (TextView) findViewById(R.id.remainingText);
        numberBox  = (EditText) findViewById(R.id.callEditText);

        rippleBackground = (RippleBackground) findViewById(R.id.content_loading);

        extras = getIntent().getExtras();
        if (!(extras == null)) {
            numberID = extras.getString("ghostIDExtra");
            if (numberID.equals("0")) {
                verified = "true";
            } else {
                verified = "";
            }
            numberName.setText(extras.getString("callName"));
            String toNumber = extras.getString("toNumber");
            if (toNumber != null) {
                numberBox.append(extras.getString("toNumber"));
            }

        }

        dialpadHolder = (RelativeLayout) findViewById(R.id.dialpadLayout);

        numberBox.addTextChangedListener(new PhoneNumberTextWatcher(numberBox));

        recordImage = (ImageView) findViewById(R.id.recordImage);

        voiceChangeBar = (DiscreteSeekBar) findViewById(R.id.voiceSeekBar);

        closeButton = (Button) findViewById(R.id.closeVCButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeAllViews();
                showDialpad();
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
                changeTextColor();
            }
        });

        makeCallButton = (Button) findViewById(R.id.makeCallButton);
        makeCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                voiceChangerNumber = Integer.toString(voiceChangeBar.getProgress());
                PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
                try {
                    Phonenumber.PhoneNumber usaNumber = phoneUtil.parse(numberBox.getText().toString(), "US");
                    if (phoneUtil.isValidNumberForRegion(usaNumber, "US")) {

                        rippleBackground.setVisibility(View.VISIBLE);
                        rippleBackground.startRippleAnimation();
                        dialpadHolder.setVisibility(View.GONE);

                        toNumber = phoneUtil.format(usaNumber, PhoneNumberUtil.PhoneNumberFormat.E164);

                        RequestInterceptor requestInterceptor = new RequestInterceptor() {
                            @Override
                            public void intercept(RequestFacade request) {
                                request.addHeader("X-api-key", apiKey);
                            }
                        };

                        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint("http://www.ghostcall.in/api")
                                .setRequestInterceptor(requestInterceptor).build();
                        GhostCallAPIInterface service = restAdapter.create(GhostCallAPIInterface.class);

                        service.makeCall(toNumber, numberID, backgroundID, voiceChangerNumber, verified, new Callback<CallData>() {
                            @Override
                            public void success(CallData callData, Response response) {
                                String toCallNumber = callData.getDial();
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:" + toCallNumber));
                                startActivity(callIntent);
                                Toast.makeText(getApplicationContext(), "Making Call", Toast.LENGTH_SHORT).show();
                                rippleBackground.stopRippleAnimation();
                                rippleBackground.setVisibility(View.GONE);
                                dialpadHolder.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void failure(RetrofitError retrofitError) {
                                Toast.makeText(getApplicationContext(), "Error Calling", Toast.LENGTH_SHORT).show();
                                rippleBackground.stopRippleAnimation();
                                dialpadHolder.setVisibility(View.VISIBLE);
                                rippleBackground.setVisibility(View.GONE);
                            }
                        });

                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid Number", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberParseException e) {
                    Toast.makeText(getApplicationContext(), "Invalid Number", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialpadOne = (LinearLayout) findViewById(R.id.dialPadOne);
        dialpadTwo = (LinearLayout) findViewById(R.id.dialPadTwo);
        dialpadThree = (LinearLayout) findViewById(R.id.dialPadThree);
        dialpadFour = (LinearLayout) findViewById(R.id.dialPadFour);
        dialpadFive = (LinearLayout) findViewById(R.id.dialPadFive);
        dialpadSix = (LinearLayout) findViewById(R.id.dialPadSix);
        dialpadSeven = (LinearLayout) findViewById(R.id.dialPadSeven);
        dialpadEight = (LinearLayout) findViewById(R.id.dialPadEight);
        dialpadNine = (LinearLayout) findViewById(R.id.dialPadNine);
        dialpadContacts = (LinearLayout) findViewById(R.id.dialPadContact);
        dialpadDelete = (LinearLayout) findViewById(R.id.dialPadDelete);
        dialpadZero = (LinearLayout) findViewById(R.id.dialPadZero);
        recordButton = (LinearLayout) findViewById(R.id.recordHolder);

        rowNumberOne = (LinearLayout) findViewById(R.id.firstDialerRow);
        rowNumberTwo = (LinearLayout) findViewById(R.id.secondDialerRow);
        rowNumberThree = (LinearLayout) findViewById(R.id.thirdDialerRow);
        rowNumberFour = (LinearLayout) findViewById(R.id.contactRow);

        vcHolder = (LinearLayout) findViewById(R.id.vcHolder);
        vcIcon = (ImageView) findViewById(R.id.vcIcon);
        vcLayout = (LinearLayout) findViewById(R.id.vcLayout);
        vcText = (TextView) findViewById(R.id.vcText);

        bgHolder = (LinearLayout) findViewById(R.id.bgHolder);
        bgIcon = (ImageView) findViewById(R.id.bgIcon);
        bgGrid = (GridView) findViewById(R.id.bgLayout);
        bgGrid.setChoiceMode(GridView.CHOICE_MODE_SINGLE);
        bgText = (TextView) findViewById(R.id.bgText);
        GhostCallDatabaseAdapter databaseAdapter = new GhostCallDatabaseAdapter(CallScreen.this);

        try {
            databaseAdapter.open();
            backgroundList = databaseAdapter.getBackgroundObjects();
            backgroundAdapter = new BackgroundAdapter(this, backgroundList);
            bgGrid.setAdapter(backgroundAdapter);
            databaseAdapter.close();
        } catch (SQLException e) {

        }

        bgGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                if (backgroundList.get(position).getBackgroundState() != null) {
                    for (int i = 0; i < backgroundList.size(); i++) {
                        if (i != position) {
                            backgroundList.get(i).setBackgroundState("");
                            if (mediaPlayer != null) {
                                mediaPlayer.stop();
                                mediaPlayer.release();
                                mediaPlayer = null;
                            }
                        }
                    }

                    if (!backgroundList.get(position).getBackgroundState().equals("selected")) {
                        backgroundList.get(position).setBackgroundState("selected");
                        backgroundID = backgroundList.get(position).getBackgroundID();
                        mediaPlayer = new MediaPlayer();

                        try {
                            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                            mediaPlayer.setDataSource(backgroundList.get(position).getBackgroundURL());
                            mediaPlayer.setLooping(true);
                            mediaPlayer.prepareAsync();
                        } catch (IOException e) {
                            Toast.makeText(getApplicationContext(), "Fail to load sound", Toast.LENGTH_SHORT).show();
                        }

                        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                mp.start();
                            }
                        });

                        backgroundAdapter.notifyDataSetChanged();
                    } else {
                        backgroundList.get(position).setBackgroundState("");
                        backgroundAdapter.notifyDataSetChanged();
                        backgroundID = "0";
                    }
                }

            }
        });

        dialpadOne.setOnClickListener(this);
        dialpadTwo.setOnClickListener(this);
        dialpadThree.setOnClickListener(this);
        dialpadFour.setOnClickListener(this);
        dialpadFive.setOnClickListener(this);
        dialpadSix.setOnClickListener(this);
        dialpadSeven.setOnClickListener(this);
        dialpadEight.setOnClickListener(this);
        dialpadNine.setOnClickListener(this);
        dialpadContacts.setOnClickListener(this);
        dialpadDelete.setOnClickListener(this);
        dialpadZero.setOnClickListener(this);
        recordButton.setOnClickListener(this);
        vcHolder.setOnClickListener(this);
        bgHolder.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialPadOne:
                numberBox.append("1");
                break;
            case R.id.dialPadTwo:
                numberBox.append("2");
                break;
            case R.id.dialPadThree:
                numberBox.append("3");
                break;
            case R.id.dialPadFour:
                numberBox.append("4");
                break;
            case R.id.dialPadFive:
                numberBox.append("5");
                break;
            case R.id.dialPadSix:
                numberBox.append("6");
                break;
            case R.id.dialPadSeven:
                numberBox.append("7");
                break;
            case R.id.dialPadEight:
                numberBox.append("8");
                break;
            case R.id.dialPadNine:
                numberBox.append("9");
                break;
            case R.id.dialPadContact:
                startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), REQUEST_CODE_PICK_CONTACTS);
                break;
            case R.id.dialPadDelete:
                int length = numberBox.getText().length();
                if (length > 0) {
                    numberBox.getText().delete(length - 1, length);
                }
                break;
            case R.id.dialPadZero:
                numberBox.append("0");
                break;
            case R.id.recordHolder:
                if (isRecording) {
                    isRecording = false;
                    recordImage.setImageResource(R.drawable.record_off);
                } else {
                    isRecording = true;
                    recordImage.setImageResource(R.drawable.record_on);
                }
                break;
            case R.id.vcHolder:
                if (isChangingVoice) {
                    removeAllViews();
                    isChangingVoice = false;
                    vcIcon.setImageResource(R.drawable.vc_off);
                    showDialpad();
                } else {
                    removeAllViews();
                    isChangingVoice = true;
                    vcLayout.setVisibility(View.VISIBLE);
                    closeButton.setVisibility(View.VISIBLE);
                    vcIcon.setImageResource(R.drawable.vc_on);
                }
                changeTextColor();
                break;
            case R.id.bgHolder:
                if (isChangingBG) {
                    removeAllViews();
                    isChangingBG = false;
                    bgIcon.setImageResource(R.drawable.bg_off);
                    showDialpad();
                    if (mediaPlayer != null) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }
                } else {
                    removeAllViews();
                    isChangingBG = true;
                    bgGrid.setVisibility(View.VISIBLE);
                    closeButton.setVisibility(View.VISIBLE);
                    bgIcon.setImageResource(R.drawable.bg_on);
                }
                changeTextColor();
                break;


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_CONTACTS && resultCode == RESULT_OK) {
            uriContact = data.getData();
            contactNumber = retrieveContactNumber();
            numberBox.setText(contactNumber);
        }

        }

    private String retrieveContactNumber() {

        String contactNumber;

        // getting contacts ID
        Cursor cursorID = getContentResolver().query(uriContact,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);

        if (cursorID.moveToFirst()) {

            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }

        cursorID.close();

        Log.d(TAG, "Contact ID: " + contactID);

        // Using the contact ID now we will get contact phone number
        Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},

                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,

                new String[]{contactID},
                null);

        if (cursorPhone.moveToFirst()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            return contactNumber;

        } else {
            cursorPhone.close();
            return "";
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void removeAllViews() {
        dialpadHolder.setVisibility(View.GONE);
        makeCallButton.setVisibility(View.GONE);
        vcLayout.setVisibility(View.GONE);
        bgGrid.setVisibility(View.GONE);
        closeButton.setVisibility(View.GONE);
        bgIcon.setImageResource(R.drawable.bg_off);
        vcIcon.setImageResource(R.drawable.vc_off);
        isChangingBG = false;
        isChangingVoice = false;
    }

    private void showDialpad() {
        dialpadHolder.setVisibility(View.VISIBLE);
        makeCallButton.setVisibility(View.VISIBLE);
    }

    private void changeTextColor() {
        if (!backgroundID.equals("0")) {
            bgText.setTextColor(getResources().getColor(R.color.selectedyellow));
        } else {
            bgText.setTextColor(getResources().getColor(R.color.white));
        }

        voiceChangerNumber = Integer.toString(voiceChangeBar.getProgress());
        if (!voiceChangerNumber.equals("0")) {
            vcText.setTextColor(getResources().getColor(R.color.selectedyellow));
        } else {
            vcText.setTextColor(getResources().getColor(R.color.white));
        }
    }
}
