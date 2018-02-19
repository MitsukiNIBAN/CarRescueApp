package com.kuaijie.carrescue.viewmodel.fragment;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.kuaijie.carrescue.adapter.OrderListAdapter;
import com.kuaijie.carrescue.constant.OrderKey;
import com.kuaijie.carrescue.constant.Other;
import com.kuaijie.carrescue.constant.Status;
import com.kuaijie.carrescue.databinding.FragmentMainContentBinding;
import com.kuaijie.carrescue.dto.OrderSet;
import com.kuaijie.carrescue.dto.UserDTO;
import com.kuaijie.carrescue.entity.Order;
import com.kuaijie.carrescue.util.ConsumerContainer;
import com.kuaijie.carrescue.util.FunctionContainer;
import com.kuaijie.carrescue.util.Tools;
import com.kuaijie.carrescue.util.datahelper.DataCache;
import com.kuaijie.carrescue.util.socket.SocketClient;
import com.kuaijie.carrescue.util.socket.message.Message;
import com.kuaijie.carrescue.util.texttospeech.AppTTSController;
import com.kuaijie.carrescue.util.texttospeech.TaskHint;

import java.util.List;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by MitsukiSaMa on 12-4.
 */

public class OrderListViewModel {
    private Activity activity;
    private FragmentMainContentBinding binding;

    private OrderListAdapter orderListAdapter;
    private String key;

    private Boolean isCount = false;
    private String time = null;

    public OrderListViewModel(Activity activity, FragmentMainContentBinding fragmentMainContentBinding, String k) {
        this.activity = activity;
        this.binding = fragmentMainContentBinding;
        this.key = k;
    }

    public void init() {
        orderListAdapter = new OrderListAdapter(activity, null);
        binding.setAdapter(orderListAdapter);

        binding.srlIndexDownRefresh.setOnRefreshListener(() -> refreshList());
        binding.srlIndexDownRefreshEmpty.setOnRefreshListener(() -> refreshList());
    }

    public void refreshList() {
        binding.srlIndexDownRefresh.setRefreshing(true);
        binding.srlIndexDownRefreshEmpty.setRefreshing(true);

        try {
            UserDTO userDTO = DataCache.getInstance().getUser();
            Long orderStaff = new Long(userDTO.getId());
            Message message = Message.getMessage(key, orderStaff);
            if (!SocketClient.getInstance().sendMsg(message, key)) {
                binding.srlIndexDownRefresh.setRefreshing(false);
                binding.srlIndexDownRefreshEmpty.setRefreshing(false);
                Toast.makeText(activity, "与服务器失去连接", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            binding.srlIndexDownRefresh.setRefreshing(false);
            binding.srlIndexDownRefreshEmpty.setRefreshing(false);
            return;
        }

        Function<Message, Integer> function = msg -> {
            DataCache.getInstance().saveOrder(msg.getOptionData(), key);
            return Status.CACHING_COMPLETION;
        };
        Log.i("OrderListViewModel", key);
        FunctionContainer.getInstance().putFunction(key, function);

        Consumer<Integer> consumer = state -> {
            try {
                switch (state) {
                    case Status.CACHING_COMPLETION:
                        refreshData();
                        break;
                    case Status.SERVICE_TIME_OUT:
                        new Handler(activity.getMainLooper()).post(() ->
                                Toast.makeText(activity, "服务器超时", Toast.LENGTH_SHORT).show());
                        break;
                    default:
                        break;
                }
                new Handler(activity.getMainLooper()).post(() -> {
                    binding.srlIndexDownRefresh.setRefreshing(false);
                    binding.srlIndexDownRefreshEmpty.setRefreshing(false);
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        ConsumerContainer.getInstance().putConsumer(key, consumer);
    }

    public void refreshData() {
        //刷新UI 数据
        Log.e("refreshData", "refreshData");
        OrderSet orderSet = DataCache.getInstance().getOrder(key);
        //数据筛选
        //未接单的订单，并且计时已经到了的进行剔除
        if (orderSet != null) {
            List<Order> orders = orderSet.getOrders();
            Log.e("orders.size:", orders.size()+"");
            orderListAdapter.getItems().clear();
            if (key.equals(OrderKey.UNDO_ORDER_LIST)) {
                for (int i = 0; i < orderSet.getOrders().size(); i++) {
                    long time = Tools.timeDifference(orderSet.getOrders().get(i).getDisGmtCreate()) + Other.ORDER_WATING_TIME;
                    if (time < 0 && orderSet.getOrders().get(i).getOrderState() == Status.ORDER_ACCEPT) {
//                        orders.remove(i);
                    } else if (orderSet.getOrders().get(i).getOrderState() == Status.ORDER_COMPLETE) {
                    } else {
                        orderListAdapter.getItems().add(orderSet.getOrders().get(i));
                    }
                }
            } else {
                orderListAdapter.getItems().addAll(orders);
                new Handler(activity.getMainLooper()).post(()
                        -> orderListAdapter.notifyDataSetChanged());
                Log.e("orders.size:", orderListAdapter.getCount()+"");
            }

        }
        //开启倒计时
        new Handler(activity.getMainLooper()).post(() -> {
//            orderListAdapter.notifyDataSetChanged();
            isCount = true;
            if (key.equals(OrderKey.UNDO_ORDER_LIST))
                handler.sendEmptyMessage(1);
        });
    }

    public Long saveOrderAndGetId(int i) {
        Order order = orderListAdapter.getItems().get(i);
//        OrderSet orderSet = DataCache.getInstance().getOrder(key);
//        Order od = orderSet.getOrders().get(i);
        DataCache.getInstance().saveOneOrder(order);
        return order.getId();
    }

    public void startCount() {
        this.isCount = true;
    }

    public void stopCount() {
//        count = 0;
        this.isCount = false;
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
//                    Log.e("TAG", "倒计时");
                    boolean isNeedCountTime = false;
                    for (int index = 0; index < orderListAdapter.getItems().size(); index++) {
                        Order o = orderListAdapter.getItems().get(index);
                        long time = Tools.timeDifference(o.getDisGmtCreate()) + Other.ORDER_WATING_TIME;
                        if (time >= 0 && o.getOrderState() == Status.ORDER_ACCEPT)// 判断是否还有条目能够倒计时，如果能够倒计时的话，延迟一秒，让它接着倒计时
                            isNeedCountTime = true;
                        else {
                            //没有倒计时就从列表中移除，不再允许接单
                            if (o.getOrderState() == Status.ORDER_ACCEPT) {
                                orderListAdapter.getItems().remove(o);
                            }
                        }
                    }
                    orderListAdapter.notifyDataSetChanged();
                    if (isNeedCountTime && isCount) {
                        //接单提醒
                        time = Tools.timing(time, Other.INTERVAL, () -> {
                            AppTTSController appTTSController = AppTTSController.getInstance(activity.getApplicationContext());
                            appTTSController.init();
                            TaskHint taskHint = appTTSController;
                            taskHint.addHint("您有新的未接订单，请尽快接单");
                        });
                        handler.sendEmptyMessageDelayed(1, 1000);
                    }
                    break;
            }
        }
    };
}
