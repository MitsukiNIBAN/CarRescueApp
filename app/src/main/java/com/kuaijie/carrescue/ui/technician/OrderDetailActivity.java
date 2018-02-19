package com.kuaijie.carrescue.ui.technician;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.kuaijie.carrescue.R;
import com.kuaijie.carrescue.databinding.ActivityOrderDetailBinding;
import com.kuaijie.carrescue.util.Activity;
import com.kuaijie.carrescue.viewmodel.activity.OrderDetailViewModel;

/**
 * Created by MitsukiSaMa on 11-22.
 */

public class OrderDetailActivity extends Activity{
    private ActivityOrderDetailBinding binding;
    private OrderDetailViewModel orderDetailViewModel;

    private Long id;
    private Integer orderNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order_detail);
        binding.includeTitleBar.setTitleName(getResources().getString(R.string.title_text_order_detail));

        Intent in = getIntent();
        id = in.getLongExtra("id", Long.MIN_VALUE);
        orderNum = in.getIntExtra("orderNum", Integer.MIN_VALUE);

        orderDetailViewModel = new OrderDetailViewModel(this, binding, id);
        binding.setOrderDetailViewModel(orderDetailViewModel);
        orderDetailViewModel.init();

        binding.includeTitleBar.tbTitleToolbar.setNavigationOnClickListener(view -> onBackPressed());

    }
}
