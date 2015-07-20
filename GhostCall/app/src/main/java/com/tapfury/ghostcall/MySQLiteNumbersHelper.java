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

    private static final String DATABASE_NAME = "ghostcall.db";
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "CREATE TABLE " + TABLE_NUMBERS + "(" + PRIMARY_ID + " integer primary key autoincrement, "
            + ID + " TEXT, " + NAME + " TEXT, " + NUMBER + " TEXT, " + VOICEMAIL + " TEXT, " + DISABLE_CALLS + " TEXT, " +
            DISABLE_MESSAGES + " TEXT, " + EXPIRE_ON + " TEXT" + ");";


    public MySQLiteNumbersHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NUMBERS);
    }

    public SQLiteDatabase getWritable() {
        return getWritableDatabase();
    }
}
