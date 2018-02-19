package com.kuaijie.carrescue.viewmodel.activity;

import android.databinding.ObservableField;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.kuaijie.carrescue.constant.OrderKey;
import com.kuaijie.carrescue.constant.Other;
import com.kuaijie.carrescue.constant.Status;
import com.kuaijie.carrescue.databinding.ActivityTaskAcceptBinding;
import com.kuaijie.carrescue.dto.OrderProcedureSet;
import com.kuaijie.carrescue.entity.Order;
import com.kuaijie.carrescue.util.Activity;
import com.kuaijie.carrescue.util.ConsumerContainer;
import com.kuaijie.carrescue.util.FunctionContainer;
import com.kuaijie.carrescue.util.Tools;
import com.kuaijie.carrescue.util.datahelper.DataCache;
import com.kuaijie.carrescue.util.socket.SocketClient;
import com.kuaijie.carrescue.util.socket.message.Message;

import org.msgpack.MessagePack;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by MitsukiSaMa on 11-24.
 */

public class TaskAcceptViewModel {
    private final String TAG = "TaskAcceptViewModel";

    private Activity activity;
    private ActivityTaskAcceptBinding binding;

    public ObservableField<Order> orderField = new ObservableField<>();
    private Long id;
    private Boolean isCount = false;

    public TaskAcceptViewModel(Activity activity, ActivityTaskAcceptBinding binding, Long k) {
        this.activity = activity;
        this.binding = binding;
        this.id = k;
    }

    public void init() {
        orderField.set(DataCache.getInstance().getOneOrder(id));
        binding.setOrder(orderField.get());
        isCount = true;
        handler.sendEmptyMessage(1);
    }

    public void accept(Consumer consumer) {
        Message message = Message.getMessage(OrderKey.ACCEPT_ORDER, id);
        if (!SocketClient.getInstance().sendMsg(message, OrderKey.ACCEPT_ORDER)) {
            Toast.makeText(activity, "请求服务器失败", Toast.LENGTH_SHORT).show();
            return;
        }

        Function<Message, Integer> function = msg -> {
            //根据服务拿到的数据进行业务处理
            OrderProcedureSet orderProcedureSet = DataCache.getInstance().readData(msg.getOptionData(), OrderProcedureSet.class);
            DataCache.getInstance().saveOrderProcedure(msg.getOptionData(), id);
            Log.i(TAG, "Get order procedure,size is " + orderProcedureSet.getOrderProcedures().size());
            if (orderProcedureSet.getOrderProcedures().size() > 0)
                return Status.SEND_SUCCESS;
            else
                return Status.SERVICE_TIME_OUT;
        };
        FunctionContainer.getInstance().putFunction(OrderKey.ACCEPT_ORDER, function);
        ConsumerContainer.getInstance().putConsumer(OrderKey.ACCEPT_ORDER, consumer);
    }

    public void startCount() {
        this.isCount = true;
    }

    public void stopCount() {
        this.isCount = false;
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    boolean isNeedCountTime = false;
                    long time = Tools.timeDifference(orderField.get().getDisGmtCreate()) + Other.ORDER_WATING_TIME;
                    if (time >= 0)// 判断是否还有条目能够倒计时，如果能够倒计时的话，延迟一秒，让它接着倒计时
                        isNeedCountTime = true;
                    else {
                        //计时结束还未接单就关闭当前界面
                        if (DataCache.getInstance().getOneOrder(id).getOrderState() == Status.ORDER_ACCEPT)
                            activity.finish();
                    }
                    String str = "接受订单 ";
                    str = str + ((time / 3600 >= 10) ? time / 3600 + ":" : "0" + time / 3600 + ":");
                    str = str + ((time / 60 % 60 >= 10) ? time / 60 % 60 + ":" : "0" + time / 60 % 60 + ":");
                    str = str + ((time % 60 >= 10) ? time % 60 + "" : "0" + time % 60);
                    binding.btnAcceptOrder.setText(str);
                    if (isNeedCountTime && isCount)
                        handler.sendEmptyMessageDelayed(1, 1000);
                    break;
                case 2:
                    break;
            }
        }
    };
}
