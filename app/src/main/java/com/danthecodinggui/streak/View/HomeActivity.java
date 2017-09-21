package com.danthecodinggui.streak.View;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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

/**
 * The home screen containing all current streaks in card form, which can be displayed in a number
 * of ways
 */
public class HomeActivity extends AppCompatActivity implements Viewable {

    public static final int ADD_STREAK = 0;
    public static final int EDIT_STREAK = 1;

    public static final int UPDATE_TEXT = 0;
    public static final int UPDATE_IS_PRIORITY = 1;

    public static final boolean RECYCLERVIEW_LINEAR_LAYOUT_MANAGER = false;
    public static final boolean RECYCLERVIEW_STAGGERED_GRID_LAYOUT_MANAGER = true;
    private boolean recyclerviewListType;

    //public static final String LIST_SIZE = "LIST_SIZE";

    private List<StreakObject> streakList;
    private List<StreakObject> streakListBeforeMove;

    private HomePresenter presenter;

    private RecyclerView streakRecycler;
    private StreakRecyclerViewAdapter rcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = new HomePresenter(this);
        streakList = presenter.getModelData(new ArrayList<StreakObject>());

        streakRecycler = (RecyclerView)findViewById(R.id.home_container);

        if (presenter.getListLayoutManager(this) == RECYCLERVIEW_STAGGERED_GRID_LAYOUT_MANAGER) {
            StaggeredGridLayoutManager streakLayoutManager = new StaggeredGridLayoutManager(2, 1);
            streakRecycler.setLayoutManager(streakLayoutManager);
            recyclerviewListType = RECYCLERVIEW_STAGGERED_GRID_LAYOUT_MANAGER;
        }
        else {
            LinearLayoutManager streakLayoutManager = new LinearLayoutManager(this);
            streakRecycler.setLayoutManager(streakLayoutManager);
            recyclerviewListType = RECYCLERVIEW_LINEAR_LAYOUT_MANAGER;
        }

