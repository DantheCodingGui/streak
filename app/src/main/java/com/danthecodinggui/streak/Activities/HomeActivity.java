package com.danthecodinggui.streak.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.danthecodinggui.streak.Activities.Util.SimpleItemTouchHelperCallback;
import com.danthecodinggui.streak.Database.StreakDbHelper;
import com.danthecodinggui.streak.R;

import java.util.ArrayList;
import java.util.List;

/**
 * The home screen containing all current streaks in card form, which can be displayed in a number
 * of ways
 */
public class HomeActivity extends AppCompatActivity {

    public static final int ADD_STREAK = 0;
    public static final int EDIT_STREAK = 1;

    public static final int UPDATE_TEXT = 0;
    public static final int UPDATE_IS_PRIORITY = 1;

    //public static final String LIST_SIZE = "LIST_SIZE";

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

        List<StreakObject> streakData = getListItemData();

        rcAdapter = new StreakRecyclerViewAdapter(streakData, this);
        streakRecycler.setAdapter(rcAdapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(rcAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(streakRecycler);
    }

    /**
     * Inflate central app bar
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
                Intent newStreak = new Intent(this, EditStreakActivity.class);
                newStreak.putExtra("listSize", listViewItems.size());
                newStreak.putExtra("function", EditStreakActivity.ADD_STREAK);
                startActivityForResult(newStreak, ADD_STREAK);
                return true;
            case R.id.home_action_bar_remove:
                rcAdapter.notifyItemRemoved(listViewItems.size() - 1);
                StreakObject deletedStreak = listViewItems.remove(listViewItems.size() - 1);

                StreakDbHelper sDbHelper = StreakDbHelper.getInstance(this);
                sDbHelper.DeleteStreak(deletedStreak);

                return true;
            case R.id.home_action_bar_overflow:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Checks for existing streaks saved in database, if they exist then load into list.
     * @return List containing existing streaks
     */
    private List<StreakObject> getListItemData(){
        listViewItems = new ArrayList<>();

        StreakDbHelper sDbHelper = StreakDbHelper.getInstance(this);
        sDbHelper.GetAllStreaks(listViewItems);

        return listViewItems;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String streakText;
        switch(requestCode) {
            case ADD_STREAK:
                long streakId = data.getLongExtra("newStreakId", 0);
                streakText = data.getStringExtra("newStreak");
                int streakDuration = data.getIntExtra("newStreakDuration", 0);
                //boolean streakIsPriority = data.getBooleanExtra("newStreakIsPriority", false);

                StreakObject newStreak = new StreakObject(streakText, streakDuration, listViewItems.size());
                listViewItems.add(newStreak);
                newStreak.setStreakId(streakId);
                rcAdapter.notifyItemInserted(listViewItems.size() - 1);
                break;

            case EDIT_STREAK:
                boolean streakHasChanged = data.getBooleanExtra("hasStreakChanged", false);
                if (!streakHasChanged)
                    break;
                Log.d("boogie", "value received was true");
                streakText = data.getStringExtra("editedStreak");
                int streakPosition = data.getIntExtra("editedStreakPosition", -1);
                //boolean streakIsPriority = data.getBooleanExtra("newStreakIsPriority", false);

                listViewItems.get(streakPosition).setStreakText(streakText);
                rcAdapter.notifyItemChanged(streakPosition);
                break;
            default:
                Log.d("Error", "Invalid return to HomeActivity");
                break;
        }
    }
}