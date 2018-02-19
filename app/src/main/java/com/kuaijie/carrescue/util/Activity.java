package com.kuaijie.carrescue.util;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.kuaijie.carrescue.R;
import com.kuaijie.carrescue.ui.dialog.loading.Loading;
import com.kuaijie.carrescue.util.texttospeech.AppTTSController;
import com.kuaijie.carrescue.util.texttospeech.TaskHint;

/**
 * Created by MitsukiSaMa on 11-6.
 */

public class Activity extends AppCompatActivity{

    protected TaskHint taskHint;
    private PopupWindow optionWindow;
    private View optionView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Application.getInstance().addActivity(this);
        AppTTSController appTTSController = AppTTSController.getInstance(getApplicationContext());
        appTTSController.init();
        taskHint = appTTSController;
        initOptionWindow();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Loading.getInstance().unloading();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }


    protected void speakHint(String str){
        Log.i("Activity", "speakHint");
        taskHint.addHint(str);
    }

    private void initOptionWindow(){
        optionView = LayoutInflater.from(this).inflate(R.layout.dialog_pop_menu, null);

        optionWindow = new PopupWindow(optionView, Tools.dip2px(150), LinearLayout.LayoutParams.WRAP_CONTENT,true);
        optionWindow.setFocusable(true);
        optionWindow.setTouchable(true);
        optionWindow.setOutsideTouchable(true);
        optionWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        optionWindow.setOnDismissListener(() -> {});
        optionWindow.update();
    }

    protected void showOptionWindow(View view)
    {
        if(!optionWindow.isShowing()){
            optionWindow.showAsDropDown(view, 0, Tools.dip2px(10));
        }else{
            optionWindow.dismiss();
        }
    }
}
