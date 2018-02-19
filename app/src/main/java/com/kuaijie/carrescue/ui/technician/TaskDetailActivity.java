package com.kuaijie.carrescue.ui.technician;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.kuaijie.carrescue.R;
import com.kuaijie.carrescue.constant.OrderType;
import com.kuaijie.carrescue.databinding.ActivityTaskDetailBinding;
import com.kuaijie.carrescue.entity.Order;
import com.kuaijie.carrescue.entity.OrderProcedure;
import com.kuaijie.carrescue.ui.dialog.loading.Loading;
import com.kuaijie.carrescue.util.Activity;
import com.kuaijie.carrescue.util.PageJump;
import com.kuaijie.carrescue.util.datahelper.DataCache;
import com.kuaijie.carrescue.viewmodel.activity.TaskDetailViewModel;

/**
 * Created by MitsukiSaMa on 11-8.
 */

public class TaskDetailActivity extends Activity {

    private final String TAG = "TaskDetailActivity";

    private ActivityTaskDetailBinding binding;
    private Long id;
    private Long pageId = -2l;
    private TaskDetailViewModel taskDetailViewModel;
    private OrderProcedure orderProcedure;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_task_detail);
        binding.includeTitleBar.setTitleName(getResources().getString(R.string.title_text_task_detail));

        Intent in = getIntent();
        id = in.getLongExtra("id", Long.MIN_VALUE);

        Order order = DataCache.getInstance().getOneOrder(id);
        if (order != null)
            if (order.getOperationTypeId() != OrderType.TRAILER) {
                binding.includeHeader.llDestination.setVisibility(View.GONE);
            }

        //第一步是否是联系车主
        orderProcedure = DataCache.getInstance().getOrderProcedure(-1024l, id);

        taskDetailViewModel = new TaskDetailViewModel(this, binding, id);
        taskDetailViewModel.init();
        binding.setTaskDetailViewModel(taskDetailViewModel);
        if (orderProcedure != null)
            binding.btnContacRequest.setText(orderProcedure.getProcedureId() == 11 ? "联系车主" : "下一步");

        binding.btnContacRequest.setOnClickListener(view -> {
            Loading.getInstance().loading(this);
//
//            Consumer consumer = state -> {
//                //收到服务器的指令后才可以开始跳转页面
//                PageJump.getInstance().jump(this, pageId, id, 1);
//                Loading.getInstance().unloading();
//            };
//            if (orderProcedure.getProcedureId() == 11)
//                taskDetailViewModel.requestContect(consumer);

            //下测试方法***************
            new Thread(() -> {
                PageJump.getInstance().jump(this, pageId, id, 1);
                Loading.getInstance().unloading();
            }).start();

        });

        binding.includeHeader.llRescueLocation.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setClass(this, SiteMapActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("type", 1);//代表救援地
            startActivity(intent);
        });
        binding.includeHeader.llDestination.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setClass(this, SiteMapActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("type", 2);//代表目的地
            startActivity(intent);
        });
        binding.includeTitleBar.tbTitleToolbar.setNavigationOnClickListener(view -> onBackPressed());
        binding.includeTitleBar.ibTitleMore.setOnClickListener(view -> showOptionWindow(view));
    }

}
