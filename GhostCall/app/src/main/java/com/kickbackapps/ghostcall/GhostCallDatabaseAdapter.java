package com.kickbackapps.ghostcall;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kickbackapps.ghostcall.BackgroundEffects.BackgroundObject;
import com.kickbackapps.ghostcall.SoundEffects.EffectsObject;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

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
        values.put(MySQLiteGhostCallHelper.CALLS_TYPE, "call");

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
        values.put(MySQLiteGhostCallHelper.MESSAGES_TYPE, "message");

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
        values.put(MySQLiteGhostCallHelper.VOICEMAILS_TYPE, "voicemail");

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
            ghostNumber.setExpirationDate(cursor.getString(cursor.getColumnIndex(MySQLiteGhostCallHelper.EXPIRE_ON)));
            userNumbers.add(ghostNumber);
            cursor.moveToNext();
        }
        cursor.close();
        return userNumbers;
    }

    public ArrayList<SmsObject> getSmsHistory(String numberID, String toNumber) {
        ArrayList<SmsObject> smsList = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT " + MySQLiteGhostCallHelper.MESSAGES_TO + ", " + MySQLiteGhostCallHelper.MESSAGES_STATUS + ", " + MySQLiteGhostCallHelper.MESSAGES_CREATED_AT + ", " +
        MySQLiteGhostCallHelper.MESSAGES_DIRECTION + ", " + MySQLiteGhostCallHelper.MESSAGES_TEXT + ", " + MySQLiteGhostCallHelper.MESSAGES_FROM + " FROM "
        + MySQLiteGhostCallHelper.TABLE_MESSAGES + " WHERE " + MySQLiteGhostCallHelper.MESSAGES_NUMBER_ID + " = " +
                 numberID + " AND (" + MySQLiteGhostCallHelper.MESSAGES_TO + " = " + "\"" + toNumber + "\"" + " OR " + MySQLiteGhostCallHelper.MESSAGES_FROM + " = " + "\"" + toNumber + "\")", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            SmsObject smsObject = new SmsObject();
            StringBuilder formatNumber = new StringBuilder(cursor.getString(cursor.getColumnIndex(MySQLiteGhostCallHelper.MESSAGES_TO)));
            formatNumber.replace(0, 2, "(");
            formatNumber.insert(4, ")");
            formatNumber.insert(5, " ");
            formatNumber.insert(9, "-");
            smsObject.setMessageNumber(formatNumber.toString());
            smsObject.setMessageName("#");
            smsObject.setMessageText(cursor.getString(cursor.getColumnIndex(MySQLiteGhostCallHelper.MESSAGES_TEXT)));
            smsObject.setMessageDirection(cursor.getString(cursor.getColumnIndex(MySQLiteGhostCallHelper.MESSAGES_DIRECTION)));
//            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
//            DateTime dateTime = formatter.parseDateTime(cursor.getString(cursor.getColumnIndex(MySQLiteGhostCallHelper.MESSAGES_CREATED_AT))).withZone(DateTimeZone.UTC);
//            DateTimeFormatter formatterTime = DateTimeFormat.forPattern("MMM dd, yyyy hh:mm a").withZone(DateTimeZone.getDefault());
//            smsObject.setMessageDate(formatterTime.print(dateTime));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            try {
                Date parsed = simpleDateFormat.parse(cursor.getString(cursor.getColumnIndex(MySQLiteGhostCallHelper.MESSAGES_CREATED_AT)));
                SimpleDateFormat smsDateFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a");
                smsDateFormat.setTimeZone(TimeZone.getDefault());
                smsObject.setMessageDate(smsDateFormat.format(parsed));
            } catch (ParseException e) {

            }


            smsList.add(smsObject);
            cursor.moveToNext();
        }
        cursor.close();
        return smsList;
    }

    public ArrayList<HistoryObject> getCallHistory(String numberID, int amount) {
        ArrayList<HistoryObject> historyList = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT " + MySQLiteGhostCallHelper.CALLS_ID + ", " + MySQLiteGhostCallHelper.CALLS_TO + ", " + MySQLiteGhostCallHelper.CALLS_FROM + ", " + MySQLiteGhostCallHelper.CALLS_UPDATED_AT + ", " + MySQLiteGhostCallHelper.CALLS_DIRECTION
                + ", " + MySQLiteGhostCallHelper.CALLS_CREATED_AT + ", " + MySQLiteGhostCallHelper.CALLS_TYPE + ", " + MySQLiteGhostCallHelper.CALLS_RECORD + " FROM " + MySQLiteGhostCallHelper.TABLE_CALLS + " WHERE number_id = " + numberID + " UNION " + "SELECT " + MySQLiteGhostCallHelper.VOICEMAILS_ID + ", " + MySQLiteGhostCallHelper.VOICEMAILS_TO + ", " + MySQLiteGhostCallHelper.VOICEMAILS_FROM + ", " + MySQLiteGhostCallHelper.VOICEMAILS_UPDATED_AT + ", " + MySQLiteGhostCallHelper.VOICEMAILS_DURATION + ", " + MySQLiteGhostCallHelper.VOICEMAILS_CREATED_AT
                + ", " + MySQLiteGhostCallHelper.VOICEMAILS_TYPE + ", " + MySQLiteGhostCallHelper.VOICEMAILS_DURATION + " FROM " + MySQLiteGhostCallHelper.TABLE_VOICEMAILS + " WHERE number_id = " + numberID + " UNION SELECT " + MySQLiteGhostCallHelper.MESSAGES_ID + ", " + MySQLiteGhostCallHelper.MESSAGES_TO + ", " + MySQLiteGhostCallHelper.MESSAGES_FROM + ", " + MySQLiteGhostCallHelper.MESSAGES_UPDATED_AT + ", " + MySQLiteGhostCallHelper.MESSAGES_TEXT
                + ", " + MySQLiteGhostCallHelper.MESSAGES_CREATED_AT +  ", " + MySQLiteGhostCallHelper.MESSAGES_TYPE + ", " + MySQLiteGhostCallHelper.MESSAGES_STATUS + " FROM " + MySQLiteGhostCallHelper.TABLE_MESSAGES + " WHERE number_id = " + numberID + " GROUP BY " + MySQLiteGhostCallHelper.MESSAGES_TO + " , " + MySQLiteGhostCallHelper.MESSAGES_FROM + " ORDER BY updated_at DESC LIMIT " + Integer.toString(amount), null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            HistoryObject historyObject = new HistoryObject();
            StringBuilder formatNumber = new StringBuilder(cursor.getString(cursor.getColumnIndex(MySQLiteGhostCallHelper.CALLS_TO)));
            formatNumber.replace(0, 2, "(");
            formatNumber.insert(4, ")");
            formatNumber.insert(5, " ");
            formatNumber.insert(9, "-");
            historyObject.setHistoryNumber(formatNumber.toString());

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

            try {
                Date parsed = simpleDateFormat.parse(cursor.getString(cursor.getColumnIndex(MySQLiteGhostCallHelper.CALLS_UPDATED_AT)));

                SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd");
                dateFormat.setTimeZone(TimeZone.getDefault());
                historyObject.setHistoryDate(dateFormat.format(parsed));

                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
                timeFormat.setTimeZone(TimeZone.getDefault());
                historyObject.setHistoryTime(timeFormat.format(parsed));

            } catch (ParseException e) {

            }

            // remove this later

//            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
//            DateTime dateTime = formatter.parseDateTime(cursor.getString(cursor.getColumnIndex(MySQLiteGhostCallHelper.CALLS_UPDATED_AT)));
//            DateTimeFormatter formatterDate = DateTimeFormat.forPattern("MMM dd");
//            historyObject.setHistoryDate(formatterDate.print(dateTime));
//            DateTimeFormatter formatterTime = DateTimeFormat.forPattern("hh:mm a").withZone(DateTimeZone.getDefault());
//            historyObject.setHistoryTime(formatterTime.print(dateTime));

            historyObject.setHistoryID(cursor.getString(cursor.getColumnIndex(MySQLiteGhostCallHelper.CALLS_ID)));
            historyObject.setHistoryRecord(cursor.getString(cursor.getColumnIndex(MySQLiteGhostCallHelper.CALLS_RECORD)));
            if (historyObject.getHistoryRecord().equals("1")) {
                historyObject.setHistoryState("not_playing");
            } else {
                historyObject.setHistoryState("");
            }

            historyObject.setHistoryDescription(cursor.getString(cursor.getColumnIndex(MySQLiteGhostCallHelper.CALLS_DIRECTION)));
            historyObject.setHistoryType(cursor.getString(cursor.getColumnIndex(MySQLiteGhostCallHelper.CALLS_TYPE)));
            StringBuilder formatNumberTwo = new StringBuilder(cursor.getString(cursor.getColumnIndex(MySQLiteGhostCallHelper.CALLS_FROM)));
            formatNumberTwo.replace(0, 2, "(");
            formatNumberTwo.insert(4, ")");
            formatNumberTwo.insert(5, " ");
            formatNumberTwo.insert(9, "-");
            historyObject.setHistoryOutNumber(formatNumberTwo.toString());
            historyList.add(historyObject);
            cursor.moveToNext();
        }
        cursor.close();
        return historyList;
    }

    public ArrayList<BackgroundObject> getBackgroundObjects() {
        ArrayList<BackgroundObject> backgroundList = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT " + MySQLiteGhostCallHelper.BACKGROUND_NAME + ", " + MySQLiteGhostCallHelper.BACKGROUND_AUDIO_URL + ", " + MySQLiteGhostCallHelper.BACKGROUND_ID + " FROM " + MySQLiteGhostCallHelper.TABLE_BACKGROUND_EFFECTS, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            BackgroundObject backgroundObject = new BackgroundObject();
            backgroundObject.setBackgroundName(cursor.getString(cursor.getColumnIndex(MySQLiteGhostCallHelper.BACKGROUND_NAME)));
            backgroundObject.setBackgroundURL(cursor.getString(cursor.getColumnIndex(MySQLiteGhostCallHelper.BACKGROUND_AUDIO_URL)));
            backgroundObject.setBackgroundID(cursor.getString(cursor.getColumnIndex(MySQLiteGhostCallHelper.BACKGROUND_ID)));
            backgroundObject.setBackgroundState("Unselected");
            backgroundList.add(backgroundObject);
            cursor.moveToNext();
        }
        cursor.close();
        return backgroundList;
    }

    public ArrayList<EffectsObject> getEffectsObjects() {
        ArrayList<EffectsObject> effectsList = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT " + MySQLiteGhostCallHelper.EFFECTS_NAME + ", " + MySQLiteGhostCallHelper.EFFECTS_AUDIO_URL + ", " + MySQLiteGhostCallHelper.EFFECTS_ID + " FROM " + MySQLiteGhostCallHelper.TABLE_SOUND_EFFECTS, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            EffectsObject effectsObject = new EffectsObject();
            effectsObject.setEffectsName(cursor.getString(cursor.getColumnIndex(MySQLiteGhostCallHelper.EFFECTS_NAME)));
            effectsObject.setEffectsURL(cursor.getString(cursor.getColumnIndex(MySQLiteGhostCallHelper.EFFECTS_AUDIO_URL)));
            effectsObject.setEffectsID(cursor.getString(cursor.getColumnIndex(MySQLiteGhostCallHelper.EFFECTS_ID)));
            effectsObject.setEffectsState("unselected");
            effectsList.add(effectsObject);
            cursor.moveToNext();
        }
        cursor.close();
        return effectsList;
    }

    public boolean numberExists(String id) {
        Cursor cursor = database.rawQuery("select * from " + MySQLiteGhostCallHelper.TABLE_NUMBERS + " where id = " + id, null);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public String getNumberName(String id) {
        Cursor cursor = database.rawQuery("SELECT " + MySQLiteGhostCallHelper.NUMBERS_NAME + " FROM " + MySQLiteGhostCallHelper.TABLE_NUMBERS + " WHERE id = " + id, null);
        cursor.moveToFirst();
        String numberName = cursor.getString(cursor.getColumnIndex(MySQLiteGhostCallHelper.NUMBERS_NAME));
        cursor.close();
        return numberName;
    }

    public String getLatestTimestamp() {
        Cursor cursor = database.rawQuery("SELECT updated_at FROM " + MySQLiteGhostCallHelper.TABLE_CALLS + " UNION SELECT updated_at FROM " + MySQLiteGhostCallHelper.TABLE_MESSAGES +
                " UNION SELECT updated_at FROM " + MySQLiteGhostCallHelper.TABLE_VOICEMAILS + " ORDER BY updated_at DESC", null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            String latestTimestamp = cursor.getString(0);
            cursor.close();
            return latestTimestamp;
        }
        cursor.close();
        return "";
    }

    public ArrayList<GhostPackage> getPackages(String type) {
        ArrayList<GhostPackage> ghostPackageArrayList = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + MySQLiteGhostCallHelper.TABLE_NUMBER_PACKAGES + " WHERE " + MySQLiteGhostCallHelper.NUMBERS_TYPE + " = '" + type + "'", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            GhostPackage ghostPackage = new GhostPackage();
            ghostPackage.setPackageName(cursor.getString(cursor.getColumnIndex(MySQLiteGhostCallHelper.NUMBERS_NAME)));
            ghostPackage.setPackagePrice(cursor.getString(cursor.getColumnIndex(MySQLiteGhostCallHelper.NUMBERS_COST)));
            ghostPackage.setPackageTime(cursor.getString(cursor.getColumnIndex(MySQLiteGhostCallHelper.NUMBERS_EXPIRATION)));
            ghostPackage.setPackageCredits(cursor.getString(cursor.getColumnIndex(MySQLiteGhostCallHelper.NUMBERS_CREDITS)));
            ghostPackage.setPackageType(cursor.getString(cursor.getColumnIndex(MySQLiteGhostCallHelper.NUMBERS_TYPE)));
            ghostPackage.setPackageID(cursor.getString(cursor.getColumnIndex(MySQLiteGhostCallHelper.NUMBERS_ID)));
            ghostPackage.setPackageAndroidID(cursor.getString(cursor.getColumnIndex(MySQLiteGhostCallHelper.NUMBERS_ANDROID_PRODUCT_ID)));
            ghostPackageArrayList.add(ghostPackage);
            cursor.moveToNext();
        }
        cursor.close();
        return ghostPackageArrayList;
    }

    public ArrayList<GhostPackage> getCreditPackages() {
        ArrayList<GhostPackage> ghostPackageArrayList = new ArrayList<>();
        Cursor cursor = database.rawQuery("Select * FROM " + MySQLiteGhostCallHelper.TABLE_CREDIT_PACKAGES, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            GhostPackage ghostPackage = new GhostPackage();
            ghostPackage.setPackageName(cursor.getString(cursor.getColumnIndex(MySQLiteGhostCallHelper.CREDITS_NAME)));
            ghostPackage.setPackageType("credits");
            ghostPackage.setPackageTime(cursor.getString(cursor.getColumnIndex(MySQLiteGhostCallHelper.CREDITS_DESCRIPTION)));
            ghostPackage.setPackagePrice(cursor.getString(cursor.getColumnIndex(MySQLiteGhostCallHelper.CREDITS_COST)));
            ghostPackage.setPackageID(cursor.getString(cursor.getColumnIndex(MySQLiteGhostCallHelper.CREDITS_ID)));
            ghostPackage.setPackageAndroidID(cursor.getString(cursor.getColumnIndex(MySQLiteGhostCallHelper.CREDITS_ANDROID_PRODUCT_ID)));
            ghostPackageArrayList.add(ghostPackage);
            cursor.moveToNext();
        }
        cursor.close();
        return ghostPackageArrayList;
    }

    public void updateNumberTimestamp(String newTimestamp, String numberID) {
        Cursor cursor = database.rawQuery("UPDATE " + MySQLiteGhostCallHelper.TABLE_NUMBERS + " SET " + MySQLiteGhostCallHelper.EXPIRE_ON + " = '" + newTimestamp + "' WHERE " + MySQLiteGhostCallHelper.ID
                 + " = '" + numberID + "'", null);
        cursor.moveToFirst();
        cursor.close();
    }
}
