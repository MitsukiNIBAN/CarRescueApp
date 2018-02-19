package com.kuaijie.carrescue.util;

import android.os.Handler;
import android.util.Log;

import com.kuaijie.carrescue.constant.OrderKey;
import com.kuaijie.carrescue.constant.Status;
import com.kuaijie.carrescue.dto.UserDTO;
import com.kuaijie.carrescue.entity.Order;
import com.kuaijie.carrescue.ui.base.MainActivity;
import com.kuaijie.carrescue.util.datahelper.DataCache;
import com.kuaijie.carrescue.util.socket.SocketClient;
import com.kuaijie.carrescue.util.socket.message.Message;
import com.kuaijie.carrescue.util.texttospeech.IFlyTTS;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * 存放收到推送时对数据处理的方法
 * Created by Mitsuki on 1-05.
 */

public class PushMsgContainer {
    private static class PushMsgContainerHolder {
        private static final PushMsgContainer INSTANCE = new PushMsgContainer();
    }

    private PushMsgContainer() {
    }

    public static final PushMsgContainer getInstance() {
        return PushMsgContainer.PushMsgContainerHolder.INSTANCE;
    }

    private ConcurrentMap<String, Consumer> consumerMap = new ConcurrentHashMap<>();
    private ConcurrentMap<String, Function> functionMap = new ConcurrentHashMap<>();

    public Consumer getConsumer(String key) {
        return consumerMap.get(key);
    }

    public void putConsumer(String key, Consumer consumer) {
        consumerMap.put(key, consumer);
    }

    public Function getFunction(String key) {
        return functionMap.get(key);
    }

    public void putFunction(String key, Function function) {
        functionMap.put(key, function);
    }

    public Consumer removeConsumer(String key) {
        return consumerMap.remove(key);
    }

    public void init() {
        //在此加入各种收到的推送的处理
        /**
         * 收到新的订单
         */
        Consumer<Message> newTask = msg -> {
            Log.e("PushMsgContainer", "收到新的订单");
            //任务栏显示通知，语音播报
            TaskNotification.getInstance().newTask();

            //更新本地缓存数据
            handler.sendEmptyMessage(1);
            //更新list UI [已写于consumer中]
//            Activity activity = Application.getInstance().getActivity(MainActivity.class);
//            if (activity != null) {
//                if (activity.)
//                activity.onResume();
//            }
        };
        consumerMap.put(OrderKey.ADD_NOTIFY, newTask);

        /**
         * 已联系客户可进行下一步
         */
        Consumer<Message> cmnCli = msg -> {
            //通知联系客户dialog进行下一步跳转
            PageJump.getInstance().jumpNext();
        };
        consumerMap.put("", cmnCli);

        /**
         * 调度中心指令
         */
        Consumer<Message> disCmd = msg -> {
            //根据指令进行操作

        };
        consumerMap.put("disCmd", disCmd);
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    UserDTO userDTO = DataCache.getInstance().getUser();
                    Long orderStaff = new Long(userDTO.getId());
                    Message message = Message.getMessage(OrderKey.UNDO_ORDER_LIST, orderStaff);
                    SocketClient.getInstance().sendMsg(message, OrderKey.UNDO_ORDER_LIST);
                    Function<Message, Integer> function = m -> {
                        DataCache.getInstance().saveOrder(m.getOptionData(), OrderKey.UNDO_ORDER_LIST);
                        return Status.CACHING_COMPLETION;
                    };
                    FunctionContainer.getInstance().putFunction(OrderKey.UNDO_ORDER_LIST, function);

                    if (getConsumer(OrderKey.UNDO_ORDER_LIST) == null) {
                        Consumer<Integer> consumer = state -> {};
                        ConsumerContainer.getInstance().putConsumer(OrderKey.UNDO_ORDER_LIST, consumer);
                    } else {
                        Consumer consumer = getConsumer(OrderKey.UNDO_ORDER_LIST);
                        ConsumerContainer.getInstance().putConsumer(OrderKey.UNDO_ORDER_LIST, consumer);
                    }
                    break;
            }
        }
    };
}
