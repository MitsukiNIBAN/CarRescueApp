package com.kuaijie.carrescue.ui.technician;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.kuaijie.carrescue.R;
import com.kuaijie.carrescue.constant.Status;
import com.kuaijie.carrescue.databinding.ActivityStartOffBinding;
import com.kuaijie.carrescue.ui.dialog.loading.Loading;
import com.kuaijie.carrescue.util.Activity;
import com.kuaijie.carrescue.util.PageJump;
import com.kuaijie.carrescue.viewmodel.activity.StartOffViewModel;

import io.reactivex.functions.Consumer;

/**
 * Created by Mitsuki on 12-20.
 */

public class StartOffActivity extends Activity {

    private ActivityStartOffBinding binding;
    private StartOffViewModel startOffViewModel;

    private Long id;
    private Long pageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_start_off);

        binding.includeTitleBar.setTitleName("出发");

        Intent in = getIntent();
        id = in.getLongExtra("id", Long.MIN_VALUE);
        pageId = in.getLongExtra("pageId", Long.MIN_VALUE);

        binding.tvOnTheWay.setText("未出发");
        startOffViewModel = new StartOffViewModel(this, binding, id);
        startOffViewModel.init();

        binding.btnNextStep.setOnClickListener(view -> {
            //下一步按钮
            Consumer<Integer> consumer = state -> {
                switch (state) {
                    case Status.SERVICE_TIME_OUT:
                        new Handler(getMainLooper()).post(() -> {
                            Toast.makeText(this, "连接服务器失败", Toast.LENGTH_SHORT).show();
                        });
                        break;
                    case Status.SEND_SUCCESS:
                        PageJump.getInstance().jump(this, pageId, id, 1);
                        break;
                    default:
                        break;
                }
            };
//            startOffViewModel.arrive(consumer);

            //测试方法
            PageJump.getInstance().jump(this, pageId, id, 1);
        });

        binding.btnInput.setOnClickListener(view -> {
            //出发按钮
            //缓存需要保存是否已经出发的状态【未完成】
            Loading.getInstance().loading(this);
            Consumer<Integer> consumer = state -> {
                switch (state) {
                    case Status.SEND_SUCCESS:
                        new Handler(getMainLooper()).post(() -> {
                            binding.btnNextStep.setEnabled(true);
                            binding.btnNextStep.setClickable(true);
                            binding.btnInput.setEnabled(false);
                            binding.btnInput.setClickable(false);
                            binding.tvOnTheWay.setText("正在路上");
                        });
                        break;
                    case Status.SERVICE_TIME_OUT:
                        new Handler(getMainLooper()).post(() -> {
                            Toast.makeText(this, "连接服务器失败", Toast.LENGTH_SHORT).show();
                        });
                        break;
                    default:
                        break;
                }
                new Handler(getMainLooper()).post(() -> Loading.getInstance().unloading());
            };
            startOffViewModel.startOff(consumer);

            //测试方法
//            binding.btnNextStep.setEnabled(true);
//            binding.btnNextStep.setClickable(true);
//            binding.btnInput.setEnabled(false);
//            binding.btnInput.setClickable(false);
//            binding.tvOnTheWay.setText("正在路上");
        });

        binding.includeTitleBar.tbTitleToolbar.setNavigationOnClickListener(view -> onBackPressed());
        binding.includeTitleBar.ibTitleMore.setOnClickListener(view -> showOptionWindow(view));


    }
}
