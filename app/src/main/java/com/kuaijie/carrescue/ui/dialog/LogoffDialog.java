package com.kuaijie.carrescue.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.kuaijie.carrescue.R;
import com.kuaijie.carrescue.databinding.DialogLogoffBinding;
import com.kuaijie.carrescue.ui.base.LoginActivity;
import com.kuaijie.carrescue.util.Activity;
import com.kuaijie.carrescue.util.datahelper.DataCache;

/**
 * Created by MitsukiSaMa on 11-24.
 */

public class LogoffDialog extends Dialog {

    private Activity activity;
    public LogoffDialog(Context context) {
        super(context, R.style.DialogStyle);
        this.activity = (Activity) context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DialogLogoffBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_logoff, null, false);
        setContentView(binding.getRoot());
        setCanceledOnTouchOutside(false);

        binding.negativeButton.setOnClickListener(view -> {
            if (this != null)
                this.dismiss();
        });
        binding.positiveButton.setOnClickListener(view -> {
            if (this != null)
                this.dismiss();
            Intent logoutIntent = new Intent(view.getContext(), LoginActivity.class);
            logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            view.getContext().startActivity(logoutIntent);
            activity.finish();
        });
    }
}
