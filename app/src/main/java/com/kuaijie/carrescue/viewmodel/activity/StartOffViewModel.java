package com.kuaijie.carrescue.viewmodel.activity;

import android.databinding.ObservableField;

import com.kuaijie.carrescue.constant.OrderKey;
import com.kuaijie.carrescue.constant.Status;
import com.kuaijie.carrescue.databinding.ActivityStartOffBinding;
import com.kuaijie.carrescue.entity.Order;
import com.kuaijie.carrescue.util.Activity;
import com.kuaijie.carrescue.util.ConsumerContainer;
import com.kuaijie.carrescue.util.FunctionContainer;
import com.kuaijie.carrescue.util.datahelper.DataCache;
import com.kuaijie.carrescue.util.socket.SocketClient;
import com.kuaijie.carrescue.util.socket.message.Message;

import org.msgpack.MessagePack;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;


/**
 * Created by Mitsuki on 12-21.
 */

public class StartOffViewModel {

    private final String TAG = "StartOffViewModel";

    private Activity activity;
    private ActivityStartOffBinding binding;

    public ObservableField<Order> orderField = new ObservableField<>();
    private Long id;

    public StartOffViewModel(Activity activity, ActivityStartOffBinding binding, Long k) {
        this.activity = activity;
        this.binding = binding;
        this.id = k;
    }

    public void init() {
        orderField.set(DataCache.getInstance().getOneOrder(id));
        binding.setOrder(orderField.get());

    }

    public void startOff(Consumer consumer) {
        //在此向服务器提交开始的消息
        Message message = Message.getMessage(OrderKey.GO, new Long(id));
        if (!SocketClient.getInstance().sendMsg(message, OrderKey.GO)) {
            //发送失败
            return;
        }

        Function<Message, Integer> function = msg -> {
            Boolean b = DataCache.getInstance().readData(msg.getOptionData(), Boolean.class);
            if (b)
                return Status.SEND_SUCCESS;
            else
                return Status.SERVICE_TIME_OUT;
        };
        FunctionContainer.getInstance().putFunction(OrderKey.GO, function);
        ConsumerContainer.getInstance().putConsumer(OrderKey.GO, consumer);
    }

    public void arrive(Consumer consumer) {
        //在此进入下一步
        //在此可不用等候服务器回应，自行跳转
    }
}
