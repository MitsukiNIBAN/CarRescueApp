package com.kuaijie.carrescue.viewmodel.dialog;

import android.view.View;

import com.kuaijie.carrescue.databinding.DialogInputCostBinding;
import com.kuaijie.carrescue.ui.dialog.InputCostDialog;

/**
 * Created by MitsukiSaMa on 11-24.
 */

public class InputCostViewModel {
    private InputCostDialog inputCostDialog;
    private DialogInputCostBinding binding;

    public InputCostViewModel(InputCostDialog inputCostDialog, DialogInputCostBinding binding)
    {
        this.inputCostDialog = inputCostDialog;
        this.binding = binding;
    }

    public void dismiss(View view)
    {
        if(inputCostDialog != null)
            inputCostDialog.dismiss();
    }

    public void inputConfirm(View view)
    {
        if(inputCostDialog != null)
            inputCostDialog.dismiss();
    }
}
