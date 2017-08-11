package com.danthecodinggui.streak.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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

    /**
     * Called when changes made to database structure, look into later
     */
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

        db.update(
                StreakContract.StreakTable.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );
    }

    /**
     * Swaps the view index values of two database records
     * @param firstStreak
     * @param secondStreak
     */
    public void SwapListViewIndexes(StreakObject firstStreak, int firstViewPos, StreakObject secondStreak, int secondViewPos) {

        SQLiteDatabase db = instance.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(StreakContract.StreakTable.STREAK_VIEW_INDEX, secondViewPos);

        String selection = StreakContract.StreakTable._ID + " = ?";
        String[] selectionArgs = { Long.toString(firstStreak.getStreakId()) };

        db.update(
                StreakContract.StreakTable.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );

        values = new ContentValues();

        values.put(StreakContract.StreakTable.STREAK_VIEW_INDEX, firstViewPos);

        selection = StreakContract.StreakTable._ID + " = ?";
        selectionArgs = new String[]{ Long.toString(secondStreak.getStreakId()) };

        db.update(
                StreakContract.StreakTable.TABLE_NAME,
                values,
                selection,
                selectionArgs
        );
    }

    /**
     * Deletes a streak entry from the database
     * @param streakToDelete
     */
    public void DeleteStreak(StreakObject streakToDelete) {
        SQLiteDatabase db = instance.getWritableDatabase();

        String selection = StreakContract.StreakTable._ID + " = ?";
        String[] selectionArgs = {Long.toString(streakToDelete.getStreakId())};

        db.delete(StreakContract.StreakTable.TABLE_NAME, selection, selectionArgs);
    }

    /**
     * Grabs all the existing streaks in the database
     * @param recyclerViewItems
     */
    public void GetAllStreaks(List<StreakObject> recyclerViewItems) {
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
            boolean streakPriority = (cursor.getInt(cursor.getColumnIndexOrThrow(StreakContract.StreakTable.STREAK_IS_PRIORITY)) != 0);
            StreakObject ob = new StreakObject(streakText, streakDuration);
            recyclerViewItems.add(ob);
            ob.setStreakId(streakId);
        }
        cursor.close();
    }

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
}
