package com.danthecodinggui.streak.Activities.Util;

/**
 * Created by Dan on 07/08/2017.
 */

public interface ItemTouchHelperAdapter {
    boolean onItemMove(int fromPosition, int toPosition);

    void onItemDismiss(int position);
}
