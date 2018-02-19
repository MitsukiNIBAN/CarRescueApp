package com.kuaijie.carrescue.util;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.kuaijie.carrescue.entity.OrderProcedure;
import com.kuaijie.carrescue.ui.base.MainActivity;
import com.kuaijie.carrescue.ui.dialog.WaitingContactDialog;
import com.kuaijie.carrescue.ui.dialog.writepad.WritePadDialog;
import com.kuaijie.carrescue.ui.technician.TaskAcceptActivity;
import com.kuaijie.carrescue.ui.technician.TaskDetailActivity;
import com.kuaijie.carrescue.ui.technician.TaskExecutionActivity;
import com.kuaijie.carrescue.util.datahelper.DataCache;

/**
 * Created by MitsukiSaMa on 12-14.
 */

public class PageJump {
    private final String TAG = "PageJump";
    private Long waitId = -9l;
    private Activity waitActivity = null;
    private Long waitPageId = -9l;

    private static class PageJumpHolder {
        private static final PageJump INSTANCE = new PageJump();
    }

    private PageJump() {
    }

    public static final PageJump getInstance() {
        return PageJumpHolder.INSTANCE;
    }

    /**
     * @param activity 当前的activity
     * @param pageId   当前页面的id
     * @param id       当前订单的id
     * @param i        child的选择 1为child1 2为child2 3为从首页发起的跳转请求
     */
    public void jump(Activity activity, Long pageId, Long id, int i) {
        Log.i(TAG, "Activity:" + activity.getClass() + "    pageId:" + pageId + "    orderId:" + id + "   child:" + i);
//        Loading.getInstance().loading(activity);
//        if (waitId == -9l || waitPageId == -9l || activity == null){
//            Log.e(TAG, "异常跳转");
//            return;
//        }
        Intent intent = new Intent();
        OrderProcedure temp = null; //当前的流程块
        OrderProcedure orderProcedure = null; //下一个流程块
        if (pageId == -4l) {
            //当前页面为首页，从缓存中读取下一页
            Long tempId = DataCache.getInstance().getOrderPageId(id);
            Log.i(TAG, "tempId" + tempId);
            if (tempId != null) {
                if (tempId == -2l) {
                    intent.setClass(activity, TaskDetailActivity.class);
                    intent.putExtra("pageId", tempId);
                    intent.putExtra("id", id);
                    activity.startActivity(intent);
                    if (!activity.getClass().equals(MainActivity.class))
                        activity.finish();
                    DataCache.getInstance().setOrderPageId(tempId, id);
                    return;
                } else if (tempId == -3l) {
                    intent.setClass(activity, TaskAcceptActivity.class);
                    intent.putExtra("pageId", tempId);
                    intent.putExtra("id", id);
                    activity.startActivity(intent);
                    if (!activity.getClass().equals(MainActivity.class))
                        activity.finish();
                    DataCache.getInstance().setOrderPageId(tempId, id);
                    return;
                } else
                    orderProcedure = DataCache.getInstance().getOrderProcedure(tempId, id);
            } else {
                intent.setClass(activity, TaskAcceptActivity.class);
                intent.putExtra("pageId", -3l);
                intent.putExtra("id", id);
                activity.startActivity(intent);
//                Loading.getInstance().unloading();
                if (!activity.getClass().equals(MainActivity.class))
                    activity.finish();
                DataCache.getInstance().setOrderPageId(-3l, id);
                return;
            }
        } else if (pageId == -3l) {
            //当前页面为接单页面，下一个界面为taskdetail的界面，pageid为-2l
            intent.setClass(activity, TaskDetailActivity.class);
            intent.putExtra("pageId", -2l);
            intent.putExtra("id", id);
            activity.startActivity(intent);
//            Loading.getInstance().unloading();
            //非首页的时候关闭activity【虽然情况一般不可能，预防异常】
            if (!activity.getClass().equals(MainActivity.class))
                activity.finish();
            DataCache.getInstance().setOrderPageId(-2l, id);
            return;
        }
        //剩下的情况均为正常的跳转
        if (null == orderProcedure)
            if (pageId == -2l) {
                //如果是从任务详情界面开始的
                //则下一个流程块为第一个流程块
                orderProcedure = DataCache.getInstance().getOrderProcedure(-1024l, id);
            } else {
                //如果不是从任务详情界面开始
                //则先获取当前流程块，再通过child获得下一个流程块
                temp = DataCache.getInstance().getOrderProcedure(pageId, id);
                if (temp != null) {
                    if (1 == i) {
                        orderProcedure = DataCache.getInstance().getOrderProcedure(temp.getChild1(), id);
                    } else if (2 == i) {
                        orderProcedure = DataCache.getInstance().getOrderProcedure(temp.getChild2(), id);
                    } else return; //传参错误，直接return结束
                } else {
                    Log.e(TAG, "page(" + pageId + ") is null");
                }
            }

        if (null == orderProcedure) {
            //如果找不到最后一个流程了，则表明当前流程块是最后一个流程块了
            Log.e(TAG, "Procedure finish");
            DataCache.getInstance().removePageId(id);
            if (!activity.getClass().equals(MainActivity.class))
                activity.finish();
            return;
        } else {
            //有下一个流程块，则按照流程块开始跳转
            //按照页面类型进行跳转
            Class cls = com.kuaijie.carrescue.constant.OrderProcedure
                    .getNextPageClass(orderProcedure.getProcedureId());
            Log.i(TAG, "Next page type：" + orderProcedure.getProcedureId() + "::::" + cls);
            if (cls != null) {
                //非空则为activity跳转
                intent.setClass(activity, cls);
                intent.putExtra("id", id);
                intent.putExtra("pageId", orderProcedure.getId());
                intent.putExtra("procedureId", orderProcedure.getProcedureId());
                if (cls.equals(TaskExecutionActivity.class)) {
                    //如果是拍照任务，则额外加入拍照类型以及拍照数量。
                    intent.putExtra("photoType", orderProcedure.getPictureType());
                    intent.putExtra("photoNum", orderProcedure.getPictureNum());
                }
                activity.startActivity(intent);
//                Loading.getInstance().unloading();
                if (!activity.getClass().equals(MainActivity.class))
                    activity.finish();
            } else {
                //为空则是打开dialog
                //具体分类
                if (orderProcedure.getProcedureId() == 11l ||
                        orderProcedure.getProcedureId() == 42l) {
                    //联系客户等待连接dialog
                    final OrderProcedure tempOP = orderProcedure;
                    new Handler(activity.getMainLooper()).post(() -> {
                        new WaitingContactDialog(activity, tempOP.getId(),
                                id, tempOP.getProcedureId()).show();
//                        Loading.getInstance().unloading();
                    });
                    this.waitActivity = activity;
                    this.waitPageId = tempOP.getId();
                    this.waitId = id;
                } else if (orderProcedure.getProcedureId() == 14l ||
                        orderProcedure.getProcedureId() == 38l ||
                        orderProcedure.getProcedureId() == 39l) {
                    //签字dialog

                    final OrderProcedure tempOP = orderProcedure;
                    new Handler(activity.getMainLooper()).post(() -> {
                        WritePadDialog writePadDialog = new WritePadDialog(activity,
                                tempOP.getId(), id, tempOP.getProcedureId());
                        writePadDialog.show();
//                        Loading.getInstance().unloading();
                    });
                }
            }
            DataCache.getInstance().setOrderPageId(orderProcedure.getId(), id);
        }

    }

    public void jumpNext(){
        jump(this.waitActivity, this.waitPageId, this.waitId, 1);
        this.waitActivity = null;
        this.waitId = -9l;
        this.waitPageId = -9l;
    }
}
