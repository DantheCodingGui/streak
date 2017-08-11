package com.danthecodinggui.streak.View;

import android.content.Context;
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

import com.danthecodinggui.streak.View.ItemTouchHelper.ItemTouchHelperAdapter;
import com.danthecodinggui.streak.View.ItemTouchHelper.ItemTouchHelperViewHolder;
import com.danthecodinggui.streak.View.ItemTouchHelper.SimpleItemTouchHelperCallback;
import com.danthecodinggui.streak.Data.StreakObject;
import com.danthecodinggui.streak.Presenter.HomePresenter;
import com.danthecodinggui.streak.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.R.attr.data;
import static com.danthecodinggui.streak.R.id.streak_card_view;

/**
 * The home screen containing all current streaks in card form, which can be displayed in a number
 * of ways
 */
public class HomeActivity extends AppCompatActivity implements Viewable {

    public static final int ADD_STREAK = 0;
    public static final int EDIT_STREAK = 1;

    public static final int UPDATE_TEXT = 0;
    public static final int UPDATE_IS_PRIORITY = 1;

    //public static final String LIST_SIZE = "LIST_SIZE";

    private List<StreakObject> streakList;
    private List<StreakObject> streakListBeforeMove;

    private HomePresenter presenter;

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

        presenter = new HomePresenter(this);
        streakList = presenter.getModelData(new ArrayList<StreakObject>());

        RecyclerView streakRecycler = (RecyclerView)findViewById(R.id.home_container);

        StaggeredGridLayoutManager streakLayoutManager = new StaggeredGridLayoutManager(2, 1);
        streakRecycler.setLayoutManager(streakLayoutManager);

        rcAdapter = new StreakRecyclerViewAdapter(streakList);
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
                if (streakList.size() > 0) {
                    rcAdapter.notifyItemRemoved(streakList.size() - 1);
                    StreakObject deletedStreak = streakList.remove(streakList.size() - 1);

                    presenter.DeleteStreak(deletedStreak);
                }
                return true;
            case R.id.home_action_bar_overflow:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String streakText;
        switch(requestCode) {
            case ADD_STREAK:
                if (resultCode == RESULT_OK) {
                    long streakId = data.getLongExtra("newStreakId", 0);
                    streakText = data.getStringExtra("newStreak");
                    int streakDuration = data.getIntExtra("newStreakDuration", 0);
                    //boolean streakIsPriority = data.getBooleanExtra("newStreakIsPriority", false);

                    StreakObject newStreak = new StreakObject(streakText, streakDuration);
                    streakList.add(newStreak);
                    newStreak.setStreakId(streakId);
                    rcAdapter.notifyItemInserted(streakList.size() - 1);
                }
                break;

            case EDIT_STREAK:
                if (resultCode == RESULT_OK) {
                    boolean streakHasChanged = data.getBooleanExtra("hasStreakChanged", false);
                    if (!streakHasChanged)
                        break;
                    streakText = data.getStringExtra("editedStreak");
                    int streakPosition = data.getIntExtra("editedStreakPosition", -1);
                    //boolean streakIsPriority = data.getBooleanExtra("newStreakIsPriority", false);

                    streakList.get(streakPosition).setStreakText(streakText);
                    rcAdapter.notifyItemChanged(streakPosition);
                }
                break;
            default:
                Log.d("Error", "Invalid return to HomeActivity");
                break;
        }
    }

    @Override
    public Context getActivityContext() {
        return this;
    }

    @Override
    public String getStringResource(int resource) {
        return getString(resource);
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
            StreakObject streakToDelete = streakList.remove(position);
            notifyItemRemoved(position);

            presenter.DeleteStreak(streakToDelete);
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
            if (fromPosition < toPosition) {
                for (; i < toPosition; ++i) {
                    Collections.swap(streakList, i, i + 1);
                }
            }
            else {
                for (; i > toPosition; --i) {
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
                view.setOnLongClickListener(this);
            }

            /**
             * Opens edit streak activity
             * @param view
             */
            @Override
            public void onClick(View view) {
                int pos = getAdapterPosition();
                presenter.EditStreak(view, streakList.get(pos), pos);
            }

            /**
             * Switches app bar to other version with options such as delete etc.
             * @param view
             * @return Has long click been handled
             */
            @Override
            public boolean onLongClick(View view) {
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

                streakListBeforeMove = new ArrayList<>(streakList);
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

                presenter.UpdateMovedList(streakListBeforeMove, streakList);
                streakListBeforeMove.clear();
            }
        }
    }
}