package com.kuaijie.carrescue.loadhelper;

import android.databinding.BindingAdapter;
import android.widget.TextView;

import com.kuaijie.carrescue.constant.Status;

/**
 * Created by MitsukiSaMa on 12-7.
 */

public class OrderStatusHelper {
    @BindingAdapter({"orderStatus"})
    public static void setStatus(TextView textView, Byte state) {
        switch (state){
            case Status.ORDER_CANCEL:
                textView.setText("已取消");
                break;
            case Status.ORDER_DISPATCH:
                textView.setText("未调度");
                break;
            case Status.ORDER_ACCEPT:
                textView.setText("未接单");
                break;
            case Status.ORDER_DOING:
                textView.setText("进行中");
                break;
            case Status.ORDER_COMPLETE:
                textView.setText("已完成");
                break;
            case Status.ORDER_:
                textView.setText("已报数");
                break;
            case Status.ORDER_CHECK:
                textView.setText("已质检");
                break;
        }
    }
}
