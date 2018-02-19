package com.kuaijie.carrescue.viewmodel.activity;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.kuaijie.carrescue.constant.OrderKey;
import com.kuaijie.carrescue.constant.Status;
import com.kuaijie.carrescue.databinding.ActivityLoginBinding;
import com.kuaijie.carrescue.dto.UserDTO;
import com.kuaijie.carrescue.ui.base.LoginActivity;
import com.kuaijie.carrescue.util.Activity;
import com.kuaijie.carrescue.util.ConsumerContainer;
import com.kuaijie.carrescue.util.FunctionContainer;
import com.kuaijie.carrescue.util.datahelper.DataCache;
import com.kuaijie.carrescue.util.socket.SocketClient;
import com.kuaijie.carrescue.util.socket.message.Message;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by Mitsuki on 1-03.
 */

public class LoginViewModel {
    private ActivityLoginBinding binding;
    private Activity activity;

    public LoginViewModel(Activity activity, ActivityLoginBinding binding) {
        this.activity = activity;
        this.binding = binding;
    }

    public void saveUser(Consumer consumer) {
        Log.i("TAG", binding.etAccount.getText().toString() + ":" + binding.etPassword.getText().toString());

        UserDTO userDTO = new UserDTO();
        userDTO.setPhone(binding.etAccount.getText().toString());
        userDTO.setPassword(binding.etPassword.getText().toString());
        Message message = Message.getMessage(OrderKey.LOGIN, userDTO);

        if (!SocketClient.getInstance().sendMsg(message, OrderKey.LOGIN)) {
            Toast.makeText(activity, "与服务器失去连接", Toast.LENGTH_SHORT).show();
            return;
        }

        Function<Message, Integer> function = msg -> {
            Log.e("tag length", msg.getOptionData().length+"");
            if (msg.getOptionData().length == 1) {
                return Status.SERVICE_TIME_OUT;
            } else {
                DataCache.getInstance().saveUser(msg.getOptionData());
                return Status.CACHING_COMPLETION;
            }
        };

        FunctionContainer.getInstance().putFunction(OrderKey.LOGIN, function);
        ConsumerContainer.getInstance().putConsumer(OrderKey.LOGIN, consumer);
    }
}
