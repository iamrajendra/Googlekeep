package com.iamrajendra.googlekeep.adapter.draganddrop;

import androidx.recyclerview.widget.RecyclerView;

public interface ItemTouchHelperAdapter {
    /**
     * Called when an item has been dragged far enough to trigger a move. This is called every time
     * an item is shifted, and not at the end of a "drop" event.
     *
     *
     */
    void onItemMove(int fromPosition, int toPosition);


    /**
     * Called when an item has been dismissed by a swipe.
     *
     * @param position The position of the item dismissed.

     */
    void onItemDismiss(int position);
}
