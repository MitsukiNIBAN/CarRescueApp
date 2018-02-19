package com.kuaijie.carrescue.ui.base;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.kuaijie.carrescue.R;
import com.kuaijie.carrescue.databinding.ActivityForgetPasswordBinding;
import com.kuaijie.carrescue.util.Activity;

/**
 * Created by MitsukiSaMa on 11-7.
 */

public class ForgetPasswordActivity extends Activity
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        ActivityForgetPasswordBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_forget_password);
        binding.includeTitleBar.setTitleName("找回密码");
    }

}
