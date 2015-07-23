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
public class NumbersDatabaseAdapter {

    private SQLiteDatabase database;
    private MySQLiteNumbersHelper dbHelper;

    private String[] allColumns = { MySQLiteNumbersHelper.PRIMARY_ID, MySQLiteNumbersHelper.ID, MySQLiteNumbersHelper.NUMBER, MySQLiteNumbersHelper.NAME,
            MySQLiteNumbersHelper.VOICEMAIL, MySQLiteNumbersHelper.DISABLE_CALLS,
            MySQLiteNumbersHelper.DISABLE_MESSAGES, MySQLiteNumbersHelper.EXPIRE_ON
    };

    public NumbersDatabaseAdapter(Context context) {
        dbHelper = new MySQLiteNumbersHelper(context);
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
        values.put(MySQLiteNumbersHelper.ID, id);
        values.put(MySQLiteNumbersHelper.NUMBER, number);
        values.put(MySQLiteNumbersHelper.NAME, name);
        values.put(MySQLiteNumbersHelper.VOICEMAIL, voicemail);
        values.put(MySQLiteNumbersHelper.DISABLE_CALLS, disable_calls);
        values.put(MySQLiteNumbersHelper.DISABLE_MESSAGES, disable_messages);
        values.put(MySQLiteNumbersHelper.EXPIRE_ON, expire_on);

        long insertID = database.insert(MySQLiteNumbersHelper.TABLE_NUMBERS, null, values);
        Cursor cursor = database.query(MySQLiteNumbersHelper.TABLE_NUMBERS, allColumns, MySQLiteNumbersHelper.PRIMARY_ID + "= " + insertID, null, null, null, null);
        cursor.moveToFirst();
        cursor.close();
    }

    public void createCreditsNumber(String id, String name, String description, String cost, String credits, String iosProductID, String androidProductID, String createdAt, String updatedAt, String deleted) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteNumbersHelper.CREDITS_ID, id);
        values.put(MySQLiteNumbersHelper.CREDITS_NAME, name);
        values.put(MySQLiteNumbersHelper.CREDITS_DESCRIPTION, description);
        values.put(MySQLiteNumbersHelper.CREDITS_COST, cost);
        values.put(MySQLiteNumbersHelper.CREDITS_CREDITS, credits);
        values.put(MySQLiteNumbersHelper.CREDITS_IOS_PRODUCT_ID, iosProductID);
        values.put(MySQLiteNumbersHelper.CREDITS_ANDROID_PRODUCT_ID, androidProductID);
        values.put(MySQLiteNumbersHelper.CREDITS_CREATED_AT, createdAt);
        values.put(MySQLiteNumbersHelper.CREDITS_UPDATED_AT, updatedAt);
        values.put(MySQLiteNumbersHelper.CREDITS_DELETED, deleted);

        database.insert(MySQLiteNumbersHelper.TABLE_CREDIT_PACKAGES, null, values);
    }

    public void createNumbersPackage(String id, String name, String type, String credits, String cost, String iosProductID, String androidProductID, String expiration, String createdOn) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteNumbersHelper.NUMBERS_ID, id);
        values.put(MySQLiteNumbersHelper.NUMBERS_NAME, name);
        values.put(MySQLiteNumbersHelper.NUMBERS_TYPE, type);
        values.put(MySQLiteNumbersHelper.NUMBERS_CREDITS, credits);
        values.put(MySQLiteNumbersHelper.NUMBERS_COST, cost);
        values.put(MySQLiteNumbersHelper.NUMBERS_IOS_PRODUCT_ID, iosProductID);
        values.put(MySQLiteNumbersHelper.NUMBERS_ANDROID_PRODUCT_ID, androidProductID);
        values.put(MySQLiteNumbersHelper.NUMBERS_EXPIRATION, expiration);
        values.put(MySQLiteNumbersHelper.NUMBERS_CREATED_ON, createdOn);

        database.insert(MySQLiteNumbersHelper.TABLE_NUMBER_PACKAGES, null, values);
    }

    public void createSoundEffect(String id, String effectID, String name, String audioName, String volume, String imageActive, String imageOn, String imageOff, String createdAt, String updatedAt, String audioURL) {
        ContentValues values = new ContentValues();
        values.put(MySQLiteNumbersHelper.EFFECTS_ID, id);
        values.put(MySQLiteNumbersHelper.EFFECTS_EFFECT_ID, effectID);
        values.put(MySQLiteNumbersHelper.EFFECTS_NAME, name);
        values.put(MySQLiteNumbersHelper.EFFECTS_AUDIO_NAME, audioName);
        values.put(MySQLiteNumbersHelper.EFFECTS_VOLUME, volume);
        values.put(MySQLiteNumbersHelper.EFFECTS_IMAGE_ACTIVE, imageActive);
        values.put(MySQLiteNumbersHelper.EFFECTS_IMAGE_ON, imageOn);
        values.put(MySQLiteNumbersHelper.EFFECTS_IMAGE_OFF, imageOff);
        values.put(MySQLiteNumbersHelper.EFFECTS_CREATED_AT, createdAt);
        values.put(MySQLiteNumbersHelper.EFFECTS_UPDATED_AT, updatedAt);
        values.put(MySQLiteNumbersHelper.EFFECTS_AUDIO_URL, audioURL);

        database.insert(MySQLiteNumbersHelper.TABLE_SOUND_EFFECTS, null, values);
    }

    public ArrayList<GhostNumbers> getUserNumbers() {
        ArrayList<GhostNumbers> userNumbers = new ArrayList();
        Cursor cursor = database.rawQuery("select * from " + MySQLiteNumbersHelper.TABLE_NUMBERS, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            GhostNumbers ghostNumber = new GhostNumbers();
            StringBuilder formatNumber = new StringBuilder(cursor.getString(cursor.getColumnIndex(MySQLiteNumbersHelper.NUMBER)));
            formatNumber.replace(0, 2, "(");
            formatNumber.insert(4, ")");
            formatNumber.insert(5, " ");
            formatNumber.insert(9, "-");
            ghostNumber.setGhostNumber(formatNumber.toString());
            ghostNumber.setGhostTitle((cursor.getString(cursor.getColumnIndex(MySQLiteNumbersHelper.NAME))));
            userNumbers.add(ghostNumber);
            cursor.moveToNext();
        }
        cursor.close();
        return userNumbers;
    }

    public boolean numberExists(String id) {
        Cursor cursor = database.rawQuery("select * from " + MySQLiteNumbersHelper.TABLE_NUMBERS + " where id = " + id, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public boolean creditPackageExists(String id) {
        Cursor cursor = database.rawQuery("select * from " + MySQLiteNumbersHelper.TABLE_CREDIT_PACKAGES + " where id = " + id, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public boolean soundEffectExists(String id) {
        Cursor cursor = database.rawQuery("select * from " + MySQLiteNumbersHelper.TABLE_SOUND_EFFECTS + " where id = " + id, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public boolean numberPackageExists(String id) {
        Cursor cursor = database.rawQuery("select * from " + MySQLiteNumbersHelper.TABLE_NUMBER_PACKAGES + " where id = " + id, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }


}
