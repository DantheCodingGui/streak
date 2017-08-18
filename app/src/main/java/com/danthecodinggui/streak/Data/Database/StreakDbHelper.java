package com.danthecodinggui.streak.Data.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.danthecodinggui.streak.Data.StreakObject;
import com.danthecodinggui.streak.View.HomeActivity;

import java.util.List;

/**
 * Manages all interaction between the database and the rest of the application
 */

public class StreakDbHelper extends SQLiteOpenHelper {

    private static StreakDbHelper instance;

    private static final String DATABASE_NAME = "streak.db";

    private static final int DATABASE_VERSION = 1;

    private StreakDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized StreakDbHelper getInstance(Context context) {
        if (instance == null) {
            instance = new StreakDbHelper(context.getApplicationContext());
        }
        return instance;
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

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + StreakContract.StreakTable.TABLE_NAME);
        onCreate(db);
    }

    /**
     * Adds a new streak object to the database
     * @param newStreak the object to add
     * @return primary key of the new database entry
     */
    public long AddStreak(StreakObject newStreak, int viewPos) {

        SQLiteDatabase db = instance.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(StreakContract.StreakTable.STREAK_DESCRIPTION, newStreak.getStreakText());
        values.put(StreakContract.StreakTable.STREAK_DURATION, newStreak.getStreakDuration());
        values.put(StreakContract.StreakTable.STREAK_IS_PRIORITY, newStreak.getStreakIsPriority());
        values.put(StreakContract.StreakTable.STREAK_VIEW_INDEX, viewPos);

        return db.insert(StreakContract.StreakTable.TABLE_NAME, null, values);
    }

    /**
     * Updates the database to changes made to a streak
     * @param editedStreak The streak to grab the updated values from
     * @param whatToUpdate Defines what specific values to update from the object
     */
    public void UpdateStreakValues(StreakObject editedStreak, int whatToUpdate) {

        SQLiteDatabase db = instance.getWritableDatabase();

        ContentValues values = new ContentValues();

        switch(whatToUpdate) {
            case (HomeActivity.UPDATE_TEXT):
                values.put(StreakContract.StreakTable.STREAK_DESCRIPTION, editedStreak.getStreakText());
                break;
            case (HomeActivity.UPDATE_IS_PRIORITY):
                values.put(StreakContract.StreakTable.STREAK_IS_PRIORITY, editedStreak.getStreakIsPriority());
                break;
            default:
                Log.d("Er", "Invalid call to UpdateStreak");
                return;
        }

        String selection = StreakContract.StreakTable._ID + " = ?";
        String[] selectionArgs = { Long.toString(editedStreak.getStreakId()) };

        int val = db.update(
                StreakContract.StreakTable.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );
        Log.d("boogie", "value is " + Integer.toString(val));
    }

    /**
     * Deletes a streak entry from the database
     * @param streakToDelete The streak object to delete
     */
    public void DeleteStreak(StreakObject streakToDelete) {
        SQLiteDatabase db = instance.getWritableDatabase();

        String selection = StreakContract.StreakTable._ID + " = ?";
        String[] selectionArgs = {Long.toString(streakToDelete.getStreakId())};

        db.delete(StreakContract.StreakTable.TABLE_NAME, selection, selectionArgs);
    }

    /**
     * Grabs all the existing streaks in the database
     * @param streakList The list of streaks to occupy with database entries
     */
    public void GetAllStreaks(List<StreakObject> streakList) {
        SQLiteDatabase db = instance.getReadableDatabase();

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
            //boolean streakPriority = (cursor.getInt(cursor.getColumnIndexOrThrow(StreakContract.StreakTable.STREAK_IS_PRIORITY)) != 0);
            StreakObject ob = new StreakObject(streakText, streakDuration);
            streakList.add(ob);
            ob.setStreakId(streakId);
        }
        cursor.close();
    }

    /**
     * Updates the order of any moved streak cards in the view
     * @param movedStreaks The list of all streaks that need their database entries updated
     * @param streaksNewPositions The corresponding list of changed positions to update with
     */
    public void UpdateEntriesOrder(List<StreakObject> movedStreaks, List<Integer> streaksNewPositions) {
        SQLiteDatabase db = instance.getWritableDatabase();

        ContentValues values;

        for (int i = 0; i < movedStreaks.size(); ++i) {
            values = new ContentValues();

            values.put(StreakContract.StreakTable.STREAK_VIEW_INDEX, streaksNewPositions.get(i));

            String selection = StreakContract.StreakTable._ID + " = ?";
            String[] selectionArgs = { Long.toString(movedStreaks.get(i).getStreakId()) };

            db.update(
                    StreakContract.StreakTable.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs
            );
        }

    }

    /**
     * Get a specific streak object
     * @param streakUniqueId The primary key of the streak to get
     * @return The streak object requested for
     */
    public StreakObject GetEntry(long streakUniqueId) {
        SQLiteDatabase db = instance.getReadableDatabase();

        String[] projection = {
                StreakContract.StreakTable._ID,
                StreakContract.StreakTable.STREAK_DESCRIPTION,
                StreakContract.StreakTable.STREAK_DURATION,
                StreakContract.StreakTable.STREAK_IS_PRIORITY
        };

        String selection = StreakContract.StreakTable._ID + " = ?";
        String[] selectionArgs = { Long.toString(streakUniqueId) };

        Cursor cursor = db.query(
                StreakContract.StreakTable.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        StreakObject queriedEntry = null;

        while(cursor.moveToNext()) {
            long streakId = cursor.getLong(cursor.getColumnIndexOrThrow((StreakContract.StreakTable._ID)));
            String streakText = cursor.getString(cursor.getColumnIndexOrThrow(StreakContract.StreakTable.STREAK_DESCRIPTION));
            int streakDuration = cursor.getInt(cursor.getColumnIndexOrThrow(StreakContract.StreakTable.STREAK_DURATION));
            boolean streakPriority = (cursor.getInt(cursor.getColumnIndexOrThrow(StreakContract.StreakTable.STREAK_IS_PRIORITY)) != 0);

            queriedEntry = new StreakObject(streakText, streakDuration);
            queriedEntry.setStreakIsPriority(streakPriority);
            queriedEntry.setStreakId(streakId);
        }
        cursor.close();
        return queriedEntry;
    }

    /**
     * Increments saved streak duration into database then re-saves it
     * @param streakObject The streak object to increment the duration of
     */
    public void IncrementStreak(StreakObject streakObject) {
        SQLiteDatabase db = instance.getWritableDatabase();

        ContentValues values;

        values = new ContentValues();

        values.put(StreakContract.StreakTable.STREAK_DURATION, streakObject.getStreakDuration() + 1);

        String selection = StreakContract.StreakTable._ID + " = ?";
        String[] selectionArgs = { Long.toString(streakObject.getStreakId()) };

        db.update(
                StreakContract.StreakTable.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );
    }
}
