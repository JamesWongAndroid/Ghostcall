package com.tapfury.ghostcall;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ynott on 7/20/15.
 */
public class MySQLiteNumbersHelper extends SQLiteOpenHelper {

    public static final String TABLE_NUMBERS = "numbers";
    public static final String PRIMARY_ID = "_id";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String NUMBER = "number";
    public static final String EXPIRE_ON = "expire_on";
    public static final String VOICEMAIL = "voicemail";
    public static final String DISABLE_CALLS = "disable_calls";
    public static final String DISABLE_MESSAGES = "disable_messages";

    public static final String TABLE_CREDIT_PACKAGES = "credit_packages";
    public static final String CREDITS_PRIMARY_ID = "_id";
    public static final String CREDITS_ID = "id";
    public static final String CREDITS_NAME = "name";
    public static final String CREDITS_DESCRIPTION = "description";
    public static final String CREDITS_COST = "cost";
    public static final String CREDITS_CREDITS = "credits";
    public static final String CREDITS_IOS_PRODUCT_ID = "ios_product_id";
    public static final String CREDITS_ANDROID_PRODUCT_ID = "android_product_id";
    public static final String CREDITS_CREATED_AT = "created_at";
    public static final String CREDITS_UPDATED_AT = "updated_at";
    public static final String CREDITS_DELETED = "deleted";

    public static final String TABLE_SOUND_EFFECTS = "sound_effects";
    public static final String EFFECTS_PRIMARY_ID = "_id";
    public static final String EFFECTS_ID = "id";
    public static final String EFFECTS_EFFECT_ID = "effect_id";
    public static final String EFFECTS_NAME = "name";
    public static final String EFFECTS_AUDIO_NAME = "audio_name";
    public static final String EFFECTS_VOLUME = "volume";
    public static final String EFFECTS_IMAGE_ACTIVE = "images_active";
    public static final String EFFECTS_IMAGE_ON = "images_on";
    public static final String EFFECTS_IMAGE_OFF = "images_off";
    public static final String EFFECTS_CREATED_AT = "created_at";
    public static final String EFFECTS_UPDATED_AT = "updated_at";
    public static final String EFFECTS_AUDIO_URL = "audio_url";

    public static final String TABLE_NUMBER_PACKAGES = "number_packages";
    public static final String NUMBERS_PRIMARY_ID = "_id";
    public static final String NUMBERS_ID = "id";
    public static final String NUMBERS_NAME = "name";
    public static final String NUMBERS_TYPE = "type";
    public static final String NUMBERS_CREDITS = "credits";
    public static final String NUMBERS_COST = "cost";
    public static final String NUMBERS_IOS_PRODUCT_ID = "ios_product_id";
    public static final String NUMBERS_ANDROID_PRODUCT_ID = "android_product_id";
    public static final String NUMBERS_EXPIRATION = "expiration";
    public static final String NUMBERS_CREATED_ON = "created_on";

    public static final String TABLE_BACKGROUND_EFFECTS = "background_effects";
    public static final String BACKGROUND_PRIMARY_ID = "_id";
    public static final String BACKGROUND_ID = "id";
    public static final String BACKGROUND_BACKGROUND_ID = "background_id";
    public static final String BACKGROUND_NAME = "name";
    public static final String BACKGROUND_AUDIO_NAME = "audio_name";
    public static final String BACKGROUND_VOLUME = "volume";
    public static final String BACKGROUND_AUDIO_URL = "audio_url";

    public static final String TABLE_MESSAGES = "messages";
    public static final String MESSAGES_PRIMARY_ID = "_id";
    public static final String MESSAGES_ID = "id";
    public static final String MESSAGES_USER_ID = "user_id";
    public static final String MESSAGES_NUMBER_ID = "number_id";
    public static final String MESSAGES_TO = "message_to";
    public static final String MESSAGES_FROM = "message_from";
    public static final String MESSAGES_DIRECTION = "direction";
    public static final String MESSAGES_STATUS = "status";
    public static final String MESSAGES_RESOURCE_ID = "resource_id";
    public static final String MESSAGES_TEXT = "text";
    public static final String MESSAGES_CREATED_AT = "created_at";
    public static final String MESSAGES_UPDATED_AT = "updated_at";
    public static final String MESSAGES_DELETED = "deleted";

