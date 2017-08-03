package com.danthecodinggui.streak;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Link between Streak data and User Interface
 */

public class StreakRecyclerViewAdapter extends RecyclerView.Adapter<StreakRecyclerViewAdapter.StreakViewHolder> {

    private List<StreakObject> streakList;

    public StreakRecyclerViewAdapter(List<StreakObject> streakList) {
        this.streakList = streakList;
    }

    @Override
    public StreakViewHolder onCreateViewHolder(ViewGroup viewGroup, int pos) {

        View layoutView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.streak_card, viewGroup, false);
        return new StreakViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(StreakViewHolder holder, int pos) {
        StreakObject ob = streakList.get(pos);
        holder.streakText.setText(ob.getStreakText());
    }

    @Override
    public int getItemCount() {
        return streakList.size();
    }

    public class StreakViewHolder extends RecyclerView.ViewHolder {

        public TextView streakText;

        public StreakViewHolder(View view) {
            super(view);
            streakText = (TextView)itemView.findViewById(R.id.streak_text);
        }
    }
}