package com.danthecodinggui.streak.Data;

import android.content.Context;

import java.util.List;

import static android.R.attr.type;

/**
 * Created by Dan on 10/08/2017.
 */

public interface Modelable {
    long AddStreak(StreakObject entryToAdd, int viewPosition);
    void DeleteStreak(StreakObject entryToDelete);
    List<StreakObject> GetAllStreaks(List<StreakObject> entryList);
    void UpdateStreak(StreakObject streakObject, int whatToUpdate);
    void SwapStreaks(StreakObject firstStreak, int firstViewPos, StreakObject secondStreak, int secondViewPos);
    void UpdateStreaksOrder(List<StreakObject> movedStreaks, List<Integer> movedStreaksViewPositions);
    StreakObject GetStreak(long streakUniqueId);
    void SaveListViewType(Context context, boolean type);
    boolean GetListViewType(Context context, boolean defaultValue);
}
