package com.danthecodinggui.streak.Data;

import android.content.Context;
import android.content.SharedPreferences;

import com.danthecodinggui.streak.Data.Database.StreakDbHelper;
import com.danthecodinggui.streak.R;

import java.util.List;

/**
 * Holds all data for the Application, also a singleton so can be shared across whole app
 */
public class Model implements Modelable {

    private static Model model;

    private StreakDbHelper database;
    private SharedPreferences sharedPref;

    private Model(Context context) {
        this.database = StreakDbHelper.getInstance(context);
    }

    @Override
    public void UpdateStreak(StreakObject streakObject, int whatToUpdate) {
        database.UpdateStreakValues(streakObject, whatToUpdate);
    }

    /**
     * Get singleton instance of Model object
     * @param context The application context needed to create the model
     * @return A model object, either a new instance or the existing one
     */
    public static synchronized Model getInstance(Context context) {
        if (model == null) {
            model = new Model(context);
        }
        return model;
    }

    @Override
    public long AddStreak(StreakObject entryToAdd, int viewPosition) {
        return database.AddStreak(entryToAdd, viewPosition);
    }

    @Override
    public void DeleteStreak(StreakObject entryToDelete) {
        database.DeleteStreak(entryToDelete);
    }

    @Override
    public void UpdateStreaksOrder(List<StreakObject> movedStreaks, List<Integer> movedStreaksViewPositions) {
        database.UpdateEntriesOrder(movedStreaks, movedStreaksViewPositions);
    }

    @Override
    public StreakObject GetStreak(long streakUniqueId) {
        return database.GetEntry(streakUniqueId);
    }

    @Override
    public List<StreakObject> GetAllStreaks(List<StreakObject> entryList) {
        database.GetAllStreaks(entryList);
        return entryList;
    }

    @Override
    public boolean GetListViewType(Context context, boolean defaultValue) {
        sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        return sharedPref.getBoolean(context.getString(R.string.recyclerview_layout_manager), defaultValue);
    }

    @Override
    public void IncrementStreak(StreakObject streakObject) {
        database.IncrementStreak(streakObject);
    }

    @Override
    public void SaveListViewType(Context context, boolean type) {
        sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(context.getString(R.string.recyclerview_layout_manager), type);
        editor.apply();
    }
}
