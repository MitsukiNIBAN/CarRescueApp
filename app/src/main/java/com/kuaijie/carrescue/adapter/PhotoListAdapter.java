package com.kuaijie.carrescue.adapter;

import android.content.Context;

import com.kuaijie.carrescue.R;
import com.kuaijie.carrescue.databinding.ItemTaskPhotoBinding;
import com.kuaijie.carrescue.entity.Photo;

import java.util.List;

/**
 * Created by 87706 on 11-18.
 */

public class PhotoListAdapter extends BaseRecyclerViewAdapter<Photo, ItemTaskPhotoBinding> {

    public PhotoListAdapter(Context context, List<Photo> ss) {
        super(context);
        items.addAll(ss);
    }

    @Override
    protected int getLayoutResId(int viewType) {
        return R.layout.item_task_photo;
    }

    @Override
    protected void onBindItem(ItemTaskPhotoBinding binding, Photo item) {
        binding.setFilePath(item.getFileAddress());
        binding.setStr(item.getId() + "/" + getItemCount());
        binding.executePendingBindings();
    }
}
