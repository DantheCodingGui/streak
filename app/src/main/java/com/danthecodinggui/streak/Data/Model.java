package com.danthecodinggui.streak.Data;

import android.content.Context;

import java.util.List;

/**
 * Created by Dan on 10/08/2017.
 */

public class Model implements Modelable {

    private static Model model;

    private StreakDbHelper database;

    private Model(Context context) {
        this.database = StreakDbHelper.getInstance(context);
    }

    @Override
    public void UpdateStreak(StreakObject streakObject, int whatToUpdate) {
        database.UpdateStreakValues(streakObject, whatToUpdate);
    }

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
    public void SwapStreaks(StreakObject firstStreak, int firstViewPos, StreakObject secondStreak, int secondViewPos) {
        database.SwapListViewIndexes(firstStreak, firstViewPos, secondStreak, secondViewPos);
    }

    @Override
    public void UpdateStreaksOrder(List<StreakObject> movedStreaks, List<Integer> movedStreaksViewPositions) {
        database.UpdateEntriesOrder(movedStreaks, movedStreaksViewPositions);
    }

    @Override
    public List<StreakObject> GetAllStreaks(List<StreakObject> entryList) {
        database.GetAllStreaks(entryList);
        return entryList;
    }
}
