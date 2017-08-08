package com.danthecodinggui.streak.Activities.Util;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.danthecodinggui.streak.Activities.StreakObject;
import com.danthecodinggui.streak.Database.StreakDbHelper;

import java.util.List;

/**
 * Created by Dan on 07/08/2017.
 */

public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final ItemTouchHelperAdapter mAdapter;

    private Context activityContext;

    private List<StreakObject> streakList;

    private boolean firstMove;
    private int initialPosition;

    public SimpleItemTouchHelperCallback(ItemTouchHelperAdapter adapter, Context context, List<StreakObject> list) {
        mAdapter = adapter;
        activityContext = context;
        streakList = list;
        firstMove = true;
        initialPosition = 0;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN
                            | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
    }


    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        //if (viewHolder.getItemViewType() != target.getItemViewType()) {
        //    return false;
        //}
        mAdapter.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        if (firstMove) {
            initialPosition = viewHolder.getAdapterPosition();
            firstMove = false;
        }
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
    }
}
