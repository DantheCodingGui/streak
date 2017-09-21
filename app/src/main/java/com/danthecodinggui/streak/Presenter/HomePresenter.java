package com.danthecodinggui.streak.Presenter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;

import com.danthecodinggui.streak.Data.Model;
import com.danthecodinggui.streak.Data.Modelable;
import com.danthecodinggui.streak.Data.StreakObject;
import com.danthecodinggui.streak.R;
import com.danthecodinggui.streak.View.EditStreakActivity;
import com.danthecodinggui.streak.View.HomeActivity;
import com.danthecodinggui.streak.View.Viewable;

import java.util.ArrayList;
import java.util.List;

/**
 * Presenter in MVP for HomeActivity view
 */
public class HomePresenter {

    private Modelable model;
    private Viewable view;

    public HomePresenter(Viewable view) {
        this.view = view;
        this.model = Model.getInstance(view.getActivityContext());
    }

    /**
     * Gets all streaks from data model
     * @param streakList The list to occupy with the model data
     * @return The occupied list
     */
    public List<StreakObject> getModelData(List<StreakObject> streakList) {
        model.GetAllStreaks(streakList);
        return streakList;
    }

    /**
     * Forwards view request to delete a specific streak from the model
     * @param streakToDelete The streak object to remove from the model
     */
    public void DeleteStreak(StreakObject streakToDelete) {
        model.DeleteStreak(streakToDelete);
    }

    /**
     * Compares streak list before and after move, checks which items have moved, and tells the
     * model to update those items
     * @param streakListBeforeMove The streak list before a drag and drop gesture was initiated
     * @param streakList The streak list after the item was dropped
     */
    public void UpdateMovedList(List<StreakObject> streakListBeforeMove, List<StreakObject> streakList) {

        List<StreakObject> movedStreaks = new ArrayList<>();
        List<Integer> movedStreaksViewIds = new ArrayList<>();
        StreakObject movedStreak;

        for (int i = 0; i < streakList.size(); ++i) {
            if (streakListBeforeMove.get(i).getStreakId() != (movedStreak = streakList.get(i)).getStreakId()) {
                movedStreaks.add(movedStreak);
                movedStreaksViewIds.add(i);
            }
        }
        model.UpdateStreaksOrder(movedStreaks, movedStreaksViewIds);
    }

    /**
     * Starts edit streak activity upon streak card clicked
     * @param itemView The card view clicked
     * @param streakToEdit The streak object associated with that card
     * @param viewPos The position of the view in the RecyclerView
     */
    public void EditStreak(View itemView, StreakObject streakToEdit, int viewPos) {

        Intent editStreak = new Intent((HomeActivity)view, EditStreakActivity.class);
        editStreak.putExtra("viewId", viewPos);
        editStreak.putExtra("streakId", streakToEdit.getStreakId());
        editStreak.putExtra("function", EditStreakActivity.EDIT_STREAK);

        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation((HomeActivity)this.view, itemView, view.getStringResource(R.string.transition_edit_streak));
        ActivityCompat.startActivityForResult((HomeActivity)view, editStreak, HomeActivity.EDIT_STREAK, options.toBundle());
    }

    /**
     * Get the layout manager type stored in the model
     * @param context The application context needed by the model to use SharedPreferences
     * @return A boolean value determining if the layout manager is a list or grid format
     */
    public boolean getListLayoutManager(Context context) {
        return model.GetListViewType(context, HomeActivity.RECYCLERVIEW_STAGGERED_GRID_LAYOUT_MANAGER);
    }

    /**
     * Save the layout manager to the model if it changes
     * @param context The application context needed by the model to use SharedPreferences
     * @param type The new layout manager
     */
    public void SaveListLayoutManager(Context context, boolean type) {
        model.SaveListViewType(context, type);
    }

    /**
     * Increments the saved duration value of a streak object in the model
     * @param streakObject The streak object to implement
     */
    public void IncrementStreak(StreakObject streakObject) {
        model.IncrementStreak(streakObject);
    }

    /**
     * Breaks the streak, resetting the duration value to 0
     * @param streakObject
     */
    public void BreakStreak(StreakObject streakObject) {
        model.BreakStreak(streakObject);
    }
}
