package com.kuaijie.carrescue.view.swipecardrecyclerview;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.kuaijie.carrescue.adapter.PhotoListAdapter;
import com.kuaijie.carrescue.entity.Photo;

import java.util.List;

/**
 * Created by MitsukiSaMa on 11-16.
 */

public class SwipeCardCallback extends ItemTouchHelper.SimpleCallback {

    private RecyclerView recyclerView;
    private PhotoListAdapter adapter;
    public SwipeCardCallback(RecyclerView recyclerView, PhotoListAdapter adapter)
    {
        super(0, ItemTouchHelper.DOWN|ItemTouchHelper.UP|ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT);
        this.recyclerView = recyclerView;
        this.adapter = adapter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        Photo remove = adapter.getItems().get(viewHolder.getLayoutPosition());
        System.out.println("===>first"+remove);
        adapter.getItems().remove(viewHolder.getLayoutPosition());
        adapter.getItems().add(0, remove);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        double distance = Math.sqrt(dX*dX + dY*dY);
        double fraction = distance/getThreashold();
        if (fraction > 1) {
            fraction = 1;
        }

        int childCount = recyclerView.getChildCount();
        for (int i = 0; i < childCount; i++){
            View child = recyclerView.getChildAt(i);

            int level = childCount - i - 1;
            if(level > 0) {
                child.setScaleX((float) (1 - CardConfig.SCALE_GAP * (level - fraction)));

                if (level < CardConfig.MAX_SHOW_COUNT - 1) {
                    child.setTranslationY((float) (CardConfig.TRANS_Y_GAP * (level - fraction)));
                    child.setScaleY((float) (1 - CardConfig.SCALE_GAP * (level - fraction)));
                }
                else {
//                    child.setTranslationY(CardConfig.TRANS_Y_GAP * (level - 1));
//                    child.setScaleY(1 - CardConfig.SCALE_GAP * (level - 1));
                }
            }
        }

    }

    private float getThreashold()
    {
        return recyclerView.getWidth() * 0.5f;
    }
}
