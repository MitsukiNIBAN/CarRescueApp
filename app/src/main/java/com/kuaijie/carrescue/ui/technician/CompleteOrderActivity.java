package com.kuaijie.carrescue.ui.technician;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.kuaijie.carrescue.R;
import com.kuaijie.carrescue.databinding.ActivityCompleteOrderBinding;
import com.kuaijie.carrescue.util.Activity;
import com.kuaijie.carrescue.util.datahelper.DataCache;

/**
 * Created by MitsukiSaMa on 11-17.
 */

public class CompleteOrderActivity extends Activity {

    private Long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCompleteOrderBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_complete_order);
        binding.includeTitleBar.setTitleName(getResources().getString(R.string.complete_order));
        binding.includeTitleBar.tbTitleToolbar.setNavigationOnClickListener(view -> onBackPressed());

        Intent in = getIntent();
        id = in.getLongExtra("id", Long.MIN_VALUE);

        binding.ivFinish.setOnClickListener(view -> onBackPressed());
        binding.ivReturn.setOnClickListener(view -> onBackPressed());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DataCache.getInstance().cmpOrder(id);
    }
}
