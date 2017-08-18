package com.danthecodinggui.streak.Data;

import android.content.Context;

import java.util.List;

/**
 * Allows presenters in MVP to code to this interface
 */

public interface Modelable {
    long AddStreak(StreakObject entryToAdd, int viewPosition);
    void DeleteStreak(StreakObject entryToDelete);
    List<StreakObject> GetAllStreaks(List<StreakObject> entryList);
    void UpdateStreak(StreakObject streakObject, int whatToUpdate);
    void UpdateStreaksOrder(List<StreakObject> movedStreaks, List<Integer> movedStreaksViewPositions);
    StreakObject GetStreak(long streakUniqueId);
    void SaveListViewType(Context context, boolean type);
    boolean GetListViewType(Context context, boolean defaultValue);
    void IncrementStreak(StreakObject streakObject);
}
