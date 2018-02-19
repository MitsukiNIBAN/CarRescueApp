package com.kuaijie.carrescue.ui.base;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.kuaijie.carrescue.R;
import com.kuaijie.carrescue.constant.OrderKey;
import com.kuaijie.carrescue.constant.Status;
import com.kuaijie.carrescue.dto.UserDTO;
import com.kuaijie.carrescue.util.Activity;
import com.kuaijie.carrescue.util.ConsumerContainer;
import com.kuaijie.carrescue.util.FunctionContainer;
import com.kuaijie.carrescue.util.datahelper.DataCache;
import com.kuaijie.carrescue.util.socket.SocketClient;
import com.kuaijie.carrescue.util.socket.SocketService;
import com.kuaijie.carrescue.util.socket.message.Message;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by MitsukiSaMa on 11-6.
 */

public class SplashActivity extends Activity {

    private SharedPreferences sp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash);

        Intent in = new Intent();
        in.setClass(this, SocketService.class);
        startService(in);

        sp = this.getSharedPreferences("userLogin", Context.MODE_WORLD_READABLE);
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                UserDTO userDTO = new UserDTO();
                userDTO.setPhone(sp.getString("ACCOUNT", ""));
                userDTO.setPassword(sp.getString("PASSWORD", ""));
                Message message = Message.getMessage(OrderKey.LOGIN, userDTO);

                if (!SocketClient.getInstance().sendMsg(message, OrderKey.LOGIN)) {
                    Intent intent2 = new Intent();
                    intent2.setClass(this, LoginActivity.class);
                    startActivity(intent2);
                    finish();
                    return;
                }

                Function<Message, Integer> function = msg -> {
                    if (msg.getOptionData().length == 1) {
                        return Status.SERVICE_TIME_OUT;
                    } else {
                        DataCache.getInstance().saveUser(msg.getOptionData());
                        return Status.CACHING_COMPLETION;
                    }
                };
                Consumer<Integer> consumer = state -> {
                    switch (state) {
                        case Status.CACHING_COMPLETION:
                            Intent intent1 = new Intent();
                            intent1.setClass(this, MainActivity.class);
                            startActivity(intent1);
                            finish();
                            break;
                        case Status.SERVICE_TIME_OUT:
                            Intent intent2 = new Intent();
                            intent2.setClass(this, LoginActivity.class);
                            startActivity(intent2);
                            finish();
                            break;
                        default:
                            break;
                    }
                };
                FunctionContainer.getInstance().putFunction(OrderKey.LOGIN, function);
                ConsumerContainer.getInstance().putConsumer(OrderKey.LOGIN, consumer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
