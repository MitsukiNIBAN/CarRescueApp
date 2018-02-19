package com.kuaijie.carrescue.view.swipecardrecyclerview;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by MitsukiSaMa on 11-15.
 */

public class SwipeCardLayoutManager extends RecyclerView.LayoutManager{
    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.WRAP_CONTENT);
    }
    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        detachAndScrapAttachedViews(recycler);
        int itemCount = getItemCount();
        if (itemCount < 1) return;

        int bottomPosition;
        if(itemCount < CardConfig.MAX_SHOW_COUNT) {
            bottomPosition = 0;
        }
        else {
            bottomPosition = itemCount - CardConfig.MAX_SHOW_COUNT;
        }

        for (int position = bottomPosition; position < itemCount; position ++) {
            View child = recycler.getViewForPosition(position);
            addView(child);
            measureChildWithMargins(child, 0 , 0);

            int widthSpace = getWidth() - getDecoratedMeasuredWidth(child);
            int heightSpace = getHeight() - getDecoratedMeasuredHeight(child);
            Rect mTmpRect = new Rect();
            calculateItemDecorationsForChild(child, mTmpRect);
            layoutDecorated(child, widthSpace/2,
                    heightSpace/2,
                    widthSpace/2 + getDecoratedMeasuredWidth(child),
                    heightSpace/2 + getDecoratedMeasuredHeight(child));

            int level = itemCount - position - 1;
            if(level > 0) {
                child.setScaleX(1 - CardConfig.SCALE_GAP * level);

                if (level < CardConfig.MAX_SHOW_COUNT - 1) {
                    child.setTranslationY(CardConfig.TRANS_Y_GAP * level);
                    child.setScaleY(1 - CardConfig.SCALE_GAP * level);
                }
                else {
                    child.setTranslationY(CardConfig.TRANS_Y_GAP * (level - 1));
                    child.setScaleY(1 - CardConfig.SCALE_GAP * (level - 1));
                }
            }
        }
    }
}
