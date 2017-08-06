package com.danthecodinggui.streak.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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

        Log.d("boogie", "attempt to create database...");

        final String SQL_CREATE_STREAK_TABLE = "CREATE TABLE " + StreakContract.StreakTable.TABLE_NAME
                + "(" + StreakContract.StreakTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + StreakContract.StreakTable.STREAK_DESCRIPTION + " TEXT NOT NULL, "
                + StreakContract.StreakTable.STREAK_DURATION + " INT NOT NULL, "
                + StreakContract.StreakTable.STREAK_IS_PRIORITY + " INT NOT NULL, "
                + StreakContract.StreakTable.STREAK_VIEW_INDEX + " INT NOT NULL);";

        db.execSQL(SQL_CREATE_STREAK_TABLE);

        Log.d("boogie", "database created");
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

        Log.d("boogie", "getting writable database...");

        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("boogie", "got database");
        Log.d("boogie", "setting column values...");

        ContentValues values = new ContentValues();
        values.put(StreakContract.StreakTable.STREAK_DESCRIPTION, newStreak.getStreakText());
        values.put(StreakContract.StreakTable.STREAK_DURATION, newStreak.getStreakDuration());
        values.put(StreakContract.StreakTable.STREAK_IS_PRIORITY, newStreak.getStreakIsPriority());
        values.put(StreakContract.StreakTable.STREAK_VIEW_INDEX, newStreak.getStreakViewIndex());

        db.insert(StreakContract.StreakTable.TABLE_NAME, null, values);

        Log.d("boogie", "Streak added to database");
    }

    public void DeleteStreak(StreakObject streakToDelete) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selection = StreakContract.StreakTable.STREAK_VIEW_INDEX + " = ?";
        String[] selectionArgs = {Integer.toString(streakToDelete.getStreakViewIndex())};

        db.delete(StreakContract.StreakTable.TABLE_NAME, selection, selectionArgs);

    }

    public void GetAllStreaks(List<StreakObject> recyclerViewItems) {
        SQLiteDatabase db = this.getReadableDatabase();

        Log.d("boogie", "cursor has not loaded yet");

        String[] projection = {
                StreakContract.StreakTable._ID,
                StreakContract.StreakTable.STREAK_DESCRIPTION,
                StreakContract.StreakTable.STREAK_DURATION,
                StreakContract.StreakTable.STREAK_IS_PRIORITY,
                StreakContract.StreakTable.STREAK_VIEW_INDEX
        };

        String sortOrder = StreakContract.StreakTable.STREAK_VIEW_INDEX + " ASC";

        Cursor cursor = db.query(
                StreakContract.StreakTable.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );

        Log.d("boogie", "cursor has succeeded");

        while(cursor.moveToNext()) {
            String streakText = cursor.getString(cursor.getColumnIndexOrThrow(StreakContract.StreakTable.STREAK_DESCRIPTION));
            int streakDuration = cursor.getInt(cursor.getColumnIndexOrThrow(StreakContract.StreakTable.STREAK_DURATION));
            boolean streakPriority = (cursor.getInt(cursor.getColumnIndexOrThrow(StreakContract.StreakTable.STREAK_IS_PRIORITY)) != 0);
            int streakViewIndex = cursor.getInt(cursor.getColumnIndexOrThrow(StreakContract.StreakTable.STREAK_VIEW_INDEX));
            recyclerViewItems.add(new StreakObject(streakText, streakDuration, streakViewIndex));
        }
        cursor.close();
    }
}
