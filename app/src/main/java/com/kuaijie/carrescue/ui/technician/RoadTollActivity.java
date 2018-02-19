package com.kuaijie.carrescue.ui.technician;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kuaijie.carrescue.R;
import com.kuaijie.carrescue.constant.Status;
import com.kuaijie.carrescue.databinding.ActivityRoadTollBinding;
import com.kuaijie.carrescue.ui.base.TakePhotoActivity;
import com.kuaijie.carrescue.ui.dialog.loading.Loading;
import com.kuaijie.carrescue.util.Activity;
import com.kuaijie.carrescue.util.PageJump;
import com.kuaijie.carrescue.viewmodel.activity.RoadTollViewModel;

import io.reactivex.functions.Consumer;

/**
 * Created by Mitsuki on 12-20.
 */

public class RoadTollActivity extends Activity {
    private ActivityRoadTollBinding binding;
    private RoadTollViewModel roadTollViewModel;

    private Long id;
    private Long pageId;
    private Long procedureId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_road_toll);

        binding.includeTitleBar.setTitleName(getResources().getString(R.string.title_text_roll_toll));

        Intent in = getIntent();
        id = in.getLongExtra("id", Long.MIN_VALUE);
        pageId = in.getLongExtra("pageId", Long.MIN_VALUE);
        procedureId = in.getLongExtra("procedureId", Long.MIN_VALUE);

        if (procedureId == 44l)
            binding.includeTitleBar.setTitleName("救援费用填写");

        roadTollViewModel = new RoadTollViewModel(this, binding, id);


        binding.btnNextStep.setOnClickListener(view -> {
            Loading.getInstance().loading(this);
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
            roadTollViewModel.submitToll(consumer);

            //以下测试
//            PageJump.getInstance().jump(this, pageId, id, 1);
        });

        binding.ivTakePhoto.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setClass(this, TakePhotoActivity.class);
            startActivityForResult(intent, 3);
        });

        binding.includeTitleBar.tbTitleToolbar.setNavigationOnClickListener(view -> onBackPressed());
        binding.includeTitleBar.ibTitleMore.setOnClickListener(view -> showOptionWindow(view));
        speakHint("请填写路费信息，如果没有产生路费，请填零。");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            String fileName = data.getStringExtra("fileName");
            if (fileName != null) {
                roadTollViewModel.setFileName(fileName);
                Glide.with(this)
                        .load(Environment.getExternalStorageDirectory() + "/CarRescue/PhotoCache/" + fileName)
                        .into(binding.ivTakePhoto);
            }
        }
    }
}
