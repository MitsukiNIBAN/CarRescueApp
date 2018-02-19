package com.kuaijie.carrescue.ui.technician;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;

import com.kuaijie.carrescue.R;
import com.kuaijie.carrescue.databinding.ActivityCarCheckBinding;
import com.kuaijie.carrescue.ui.dialog.loading.Loading;
import com.kuaijie.carrescue.util.Activity;
import com.kuaijie.carrescue.util.PageJump;

/**
 * Created by Mitsuki on 12-20.
 */

public class CarCheckActivity extends Activity {
    private ActivityCarCheckBinding binding;

    private Long id;
    private Long pageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_car_check);

        binding.includeTitleBar.setTitleName(getResources().getString(R.string.title_text_car_check));

        Intent in = getIntent();
        id = in.getLongExtra("id", Long.MIN_VALUE);
        pageId = in.getLongExtra("pageId", Long.MIN_VALUE);

        binding.btnNotOk.setOnClickListener(view -> PageJump.getInstance().jump(this, pageId, id, 2));
        binding.btnOk.setOnClickListener(view -> {
            Loading.getInstance().loading(this);
            new Thread(() -> {
                PageJump.getInstance().jump(this, pageId, id, 1);
                new Handler(getMainLooper()).post(() -> Loading.getInstance().unloading());
            }).start();
        });
        binding.includeTitleBar.tbTitleToolbar.setNavigationOnClickListener(view -> onBackPressed());
        binding.includeTitleBar.ibTitleMore.setOnClickListener(view -> showOptionWindow(view));
    }
}
