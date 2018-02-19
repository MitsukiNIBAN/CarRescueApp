package com.kuaijie.carrescue.viewmodel.activity;

import android.databinding.ObservableField;

import com.kuaijie.carrescue.databinding.ActivityOrderDetailBinding;
import com.kuaijie.carrescue.entity.Order;
import com.kuaijie.carrescue.util.Activity;
import com.kuaijie.carrescue.util.datahelper.DataCache;

/**
 * Created by MitsukiSaMa on 12-8.
 */

public class OrderDetailViewModel {
    private Activity activity;
    private ActivityOrderDetailBinding binding;

    private Long key;
    public ObservableField<Order> orderField = new ObservableField<>();

    public OrderDetailViewModel(Activity activity, ActivityOrderDetailBinding binding, Long k){
        this.activity = activity;
        this.binding = binding;
        this.key = k;
    }

    public void init(){
        orderField.set(DataCache.getInstance().getOneOrder(key));
    }
}
