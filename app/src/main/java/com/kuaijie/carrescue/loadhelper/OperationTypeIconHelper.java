package com.kuaijie.carrescue.loadhelper;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.kuaijie.carrescue.R;
import com.kuaijie.carrescue.constant.OrderType;

/**
 * Created by MitsukiSaMa on 12-6.
 */

public class OperationTypeIconHelper {

    @BindingAdapter({"opType"})
    public static void loadImage(ImageView imageView, Long type) {
        int i = new Long(type).intValue();
        switch (i){
            case OrderType.TRAILER:
                //拖车
                Glide.with(imageView.getContext())
                        .load(R.drawable.ic_order_trailer_normal)
                        .into(imageView);
                break;
            case OrderType.CHARGE_UP:
                //搭电
                Glide.with(imageView.getContext())
                        .load(R.drawable.ic_order_charge_up_normal)
                        .into(imageView);
                break;
            case OrderType.TIRE:
                //换胎
                Glide.with(imageView.getContext())
                        .load(R.drawable.ic_order_tire_normal)
                        .into(imageView);
                break;
            case OrderType.BASEMENT_TRACTION:
                //地库牵引
                Glide.with(imageView.getContext())
                        .load(R.drawable.ic_order_basement_traction_normal)
                        .into(imageView);
                break;
            case OrderType.THE_PLIGHT_OF_RESCUE:
                //困境救援
                Glide.with(imageView.getContext())
                        .load(R.drawable.ic_order_the_plight_of_rescue_normal)
                        .into(imageView);
                break;
            case OrderType.CRANE:
                //吊车
                Glide.with(imageView.getContext())
                        .load(R.drawable.ic_order_the_plight_of_rescue_normal)
                        .into(imageView);
                break;
            default:
                //默认其他
                Glide.with(imageView.getContext())
                        .load(R.drawable.ic_order_other_normal)
                        .into(imageView);
                break;
        }
    }
}
