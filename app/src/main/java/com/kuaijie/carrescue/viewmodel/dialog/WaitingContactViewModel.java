package com.kuaijie.carrescue.viewmodel.dialog;

import com.kuaijie.carrescue.databinding.DialogWaitingContactBinding;
import com.kuaijie.carrescue.ui.dialog.WaitingContactDialog;

/**
 * Created by 87706 on 12-9.
 */

public class WaitingContactViewModel {
    private WaitingContactDialog waitingContactDialog;
    private DialogWaitingContactBinding binding;

    public WaitingContactViewModel(WaitingContactDialog waitingContactDialog, DialogWaitingContactBinding binding){
        this.waitingContactDialog = waitingContactDialog;
        this.binding = binding;
    }

}
