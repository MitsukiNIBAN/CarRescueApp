package com.kuaijie.carrescue.ui.dialog.loading;

import android.content.Context;

/**
 * Created by MitsukiSaMa on 12-7.
 */

public class Loading {
    private LoadingDialog loadingDialog;

    private static class LoadingHolder{
        private static final Loading INSTANCE = new Loading();
    }

    private  Loading(){}

    public static final Loading getInstance(){
        return LoadingHolder.INSTANCE;
    }

    public void loading(Context context){
        if (loadingDialog != null){
            loadingDialog.dismiss();
            loadingDialog = null;
        }
        loadingDialog = new LoadingDialog(context);
        loadingDialog.show();
    }

    public void unloading(){
        if (loadingDialog != null){
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }

}
