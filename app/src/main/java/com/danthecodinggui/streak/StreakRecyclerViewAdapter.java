package com.danthecodinggui.streak;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Adapter class linking streak data to user interface
 */
class StreakRecyclerViewAdapter extends
        RecyclerView.Adapter<StreakRecyclerViewAdapter.StreakViewHolder> {

    private List<StreakObject> streakList;

    /**
     * @param streakList Initialise data if some already exists
     */
    StreakRecyclerViewAdapter(List<StreakObject> streakList) {
        this.streakList = streakList;
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

    /**
     * @return Size of list to be displayed in RecyclerView
     */
    @Override
    public int getItemCount() {
        return streakList.size();
    }

    /**
     * Holds the streak views & what RecyclerView uses rather than individual views themselves
     */
    class StreakViewHolder extends RecyclerView.ViewHolder {

        TextView streakText;

        /**
         * @param view The view item that the ViewHolder will contain
         */
        StreakViewHolder(View view) {
            super(view);
            streakText = (TextView)itemView.findViewById(R.id.streak_text);
        }
    }
}