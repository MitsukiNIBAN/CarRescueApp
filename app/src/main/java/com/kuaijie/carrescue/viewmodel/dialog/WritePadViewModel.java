package com.kuaijie.carrescue.viewmodel.dialog;

import android.content.Intent;
import android.view.View;

import com.kuaijie.carrescue.databinding.DialogWritePadBinding;
import com.kuaijie.carrescue.ui.dialog.writepad.WritePadDialog;
import com.kuaijie.carrescue.ui.technician.CompletionFeedbackActivity;

/**
 * Created by 87706 on 11-18.
 */

public class WritePadViewModel {
    private WritePadDialog writePadDialog;
    private DialogWritePadBinding binding;
    public WritePadViewModel(WritePadDialog writePadDialog, DialogWritePadBinding binding) {
        this.writePadDialog = writePadDialog;
        this.binding = binding;
    }
}
