package com.danthecodinggui.streak.Presenter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.util.Log;
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
 * Presenter in MVP
 * Created by Dan on 10/08/2017.
 */

public class HomePresenter {

    private Modelable model;
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

    public void UpdateMovedList(List<StreakObject> streakListBeforeMove, List<StreakObject> streakList) {
        //check which objects have changed position
        //send new list of streakobjects with changed positions to database method, remove swap one and old implementation

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

    public void EditStreak(View itemView, StreakObject streakToEdit, int viewPos) {

        Intent editStreak = new Intent((HomeActivity)view, EditStreakActivity.class);
        editStreak.putExtra("viewId", viewPos);
        editStreak.putExtra("streakId", streakToEdit.getStreakId());
        editStreak.putExtra("function", EditStreakActivity.EDIT_STREAK);

        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation((HomeActivity)this.view, itemView, view.getStringResource(R.string.transition_edit_streak));
        ActivityCompat.startActivityForResult((HomeActivity)view, editStreak, HomeActivity.EDIT_STREAK, options.toBundle());
    }

    public boolean getListLayoutManager(Context context) {
        return model.GetListViewType(context, HomeActivity.RECYCLERVIEW_STAGGERED_GRID_LAYOUT_MANAGER);
    }

    public void SaveListLayoutManager(Context context, boolean type) {
        model.SaveListViewType(context, type);
    }

    public void IncrementStreak(StreakObject streakObject) {
        model.IncrementStreak(streakObject);
    }
}
