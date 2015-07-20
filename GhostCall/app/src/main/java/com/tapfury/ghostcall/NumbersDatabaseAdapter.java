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

    private String[] numberColumns = {MySQLiteNumbersHelper.NAME, MySQLiteNumbersHelper.NUMBER };

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

    public ArrayList<GhostNumbers> getUserNumbers() {
        ArrayList<GhostNumbers> userNumbers = new ArrayList<GhostNumbers>();
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


}
