package com.danthecodinggui.streak.Presenter;

import com.danthecodinggui.streak.Data.Model;
import com.danthecodinggui.streak.Data.StreakObject;
import com.danthecodinggui.streak.View.Viewable;

/**
 * Created by Dan on 10/08/2017.
 */

public class EditPresenter {

    private Model model;
    private Viewable view;

    public EditPresenter(Viewable view) {
        this.view = view;
        this.model = Model.getInstance(view.getActivityContext());
    }

    public long AddStreak(StreakObject streakToAdd, int viewPos) {
        return model.AddStreak(streakToAdd, viewPos);
    }

    public void UpdateStreak(StreakObject streakObject, int whatToUpdate) {
        model.UpdateStreak(streakObject, whatToUpdate);
    }
}
