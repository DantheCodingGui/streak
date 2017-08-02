package com.danthecodinggui.streak;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Adapter for recycleview holding all streaks on home activity
 */

public class StreakRecyclerViewAdapter extends RecyclerView.Adapter<StreakViewHolders> {

    private List<StreakObject> streakList;
    private Context context;

    public StreakRecyclerViewAdapter(Context context, List<StreakObject> streakList) {
        this.streakList = streakList;
        this.context = context;
    }

    @Override
    public StreakViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.streak_card, null);
        StreakViewHolders rcv = new StreakViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(StreakViewHolders holder, int position) {
        holder.streakText.setText(streakList.get(position).getStreakText());
    }

    @Override
    public int getItemCount() {
        return this.streakList.size();
    }
}