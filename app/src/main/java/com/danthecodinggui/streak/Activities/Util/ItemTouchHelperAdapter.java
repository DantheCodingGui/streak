package com.danthecodinggui.streak.Activities.Util;

/**
 * Defines methods that the ItemTouchHelper callback subclass will redirect to the
 * RecyclerViewAdapter
 */

public interface ItemTouchHelperAdapter {
    boolean onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
}
