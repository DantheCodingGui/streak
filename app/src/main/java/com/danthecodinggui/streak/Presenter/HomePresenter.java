package com.danthecodinggui.streak.Presenter;

import com.danthecodinggui.streak.Data.Model;
import com.danthecodinggui.streak.Data.StreakObject;
import com.danthecodinggui.streak.View.Viewable;

import java.util.List;

/**
 * Presenter in MVP
 * Created by Dan on 10/08/2017.
 */

public class HomePresenter {

    private Model model;
    private Viewable view;

    public HomePresenter(Viewable view) {
        this.view = view;
        this.model = Model.getInstance(view.getActivityContext());
    }

    public List<StreakObject> getModelData(List<StreakObject> streakList) {
        model.GetAllStreaks(streakList);
        return streakList;
    }

    public void DeleteStreak(StreakObject streakToDelete) {
        model.DeleteStreak(streakToDelete);
    }

    public void SwapStreaks(StreakObject firstStreak, int firstViewPos, StreakObject secondStreak, int secondViewPos) {
        model.SwapStreaks(firstStreak, firstViewPos, secondStreak, secondViewPos);
    }
}
