package com.kuaijie.carrescue.ui.technician;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.text.BoringLayout;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.kuaijie.carrescue.R;
import com.kuaijie.carrescue.constant.OrderType;
import com.kuaijie.carrescue.constant.Status;
import com.kuaijie.carrescue.databinding.ActivityTaskAcceptBinding;
import com.kuaijie.carrescue.ui.dialog.loading.Loading;
import com.kuaijie.carrescue.util.Activity;
import com.kuaijie.carrescue.util.PageJump;
import com.kuaijie.carrescue.util.datahelper.DataCache;
import com.kuaijie.carrescue.viewmodel.activity.TaskAcceptViewModel;

import io.reactivex.functions.Consumer;

/**
 * Created by MitsukiSaMa on 11-7.
 */

public class TaskAcceptActivity extends Activity {

    private final String TAG = "TaskAcceptActivity";

    private ActivityTaskAcceptBinding binding;
    private TaskAcceptViewModel taskAcceptViewModel;

    private Long id;
    private Long pageId = -3l;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_task_accept);
//        binding.includeTitleBar.ibTitleReturn.setVisibility(View.GONE);
        binding.includeTitleBar.setTitleName(getResources().getString(R.string.title_text_task_accept));

        Intent in = getIntent();
        id = in.getLongExtra("id", Long.MIN_VALUE);

        if (DataCache.getInstance().getOneOrder(id).getOperationTypeId() != OrderType.TRAILER) {
            binding.includeHeader.llDestination.setVisibility(View.GONE);
        }

        taskAcceptViewModel = new TaskAcceptViewModel(this, binding, id);
        taskAcceptViewModel.init();
        binding.setTaskAcceptViewModel(taskAcceptViewModel);

        binding.btnAcceptOrder.setOnClickListener(view -> {
            Loading.getInstance().loading(this);
            Log.i(TAG, "acceptOrder");
            Consumer<Integer> consumer = state -> {
                switch (state) {
                    case Status.SERVICE_TIME_OUT:
                        new Handler(getMainLooper()).post(() -> {
                            Toast.makeText(this, "连接服务器失败", Toast.LENGTH_SHORT).show();
                        });
                        break;
                    case Status.SEND_SUCCESS:
                        DataCache.getInstance().acceptOrder(id);
                        PageJump.getInstance().jump(this, pageId, id, 1);
                        break;
                    default:
                        break;
                }
                new Handler(getMainLooper()).post(()
                        -> Loading.getInstance().unloading());
            };
            taskAcceptViewModel.accept(consumer);
        });
        binding.includeTitleBar.tbTitleToolbar.setNavigationOnClickListener(view -> onBackPressed());
        binding.includeTitleBar.ibTitleMore.setOnClickListener(view -> showOptionWindow(view));
        speakHint("请接单");
    }

    @Override
    protected void onResume() {
        super.onResume();
        taskAcceptViewModel.startCount();
    }

    @Override
    protected void onPause() {
        super.onPause();
        taskAcceptViewModel.stopCount();
    }
}
