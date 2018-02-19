package com.kuaijie.carrescue.adapter;

import android.content.Context;
import android.util.Log;

import com.kuaijie.carrescue.R;
import com.kuaijie.carrescue.constant.OrderKey;
import com.kuaijie.carrescue.constant.Other;
import com.kuaijie.carrescue.databinding.ItemOrderBinding;
import com.kuaijie.carrescue.entity.Order;
import com.kuaijie.carrescue.util.Tools;

import java.util.List;

/**
 * Created by MitsukiSaMa on 12-5.
 */

public class OrderListAdapter extends BaseListViewAdapter<Order, ItemOrderBinding> {

    public OrderListAdapter(Context context, List<Order> orders) {
        super(context);
        if (orders != null)
            items.addAll(orders);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.item_order;
    }

    @Override
    protected void onBindItem(ItemOrderBinding binding, Order item) {
//        Log.e("onBindItem", getCount()+"");
        binding.setOrder(item);
        binding.executePendingBindings();
    }
}
