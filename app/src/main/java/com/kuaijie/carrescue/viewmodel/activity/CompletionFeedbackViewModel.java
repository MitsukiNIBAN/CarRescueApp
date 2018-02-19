package com.kuaijie.carrescue.viewmodel.activity;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.kuaijie.carrescue.constant.OrderKey;
import com.kuaijie.carrescue.constant.OrderType;
import com.kuaijie.carrescue.constant.ServerAddress;
import com.kuaijie.carrescue.constant.Status;
import com.kuaijie.carrescue.databinding.ActivityCompletionFeedbackBinding;
import com.kuaijie.carrescue.entity.Order;
import com.kuaijie.carrescue.entity.OrderCollate;
import com.kuaijie.carrescue.util.Activity;
import com.kuaijie.carrescue.util.ConsumerContainer;
import com.kuaijie.carrescue.util.FunctionContainer;
import com.kuaijie.carrescue.util.datahelper.DataCache;
import com.kuaijie.carrescue.util.socket.SocketClient;
import com.kuaijie.carrescue.util.socket.message.Message;
import com.kuaijie.carrescue.util.threadpool.TaskThreadPool;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.adapter.Call;
import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.math.BigDecimal;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by Mitsuki on 1-05.
 */

public class CompletionFeedbackViewModel {
    private Activity activity;
    private ActivityCompletionFeedbackBinding binding;
    private Long id;

    public CompletionFeedbackViewModel(Activity activity, ActivityCompletionFeedbackBinding binding, Long id) {
        this.activity = activity;
        this.binding = binding;
        this.id = id;
    }

    public void init() {
        Order order = DataCache.getInstance().getOneOrder(id);
        try {
            //设置到达里程
            float distance = AMapUtils.calculateLineDistance(
                    new LatLng(0, 0), new LatLng(0, 0));
            binding.etKmNum.setText(distance + "km");

            if (order.getOperationTypeId() == OrderType.TRAILER) {
                //设置拖车里程
                float distance2 = AMapUtils.calculateLineDistance(
                        new LatLng(0, 0), new LatLng(0, 0));
                binding.etKmNum2.setVisibility(View.VISIBLE);
                binding.etKmNum2.setText(distance2 + "km");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void sendFeedback(Consumer consumer) {
        OrderCollate orderCollate = new OrderCollate();
        try {
            orderCollate.setOrderId(id);
            orderCollate.setWheelNum(binding.etWheelNu.getText().toString().equals("") ?
                    0 : Integer.valueOf(binding.etWheelNu.getText().toString()).byteValue());
            orderCollate.setMealFee(binding.etMeals.getText().toString().equals("") ?
                    new BigDecimal(0) : new BigDecimal(binding.etMeals.getText().toString()));
            orderCollate.setAgencyCost(binding.etAgencyCost.getText().toString().equals("") ?
                    new BigDecimal(0) : new BigDecimal(binding.etAgencyCost.getText().toString()));
            orderCollate.setMemo(binding.etOther.getText().toString());
//            System.out.println(orderCollate.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Message message = Message.getMessage(OrderKey.SUB_COLLATE, orderCollate);
        if (!SocketClient.getInstance().sendMsg(message, OrderKey.SUB_COLLATE)) {
            //发送失败
            return;
        }

        Function<Message, Integer> function = msg -> {
            Boolean b = DataCache.getInstance().readData(msg.getOptionData(), Boolean.class);
            if (b) {
                return Status.SEND_SUCCESS;
            } else {
                return Status.SERVICE_TIME_OUT;
            }
        };

        FunctionContainer.getInstance().putFunction(OrderKey.SUB_COLLATE, function);
        ConsumerContainer.getInstance().putConsumer(OrderKey.SUB_COLLATE, consumer);

    }
}