        rcAdapter = new StreakRecyclerViewAdapter(streakList);
        streakRecycler.setAdapter(rcAdapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(rcAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(streakRecycler);

        Toolbar appBar = (Toolbar)findViewById(R.id.tbar_app_bar);
        setSupportActionBar(appBar);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_action_bar, menu);
        if (recyclerviewListType == RECYCLERVIEW_LINEAR_LAYOUT_MANAGER)
            menu.getItem(0).setIcon(R.drawable.staggered_grid_view_icon);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            case R.id.btn_swap_view:
                if (streakRecycler.getLayoutManager() instanceof StaggeredGridLayoutManager) {
                    streakRecycler.setLayoutManager(new LinearLayoutManager(this));
                    streakRecycler.setAdapter(rcAdapter);
                    item.setIcon(R.drawable.staggered_grid_view_icon);
                    presenter.SaveListLayoutManager(this, RECYCLERVIEW_LINEAR_LAYOUT_MANAGER);
                }
                else {
                    streakRecycler.setLayoutManager(new StaggeredGridLayoutManager(2, 1));
                    streakRecycler.setAdapter(rcAdapter);
                    item.setIcon(R.drawable.linear_view_icon);
                    presenter.SaveListLayoutManager(this, RECYCLERVIEW_STAGGERED_GRID_LAYOUT_MANAGER);
                }
                return true;
            case R.id.home_action_bar_overflow:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Opens the edit streak activity to add a new streak
     * @param view The floating action button clicked
     */
    public void AddStreak(View view) {
        Intent newStreak = new Intent(this, EditStreakActivity.class);
        newStreak.putExtra("listSize", streakList.size());
        newStreak.putExtra("function", EditStreakActivity.ADD_STREAK);
        startActivityForResult(newStreak, ADD_STREAK);
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

    //Interface methods so presenter can access activity based functionality
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


        StreakRecyclerViewAdapter(List<StreakObject> streaks) {
            streakList = streaks;
        }

        @Override
        public StreakViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

            View layoutView = LayoutInflater.
                    from(viewGroup.getContext()).
                    inflate(R.layout.streak_card, viewGroup, false);
            return new StreakViewHolder(layoutView);
        }

        @Override
        public void onBindViewHolder(StreakViewHolder holder, int pos) {
            holder.streakText.setText(streakList.get(pos).getStreakText());
            SetStreakDuration(holder, pos);
        }

        /**
         * Sets duration of streak Card text, function as code called twice
         * @param holder The ViewHolder containing the streak to change
         * @param pos The position of the ViewHolder in the RecyclerView
         */
        private void SetStreakDuration(StreakViewHolder holder, int pos) {
            int duration = streakList.get(pos).getStreakDuration();
            if (duration == 0) {
                holder.streakDuration.setText(getResources().getString(R.string.card_duration_init));
            }
            else {
                holder.streakDuration.setText(getResources().getString(R.string.card_duration, duration));
            }
        }

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
         * @param fromPosition Original Position
         * @param toPosition New Position
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
            TextView streakDuration;
            ImageButton incrementStreak;
            ImageButton breakStreak;

            int tempActionState;

            StreakViewHolder(View view) {
                super(view);
                streakText = (TextView)itemView.findViewById(R.id.txt_card_text);
                streakDuration = (TextView)itemView.findViewById(R.id.txt_card_duration);
                incrementStreak = (ImageButton)itemView.findViewById(R.id.ibtn_increment_streak);
                breakStreak = (ImageButton)itemView.findViewById(R.id.ibtn_break_streak);

                view.setOnClickListener(this);
                view.setOnLongClickListener(this);

                final StreakViewHolder temp = this;
                incrementStreak.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = getAdapterPosition();
                        presenter.IncrementStreak(streakList.get(pos));
                        streakList.get(pos).incrementStreakDuration();
                        SetStreakDuration(temp, pos);
                        //also need to edit view
                    }
                });
                breakStreak.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = getAdapterPosition();
                        presenter.BreakStreak(streakList.get(pos));
                        streakList.get(pos).resetStreakDuration();
                        SetStreakDuration(temp, pos);
                    }
                });
            }

            public void onClick(View view) {
                int pos = getAdapterPosition();
                presenter.EditStreak(view, streakList.get(pos), pos);
            }

            @Override
            public boolean onLongClick(View view) {
                return true;
            }

            @Override
            public void onItemSelected(int actionState) {
                //change appearance of card based on movement type
                if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                    CardView card = (CardView) itemView.findViewById(R.id.streak_card_view);
                    card.setCardBackgroundColor(ContextCompat.getColor(getActivityContext(),
                            R.color.card_drag_border));
                    itemView.findViewById(
                            R.id.card_content_container).setBackgroundColor(
                                    ContextCompat.getColor(getActivityContext()
                                            , R.color.card_drag_background));
                    itemView.setRotation(5);

                    //take snapshot of streaks list, used to compare to changed list later, to tell
                    // which streak objects have changed position
                    tempActionState = ItemTouchHelper.ACTION_STATE_DRAG;
                    streakListBeforeMove = new ArrayList<>(streakList);
                }
                else if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    CardView card = (CardView) itemView.findViewById(R.id.streak_card_view);
                    card.setCardBackgroundColor(ContextCompat.getColor(getActivityContext()
                            , R.color.card_swipe_border));
                    itemView.findViewById(R.id.card_content_container).setBackgroundColor(
                            ContextCompat.getColor(getActivityContext()
                                    , R.color.card_swipe_background));
                    tempActionState = ItemTouchHelper.ACTION_STATE_SWIPE;
                }
            }

            /**
             * Changes view appearance back to default when drag and drop movement ended
             */
            @Override
            public void onItemClear() {
                //reset card appearance
                CardView card = (CardView)itemView.findViewById(R.id.streak_card_view);
                card.setCardBackgroundColor(Color.WHITE);
                itemView.findViewById(R.id.card_content_container)
                        .setBackgroundColor(Color.TRANSPARENT);
                itemView.setRotation(0);

                //only update database view values when item dropped
                if (tempActionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                    presenter.UpdateMovedList(streakListBeforeMove, streakList);
                    streakListBeforeMove.clear();
                }
                tempActionState = 0;
            }
        }
    }
}