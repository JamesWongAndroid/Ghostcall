package com.kickbackapps.ghostcall;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.sip.SipAudioCall;
import android.net.sip.SipException;
import android.net.sip.SipManager;
import android.net.sip.SipProfile;
import android.net.sip.SipRegistrationListener;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import com.kickbackapps.ghostcall.BackgroundEffects.BackgroundObject;
import com.kickbackapps.ghostcall.SoundEffects.EffectsObject;
import com.kickbackapps.ghostcall.User.CallStatus;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.skyfishjy.library.RippleBackground;
import com.squareup.okhttp.OkHttpClient;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class CallScreen extends AppCompatActivity implements View.OnClickListener {

    LinearLayout dialpadOne, dialpadTwo, dialpadThree, dialpadFour, dialpadFive, dialpadSix, dialpadSeven, dialpadEight, dialpadNine,
    dialpadContacts, dialpadDelete, dialpadZero, recordButton;
    LinearLayout rowNumberOne, rowNumberTwo, rowNumberThree, rowNumberFour;
    LinearLayout vcHolder, vcLayout, bgHolder, effectsHolder;
    RelativeLayout dialpadHolder, spinnerLayout;
    DiscreteSeekBar voiceChangeBar;
    ImageView recordImage;
    GridView bgGrid, effectsGrid;
    BackgroundAdapter backgroundAdapter;
    EffectsAdapter effectAdapter;
    ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
    ScheduledFuture scheduledFuture;

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
    private String callMethod, methodCheck;
    String toCallNumber;
    String resourceID = "";
    String currentCallStatus = "";
    private static final String GHOST_PREF = "GhostPrefFile";
    private static final String TAG = CallScreen.class.getSimpleName();
    private boolean isRecording = false;
    private boolean isChangingVoice = false;
    private boolean isChangingBG = false;
    private boolean isViewingEffects = false;
    private ImageView vcIcon;
    private ImageView bgIcon;
    private ImageView effectsIcon;
    private ImageView purchaseButton;
    private ArrayList<BackgroundObject> backgroundList;
    private ArrayList<EffectsObject> effectsList;
    TextView numberName, bgText, vcText, effectsText;
    EditText numberBox;
    Bundle extras;
    private SharedPreferences settings;
    Button makeCallButton;
    Button closeButton;
    RippleBackground rippleBackground;
    MediaPlayer mediaPlayer;
    RequestInterceptor requestInterceptor;
    RestAdapter restAdapter;
    GhostCallAPIInterface service;
    CircleProgressBar progressSpinner;
    String sipUsername;
    String sipPassword;
    SipManager sipManager = null;
    SipProfile sipProfile = null;
    SipAudioCall.Listener listener;
    SipAudioCall call = null;
    OkHttpClient client;
    SipRegistrationListener registrationListener;
    SharedPreferences.Editor editor;
    int initiatedLimit = 0;

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
        editor = settings.edit();
        apiKey = settings.getString("api_key", "");
        sipUsername = settings.getString(Constants.SIP_NAME, "");
        sipPassword = settings.getString(Constants.SIP_PASSWORD, "");
        callMethod = settings.getString(Constants.CALL_METHOD, "");
        methodCheck = settings.getString(Constants.CALL_METHOD_CHECK, "");

        if (sipManager.isVoipSupported(CallScreen.this)) {
            new registerStartSIP().execute();
        }


        requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("X-api-key", apiKey);
            }
        };

        client = new OkHttpClient();

        restAdapter = new RestAdapter.Builder().setEndpoint("http://www.ghostcall.in/api")
                .setRequestInterceptor(requestInterceptor).setClient(new OkClient(client)).build();
        service = restAdapter.create(GhostCallAPIInterface.class);

        numberName = (TextView) findViewById(R.id.remainingText);
        numberBox  = (EditText) findViewById(R.id.callEditText);
        numberBox.setKeyListener(null);

        purchaseButton = (ImageView) findViewById(R.id.purchaseButton);
        purchaseButton.setVisibility(View.GONE);

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
        spinnerLayout = (RelativeLayout) findViewById(R.id.spinnerLayout);
        progressSpinner = (CircleProgressBar) findViewById(R.id.progressBar);
        progressSpinner.setColorSchemeResources(android.R.color.holo_blue_dark);

        numberBox.addTextChangedListener(new PhoneNumberTextWatcher(numberBox));

        recordImage = (ImageView) findViewById(R.id.recordImage);

        voiceChangeBar = (DiscreteSeekBar) findViewById(R.id.voiceSeekBar);

        closeButton = (Button) findViewById(R.id.closeVCButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentCallStatus.equals("connected")) {
                    spinnerLayout.setVisibility(View.VISIBLE);
                    service.hangUpCall(resourceID, new Callback<Response>() {
                        @Override
                        public void success(Response response, Response response2) {
                            if (call != null) {
                                try {
                                    call.endCall();
                                } catch (SipException e) {

                                }

                            }
                        }

                        @Override
                        public void failure(RetrofitError retrofitError) {

                        }
                    });
                } else {
                    if (call != null) {
                        try {
                            call.endCall();
                        } catch (SipException e) {

                        }

                    }
                    removeAllViews();
                    showDialpad();
                    if (mediaPlayer != null) {
                        mediaPlayer.stop();
                        mediaPlayer.release();
                        mediaPlayer = null;
                    }
                    changeTextColor();
                }

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
                        makeCallButton.setVisibility(View.GONE);
                        toNumber = phoneUtil.format(usaNumber, PhoneNumberUtil.PhoneNumberFormat.E164);


                        if (callMethod.equals("")) {
                            if (sipManager.isVoipSupported(getApplicationContext())) {
                                callMethod = "sip";
                            } else {
                                callMethod = "gateway";
                            }
                        } else {
                            callMethod = settings.getString(Constants.CALL_METHOD, "");
                        }


                        if (haveNetworkConnection()) {
                            service.makeCall(toNumber, numberID, backgroundID, voiceChangerNumber, Boolean.toString(isRecording), verified, callMethod, new Callback<CallData>() {
                                @Override
                                public void success(CallData callData, Response response) {
                                    toCallNumber = callData.getDial();
                                    resourceID = callData.getResourceId();
                                    String removePlus = toNumber.replace("+", "sip:");
                                    String sipCallAddress = removePlus + "@sip.ghostcall.in";

                                    if (callMethod.equals("sip")) {
                                        new registerSIP().execute();
                                    } else {
                                        resourceID = callData.getResourceId();
                                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                                        callIntent.setData(Uri.parse("tel:" + toCallNumber));
                                        startActivity(callIntent);
                                        Toast.makeText(getApplicationContext(), "Making Call", Toast.LENGTH_SHORT).show();
                                        Log.d("STARTED CALL", "started call");
                                        initiatedLimit = 0;
                                        scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
                                       scheduledFuture = scheduledThreadPoolExecutor.scheduleAtFixedRate(new CheckCallStatus(), 0, 5, TimeUnit.SECONDS);
                                    }
                                }

                                @Override
                                public void failure(RetrofitError retrofitError) {
                                    Toast.makeText(getApplicationContext(), "Error Calling", Toast.LENGTH_SHORT).show();
                                    rippleBackground.stopRippleAnimation();
                                    dialpadHolder.setVisibility(View.VISIBLE);
                                    rippleBackground.setVisibility(View.GONE);
                                    makeCallButton.setVisibility(View.VISIBLE);
                                }
                            });
                        } else {
                            InternetDialog.showInternetDialog(CallScreen.this);
                            rippleBackground.stopRippleAnimation();
                            dialpadHolder.setVisibility(View.VISIBLE);
                            rippleBackground.setVisibility(View.GONE);
                            makeCallButton.setVisibility(View.VISIBLE);
                        }


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

        effectsHolder = (LinearLayout) findViewById(R.id.effectsHolder);
        effectsIcon = (ImageView) findViewById(R.id.effectsImage);
        effectsText = (TextView) findViewById(R.id.effectsText);
        effectsGrid = (GridView) findViewById(R.id.effectsLayout);
        effectsGrid.setChoiceMode(GridView.CHOICE_MODE_SINGLE);

        GhostCallDatabaseAdapter databaseAdapter = new GhostCallDatabaseAdapter(CallScreen.this);

        try {
            databaseAdapter.open();
            backgroundList = databaseAdapter.getBackgroundObjects();
            backgroundAdapter = new BackgroundAdapter(this, backgroundList);
            bgGrid.setAdapter(backgroundAdapter);

            effectsList = databaseAdapter.getEffectsObjects();
            effectAdapter = new EffectsAdapter(this, effectsList);
            effectsGrid.setAdapter(effectAdapter);
            databaseAdapter.close();
        } catch (SQLException e) {

        }

        effectsGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final int positionInt = position;
                for (int i = 0; i < effectsList.size(); i++) {
                    if (i != position) {
                        effectsList.get(i).setEffectsState("");
                        ensureMediaPlayerDeath();
                    }
                }

                if (!effectsList.get(position).getEffectsState().equals("selected")) {
                    effectsList.get(position).setEffectsState("selected");
                    mediaPlayer = new MediaPlayer();

                    if (currentCallStatus.equals("connected")) {
                        service.sendEffects(effectsList.get(position).getEffectsID(), resourceID, new Callback<Response>() {
                            @Override
                            public void success(Response responseTwo, Response response) {
                                Log.d("EFFECTS RESPONSE", "i sent");
                            }

                            @Override
                            public void failure(RetrofitError retrofitError) {
                                Log.d("EFFECTS RESPONSE", "i failed " + retrofitError.getMessage());

                            }
                        });
                        Log.d("sending effects", "effects id = " + effectsList.get(position).getEffectsID() + " resourceID = " + resourceID);
                    }

                    try {
                        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mediaPlayer.setDataSource(effectsList.get(position).getEffectsURL());
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

                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            effectsList.get(positionInt).setEffectsState("");
                            ensureMediaPlayerDeath();
                            effectAdapter.notifyDataSetChanged();
                        }
                    });
                    effectAdapter.notifyDataSetChanged();
                } else {
                    effectsList.get(position).setEffectsState("");
                    effectAdapter.notifyDataSetChanged();
                }
            }
        });

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
        effectsHolder.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        service.getCallStatus(null, new Callback<CallStatus>() {
            @Override
            public void success(CallStatus callStatus, Response response) {
                if (haveNetworkConnection()) {
                    if (callStatus != null) {
                        String status = callStatus.getStatus();
                        if (status.equals("connected")) {
                            removeAllViews();
                            isViewingEffects = true;
                            effectsGrid.setVisibility(View.VISIBLE);
                            closeButton.setVisibility(View.VISIBLE);
                            effectsIcon.setImageResource(R.drawable.effects_on);
                            resourceID = callStatus.getResourceId();
                            toNumber = callStatus.getTo();
                            numberName.setText(toNumber);
                            initiatedLimit = 0;
                            scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
                            scheduledThreadPoolExecutor.scheduleAtFixedRate(new CheckCallStatus(), 0, 5, TimeUnit.SECONDS);
                        }
                    }
                }


            }

            @Override
            public void failure(RetrofitError retrofitError) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialPadOne:
                numberBox.append("1");
                ensureMediaPlayerDeath();
                mediaPlayer = mediaPlayer.create(this, R.raw.num_1);
                mediaPlayer.start();
                break;
            case R.id.dialPadTwo:
                numberBox.append("2");
                ensureMediaPlayerDeath();
                mediaPlayer = mediaPlayer.create(this, R.raw.num_2);
                mediaPlayer.start();
                break;
            case R.id.dialPadThree:
                numberBox.append("3");
                ensureMediaPlayerDeath();
                mediaPlayer = mediaPlayer.create(this, R.raw.num_3);
                mediaPlayer.start();
                break;
            case R.id.dialPadFour:
                numberBox.append("4");
                ensureMediaPlayerDeath();
                mediaPlayer = mediaPlayer.create(this, R.raw.num_4);
                mediaPlayer.start();
                break;
            case R.id.dialPadFive:
                numberBox.append("5");
                ensureMediaPlayerDeath();
                mediaPlayer = mediaPlayer.create(this, R.raw.num_5);
                mediaPlayer.start();
                break;
            case R.id.dialPadSix:
                numberBox.append("6");
                ensureMediaPlayerDeath();
                mediaPlayer = mediaPlayer.create(this, R.raw.num_6);
                mediaPlayer.start();
                break;
            case R.id.dialPadSeven:
                ensureMediaPlayerDeath();
                mediaPlayer = mediaPlayer.create(this, R.raw.num_7);
                numberBox.append("7");
                mediaPlayer.start();
                break;
            case R.id.dialPadEight:
                ensureMediaPlayerDeath();
                mediaPlayer = mediaPlayer.create(this, R.raw.num_8);
                numberBox.append("8");
                mediaPlayer.start();
                break;
            case R.id.dialPadNine:
                ensureMediaPlayerDeath();
                mediaPlayer = mediaPlayer.create(this, R.raw.num_9);
                numberBox.append("9");
                mediaPlayer.start();
                break;
            case R.id.dialPadContact:
                startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), REQUEST_CODE_PICK_CONTACTS);
                break;
            case R.id.dialPadDelete:
                ensureMediaPlayerDeath();
                mediaPlayer = mediaPlayer.create(this, R.raw.num_11);
                mediaPlayer.start();
                int length = numberBox.getText().length();
                if (length > 0) {
                    numberBox.getText().delete(length - 1, length);
                }
                break;
            case R.id.dialPadZero:
                ensureMediaPlayerDeath();
                mediaPlayer = mediaPlayer.create(this, R.raw.num_0);
                mediaPlayer.start();
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
                    ensureMediaPlayerDeath();
                } else {
                    removeAllViews();
                    isChangingBG = true;
                    bgGrid.setVisibility(View.VISIBLE);
                    closeButton.setVisibility(View.VISIBLE);
                    bgIcon.setImageResource(R.drawable.bg_on);
                }
                changeTextColor();
                break;
            case R.id.effectsHolder:
                if (isViewingEffects) {
                    removeAllViews();
                    isViewingEffects = false;
                    effectsIcon.setImageResource(R.drawable.effects_off);
                    showDialpad();
                    ensureMediaPlayerDeath();
                } else {
                    removeAllViews();
                    isViewingEffects = true;
                    effectsGrid.setVisibility(View.VISIBLE);
                    closeButton.setVisibility(View.VISIBLE);
                    effectsIcon.setImageResource(R.drawable.effects_on);
                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_CONTACTS && resultCode == RESULT_OK) {
            uriContact = data.getData();
            contactNumber = retrieveContactNumber();
            contactNumber = contactNumber.replaceAll("[^0-9]", "");
            if (contactNumber.startsWith("1")) {
                contactNumber = contactNumber.replaceFirst("1", "");
            }

            try {
                StringBuilder contactNumberBuilder = new StringBuilder(contactNumber);
                contactNumberBuilder.insert(3, '-');
                contactNumberBuilder.insert(7, '-');
                numberBox.setText(contactNumberBuilder.toString());
            } catch (Exception e) {
                numberBox.setText(contactNumber);
            }
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

    @Override
    protected void onDestroy() {
        closeLocalProfile();
        if (call != null) {
            call.close();
        }
        super.onDestroy();
    }

    @Override
    public void onPrepareSupportNavigateUpTaskStack(TaskStackBuilder builder) {
        super.onPrepareSupportNavigateUpTaskStack(builder);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_call_screen, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        if (methodCheck.equals("")) {
                if (sipManager.isVoipSupported(CallScreen.this)) {
                    menu.getItem(0).setChecked(true);
                    editor.putString(Constants.CALL_METHOD, "sip");
                    editor.apply();
                } else {
                    menu.getItem(1).setChecked(true);
                    editor.putString(Constants.CALL_METHOD, "gateway");
                    editor.apply();
                }
        } else {
            if (methodCheck.equals("sipCheck")) {
                menu.getItem(0).setChecked(true);
            } else {
                menu.getItem(1).setChecked(true);
            }
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sip_menu_item:
                   if (sipManager.isVoipSupported(CallScreen.this)) {
                       if (!item.isChecked()) {
                           item.setChecked(true);
                           editor.putString(Constants.CALL_METHOD, "sip");
                           editor.putString(Constants.CALL_METHOD_CHECK, "sipCheck");
                           methodCheck = "sipCheck";
                           callMethod = "sip";
                           editor.apply();
                       }
                   } else {
                       Toast.makeText(CallScreen.this, "Sorry, your phone does not support SIP calling.", Toast.LENGTH_SHORT).show();
                           item.setChecked(false);
                   }
                break;
            case R.id.call_menu_item:
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                if (telephonyManager.getSimState() == TelephonyManager.SIM_STATE_ABSENT) {
                    Toast.makeText(CallScreen.this, "No sim card detected", Toast.LENGTH_SHORT).show();
                        item.setChecked(false);
                } else {
                    if (!item.isChecked()) {
                        item.setChecked(true);
                        editor.putString(Constants.CALL_METHOD, "gateway");
                        editor.putString(Constants.CALL_METHOD_CHECK, "simCheck");
                        methodCheck = "simCheck";
                        callMethod = "gateway";
                        editor.apply();
                    }
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void closeLocalProfile() {
        if (sipManager == null) {
            return;
        }

        try {
            if (sipManager != null) {
                sipManager.close(sipProfile.getUriString());
                Log.d("sipmananger close", "got closed");
            }
        } catch (Exception e) {

        }
    }

    private void removeAllViews() {
        dialpadHolder.setVisibility(View.GONE);
        makeCallButton.setVisibility(View.GONE);
        vcLayout.setVisibility(View.GONE);
        bgGrid.setVisibility(View.GONE);
        effectsGrid.setVisibility(View.GONE);
        closeButton.setVisibility(View.GONE);
        bgIcon.setImageResource(R.drawable.bg_off);
        vcIcon.setImageResource(R.drawable.vc_off);
        effectsIcon.setImageResource(R.drawable.effects_off);
        spinnerLayout.setVisibility(View.GONE);
        rippleBackground.setVisibility(View.GONE);
        isChangingBG = false;
        isChangingVoice = false;
        isViewingEffects = false;
    }

    private void showDialpad() {
        dialpadHolder.setVisibility(View.VISIBLE);
        makeCallButton.setVisibility(View.VISIBLE);
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
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

    private void ensureMediaPlayerDeath() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void updateUI() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                removeAllViews();
                showDialpad();
                rippleBackground.stopRippleAnimation();
                rippleBackground.setVisibility(View.GONE);
            }
        });
    }

    private void updateErrorUI(final String error) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                removeAllViews();
                showDialpad();
                rippleBackground.stopRippleAnimation();
                rippleBackground.setVisibility(View.GONE);
                Toast.makeText(CallScreen.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateCallConnectedUI() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                closeButton.setVisibility(View.VISIBLE);
                closeButton.setText("Hang Up");
                effectsGrid.setVisibility(View.VISIBLE);
                rippleBackground.stopRippleAnimation();
                rippleBackground.setVisibility(View.GONE);

            }
        });
    }

    public class registerSIP extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            if (sipManager == null) {
                sipManager = sipManager.newInstance(getApplicationContext());
            }

            try {
                SipProfile.Builder builder = new SipProfile.Builder(sipUsername, "sip.ghostcall.in");
                builder.setPassword(sipPassword);
                builder.setAutoRegistration(false);
                builder.setSendKeepAlive(true);

                sipProfile = builder.build();

                Intent intent = new Intent();
                intent.setAction("android.GhostCall.INCOMING_CALL");
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, Intent.FILL_IN_ACTION);

                registrationListener = new SipRegistrationListener() {
                    @Override
                    public void onRegistering(String localProfileUri) {
                        Log.d("Registering", "doing work");
                    }

                    @Override
                    public void onRegistrationDone(String localProfileUri, long expiryTime) {
                        Log.d("Sip reg done", "Sip done");
                      try {
                        listener = new SipAudioCall.Listener() {
                            @Override
                            public void onCalling(SipAudioCall call) {
                                super.onCalling(call);
                            }

                            @Override
                            public void onRinging(SipAudioCall call, SipProfile caller) {
                                super.onRinging(call, caller);
                            }

                            @Override
                            public void onRingingBack(SipAudioCall call) {
                                super.onRingingBack(call);
                                ensureMediaPlayerDeath();
                                mediaPlayer = MediaPlayer.create(CallScreen.this, R.raw.ring);
                                mediaPlayer.setLooping(true);
                                mediaPlayer.start();
                                scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
                                scheduledFuture = scheduledThreadPoolExecutor.scheduleAtFixedRate(new CheckCallStatus(), 0, 5, TimeUnit.SECONDS);

                            }

                            @Override
                            public void onCallEstablished(SipAudioCall call) {
                                ensureMediaPlayerDeath();
                                call.startAudio();
                                call.setSpeakerMode(false);
                                if (call.isMuted()) {
                                    call.toggleMute();
                                }

                                Log.d("Sip call establish", "call started");
                            }

                            @Override
                            public void onCallEnded(SipAudioCall call) {
                                Log.d("Sip call done", "call ended");
                                updateUI();
                                call.close();

                            }

                            @Override
                            public void onCallBusy(SipAudioCall call) {
                                super.onCallBusy(call);
                                Log.d("Sip call busy", "call ended");
                                updateUI();
                            }

                            @Override
                            public void onCallHeld(SipAudioCall call) {
                                super.onCallHeld(call);
                                updateUI();
                            }

                            @Override
                            public void onError(SipAudioCall call, int errorCode, String errorMessage) {
                                super.onError(call, errorCode, errorMessage);
                                Log.d("Sip call error", errorMessage.toString());
                           //     updateErrorUI(errorMessage.toString());
                            }
                        };

                        call = sipManager.makeAudioCall(sipProfile.getUriString(), toCallNumber, listener, 30);

                    } catch (SipException e) {
                        Log.d("Sip error call", e.getMessage());
                        Toast.makeText(CallScreen.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }

                    }

                    @Override
                    public void onRegistrationFailed(String localProfileUri, int errorCode, String errorMessage) {
                        Log.d("Sip error message", errorMessage.toString());

                    }
                };

        //        sipManager.open(sipProfile, pendingIntent, null);
                sipManager.register(sipProfile, 20, registrationListener);
                sipManager.setRegistrationListener(sipProfile.getUriString(), registrationListener);

                Log.d("is voip supported", Boolean.toString(sipManager.isVoipSupported(getApplicationContext())));

            } catch (Exception e) {
                Log.d("Sip error", e.getMessage());
            }
            return null;
        }
    }

    public class registerStartSIP extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            if (sipManager == null) {
                sipManager = sipManager.newInstance(getApplicationContext());
            }

            try {
                SipProfile.Builder builder = new SipProfile.Builder(sipUsername, "sip.ghostcall.in");
                builder.setPassword(sipPassword);
                builder.setAutoRegistration(false);
                builder.setSendKeepAlive(true);
                sipProfile = builder.build();

                Intent intent = new Intent();
                intent.setAction("android.GhostCall.INCOMING_CALL");
                PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, Intent.FILL_IN_ACTION);

                registrationListener = new SipRegistrationListener() {
                    @Override
                    public void onRegistering(String localProfileUri) {
                        Log.d("Registering", "doing work");
                    }

                    @Override
                    public void onRegistrationDone(String localProfileUri, long expiryTime) {
                        Log.d("Sip reg done", "Sip done");
                    }

                    @Override
                    public void onRegistrationFailed(String localProfileUri, int errorCode, String errorMessage) {
                        Log.d("Sip error message", errorMessage.toString());

                    }
                };

                     sipManager.open(sipProfile, pendingIntent, null);
                sipManager.register(sipProfile, 20, registrationListener);
                sipManager.setRegistrationListener(sipProfile.getUriString(), new SipRegistrationListener() {
                    @Override
                    public void onRegistering(String localProfileUri) {
                        Log.d("Registering", "doing work");
                    }

                    @Override
                    public void onRegistrationDone(String localProfileUri, long expiryTime) {
                        Log.d("Sip reg done", "Sip done");
                    }

                    @Override
                    public void onRegistrationFailed(String localProfileUri, int errorCode, String errorMessage) {
                        Log.d("Sip error message", errorMessage.toString());
                    }
                });

                Log.d("is voip supported", Boolean.toString(sipManager.isVoipSupported(getApplicationContext())));

            } catch (Exception e) {
                Log.d("Sip error", e.getMessage());
            }
            return null;
        }
    }

    public class CheckCallStatus implements Runnable {

        @Override
        public void run() {

            service.getCallStatus(resourceID, new Callback<CallStatus>() {
                @Override
                public void success(CallStatus callStatus, Response response) {
                    Log.d("GHOSTCALL CALL STATUS", callStatus.getStatus());
                    currentCallStatus = callStatus.getStatus();
                    bgHolder.setClickable(false);
                    vcHolder.setClickable(false);
                    effectsHolder.setClickable(false);
                    recordButton.setClickable(false);

                    if (currentCallStatus.equals("initiated")) {
                        initiatedLimit++;
                        Log.d("initiated limit", Integer.toString(initiatedLimit));
                        if (initiatedLimit > 5) {
                            scheduledFuture.cancel(true);
                            scheduledThreadPoolExecutor.shutdownNow();
                            removeAllViews();
                            closeButton.setText("Close");
                            showDialpad();
                            bgHolder.setClickable(true);
                            vcHolder.setClickable(true);
                            effectsHolder.setClickable(true);
                            recordButton.setClickable(true);
                            Toast.makeText(CallScreen.this, "Call took too long to connect", Toast.LENGTH_SHORT).show();
                            Log.d("SCHEDULED GOT SHUT DOWN", "SHUT DOWN");
                        }
                    }


                    if (currentCallStatus.equals("connected")) {
                        rippleBackground.stopRippleAnimation();
                        rippleBackground.setVisibility(View.GONE);
                        removeAllViews();
                        closeButton.setVisibility(View.VISIBLE);
                        closeButton.setText("Hang Up");
                        effectsGrid.setVisibility(View.VISIBLE);
                    }

                    if (currentCallStatus.equals("connecting")) {
                       makeCallButton.setVisibility(View.GONE);
                        bgHolder.setClickable(false);
                        vcHolder.setClickable(false);
                        effectsHolder.setClickable(false);
                        recordButton.setClickable(false);
                    }

                    if (currentCallStatus.equals("hangup")) {
                        scheduledFuture.cancel(true);
                        scheduledThreadPoolExecutor.shutdownNow();

                        removeAllViews();
                        closeButton.setText("Close");
                        showDialpad();
                        bgHolder.setClickable(true);
                        vcHolder.setClickable(true);
                        effectsHolder.setClickable(true);
                        recordButton.setClickable(true);
                        Log.d("SCHEDULED GOT SHUT DOWN", "SHUT DOWN");

                        if (extras != null) {
                            if (extras.getString("callName") != null) {
                                numberName.setText(extras.getString("callName"));
                            }
                        }
                    }

                }

                @Override
                public void failure(RetrofitError retrofitError) {
                    rippleBackground.stopRippleAnimation();
                    rippleBackground.setVisibility(View.GONE);
                    removeAllViews();
                    showDialpad();
                    bgHolder.setClickable(true);
                    vcHolder.setClickable(true);
                }
            });
        }
    }
}
