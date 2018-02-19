package com.kuaijie.carrescue.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.kuaijie.carrescue.R;
import com.kuaijie.carrescue.databinding.DialogInputCostBinding;
import com.kuaijie.carrescue.viewmodel.dialog.InputCostViewModel;

/**
 * Created by MitsukiSaMa on 11-24.
 */

public class InputCostDialog extends Dialog{

    private inputCostInterface inputcostinterface;

    public InputCostDialog(Context context) {
        super(context, R.style.DialogStyle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final DialogInputCostBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_input_cost, null, false);
        setContentView(binding.getRoot());
        setCanceledOnTouchOutside(false);
        binding.setInputCostViewModel(new InputCostViewModel(this, binding));
        binding.btnOk.setOnClickListener(v -> {
            this.dismiss();
            //执行数据缓存操作，存下费用

            if (inputcostinterface != null)
                inputcostinterface.okClick();
        });
        binding.btnCancel.setOnClickListener(v -> {
            this.dismiss();
            if (inputcostinterface != null)
                inputcostinterface.cancelClick();
        });
    }

    public void setInputcostinterface(inputCostInterface inputcostinterface) {
        this.inputcostinterface = inputcostinterface;
    }

    public interface inputCostInterface {
        void okClick();
        void cancelClick();
    }
}
