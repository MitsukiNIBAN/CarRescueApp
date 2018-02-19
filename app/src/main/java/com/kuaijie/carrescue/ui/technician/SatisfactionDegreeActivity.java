package com.kuaijie.carrescue.ui.technician;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.kuaijie.carrescue.R;
import com.kuaijie.carrescue.constant.Status;
import com.kuaijie.carrescue.databinding.ActivitySatisfactionDegreeBinding;
import com.kuaijie.carrescue.ui.dialog.loading.Loading;
import com.kuaijie.carrescue.ui.dialog.writepad.WritePadDialog;
import com.kuaijie.carrescue.util.Activity;
import com.kuaijie.carrescue.util.PageJump;
import com.kuaijie.carrescue.viewmodel.activity.SatisfactionDegreeViewModel;

import io.reactivex.functions.Consumer;

/**
 * Created by MitsukiSaMa on 11-17.
 */

public class SatisfactionDegreeActivity extends Activity {

    private ActivitySatisfactionDegreeBinding binding;
    private SatisfactionDegreeViewModel satisfactionDegreeViewModel;

    private Long id;
    private Long pageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_satisfaction_degree);

        binding.includeTitleBar.setTitleName(getResources().getString(R.string.title_text_satisfaction_degree));

        Intent in = getIntent();
        id = in.getLongExtra("id", Long.MIN_VALUE);
        pageId = in.getLongExtra("pageId", Long.MIN_VALUE);

        satisfactionDegreeViewModel = new SatisfactionDegreeViewModel(this, binding, id);
        satisfactionDegreeViewModel.init();

        binding.btnNextStep.setOnClickListener(view -> {
            Loading.getInstance().loading(this);
            Consumer<Integer> consumer = state -> {
                switch (state) {
                    case Status.SEND_SUCCESS:
                        PageJump.getInstance().jump(this, pageId, id, 1);
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

            satisfactionDegreeViewModel.sendSatisfaction(consumer);

            //测试方法
//            PageJump.getInstance().jump(this, pageId, id, 1);
        });

        binding.includeTitleBar.tbTitleToolbar.setNavigationOnClickListener(view -> onBackPressed());
        binding.includeTitleBar.ibTitleMore.setOnClickListener(view -> showOptionWindow(view));
    }

    public void click(View view)
    {
//        Intent intent = new Intent();
//        intent.setClass(this,CompletionFeedbackActivity.class);
//        startActivity(intent);
//        WritePadDialog writePadDialog = new WritePadDialog(this, 2l);
//        writePadDialog.show();
    }
}
