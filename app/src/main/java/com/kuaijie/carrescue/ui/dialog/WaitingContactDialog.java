package com.kuaijie.carrescue.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.kuaijie.carrescue.R;
import com.kuaijie.carrescue.databinding.DialogWaitingContactBinding;
import com.kuaijie.carrescue.ui.base.MainActivity;
import com.kuaijie.carrescue.ui.dialog.loading.Loading;
import com.kuaijie.carrescue.util.Activity;
import com.kuaijie.carrescue.util.PageJump;


/**
 * Created by 87706 on 12-9.
 */

public class WaitingContactDialog extends Dialog {

    private Activity activity;
    private Long id;
    private Long pageId;
    private Long procedureId;

    public WaitingContactDialog(Context context, Long pageId, Long id, Long procedure) {
        super(context, R.style.DialogStyle);
        this.activity = (Activity) context;
        this.pageId = pageId;
        this.id = id;
        this.procedureId = procedure;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DialogWaitingContactBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_waiting_contact, null, false);
        setContentView(binding.getRoot());
        setCanceledOnTouchOutside(false);

        if (procedureId == 11l) {
            binding.tvTitle.setText("请等待");
            binding.tvContent.setText("等待服务器接通电话···");
//            binding.btnConfirm.setVisibility(View.GONE);
        } else if (procedureId == 42l) {
            binding.tvTitle.setText("请等待");
            binding.tvContent.setText("等待指挥部指令···");
//            binding.btnConfirm.setVisibility(View.GONE);
        }

        binding.btnConfirm.setOnClickListener(view -> {
            Loading.getInstance().loading(activity);
            new Thread(() -> {
                PageJump.getInstance().jump(activity, pageId, id, 1);
                new Handler(activity.getMainLooper()).post(() -> {
                    this.dismiss();
                    Loading.getInstance().unloading();
                });
            }).start();

//            Intent intent = new Intent();
//            //根据下一步类型跳转到下一个界面
//            intent.setClass(activity, GoToRescueActivity.class);
//
//
//            //放入id，页面跳转
//            intent.putExtra("id", id);
//            activity.startActivity(intent);
//            this.dismiss();
//            activity.finish();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!activity.getClass().equals(MainActivity.class))
            activity.finish();
    }
}
