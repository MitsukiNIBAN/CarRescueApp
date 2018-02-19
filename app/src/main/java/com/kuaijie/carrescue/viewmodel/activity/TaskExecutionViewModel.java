package com.kuaijie.carrescue.viewmodel.activity;

import android.content.Intent;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.widget.Toast;

import com.kuaijie.carrescue.adapter.PhotoListAdapter;
import com.kuaijie.carrescue.constant.OrderKey;
import com.kuaijie.carrescue.constant.Other;
import com.kuaijie.carrescue.constant.PhotoType;
import com.kuaijie.carrescue.constant.Status;
import com.kuaijie.carrescue.databinding.ActivityTaskExecutionBinding;
import com.kuaijie.carrescue.dto.OrderPhotoSet;
import com.kuaijie.carrescue.entity.OrderPhoto;
import com.kuaijie.carrescue.entity.OrderProcedure;
import com.kuaijie.carrescue.entity.Photo;
import com.kuaijie.carrescue.ui.dialog.loading.Loading;
import com.kuaijie.carrescue.util.Activity;
import com.kuaijie.carrescue.util.ConsumerContainer;
import com.kuaijie.carrescue.util.FunctionContainer;
import com.kuaijie.carrescue.util.datahelper.DataCache;
import com.kuaijie.carrescue.util.socket.SocketClient;
import com.kuaijie.carrescue.util.socket.message.Message;
import com.kuaijie.carrescue.util.threadpool.TaskThreadPool;
import com.kuaijie.carrescue.view.swipecardrecyclerview.SwipeCardCallback;
import com.kuaijie.carrescue.view.swipecardrecyclerview.SwipeCardLayoutManager;

import org.msgpack.MessagePack;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by MitsukiSaMa on 12-12.
 */

public class TaskExecutionViewModel {
    private Activity activity;
    private ActivityTaskExecutionBinding binding;

    private PhotoListAdapter photoListAdapter;

    private Long id;
    private Long pageId;
    private Long photoType;
    private Integer photoNum;

    public TaskExecutionViewModel(Activity activity, ActivityTaskExecutionBinding binding, Long id) {
        this.activity = activity;
        this.binding = binding;
        this.id = id;
    }

    public void init() {
        Intent in = activity.getIntent();
        pageId = in.getLongExtra("pageId", Long.MIN_VALUE);
        photoType = in.getLongExtra("photoType", Long.MIN_VALUE);
        photoNum = in.getIntExtra("photoNum", Integer.MAX_VALUE);

        photoListAdapter = new PhotoListAdapter(activity, new ArrayList<>());
        binding.rvPhotoList.setLayoutManager(new SwipeCardLayoutManager());
        ItemTouchHelper.Callback callback = new SwipeCardCallback(binding.rvPhotoList, photoListAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(binding.rvPhotoList);
        binding.rvPhotoList.setAdapter(photoListAdapter);
        binding.rvPhotoList.setEmptyView(binding.ivEmpty);
    }

    public String getTitle() {
        return PhotoType.getStr(photoType);
    }

    public void addPhoto(String fileName) {
        Photo photo = new Photo();
        photo.setFileName(fileName);
        photo.setFilePath(Environment.getExternalStorageDirectory() + "/CarRescue/PhotoCache/");
        photo.setFileAddress();
        photo.setId(photoListAdapter.getItemCount() + 1);
        photoListAdapter.getItems().add(photo);
    }

    public void upLoadImg(Consumer consumer) {
        if (photoListAdapter.getItems().size() < photoNum) {
            //此为ui部分，理因不写在此
            Toast.makeText(activity, "照片数量不足,请拍够" + photoNum + "张照片", Toast.LENGTH_SHORT).show();
            Loading.getInstance().unloading();
            return;
        }
        //图片在此提交
        OrderPhotoSet orderPhotoSet = new OrderPhotoSet();
        List<OrderPhoto> orderPhotos = new ArrayList<>();
        for (Photo photo : photoListAdapter.getItems()) {
            OrderPhoto orderPhoto = new OrderPhoto();
            orderPhoto.setPhotoName(photo.getFileName());
            orderPhoto.setPhotoType(photoType.byteValue());
            orderPhoto.setOrderId(id);
            orderPhoto.setPhotoStaff(DataCache.getInstance().getUser().getId());
            orderPhotos.add(orderPhoto);
        }
        orderPhotoSet.setPhotos(orderPhotos);
        Message message = Message.getMessage(OrderKey.SUB_ORDER_PHOTO, orderPhotoSet);
        if (!SocketClient.getInstance().sendMsg(message, OrderKey.SUB_ORDER_PHOTO)) {
            //发送失败
            return;
        }

        Function<Message, Integer> function = msg -> {
            Boolean b = DataCache.getInstance().readData(msg.getOptionData(), Boolean.class);
            if (b) {
                TaskThreadPool.getInstance().addImgUploadTask(photoListAdapter.getItems());
                return Status.SEND_SUCCESS;
            } else {
//                System.out.println(new MessagePack().read(msg.getOptionData()));
                return Status.SERVICE_TIME_OUT;
            }
        };

        FunctionContainer.getInstance().putFunction(OrderKey.SUB_ORDER_PHOTO, function);
        ConsumerContainer.getInstance().putConsumer(OrderKey.SUB_ORDER_PHOTO, consumer);
    }
}
