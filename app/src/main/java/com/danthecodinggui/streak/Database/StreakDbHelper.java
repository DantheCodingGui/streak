package com.danthecodinggui.streak.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.danthecodinggui.streak.StreakObject;

import java.util.List;

/**
 * Created by Dan on 05/08/2017.
 */

public class StreakDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "streak.db";

    private static final int DATABASE_VERSION = 1;

    public StreakDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_STREAK_TABLE = "CREATE TABLE " + StreakContract.StreakTable.TABLE_NAME
                + "(" + StreakContract.StreakTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + StreakContract.StreakTable.STREAK_DESCRIPTION + " TEXT NOT NULL, "
                + StreakContract.StreakTable.STREAK_DURATION + " INT NOT NULL);";

        db.execSQL(SQL_CREATE_STREAK_TABLE);
    }

    /**
     * Called when changes made to database structure, look into later
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + StreakContract.StreakTable.TABLE_NAME);
        onCreate(db);
    }

    public void AddStreak(StreakObject newStreak) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(StreakContract.StreakTable.STREAK_DESCRIPTION, newStreak.getStreakText());
        values.put(StreakContract.StreakTable.STREAK_DURATION, newStreak.getStreakDuration());

        db.insert(StreakContract.StreakTable.TABLE_NAME, null, values);
    }

    public void DeleteStreak() {

    }

    public void GetAllStreaks(List<StreakObject> recyclerViewItems) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                StreakContract.StreakTable._ID,
                StreakContract.StreakTable.STREAK_DESCRIPTION,
                StreakContract.StreakTable.STREAK_DURATION
        };

        Cursor cursor = db.query(
                StreakContract.StreakTable.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null,
                null
        );

        while(cursor.moveToNext()) {
            String streakText = cursor.getString(cursor.getColumnIndexOrThrow(StreakContract.StreakTable.STREAK_DESCRIPTION));
            recyclerViewItems.add(new StreakObject(streakText));
        }
        cursor.close();
    }
}
