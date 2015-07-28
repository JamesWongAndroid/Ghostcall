package com.tapfury.ghostcall;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Ynott on 7/20/15.
 */
public class GhostCallDatabaseAdapter {

    private SQLiteDatabase database;
    private MySQLiteGhostCallHelper dbHelper;

    private String[] allColumns = { MySQLiteGhostCallHelper.PRIMARY_ID, MySQLiteGhostCallHelper.ID, MySQLiteGhostCallHelper.NUMBER, MySQLiteGhostCallHelper.NAME,
            MySQLiteGhostCallHelper.VOICEMAIL, MySQLiteGhostCallHelper.DISABLE_CALLS,
            MySQLiteGhostCallHelper.DISABLE_MESSAGES, MySQLiteGhostCallHelper.EXPIRE_ON
    };

    public GhostCallDatabaseAdapter(Context context) {
        dbHelper = new MySQLiteGhostCallHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void createNumber(String id, String number, String name, String voicemail,
                             String disable_calls, String disable_messages, String expire_on) {

        ContentValues values = new ContentValues();
        values.put(MySQLiteGhostCallHelper.ID, id);
        values.put(MySQLiteGhostCallHelper.NUMBER, number);
        values.put(MySQLiteGhostCallHelper.NAME, name);
        values.put(MySQLiteGhostCallHelper.VOICEMAIL, voicemail);
        values.put(MySQLiteGhostCallHelper.DISABLE_CALLS, disable_calls);
        values.put(MySQLiteGhostCallHelper.DISABLE_MESSAGES, disable_messages);
        values.put(MySQLiteGhostCallHelper.EXPIRE_ON, expire_on);

        long insertID = database.insert(MySQLiteGhostCallHelper.TABLE_NUMBERS, null, values);
        Cursor cursor = database.query(MySQLiteGhostCallHelper.TABLE_NUMBERS, allColumns, MySQLiteGhostCallHelper.PRIMARY_ID + "= " + insertID, null, null, null, null);
        cursor.moveToFirst();
        cursor.close();
    }

    public void createCall(String id, String userID, String numberID, String callTo, String callFrom, String direction, String status, String pitch, String backgroundItem, String duration, String resourceID, String record, String createAt, String updateAt) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteGhostCallHelper.CALLS_ID, id);
        values.put(MySQLiteGhostCallHelper.CALLS_USER_ID, userID);
        values.put(MySQLiteGhostCallHelper.CALLS_NUMBER_ID, numberID);
        values.put(MySQLiteGhostCallHelper.CALLS_TO, callTo);
        values.put(MySQLiteGhostCallHelper.CALLS_FROM, callFrom);
        values.put(MySQLiteGhostCallHelper.CALLS_DIRECTION, direction);
        values.put(MySQLiteGhostCallHelper.CALLS_STATUS, status);
        values.put(MySQLiteGhostCallHelper.CALLS_PITCH, pitch);
        values.put(MySQLiteGhostCallHelper.CALLS_BACKGROUND_ITEM_ID, backgroundItem);
        values.put(MySQLiteGhostCallHelper.CALLS_DURATION, duration);
        values.put(MySQLiteGhostCallHelper.CALLS_RESOURCE_ID, resourceID);
        values.put(MySQLiteGhostCallHelper.CALLS_RECORD, record);
        values.put(MySQLiteGhostCallHelper.CALLS_CREATED_AT, createAt);
        values.put(MySQLiteGhostCallHelper.CALLS_UPDATED_AT, updateAt);

