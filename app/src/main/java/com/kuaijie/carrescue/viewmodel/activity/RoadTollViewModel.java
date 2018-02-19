package com.kuaijie.carrescue.viewmodel.activity;

import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.iflytek.cloud.thirdparty.B;
import com.kuaijie.carrescue.constant.OrderKey;
import com.kuaijie.carrescue.constant.Status;
import com.kuaijie.carrescue.databinding.ActivityRoadTollBinding;
import com.kuaijie.carrescue.dto.OrderCollatePhotoSet;
import com.kuaijie.carrescue.entity.OrderCollate;
import com.kuaijie.carrescue.entity.OrderPhoto;
import com.kuaijie.carrescue.entity.Photo;
import com.kuaijie.carrescue.util.Activity;
import com.kuaijie.carrescue.util.ConsumerContainer;
import com.kuaijie.carrescue.util.FunctionContainer;
import com.kuaijie.carrescue.util.datahelper.DataCache;
import com.kuaijie.carrescue.util.socket.SocketClient;
import com.kuaijie.carrescue.util.socket.message.Message;
import com.kuaijie.carrescue.util.threadpool.TaskThreadPool;

import org.msgpack.MessagePack;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by Mitsuki on 12-29.
 */

public class RoadTollViewModel {
    private final String TAG = "RoadTollViewModel";

    private Activity activity;
    private ActivityRoadTollBinding binding;

    private Long id;
    private String photoName = "";

    public RoadTollViewModel(Activity activity, ActivityRoadTollBinding binding, Long id) {
        this.activity = activity;
        this.binding = binding;
        this.id = id;
    }


    public void init() {

    }

    public void setFileName(String str) {
        if (!str.equals("")) {
            photoName = "";
            photoName += str;
        }
    }

    public void submitToll(Consumer consumer) {
        //费用提交
        OrderCollatePhotoSet orderCollatePhotoSet = new OrderCollatePhotoSet();
//        orderCollatePhotoSet.setType(1);
        OrderCollate orderCollate = new OrderCollate();
        if (!binding.etInputCost.getText().toString().equals("")) {
            orderCollate.setRoadToll(new BigDecimal(binding.etInputCost.getText().toString()));
        } else {
            orderCollate.setRoadToll(new BigDecimal("0"));
        }
        orderCollate.setOrderId(id);
        orderCollatePhotoSet.setOrderCollate(orderCollate);
        if (photoName.equals("")) {
            orderCollatePhotoSet.setOrderPhoto(null);
        } else {
            OrderPhoto orderPhoto = new OrderPhoto();
            orderPhoto.setPhotoName(photoName);
            orderPhoto.setOrderId(id);
//            orderPhoto.setPhotoType();
            orderPhoto.setPhotoStaff(DataCache.getInstance().getUser().getId());
            orderCollatePhotoSet.setOrderPhoto(orderPhoto);
        }
        Message message = Message.getMessage(OrderKey.SUB_EXPENSE, orderCollatePhotoSet);
        if (!SocketClient.getInstance().sendMsg(message, OrderKey.SUB_EXPENSE)) {
            //发送失败
            return;
        }

        Function<Message, Integer> function = msg -> {
            try {
                Boolean b = DataCache.getInstance().readData(msg.getOptionData(), Boolean.class);
                if (b) {
                    if (!photoName.equals("")) {
                        Photo photo = new Photo();
                        photo.setFileName(photoName);
                        photo.setFilePath(Environment.getExternalStorageDirectory() + "/CarRescue/PhotoCache/");
                        photo.setFileAddress();
                        List<Photo> photos = new ArrayList<>();
                        photos.add(photo);
                        TaskThreadPool.getInstance().addImgUploadTask(photos);
                    }
                    return Status.SEND_SUCCESS;
                } else return Status.SERVICE_TIME_OUT;
            } catch (Exception e) {
                e.printStackTrace();
                return Status.SERVICE_TIME_OUT;
            }
        };
        FunctionContainer.getInstance().putFunction(OrderKey.SUB_EXPENSE, function);
        ConsumerContainer.getInstance().putConsumer(OrderKey.SUB_EXPENSE, consumer);
    }
}
