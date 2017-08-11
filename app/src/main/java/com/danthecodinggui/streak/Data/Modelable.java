package com.danthecodinggui.streak.Data;

import java.util.List;

/**
 * Created by Dan on 10/08/2017.
 */

public interface Modelable {
    long AddStreak(StreakObject entryToAdd, int viewPosition);
    void DeleteStreak(StreakObject entryToDelete);
    List<StreakObject> GetAllStreaks(List<StreakObject> entryList);
    void UpdateStreak(StreakObject streakObject, int whatToUpdate);
    void SwapStreaks(StreakObject firstStreak, int firstViewPos, StreakObject secondStreak, int secondViewPos);
}
