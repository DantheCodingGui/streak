package com.danthecodinggui.streak.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.danthecodinggui.streak.Activities.HomeActivity;
import com.danthecodinggui.streak.Activities.StreakObject;

import java.util.List;

/**
 * Manages database actions
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
                + StreakContract.StreakTable.STREAK_DURATION + " INT NOT NULL, "
                + StreakContract.StreakTable.STREAK_IS_PRIORITY + " INT NOT NULL, "
                + StreakContract.StreakTable.STREAK_VIEW_INDEX + " INT NOT NULL);";

        db.execSQL(SQL_CREATE_STREAK_TABLE);
    }

    /**
     * Called when changes made to database structure, look into later
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + StreakContract.StreakTable.TABLE_NAME);
        onCreate(db);
    }

    public long AddStreak(StreakObject newStreak) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(StreakContract.StreakTable.STREAK_DESCRIPTION, newStreak.getStreakText());
        values.put(StreakContract.StreakTable.STREAK_DURATION, newStreak.getStreakDuration());
        values.put(StreakContract.StreakTable.STREAK_IS_PRIORITY, newStreak.getStreakIsPriority());
        values.put(StreakContract.StreakTable.STREAK_VIEW_INDEX, newStreak.getStreakViewIndex());

        return db.insert(StreakContract.StreakTable.TABLE_NAME, null, values);
    }

    public void UpdateStreakValues(StreakObject editedStreak, int whatToUpdate) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        switch(whatToUpdate) {
            case (HomeActivity.UPDATE_TEXT):
                values.put(StreakContract.StreakTable.STREAK_DESCRIPTION, editedStreak.getStreakText());
                break;
            case (HomeActivity.UPDATE_IS_PRIORITY):
                values.put(StreakContract.StreakTable.STREAK_IS_PRIORITY, editedStreak.getStreakIsPriority());
                break;
            default:
                Log.d("Error", "Invalid call to UpdateStreak");
                return;
        }

        String selection = StreakContract.StreakTable._ID + " = ?";
        String[] selectionArgs = { Long.toString(editedStreak.getStreakId()) };

        db.update(
                StreakContract.StreakTable.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );
    }

    public void SwapListViewIndexes(StreakObject firstStreak, StreakObject secondStreak) {

        int temp = firstStreak.getStreakViewIndex();

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(StreakContract.StreakTable.STREAK_VIEW_INDEX, secondStreak.getStreakViewIndex());

        String selection = StreakContract.StreakTable.STREAK_VIEW_INDEX + " = ?";
        String[] selectionArgs = { Integer.toString(temp) };

        db.update(
                StreakContract.StreakTable.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );

        values = new ContentValues();

        values.put(StreakContract.StreakTable.STREAK_VIEW_INDEX, temp);

        selection = StreakContract.StreakTable._ID + " = ?";
        selectionArgs = new String[]{ Long.toString(secondStreak.getStreakId()) };

        db.update(
                StreakContract.StreakTable.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );
    }

    public void DeleteStreak(StreakObject streakToDelete) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selection = StreakContract.StreakTable._ID + " = ?";
        String[] selectionArgs = {Long.toString(streakToDelete.getStreakId())};

        db.delete(StreakContract.StreakTable.TABLE_NAME, selection, selectionArgs);

    }

    public void GetAllStreaks(List<StreakObject> recyclerViewItems) {
        SQLiteDatabase db = this.getReadableDatabase();

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

        while(cursor.moveToNext()) {
            long streakId = cursor.getLong(cursor.getColumnIndexOrThrow((StreakContract.StreakTable._ID)));
            String streakText = cursor.getString(cursor.getColumnIndexOrThrow(StreakContract.StreakTable.STREAK_DESCRIPTION));
            int streakDuration = cursor.getInt(cursor.getColumnIndexOrThrow(StreakContract.StreakTable.STREAK_DURATION));
            boolean streakPriority = (cursor.getInt(cursor.getColumnIndexOrThrow(StreakContract.StreakTable.STREAK_IS_PRIORITY)) != 0);
            int streakViewIndex = cursor.getInt(cursor.getColumnIndexOrThrow(StreakContract.StreakTable.STREAK_VIEW_INDEX));
            StreakObject ob = new StreakObject(streakText, streakDuration, streakViewIndex);
            recyclerViewItems.add(ob);
            ob.setStreakId(streakId);
        }
        cursor.close();
    }
}
