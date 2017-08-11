package com.danthecodinggui.streak.View.ItemTouchHelper;

/**
 * Defines methods that the ItemTouchHelper callback subclass will redirect to the
 * RecyclerViewAdapter
 */

public interface ItemTouchHelperAdapter {
    boolean onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
}
