package com.kuaijie.carrescue.ui.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kuaijie.carrescue.R;
import com.kuaijie.carrescue.constant.Status;
import com.kuaijie.carrescue.databinding.ActivityLoginBinding;
import com.kuaijie.carrescue.ui.dialog.loading.Loading;
import com.kuaijie.carrescue.util.Activity;
import com.kuaijie.carrescue.util.socket.SocketService;
import com.kuaijie.carrescue.viewmodel.activity.LoginViewModel;

import io.reactivex.functions.Consumer;

/**
 * Created by MitsukiSaMa on 11-6.
 */

public class LoginActivity extends Activity {

    private ActivityLoginBinding binding;
    private LoginViewModel loginViewModel;

    private Boolean isPswInput = false;
    private Boolean isAccountInput = false;

    private SharedPreferences sp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        sp = this.getSharedPreferences("userLogin", Context.MODE_WORLD_READABLE);


        binding.includeTitleBar.ibTitleMore.setVisibility(View.GONE);
        binding.includeTitleBar.setTitleName(getResources().getString(R.string.title_text_login));
        binding.includeTitleBar.tbTitleToolbar.setNavigationIcon(null);

        loginViewModel = new LoginViewModel(this, binding);

        binding.btnLogin.setClickable(false);
        binding.btnLogin.setEnabled(false);

        SharedPreferences.Editor editor = sp.edit();
        editor.putString("PASSWORD", "");
        editor.commit();

        binding.etAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (binding.etAccount.getText().toString().matches("1[0-9]{10}") && editable.length() > 0) {
                    isAccountInput = true;
                } else {
                    isAccountInput = true;
                }

                if (isAccountInput && isPswInput) {
                    binding.btnLogin.setClickable(true);
                    binding.btnLogin.setEnabled(true);
                }
            }
        });

        if (sp != null)
            binding.etAccount.setText(sp.getString("ACCOUNT", ""));

        binding.etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    isPswInput = true;
                } else {
                    isPswInput = false;
                }
                if (isAccountInput && isPswInput) {
                    binding.btnLogin.setClickable(true);
                    binding.btnLogin.setEnabled(true);
                }
            }
        });

    }

    public void login(View view) {
        Loading.getInstance().loading(this);


        Consumer<Integer> consumer = state -> {
            switch (state) {
                case Status.CACHING_COMPLETION:
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString("ACCOUNT", binding.etAccount.getText().toString());
                    editor.putString("PASSWORD", binding.etPassword.getText().toString());
                    editor.commit();

                    Intent intent = new Intent();
                    intent.setClass(this, MainActivity.class);
                    startActivity(intent);
                    break;
                case Status.SERVICE_TIME_OUT:
                    new Handler(getMainLooper()).post(() ->
                        Toast.makeText(this, "登陆失败", Toast.LENGTH_SHORT).show());
                    break;
                default:
                    break;
            }
            new Handler(getMainLooper()).post(() ->
                    Loading.getInstance().unloading());
        };

        loginViewModel.saveUser(consumer);
    }

//    public void forgivepsw(View view) {
//        Intent intent = new Intent();
//        intent.setClass(this, ForgetPasswordActivity.class);
//        startActivity(intent);
//    }
}
