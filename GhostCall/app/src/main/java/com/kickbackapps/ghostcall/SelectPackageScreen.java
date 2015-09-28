package com.kickbackapps.ghostcall;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kickbackapps.ghostcall.Numbers.ExtendObject;
import com.kickbackapps.ghostcall.util.IabHelper;
import com.kickbackapps.ghostcall.util.IabResult;
import com.kickbackapps.ghostcall.util.Inventory;
import com.kickbackapps.ghostcall.util.Purchase;

import java.sql.SQLException;
import java.util.List;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SelectPackageScreen extends AppCompatActivity {

    ListView listView;
    PackageAdapter packageAdapter;
    TextView userRemainingText, selectLabel;
    GhostCallDatabaseAdapter nDatabaseAdapter;
    Bundle extras;
    String packagesType, nickName, areaCode, ghostID, skuID;
    ImageView purchaseButton;
    IabHelper mHelper;
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener;
    IabHelper.QueryInventoryFinishedListener mReceivedInventoryListener;
    private SharedPreferences settings;
    private String apiKey, productID;
    RequestInterceptor requestInterceptor;
    RestAdapter restAdapter;
    GhostCallAPIInterface service;
    List<GhostPackage> ghostPackageList;
    GetUserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_package_screen);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1d375a")));
        actionBar.setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.home_actionbar_layout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.titleblue));
        }

        userInfo = new GetUserInfo(this);
        userInfo.getUserData();

        settings = getSharedPreferences(Constants.GHOST_PREF, 0);
        apiKey = settings.getString("api_key", "");

        requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("X-api-key", apiKey);
            }
        };

        restAdapter = new RestAdapter.Builder().setEndpoint("http://www.ghostcall.in/api")
                .setRequestInterceptor(requestInterceptor).build();
        service = restAdapter.create(GhostCallAPIInterface.class);

        mHelper = new IabHelper(this, Constants.GOOGLE_ACCOUNT_BASE64);
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            @Override
            public void onIabSetupFinished(IabResult result) {
                if (result.isSuccess()) {
                    Log.d("IapHelper result",  "isSuccess");
                } else {
                    Log.d("IapHelper result",  "isFailure");
                }
            }
        });

        mReceivedInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
            @Override
            public void onQueryInventoryFinished(IabResult result, Inventory inv) {
                if (result.isFailure()) {
                    Log.d("query inventory",  "isFailure");
                } else {
                    mHelper.consumeAsync(inv.getPurchase(skuID), mConsumeFinishedListener);
                }
            }
        };

        mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
            @Override
            public void onConsumeFinished(Purchase purchase, IabResult result) {
                if (result.isSuccess()) {

                } else {
                    Toast.makeText(getApplicationContext(), result.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        };

        final IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
            @Override
            public void onIabPurchaseFinished(IabResult result, final Purchase info) {
                if (result.isFailure()) {
                    Log.d("IapHelper result",  "isFailure Listener");
                } else if (info.getSku().equals(skuID)) {
                    consumeItem();
                    if (packagesType.equals("credits")) {
                        service.purchaseCredits(packagesType, productID, info.getToken(), info.getOrderId(), new Callback<Response>() {
                            @Override
                            public void success(Response response, Response response2) {

                                Toast.makeText(getApplicationContext(), "Purchase complete!", Toast.LENGTH_SHORT).show();
                                userInfo.getUserData();
                            }

                            @Override
                            public void failure(final RetrofitError retrofitError) {
                                final AlertDialog.Builder alertBox = new AlertDialog.Builder(SelectPackageScreen.this);
                                alertBox.setTitle("Purchase error");
                                alertBox.setMessage("An error occurred during your purchase. Please contact support to get it resolved. We apologize for the inconvenience");
                                alertBox.setPositiveButton("Contact Us", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent sendEmail = new Intent(Intent.ACTION_SENDTO);
                                        String uriText = "mailto:" + Uri.encode("support@prankdial.com") + "?subject=" + Uri.encode("GhostCall - Credits Purchase Error") +
                                                "&body=" + Uri.encode(retrofitError.getMessage() + " \n \n Token ID: " + info.getToken() + "\n \n orderID: " + info.getOrderId());
                                        Uri uri = Uri.parse(uriText);
                                        sendEmail.setData(uri);
                                        startActivity(Intent.createChooser(sendEmail, "Send mail..."));
                                    }
                                });
                                alertBox.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                alertBox.show();
                            }
                        });
                    } else if (packagesType.equals("new")) {
                        service.purchaseNewNumber("number", productID, nickName, areaCode, info.getToken(), info.getOrderId(), new Callback<Response>() {
                            @Override
                            public void success(Response response, Response response2) {
                                Toast.makeText(getApplicationContext(), "Purchase complete!", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void failure(RetrofitError retrofitError) {
                                Log.d("purchasenewnumber error", retrofitError.getMessage());
                                final AlertDialog.Builder alertBox = new AlertDialog.Builder(SelectPackageScreen.this);
                                alertBox.setTitle("Purchase error");
                                alertBox.setMessage("An error occurred during your purchase. Please contact support to get it resolved. We apologize for the inconvenience");
                                alertBox.setPositiveButton("Contact Us", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent sendEmail = new Intent(Intent.ACTION_SENDTO);
                                        String uriText = "mailto:" + Uri.encode("support@prankdial.com") + "?subject=" + Uri.encode("GhostCall - New Number Purchase Error") +
                                                "&body=" + Uri.encode( "Token ID: " + info.getToken() + "orderID: " + info.getOrderId());
                                        Uri uri = Uri.parse(uriText);
                                        sendEmail.setData(uri);
                                        startActivity(Intent.createChooser(sendEmail, "Send mail..."));
                                    }
                                });
                                alertBox.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                alertBox.show();
                            }
                        });
                    } else if (packagesType.equals("extend")) {
                        service.extendNumber(packagesType, productID, ghostID, info.getToken(), info.getOrderId(), new Callback<ExtendObject>() {
                            @Override
                            public void success(ExtendObject extendObject, Response response) {
                                if (extendObject != null) {
                                    try {
                                        nDatabaseAdapter.open();
                                        nDatabaseAdapter.updateNumberTimestamp(extendObject.getExpireOn(), ghostID);
                                        nDatabaseAdapter.close();
                                    } catch (SQLException e) {
                                        Log.d("ExtendNumber error", e.getMessage());
                                    }


                                }
                            }

                            @Override
                            public void failure(RetrofitError retrofitError) {
                                Log.d("ExtendNumber error", retrofitError.getMessage());
                                final AlertDialog.Builder alertBox = new AlertDialog.Builder(SelectPackageScreen.this);
                                alertBox.setTitle("Purchase error");
                                alertBox.setMessage("An error occurred during your purchase. Please contact support to get it resolved. We apologize for the inconvenience");
                                alertBox.setPositiveButton("Contact Us", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent sendEmail = new Intent(Intent.ACTION_SENDTO);
                                        String uriText = "mailto:" + Uri.encode("support@prankdial.com") + "?subject=" + Uri.encode("GhostCall - Extend Number Purchase Error") +
                                                "&body=" + Uri.encode("Token ID: " + info.getToken() + "orderID: " + info.getOrderId());
                                        Uri uri = Uri.parse(uriText);
                                        sendEmail.setData(uri);
                                        startActivity(Intent.createChooser(sendEmail, "Send mail..."));
                                    }
                                });
                                alertBox.setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                alertBox.show();
                            }
                        });
                    }
                }
            }
        };

        purchaseButton = (ImageView) findViewById(R.id.purchaseButton);
        purchaseButton.setVisibility(View.GONE);

        userRemainingText = (TextView) findViewById(R.id.remainingText);
        SharedPreferences settings = getSharedPreferences(Constants.GHOST_PREF, 0);
        String userSMS = settings.getString("userSMS", "0");
        String userMins = settings.getString("userMins", "0");
        userRemainingText.setText(userSMS + " sms / " + userMins + " mins left");

        selectLabel = (TextView) findViewById(R.id.selectLabel);

        listView = (ListView) findViewById(R.id.packageListView);
        nDatabaseAdapter = new GhostCallDatabaseAdapter(SelectPackageScreen.this);
        extras = getIntent().getExtras();
        if (extras != null) {
            packagesType = extras.getString(Constants.PACKAGE_TYPE);
            if (packagesType.equals("new") || packagesType.equals("extend")) {
                if (packagesType.equals("new")) {
                    areaCode = extras.getString("areacode");
                    nickName = extras.getString("nickName");
                    selectLabel.setText("Select a number package");
                } else {
                    ghostID = extras.getString("GhostID");
                    selectLabel.setText("Select an extend package");
                }
                try {
                    nDatabaseAdapter.open();
                    ghostPackageList = nDatabaseAdapter.getPackages(packagesType);
                    nDatabaseAdapter.close();
                    packageAdapter = new PackageAdapter(this, ghostPackageList);
                    listView.setAdapter(packageAdapter);
                } catch (Exception e) {
                    Log.d("Select Package - Error", e.getMessage());
                    nDatabaseAdapter.close();
                }
            } else if (packagesType.equals("credits")) {
                selectLabel.setText("Select a credits package");
                try {
                    nDatabaseAdapter.open();
                    ghostPackageList = nDatabaseAdapter.getCreditPackages();
                    nDatabaseAdapter.close();
                    packageAdapter = new PackageAdapter(this, ghostPackageList);
                    listView.setAdapter(packageAdapter);

                } catch (Exception e) {
                    Log.d("Select Package - Error", e.getMessage());
                }
            }
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                productID = ghostPackageList.get(position).getPackageID();
                skuID = ghostPackageList.get(position).getPackageAndroidID();
       //        skuID = "android.test.purchased";
                mHelper.launchPurchaseFlow(SelectPackageScreen.this, skuID, 10001, mPurchaseFinishedListener, "mytestpurchase");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHelper != null) {
            mHelper.dispose();
            mHelper = null;
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void consumeItem() {
        mHelper.queryInventoryAsync(mReceivedInventoryListener);
    }
}
