package com.danthecodinggui.streak.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.danthecodinggui.streak.Activities.Util.ItemTouchHelperAdapter;
import com.danthecodinggui.streak.Activities.Util.ItemTouchHelperViewHolder;
import com.danthecodinggui.streak.Activities.Util.SimpleItemTouchHelperCallback;
import com.danthecodinggui.streak.Database.StreakDbHelper;
import com.danthecodinggui.streak.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.danthecodinggui.streak.R.id.streak_card_view;

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

    private List<StreakObject> streakList;

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

        rcAdapter = new StreakRecyclerViewAdapter(streakData);
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
                newStreak.putExtra("listSize", streakList.size());
                newStreak.putExtra("function", EditStreakActivity.ADD_STREAK);
                startActivityForResult(newStreak, ADD_STREAK);
                return true;
            case R.id.home_action_bar_remove:
                rcAdapter.notifyItemRemoved(streakList.size() - 1);
                StreakObject deletedStreak = streakList.remove(streakList.size() - 1);

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
        streakList = new ArrayList<>();

        StreakDbHelper sDbHelper = StreakDbHelper.getInstance(this);
        sDbHelper.GetAllStreaks(streakList);

        return streakList;
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

                StreakObject newStreak = new StreakObject(streakText, streakDuration, streakList.size());
                streakList.add(newStreak);
                newStreak.setStreakId(streakId);
                rcAdapter.notifyItemInserted(streakList.size() - 1);
                break;

            case EDIT_STREAK:
                boolean streakHasChanged = data.getBooleanExtra("hasStreakChanged", false);
                if (!streakHasChanged)
                    break;
                Log.d("boogie", "value received was true");
                streakText = data.getStringExtra("editedStreak");
                int streakPosition = data.getIntExtra("editedStreakPosition", -1);
                //boolean streakIsPriority = data.getBooleanExtra("newStreakIsPriority", false);

                streakList.get(streakPosition).setStreakText(streakText);
                rcAdapter.notifyItemChanged(streakPosition);
                break;
            default:
                Log.d("Error", "Invalid return to HomeActivity");
                break;
        }
    }

    /**
     * Adapter class linking streak data to user interface
     */
    class StreakRecyclerViewAdapter
            extends RecyclerView.Adapter<StreakRecyclerViewAdapter.StreakViewHolder>
            implements ItemTouchHelperAdapter {

        /**
         * @param streaks Initialise data if some already exists
         */
        StreakRecyclerViewAdapter(List<StreakObject> streaks) {
            streakList = streaks;
        }

        /**
         * Inflates the view card Xml into the RecyclerView
         * @param viewGroup The parent ViewGroup that the new view will be added to
         * @param viewType Needed if there exists different behaviour depending on view type
         * @return New ViewHolder with inflated view
         */
        @Override
        public StreakViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

            View layoutView = LayoutInflater.
                    from(viewGroup.getContext()).
                    inflate(R.layout.streak_card, viewGroup, false);
            return new StreakViewHolder(layoutView);
        }

        /**
         * Copy the streak object data to a ViewHolder
         * @param holder The ViewHolder to copy the data to
         * @param pos Specifies position of streak object in list
         */
        @Override
        public void onBindViewHolder(StreakViewHolder holder, int pos) {
            StreakObject ob = streakList.get(pos);
            holder.streakText.setText(ob.getStreakText());
        }

        /** Needed for RecyclerViewAdapter implementation
         * returns size of data model
         * @return Size of list to be displayed in RecyclerView
         */
        @Override
        public int getItemCount() {
            return streakList.size();
        }

        /**
         * Deletes streak object when ViewHolder swiped
         * @param position The position of the streak to delete in the list
         */
        @Override
        public void onItemDismiss(int position) {
            StreakObject ob = streakList.remove(position);
            notifyItemRemoved(position);

            StreakDbHelper sDbHelper = StreakDbHelper.getInstance(getApplicationContext());
            sDbHelper.DeleteStreak(ob);
        }

        /**
         * Handles swapping streak views, updating streak object view attributes and updates database
         * entries
         * @param fromPosition
         * @param toPosition
         * @return Has the movement been handled
         */
        @Override
        public boolean onItemMove(int fromPosition, int toPosition) {
            int i = fromPosition;
            int temp;
            StreakDbHelper sDbHelper = StreakDbHelper.getInstance(getApplicationContext());
            if (fromPosition < toPosition) {
                for (; i < toPosition; ++i) {
                    sDbHelper.SwapListViewIndexes(streakList.get(i), streakList.get(i + 1));

                    temp = streakList.get(i).getStreakViewIndex();
                    streakList.get(i).setStreakViewIndex(streakList.get(i + 1).getStreakViewIndex());
                    streakList.get(i + 1).setStreakViewIndex(temp);

                    Collections.swap(streakList, i, i + 1);
                }
            }
            else {
                for (; i > toPosition; --i) {
                    sDbHelper.SwapListViewIndexes(streakList.get(i), streakList.get(i - 1));

                    temp = streakList.get(i).getStreakViewIndex();
                    streakList.get(i).setStreakViewIndex(streakList.get(i - 1).getStreakViewIndex());
                    streakList.get(i - 1).setStreakViewIndex(temp);

                    Collections.swap(streakList, i, i - 1);
                }
            }
            notifyItemMoved(fromPosition, toPosition);
            return true;
        }

        /**
         * Holds the streak views & what RecyclerView uses rather than individual views themselves
         */
        class StreakViewHolder extends RecyclerView.ViewHolder
                implements View.OnClickListener, View.OnLongClickListener, ItemTouchHelperViewHolder {

            TextView streakText;

            StreakViewHolder(View view) {
                super(view);
                streakText = (TextView)itemView.findViewById(R.id.streak_text);

                view.setOnClickListener(this);
            }

            /**
             * Opens edit streak activiy
             * @param view
             */
            @Override
            public void onClick(View view) {
                //FOR EDITING STREAK

                Intent editStreak = new Intent(getApplicationContext(), EditStreakActivity.class);
                editStreak.putExtra("streakText", streakText.getText());
                view.setOnLongClickListener(this);
                editStreak.putExtra("viewId", getAdapterPosition());
                editStreak.putExtra("function", EditStreakActivity.EDIT_STREAK);

                ActivityOptionsCompat options =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(getParent(), view, getParent().getString(R.string.transition_edit_streak));

                ActivityCompat.startActivityForResult(getParent(), editStreak, HomeActivity.EDIT_STREAK, options.toBundle());
            }

            /**
             * Switches app bar to other version with options such as delete etc.
             * @param view
             * @return Has long click been handled
             */
            @Override
            public boolean onLongClick(View view) {

                //StreakDbHelper sDbHelper = StreakDbHelper.getInstance(linkedActivity);
                Log.d("boogie", "Long click detected");

                return true;
            }

            /**
             * Changes appearance of view when start drag and drop movement
             */
            @Override
            public void onItemSelected() {
                //CHANGE APPEARANCE OF PICKED UP CARDS HERE
                CardView card = (CardView)itemView.findViewById(streak_card_view);
                card.setCardBackgroundColor(Color.rgb(121, 121, 121));
                itemView.findViewById(R.id.card_content_container).setBackgroundColor(Color.LTGRAY);
                itemView.setRotation(5);
            }

            /**
             * Changes view appearance back to default when drag and drop movement ended
             */
            @Override
            public void onItemClear() {
                CardView card = (CardView)itemView.findViewById(streak_card_view);
                card.setCardBackgroundColor(Color.WHITE);
                itemView.findViewById(R.id.card_content_container).setBackgroundColor(Color.TRANSPARENT);
                itemView.setRotation(0);
            }
        }
    }
}