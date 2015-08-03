package com.tapfury.ghostcall;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;

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
    LinearLayout vcHolder, vcLayout;
    RelativeLayout dialpadHolder;
    DiscreteSeekBar voiceChangeBar;
    ImageView recordImage;

    private static final int REQUEST_CODE_PICK_CONTACTS = 1;
    private Uri uriContact;
    private String contactID;
    private String contactNumber;
    private String apiKey;
    private String toNumber;
    private String numberID;
    private String verified;
    private String voiceChangerNumber;
    private static final String GHOST_PREF = "GhostPrefFile";
    private static final String TAG = CallScreen.class.getSimpleName();
    private boolean isRecording = false;
    private boolean isChangingVoice = false;
    private ImageView vcIcon;
    TextView numberName;
    EditText numberBox;
    Bundle extras;
    private SharedPreferences settings;
    Button makeCallButton;
    Button closeButton;

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
                isChangingVoice = false;
                vcIcon.setImageResource(R.drawable.vc_off);
                dialpadHolder.setVisibility(View.VISIBLE);
                makeCallButton.setVisibility(View.VISIBLE);
                vcLayout.setVisibility(View.GONE);
                closeButton.setVisibility(View.GONE);
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

                        service.makeCall(toNumber, numberID, voiceChangerNumber, verified, new Callback<CallData>() {
                            @Override
                            public void success(CallData callData, Response response) {
                                String toCallNumber = callData.getDial();
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:" + toCallNumber));
                                startActivity(callIntent);
                                Toast.makeText(getApplicationContext(), "Making Call", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void failure(RetrofitError retrofitError) {
                                Toast.makeText(getApplicationContext(), "Error Calling", Toast.LENGTH_SHORT).show();
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
                    isChangingVoice = false;
                    vcIcon.setImageResource(R.drawable.vc_off);
                    dialpadHolder.setVisibility(View.VISIBLE);
                    makeCallButton.setVisibility(View.VISIBLE);
                    vcLayout.setVisibility(View.GONE);
                    closeButton.setVisibility(View.GONE);
                } else {
                    isChangingVoice = true;
                    dialpadHolder.setVisibility(View.GONE);
                    vcLayout.setVisibility(View.VISIBLE);
                    closeButton.setVisibility(View.VISIBLE);
                    makeCallButton.setVisibility(View.GONE);
                    vcIcon.setImageResource(R.drawable.vc_on);
                }
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
}
