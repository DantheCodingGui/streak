package com.danthecodinggui.streak;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Holds the card views for each streak
 */

public class StreakViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView streakText;

    public StreakViewHolders(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        streakText = (TextView)itemView.findViewById(R.id.streak_text);
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(view.getContext(), "Clicked Position = " + getPosition(), Toast.LENGTH_SHORT).show();
    }
}
