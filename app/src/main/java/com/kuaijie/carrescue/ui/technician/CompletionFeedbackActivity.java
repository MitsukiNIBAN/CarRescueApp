package com.kuaijie.carrescue.ui.technician;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.kuaijie.carrescue.R;
import com.kuaijie.carrescue.constant.ServerAddress;
import com.kuaijie.carrescue.constant.Status;
import com.kuaijie.carrescue.databinding.ActivityCompletionFeedbackBinding;
import com.kuaijie.carrescue.ui.dialog.loading.Loading;
import com.kuaijie.carrescue.util.Activity;
import com.kuaijie.carrescue.viewmodel.activity.CompletionFeedbackViewModel;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import io.reactivex.functions.Consumer;

/**
 * Created by MitsukiSaMa on 11-17.
 */

public class CompletionFeedbackActivity extends Activity {
    private ActivityCompletionFeedbackBinding binding;
    private CompletionFeedbackViewModel completionFeedbackViewModel;

    private Long id;
    private Long pageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_completion_feedback);
        binding.includeTitleBar.ibTitleMore.setVisibility(View.GONE);
        binding.includeTitleBar.setTitleName(getResources().getString(R.string.title_text_completion_feedback));

        Intent in = getIntent();
        id = in.getLongExtra("id", Long.MIN_VALUE);
        pageId = in.getLongExtra("pageId", Long.MIN_VALUE);

        completionFeedbackViewModel = new CompletionFeedbackViewModel(this, binding, id);
        completionFeedbackViewModel.init();

        binding.includeTitleBar.tbTitleToolbar.setNavigationOnClickListener(view -> onBackPressed());
        binding.includeTitleBar.ibTitleMore.setOnClickListener(view -> showOptionWindow(view));

        binding.btnNextStep.setOnClickListener(view -> {
            Loading.getInstance().loading(this);
            Consumer<Integer> consumer = state -> {
                switch (state) {
                    case Status.SEND_SUCCESS:
                        Intent intent = new Intent();
                        intent.setClass(this, CompleteOrderActivity.class);
                        intent.putExtra("id", id);
                        startActivity(intent);
                        finish();
//                        new Thread(() -> ).start();
                        OkGo.<String>post(ServerAddress.SUB_COL)
                                .params("orderId", id + "")
                                .execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(Response<String> response) {
                                        Log.i("CFB Http:", "Success");
                                    }

                                    @Override
                                    public void onError(Response<String> response) {
                                        super.onError(response);
                                        Log.e("CFB Http:", "Error");
                                    }
                                });
                        break;
                    case Status.SERVICE_TIME_OUT:
                        new Handler(getMainLooper()).post(() ->
                                Toast.makeText(this, "连接服务器失败", Toast.LENGTH_SHORT).show());
                        break;
                    default:
                        break;
                }
                new Handler(getMainLooper()).post(() -> Loading.getInstance().unloading());
            };

            completionFeedbackViewModel.sendFeedback(consumer);
        });

        speakHint("进行完工反馈");
    }

}
