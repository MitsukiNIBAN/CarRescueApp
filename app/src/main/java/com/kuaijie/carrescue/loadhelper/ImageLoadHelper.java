package com.kuaijie.carrescue.loadhelper;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by MitsukiSaMa on 11-18.
 */

public class ImageLoadHelper {
    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView imageView, String path) {
        Glide.with(imageView.getContext())
                .load(path)
                .into(imageView);
    }
}
