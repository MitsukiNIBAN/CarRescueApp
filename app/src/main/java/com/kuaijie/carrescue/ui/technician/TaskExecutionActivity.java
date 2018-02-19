package com.kuaijie.carrescue.ui.technician;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ListChangeRegistry;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Toast;

import com.kuaijie.carrescue.R;
import com.kuaijie.carrescue.adapter.PhotoListAdapter;
import com.kuaijie.carrescue.constant.Other;
import com.kuaijie.carrescue.constant.Status;
import com.kuaijie.carrescue.databinding.ActivityTaskExecutionBinding;
import com.kuaijie.carrescue.entity.Photo;
import com.kuaijie.carrescue.ui.base.TakePhotoActivity;
import com.kuaijie.carrescue.ui.dialog.loading.Loading;
import com.kuaijie.carrescue.ui.dialog.writepad.WritePadDialog;
import com.kuaijie.carrescue.util.Activity;
import com.kuaijie.carrescue.util.PageJump;
import com.kuaijie.carrescue.view.swipecardrecyclerview.SwipeCardCallback;
import com.kuaijie.carrescue.view.swipecardrecyclerview.SwipeCardLayoutManager;
import com.kuaijie.carrescue.viewmodel.activity.TaskExecutionViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by MitsukiSaMa on 11-13.
 */

public class TaskExecutionActivity extends Activity {

    private ActivityTaskExecutionBinding binding;
    private TaskExecutionViewModel taskExecutionViewModel;

    private Long id;
    private Long pageId;

    @Override
    public void onCreate(Bundle savedInstanceStatu) {
        super.onCreate(savedInstanceStatu);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_task_execution);

        Intent in = getIntent();
        id = in.getLongExtra("id", Long.MIN_VALUE);
        pageId = in.getLongExtra("pageId", Long.MIN_VALUE);

        taskExecutionViewModel = new TaskExecutionViewModel(this, binding, id);
        taskExecutionViewModel.init();
        binding.setTaskExecutionViewModel(taskExecutionViewModel);
        binding.includeTitleBar.setTitleName(taskExecutionViewModel.getTitle());

        binding.ivEmpty.setOnClickListener(view -> oneMore());
        binding.btnOneMore.setOnClickListener(view -> oneMore());
        binding.btnNextStep.setOnClickListener(view -> {
            Loading.getInstance().loading(this);
            //在此进行图片提交并进入下一步
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
                new Handler(getMainLooper()).post(() -> Loading.getInstance().unloading());
            };
            taskExecutionViewModel.upLoadImg(consumer);
        });

        binding.includeTitleBar.tbTitleToolbar.setNavigationOnClickListener(view -> onBackPressed());
        binding.includeTitleBar.ibTitleMore.setOnClickListener(view -> showOptionWindow(view));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            String fileName = data.getStringExtra("fileName");
            if (fileName != null)
                if (!fileName.equals("")) {
                    taskExecutionViewModel.addPhoto(fileName);
                }
        }
    }

    public void oneMore() {
        Intent intent = new Intent();
        intent.setClass(this, TakePhotoActivity.class);
        startActivityForResult(intent, 3);
    }
}