        database.insert(MySQLiteGhostCallHelper.TABLE_CALLS, null, values);
    }

    public void createMessage(String id, String userID, String numberID, String to, String from, String direction, String status, String resourceID, String text, String createdAt, String updatedAt, String deleted) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteGhostCallHelper.MESSAGES_ID, id);
        values.put(MySQLiteGhostCallHelper.MESSAGES_USER_ID, userID);
        values.put(MySQLiteGhostCallHelper.MESSAGES_NUMBER_ID, numberID);
        values.put(MySQLiteGhostCallHelper.MESSAGES_TO, to);
        values.put(MySQLiteGhostCallHelper.MESSAGES_FROM, from);
        values.put(MySQLiteGhostCallHelper.MESSAGES_DIRECTION, direction);
        values.put(MySQLiteGhostCallHelper.MESSAGES_STATUS, status);
        values.put(MySQLiteGhostCallHelper.MESSAGES_RESOURCE_ID, resourceID);
        values.put(MySQLiteGhostCallHelper.MESSAGES_TEXT, text);
        values.put(MySQLiteGhostCallHelper.MESSAGES_CREATED_AT, createdAt);
        values.put(MySQLiteGhostCallHelper.MESSAGES_UPDATED_AT, updatedAt);
        values.put(MySQLiteGhostCallHelper.MESSAGES_DELETED, deleted);

        database.insert(MySQLiteGhostCallHelper.TABLE_MESSAGES, null, values);
    }

    public void createVoicemail(String id, String userID, String numberID, String callID, String to, String from, String duration, String resource_id, String text, String created_at, String updated_at) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteGhostCallHelper.VOICEMAILS_ID, id);
        values.put(MySQLiteGhostCallHelper.VOICEMAILS_USER_ID, userID);
        values.put(MySQLiteGhostCallHelper.VOICEMAILS_NUMBER_ID, numberID);
        values.put(MySQLiteGhostCallHelper.VOICEMAILS_CALL_ID, callID);
        values.put(MySQLiteGhostCallHelper.VOICEMAILS_TO, to);
        values.put(MySQLiteGhostCallHelper.VOICEMAILS_FROM, from);
        values.put(MySQLiteGhostCallHelper.VOICEMAILS_DURATION, duration);
        values.put(MySQLiteGhostCallHelper.VOICEMAILS_RESOURCE_ID, resource_id);
        values.put(MySQLiteGhostCallHelper.VOICEMAILS_TEXT, text);
        values.put(MySQLiteGhostCallHelper.VOICEMAILS_CREATED_AT, created_at);
        values.put(MySQLiteGhostCallHelper.VOICEMAILS_UPDATED_AT, updated_at);

        database.insert(MySQLiteGhostCallHelper.TABLE_VOICEMAILS, null, values);
    }

    public void createUser(String id, String phoneNumber, String deviceToken, String appVersion, String platform, String platformVersion, String apiKeyID, String name, String email, String credits, String created_at, String updated_at, String deleted, String bundleSms, String bundleMinutes, String bundleCredits) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteGhostCallHelper.USER_ID, id);
        values.put(MySQLiteGhostCallHelper.USER_PHONE_NUMBER, phoneNumber);
        values.put(MySQLiteGhostCallHelper.USER_DEVICE_TOKEN, deviceToken);
        values.put(MySQLiteGhostCallHelper.USER_APP_VERSION, appVersion);
        values.put(MySQLiteGhostCallHelper.USER_PLATFORM, platform);
        values.put(MySQLiteGhostCallHelper.USER_PLATFORM_VERSION, platformVersion);
        values.put(MySQLiteGhostCallHelper.USER_API_KEY_ID, apiKeyID);
        values.put(MySQLiteGhostCallHelper.USER_NAME, name);
        values.put(MySQLiteGhostCallHelper.USER_EMAIL, email);
        values.put(MySQLiteGhostCallHelper.USER_CREDITS, credits);
        values.put(MySQLiteGhostCallHelper.USER_CREATED_AT, created_at);
        values.put(MySQLiteGhostCallHelper.USER_UPDATED_AT, updated_at);
        values.put(MySQLiteGhostCallHelper.USER_DELETED, deleted);
        values.put(MySQLiteGhostCallHelper.USER_BALANCE_SMS, bundleSms);
        values.put(MySQLiteGhostCallHelper.USER_BALANCE_MINUTES, bundleMinutes);
        values.put(MySQLiteGhostCallHelper.USER_BALANCE_CREDITS, bundleCredits);

        database.insert(MySQLiteGhostCallHelper.TABLE_USER, null, values);
    }

    public void createCreditsNumber(String id, String name, String description, String cost, String credits, String iosProductID, String androidProductID, String createdAt, String updatedAt, String deleted) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteGhostCallHelper.CREDITS_ID, id);
        values.put(MySQLiteGhostCallHelper.CREDITS_NAME, name);
        values.put(MySQLiteGhostCallHelper.CREDITS_DESCRIPTION, description);
        values.put(MySQLiteGhostCallHelper.CREDITS_COST, cost);
        values.put(MySQLiteGhostCallHelper.CREDITS_CREDITS, credits);
        values.put(MySQLiteGhostCallHelper.CREDITS_IOS_PRODUCT_ID, iosProductID);
        values.put(MySQLiteGhostCallHelper.CREDITS_ANDROID_PRODUCT_ID, androidProductID);
        values.put(MySQLiteGhostCallHelper.CREDITS_CREATED_AT, createdAt);
        values.put(MySQLiteGhostCallHelper.CREDITS_UPDATED_AT, updatedAt);
        values.put(MySQLiteGhostCallHelper.CREDITS_DELETED, deleted);

        database.insert(MySQLiteGhostCallHelper.TABLE_CREDIT_PACKAGES, null, values);
    }

    public void createNumbersPackage(String id, String name, String type, String credits, String cost, String iosProductID, String androidProductID, String expiration, String createdOn) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteGhostCallHelper.NUMBERS_ID, id);
        values.put(MySQLiteGhostCallHelper.NUMBERS_NAME, name);
        values.put(MySQLiteGhostCallHelper.NUMBERS_TYPE, type);
        values.put(MySQLiteGhostCallHelper.NUMBERS_CREDITS, credits);
        values.put(MySQLiteGhostCallHelper.NUMBERS_COST, cost);
        values.put(MySQLiteGhostCallHelper.NUMBERS_IOS_PRODUCT_ID, iosProductID);
        values.put(MySQLiteGhostCallHelper.NUMBERS_ANDROID_PRODUCT_ID, androidProductID);
        values.put(MySQLiteGhostCallHelper.NUMBERS_EXPIRATION, expiration);
        values.put(MySQLiteGhostCallHelper.NUMBERS_CREATED_ON, createdOn);

        database.insert(MySQLiteGhostCallHelper.TABLE_NUMBER_PACKAGES, null, values);
    }

    public void createSoundEffect(String id, String effectID, String name, String audioName, String volume, String imageActive, String imageOn, String imageOff, String createdAt, String updatedAt, String audioURL) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteGhostCallHelper.EFFECTS_ID, id);
        values.put(MySQLiteGhostCallHelper.EFFECTS_EFFECT_ID, effectID);
        values.put(MySQLiteGhostCallHelper.EFFECTS_NAME, name);
        values.put(MySQLiteGhostCallHelper.EFFECTS_AUDIO_NAME, audioName);
        values.put(MySQLiteGhostCallHelper.EFFECTS_VOLUME, volume);
        values.put(MySQLiteGhostCallHelper.EFFECTS_IMAGE_ACTIVE, imageActive);
        values.put(MySQLiteGhostCallHelper.EFFECTS_IMAGE_ON, imageOn);
        values.put(MySQLiteGhostCallHelper.EFFECTS_IMAGE_OFF, imageOff);
        values.put(MySQLiteGhostCallHelper.EFFECTS_CREATED_AT, createdAt);
        values.put(MySQLiteGhostCallHelper.EFFECTS_UPDATED_AT, updatedAt);
        values.put(MySQLiteGhostCallHelper.EFFECTS_AUDIO_URL, audioURL);

        database.insert(MySQLiteGhostCallHelper.TABLE_SOUND_EFFECTS, null, values);
    }

    public void createBackgroundEffects(String id, String backgroundID, String name, String audioName, String volume, String audioURL) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteGhostCallHelper.BACKGROUND_ID, id);
        values.put(MySQLiteGhostCallHelper.BACKGROUND_BACKGROUND_ID, backgroundID);
        values.put(MySQLiteGhostCallHelper.BACKGROUND_NAME, name);
        values.put(MySQLiteGhostCallHelper.BACKGROUND_AUDIO_NAME, audioName);
        values.put(MySQLiteGhostCallHelper.BACKGROUND_VOLUME, volume);
        values.put(MySQLiteGhostCallHelper.BACKGROUND_AUDIO_URL, audioURL);

        database.insert(MySQLiteGhostCallHelper.TABLE_BACKGROUND_EFFECTS, null, values);
    }

    public String getUserNumber() {
        Cursor cursor = database.rawQuery("Select phone_number from user", null);
        cursor.moveToFirst();
        StringBuilder formatNumber = new StringBuilder(cursor.getString(cursor.getColumnIndex(MySQLiteGhostCallHelper.USER_PHONE_NUMBER)));
        formatNumber.replace(0, 2, "(");
        formatNumber.insert(4, ")");
        formatNumber.insert(5, " ");
        formatNumber.insert(9, "-");
        return formatNumber.toString();
    }

    public ArrayList<GhostNumbers> getUserNumbers() {
        ArrayList<GhostNumbers> userNumbers = new ArrayList();
        Cursor cursor = database.rawQuery("select * from " + MySQLiteGhostCallHelper.TABLE_NUMBERS, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            GhostNumbers ghostNumber = new GhostNumbers();
            StringBuilder formatNumber = new StringBuilder(cursor.getString(cursor.getColumnIndex(MySQLiteGhostCallHelper.NUMBER)));
            formatNumber.replace(0, 2, "(");
            formatNumber.insert(4, ")");
            formatNumber.insert(5, " ");
            formatNumber.insert(9, "-");
            ghostNumber.setGhostNumber(formatNumber.toString());
            ghostNumber.setGhostTitle((cursor.getString(cursor.getColumnIndex(MySQLiteGhostCallHelper.NAME))));
            ghostNumber.setGhostID(cursor.getString(cursor.getColumnIndex(MySQLiteGhostCallHelper.ID)));
            userNumbers.add(ghostNumber);
            cursor.moveToNext();
        }
        cursor.close();
        return userNumbers;
    }

    public boolean dataExists(String id, String table) {
        Cursor cursor = database.rawQuery("SELECT * FROM " + table + " WHERE id = " + id, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public boolean numberExists(String id) {
        Cursor cursor = database.rawQuery("select * from " + MySQLiteGhostCallHelper.TABLE_NUMBERS + " where id = " + id, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public String getLatestTimestamp() {
        Cursor cursor = database.rawQuery("SELECT updated_at FROM " + MySQLiteGhostCallHelper.TABLE_CALLS + " UNION SELECT updated_at FROM " + MySQLiteGhostCallHelper.TABLE_MESSAGES +
                " UNION SELECT updated_at FROM " + MySQLiteGhostCallHelper.TABLE_VOICEMAILS + " ORDER BY updated_at DESC", null);
        cursor.moveToFirst();
        String latestTimestamp = cursor.getString(0);
        cursor.close();
        return latestTimestamp;
    }

    public boolean creditPackageExists(String id) {
        Cursor cursor = database.rawQuery("select * from " + MySQLiteGhostCallHelper.TABLE_CREDIT_PACKAGES + " where id = " + id, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public boolean soundEffectExists(String id) {
        Cursor cursor = database.rawQuery("select * from " + MySQLiteGhostCallHelper.TABLE_SOUND_EFFECTS + " where id = " + id, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public boolean numberPackageExists(String id) {
        Cursor cursor = database.rawQuery("select * from " + MySQLiteGhostCallHelper.TABLE_NUMBER_PACKAGES + " where id = " + id, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }


}
