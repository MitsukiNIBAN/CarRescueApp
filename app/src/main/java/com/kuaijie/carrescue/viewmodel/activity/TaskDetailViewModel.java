package com.kuaijie.carrescue.viewmodel.activity;

import android.databinding.ObservableField;
import android.widget.Toast;

import com.kuaijie.carrescue.constant.OrderKey;
import com.kuaijie.carrescue.databinding.ActivityTaskDetailBinding;
import com.kuaijie.carrescue.entity.Order;
import com.kuaijie.carrescue.util.Activity;
import com.kuaijie.carrescue.util.datahelper.DataCache;
import com.kuaijie.carrescue.util.socket.SocketClient;
import com.kuaijie.carrescue.util.socket.message.Message;

import io.reactivex.functions.Consumer;

/**
 * Created by MitsukiSaMa on 12-8.
 */

public class TaskDetailViewModel {
    private Activity activity;
    private ActivityTaskDetailBinding binding;

    private Long key;
    public ObservableField<Order> orderField = new ObservableField<>();

    public TaskDetailViewModel(Activity activity, ActivityTaskDetailBinding binding, Long k) {
        this.activity = activity;
        this.binding = binding;
        this.key = k;
    }

    public void init() {
        orderField.set(DataCache.getInstance().getOneOrder(key));
        binding.setOrder(orderField.get());
    }

    public void requestContect(Consumer consumer) {
        //向服务器请求接通电话
        Message message = Message.getMessage(OrderKey.ACCEPT_ORDER, key);
        if (!SocketClient.getInstance().sendMsg(message, OrderKey.ACCEPT_ORDER)) {
            Toast.makeText(activity, "与服务器失去连接", Toast.LENGTH_SHORT).show();
            return;
        }
    }
}
