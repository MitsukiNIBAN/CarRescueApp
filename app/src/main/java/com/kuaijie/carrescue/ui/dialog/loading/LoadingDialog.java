package com.kuaijie.carrescue.ui.dialog.loading;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.kuaijie.carrescue.R;
import com.kuaijie.carrescue.databinding.DialogLoadingBinding;

/**
 * Created by MitsukiSaMa on 12-6.
 */

public class LoadingDialog extends Dialog{

    public LoadingDialog(Context context) {
        super(context, R.style.DialogStyle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DialogLoadingBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),R.layout.dialog_loading, null, false);
        setContentView(binding.getRoot());
        setCanceledOnTouchOutside(false);
    }
}
