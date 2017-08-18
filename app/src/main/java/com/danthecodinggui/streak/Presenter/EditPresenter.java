package com.danthecodinggui.streak.Presenter;

import com.danthecodinggui.streak.Data.Model;
import com.danthecodinggui.streak.Data.Modelable;
import com.danthecodinggui.streak.Data.StreakObject;
import com.danthecodinggui.streak.View.Viewable;

/**
 * Presenter in MVP for the EditStreakActivity view
 */

public class EditPresenter {

    private Modelable model;
    //private Viewable view;
    //not used at the moment

    public EditPresenter(Viewable view) {
        //this.view = view;
        this.model = Model.getInstance(view.getActivityContext());
    }

    /**
     * Add a new streak to the model
     * @param streakToAdd The streak to add
     * @param viewPos The position of the streak in the RecyclerView
     * @return The primary key of the new streak in the database
     */
    public long AddStreak(StreakObject streakToAdd, int viewPos) {
        return model.AddStreak(streakToAdd, viewPos);
    }

    /**
     * Forwards the streakObject to the model to update
     * @param streakObject The streak to update
     * @param whatToUpdate Defines what values to update
     */
    public void UpdateStreak(StreakObject streakObject, int whatToUpdate) {
        model.UpdateStreak(streakObject, whatToUpdate);
    }

    /**
     * Gets the streak data to occupy the view from the saved version in the model
     * @param streakUniqueId The primary key of the streak in the model database
     * @return The streak object found in the model
     */
    public StreakObject GetStreak(long streakUniqueId) {
        return model.GetStreak(streakUniqueId);
    }
}