    private static final String DATABASE_NAME = "ghostcall.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE_NUMBERS =  "CREATE TABLE " + TABLE_NUMBERS + "(" + PRIMARY_ID + " integer primary key autoincrement, " + ID + " TEXT, " + NAME + " TEXT, " + NUMBER + " TEXT, " + VOICEMAIL + " TEXT, " + DISABLE_CALLS + " TEXT, " +
                                                            DISABLE_MESSAGES + " TEXT, " + EXPIRE_ON + " TEXT" + ");";

    private static final String DATABASE_CREATE_CREDIT_NUMBERS = "CREATE TABLE " + TABLE_CREDIT_PACKAGES + "(" + CREDITS_PRIMARY_ID + " integer primary key autoincrement, " + CREDITS_ID + " TEXT, " +
            CREDITS_NAME + " TEXT, " + CREDITS_DESCRIPTION + " TEXT, " + CREDITS_COST + " TEXT, " + CREDITS_CREDITS + " TEXT, " + CREDITS_IOS_PRODUCT_ID + " TEXT, " + CREDITS_ANDROID_PRODUCT_ID + " TEXT, " +
            CREDITS_CREATED_AT + " TEXT, " + CREDITS_UPDATED_AT + " TEXT, " + CREDITS_DELETED + " TEXT " + ");";

    private static final String DATABASE_CREATE_SOUND_EFFECTS = "CREATE TABLE " + TABLE_SOUND_EFFECTS +  "(" + EFFECTS_PRIMARY_ID + " integer primary key autoincrement, " + EFFECTS_ID + " TEXT, " + EFFECTS_EFFECT_ID + " TEXT, " + EFFECTS_NAME +
            " TEXT, " + EFFECTS_AUDIO_NAME + " TEXT, " +EFFECTS_VOLUME + " TEXT, " + EFFECTS_IMAGE_ACTIVE + " TEXT, " + EFFECTS_IMAGE_ON + " TEXT, " + EFFECTS_IMAGE_OFF + " TEXT, " + EFFECTS_CREATED_AT + " TEXT, " + EFFECTS_UPDATED_AT
            + " TEXT, " + EFFECTS_AUDIO_URL + " TEXT " + ");";

    private static final String DATABASE_CREATE_NUMBER_PACKAGES = "CREATE TABLE " + TABLE_NUMBER_PACKAGES + "(" + NUMBERS_PRIMARY_ID + " integer primary key autoincrement, " + NUMBERS_ID + " TEXT, " + NUMBERS_NAME + " TEXT, " + NUMBERS_TYPE + " TEXT, " +
            NUMBERS_CREDITS + " TEXT, " + NUMBERS_COST + " TEXT, " + NUMBERS_IOS_PRODUCT_ID + " TEXT, " + NUMBERS_ANDROID_PRODUCT_ID + " TEXT, " + NUMBERS_EXPIRATION + " TEXT, " + NUMBERS_CREATED_ON + " TEXT " + ");";

    private static final String DATABASE_CREATE_BACKGROUND_EFFECTS = "CREATE TABLE " + TABLE_BACKGROUND_EFFECTS + "(" + BACKGROUND_PRIMARY_ID + " integer primary key autoincrement, " + BACKGROUND_ID +
            " TEXT, " + BACKGROUND_BACKGROUND_ID + " TEXT, " + BACKGROUND_NAME + " TEXT, " + BACKGROUND_AUDIO_NAME + " TEXT, " + BACKGROUND_VOLUME + " TEXT, " + BACKGROUND_AUDIO_URL + " TEXT " + ");";

    private static final String DATABASE_CREATE_MESSAGES = "CREATE TABLE " + TABLE_MESSAGES + "(" + MESSAGES_PRIMARY_ID + " integer primary key autoincrement, " + MESSAGES_ID + " TEXT, " +
            MESSAGES_USER_ID + " TEXT, " + MESSAGES_NUMBER_ID + " TEXT, " + MESSAGES_TO + " TEXT, " + MESSAGES_FROM + " TEXT, " + MESSAGES_DIRECTION + " TEXT, " + MESSAGES_STATUS + " TEXT, " +
            MESSAGES_RESOURCE_ID + " TEXT, " + MESSAGES_TEXT + " TEXT, " + MESSAGES_CREATED_AT + " TEXT, " + MESSAGES_UPDATED_AT + " TEXT, " + MESSAGES_DELETED + " TEXT " + ");";


    public MySQLiteNumbersHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE_NUMBERS);
        sqLiteDatabase.execSQL(DATABASE_CREATE_CREDIT_NUMBERS);
        sqLiteDatabase.execSQL(DATABASE_CREATE_SOUND_EFFECTS);
        sqLiteDatabase.execSQL(DATABASE_CREATE_NUMBER_PACKAGES);
        sqLiteDatabase.execSQL(DATABASE_CREATE_BACKGROUND_EFFECTS);
        sqLiteDatabase.execSQL(DATABASE_CREATE_MESSAGES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NUMBERS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CREDIT_PACKAGES);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_SOUND_EFFECTS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NUMBER_PACKAGES);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_BACKGROUND_EFFECTS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);
    }

    public SQLiteDatabase getWritable() {
        return getWritableDatabase();
    }
}
