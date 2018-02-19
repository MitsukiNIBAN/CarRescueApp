package com.kuaijie.carrescue.viewmodel.activity;

import com.kuaijie.carrescue.constant.OrderKey;
import com.kuaijie.carrescue.constant.Status;
import com.kuaijie.carrescue.databinding.ActivitySatisfactionDegreeBinding;
import com.kuaijie.carrescue.dto.OrderSatisfictionSet;
import com.kuaijie.carrescue.entity.OrderSatisfiction;
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
 * Created by Mitsuki on 1-05.
 */

public class SatisfactionDegreeViewModel {
    private ActivitySatisfactionDegreeBinding binding;
    private Activity activity;
    private Long id;

    public SatisfactionDegreeViewModel(Activity activity, ActivitySatisfactionDegreeBinding binding, Long id) {
        this.id = id;
        this.activity = activity;
        this.binding = binding;
    }

    public void init() {

    }

    public void sendSatisfaction(Consumer consumer) {
        OrderSatisfiction orderSatisfiction = new OrderSatisfiction();
        orderSatisfiction.setMemo(binding.etStarSatisfaction.getText().toString());
        orderSatisfiction.setOrderId(id);
        orderSatisfiction.setSatisfiction((int) binding.rbStarSatisfaction.getRating());

        Message message = Message.getMessage(OrderKey.SUB_STFT, orderSatisfiction);
        if (!SocketClient.getInstance().sendMsg(message, OrderKey.SUB_STFT)) {
            //发送失败
            return;
        }

        Function<Message, Integer> function = msg -> {
            Boolean b = DataCache.getInstance().readData(msg.getOptionData(), Boolean.class);
            if (b) {
                return Status.SEND_SUCCESS;
            } else
                return Status.SERVICE_TIME_OUT;
        };

        FunctionContainer.getInstance().putFunction(OrderKey.SUB_STFT, function);
        ConsumerContainer.getInstance().putConsumer(OrderKey.SUB_STFT, consumer);
    }
}
