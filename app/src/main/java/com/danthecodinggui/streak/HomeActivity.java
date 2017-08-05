package com.danthecodinggui.streak;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;

import com.danthecodinggui.streak.Database.StreakDbHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * The home screen containing all current streaks in card form, which can be displayed in a number
 * of ways
 */
public class HomeActivity extends AppCompatActivity {

    public static final int ADD_STREAK = 1;

    private List<StreakObject> listViewItems;

    private StreakRecyclerViewAdapter rcAdapter;

    /**
     * Method called upon opening the application for the first time
     * @param savedInstanceState Optional state information passed to the activity to restore it to
     *                           a previous state when it was last used
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView streakRecycler = (RecyclerView)findViewById(R.id.home_container);

        StaggeredGridLayoutManager streakLayoutManager = new StaggeredGridLayoutManager(2, 1);
        streakRecycler.setLayoutManager(streakLayoutManager);

        List<StreakObject> streaks = getListItemData();

        rcAdapter = new StreakRecyclerViewAdapter(streaks);
        streakRecycler.setAdapter(rcAdapter);
    }

    /**
     * Inflate custom app bar
     * @param menu The menu object for the activity
     * @return Boolean defining whether or not app bar menu should be displayed
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_action_bar, menu);
        return true;
    }

    /**
     * Handles app bar button presses
     * @param item The button pressed in the app bar
     * @return Boolean defining whether or not button press has been handled
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            case R.id.home_action_bar_add:
                Intent newStreak = new Intent(this, EditStreak.class);
                startActivityForResult(newStreak, ADD_STREAK);
                return true;
            case R.id.home_action_bar_remove:
                listViewItems.remove(listViewItems.size() - 1);
                rcAdapter.notifyItemRemoved(listViewItems.size());
                return true;
            case R.id.home_action_bar_overflow:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Checks for existing streaks saved in file system, if they exist then load into list. Called
     * whenever home activity comes to foreground
     * @return List containing initial streaks
     */
    private List<StreakObject> getListItemData(){
        listViewItems = new ArrayList<>();

        StreakDbHelper sDbHelper = new StreakDbHelper(this);

        sDbHelper.GetAllStreaks(listViewItems);

        return listViewItems;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch(requestCode) {
            case ADD_STREAK:
                listViewItems.add(0, new StreakObject(data.getStringExtra("newStreak")));
                rcAdapter.notifyItemInserted(0);
                break;
        }
    }
}