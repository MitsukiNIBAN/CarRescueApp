package com.kuaijie.carrescue.ui.technician;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.kuaijie.carrescue.R;
import com.kuaijie.carrescue.databinding.ActivityWorkOrderBinding;
import com.kuaijie.carrescue.util.Activity;
import com.kuaijie.carrescue.util.PageJump;

/**
 * Created by Mitsuki on 12-21.
 */

public class WorkOrderActivity extends Activity{
    private ActivityWorkOrderBinding binding;

    private Long id;
    private Long pageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_work_order);

        binding.includeTitleBar.setTitleName(getResources().getString(R.string.title_text_work_order));

        Intent in = getIntent();
        id = in.getLongExtra("id", Long.MIN_VALUE);
        pageId = in.getLongExtra("pageId", Long.MIN_VALUE);

        binding.btnNextStep.setOnClickListener(view -> {
            PageJump.getInstance().jump(this, pageId, id, 1);
        });

        binding.includeTitleBar.tbTitleToolbar.setNavigationOnClickListener(view -> onBackPressed());
        binding.includeTitleBar.ibTitleMore.setOnClickListener(view -> showOptionWindow(view));
    }
}
