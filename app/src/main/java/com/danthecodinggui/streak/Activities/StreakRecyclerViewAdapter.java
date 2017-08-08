package com.danthecodinggui.streak.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.danthecodinggui.streak.Activities.Util.ItemTouchHelperAdapter;
import com.danthecodinggui.streak.Activities.Util.ItemTouchHelperViewHolder;
import com.danthecodinggui.streak.Database.StreakDbHelper;
import com.danthecodinggui.streak.R;

import java.util.Collections;
import java.util.List;

import static com.danthecodinggui.streak.R.id.streak_card_view;


/**
 * Adapter class linking streak data to user interface
 */
class StreakRecyclerViewAdapter
        extends RecyclerView.Adapter<StreakRecyclerViewAdapter.StreakViewHolder>
        implements ItemTouchHelperAdapter {

    private List<StreakObject> streakList;

    private Activity linkedActivity;

    /**
     * @param streakList Initialise data if some already exists
     */
    StreakRecyclerViewAdapter(List<StreakObject> streakList, Activity linkedActivity) {
        this.streakList = streakList;
        this.linkedActivity = linkedActivity;
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

        StreakDbHelper sDbHelper = new StreakDbHelper(linkedActivity);
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
        StreakDbHelper sDbHelper = new StreakDbHelper(linkedActivity);
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

            Intent editStreak = new Intent(linkedActivity, EditStreakActivity.class);
            editStreak.putExtra("streakText", streakText.getText());
            view.setOnLongClickListener(this);
            editStreak.putExtra("viewId", getAdapterPosition());
            editStreak.putExtra("function", EditStreakActivity.EDIT_STREAK);

            ActivityOptionsCompat options =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(linkedActivity, view, linkedActivity.getString(R.string.transition_edit_streak));

            ActivityCompat.startActivityForResult(linkedActivity, editStreak, HomeActivity.EDIT_STREAK, options.toBundle());
        }

        /**
         * Switches app bar to other version with options such as delete etc.
         * @param view
         * @return Has long click been handled
         */
        @Override
        public boolean onLongClick(View view) {

            //StreakDbHelper sDbHelper = new StreakDbHelper();
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
        }

        /**
         * Changes view appearance back to default when drag and drop movement ended
         */
        @Override
        public void onItemClear() {
            CardView card = (CardView)itemView.findViewById(streak_card_view);
            card.setCardBackgroundColor(Color.WHITE);
            itemView.findViewById(R.id.card_content_container).setBackgroundColor(Color.TRANSPARENT);
        }
    }
}