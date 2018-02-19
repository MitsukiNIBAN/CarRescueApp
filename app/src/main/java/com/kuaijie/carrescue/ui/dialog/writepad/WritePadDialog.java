package com.kuaijie.carrescue.ui.dialog.writepad;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.kuaijie.carrescue.R;
import com.kuaijie.carrescue.constant.OrderKey;
import com.kuaijie.carrescue.constant.Status;
import com.kuaijie.carrescue.databinding.DialogWritePadBinding;
import com.kuaijie.carrescue.dto.OrderPhotoSet;
import com.kuaijie.carrescue.entity.OrderPhoto;
import com.kuaijie.carrescue.entity.Photo;
import com.kuaijie.carrescue.ui.dialog.loading.Loading;
import com.kuaijie.carrescue.util.Activity;
import com.kuaijie.carrescue.util.ConsumerContainer;
import com.kuaijie.carrescue.util.FunctionContainer;
import com.kuaijie.carrescue.util.PageJump;
import com.kuaijie.carrescue.util.datahelper.DataCache;
import com.kuaijie.carrescue.util.socket.SocketClient;
import com.kuaijie.carrescue.util.socket.message.Message;
import com.kuaijie.carrescue.util.threadpool.TaskThreadPool;
import com.kuaijie.carrescue.viewmodel.dialog.WritePadViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by 87706 on 11-18.
 */

public class WritePadDialog extends Dialog {

    private Long id;
    private Long pageId;
    private Activity activity;
    private Long procedureId;

    private Long type = -1l;

    public WritePadDialog(Context context, Long pageId, Long id, Long procedure) {
        super(context, R.style.DialogStyle);
        this.pageId = pageId;
        this.activity = (Activity) context;
        this.id = id;
        this.procedureId = procedure;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DialogWritePadBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_write_pad, null, false);
        setContentView(binding.getRoot());
        setCanceledOnTouchOutside(false);
        binding.setWritePadViewModel(new WritePadViewModel(this, binding));

        if (procedureId == 14l)
            binding.tvTitle.setText("免责签字");
        else if (procedureId == 38l)
            binding.tvTitle.setText("到达签字");
        else if (procedureId == 39l)
            binding.tvTitle.setText("客户签字");
        else
            binding.tvTitle.setText("签字板");


        binding.tvOkBtn.setOnClickListener(view -> {
            if (this != null) {
                Loading.getInstance().loading(activity);
                String fileName = "KJCR_" + System.currentTimeMillis() + ".png";
                Function<Message, Integer> function = msg -> {
                    Boolean b = DataCache.getInstance().readData(msg.getOptionData(), Boolean.class);
                    if (b) {
                        Photo photo = new Photo();
                        photo.setFileName(fileName);
                        photo.setFilePath("/sdcard/pictures/");
                        photo.setFileAddress();
                        List<Photo> photos = new ArrayList<>();
                        photos.add(photo);
                        TaskThreadPool.getInstance().addImgUploadTask(photos);
                        return Status.SEND_SUCCESS;
                    } else {
//                System.out.println(new MessagePack().read(msg.getOptionData()));
                        return Status.SERVICE_TIME_OUT;
                    }
                };
//                this.dismiss();
                Consumer<Integer> consumer = state -> {
                    switch (state) {
                        case Status.SERVICE_TIME_OUT:
                            new Handler(activity.getMainLooper()).post(() -> {
                                Toast.makeText(activity, "连接服务器失败", Toast.LENGTH_SHORT).show();
                            });
                            break;
                        case Status.SEND_SUCCESS:
                            PageJump.getInstance().jump(activity, pageId, id, 1);
                            break;
                        default:
                            break;
                    }
                    new Handler(getContext().getMainLooper()).post(() -> {
                        this.dismiss();
                        Loading.getInstance().unloading();
                    });
                };
                new Thread(() -> {
                    try {
                        binding.dvWritePad.saveBitmap(fileName);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    OrderPhotoSet orderPhotoSet = new OrderPhotoSet();
                    List<OrderPhoto> orderPhotos = new ArrayList<>();
                    OrderPhoto orderPhoto = new OrderPhoto();
                    orderPhoto.setPhotoName(fileName);
                    orderPhoto.setPhotoType(type.byteValue());
                    orderPhoto.setOrderId(id);
                    orderPhoto.setPhotoStaff(DataCache.getInstance().getUser().getId());
                    orderPhotos.add(orderPhoto);
                    orderPhotoSet.setPhotos(orderPhotos);
                    Message message = Message.getMessage(OrderKey.SUB_ORDER_PHOTO, orderPhotoSet);
                    if (!SocketClient.getInstance().sendMsg(message, OrderKey.SUB_ORDER_PHOTO)) {
                        //发送失败
                        return;
                    }

                    FunctionContainer.getInstance().putFunction(OrderKey.SUB_ORDER_PHOTO, function);
                    ConsumerContainer.getInstance().putConsumer(OrderKey.SUB_ORDER_PHOTO, consumer);
                }).start();

//                PageJump.getInstance().jump(activity, pageId, id, 1);
                //saveBitmap();
//                if (number == 1) {
//                    Intent intent = new Intent();
//                    intent.setClass(view.getContext(), GoToDestinationActivity.class);
//                    view.getContext().startActivity(intent);
//                } else {
//                    Intent intent = new Intent();
//                    intent.setClass(view.getContext(), CompletionFeedbackActivity.class);
//                    view.getContext().startActivity(intent);
//
//                }
//                dismiss();
//                activity.finish();
            }
        });

        binding.tvCancelBtn.setOnClickListener(view -> dismiss());
    }

}
