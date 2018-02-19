package com.kuaijie.carrescue.util.datahelper;

import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.kuaijie.carrescue.constant.OrderKey;
import com.kuaijie.carrescue.constant.Status;
import com.kuaijie.carrescue.dto.OrderProcedureSet;
import com.kuaijie.carrescue.dto.OrderSet;
import com.kuaijie.carrescue.dto.UserDTO;
import com.kuaijie.carrescue.entity.Order;
import com.kuaijie.carrescue.entity.OrderProcedure;
import com.kuaijie.carrescue.util.Application;

import org.msgpack.MessagePack;

import java.io.IOException;

/**
 * Created by MitsukiSaMa on 11-27.
 */

public class DataCache {

    private static final String TAG = "DataCache";

    private ACache aCache;
    private MessagePack messagePack;

    private static class DataCacheHolder {
        private static final DataCache INSTANCE = new DataCache();
    }

    private DataCache() {
    }

    public static final DataCache getInstance() {
        return DataCacheHolder.INSTANCE;
    }

    public void init() {
        if (aCache == null)
            aCache = ACache.get(Application.getInstance());
        if (messagePack == null)
            messagePack = new MessagePack();
    }

    public void saveUser(byte[] value) {
        aCache.put("user", value);
    }

    public UserDTO getUser() {
        try {
            byte[] bytes = aCache.getAsBinary("user");
            Log.i(TAG, "Get user===>" + bytes.toString());
            UserDTO userDTO = messagePack.read(bytes, UserDTO.class);
            return userDTO;
        } catch (Exception e) {
            new Handler(Application.getInstance().getMainLooper()).post(() -> {
                Toast.makeText(Application.getInstance(), "数据解析异常", Toast.LENGTH_SHORT).show();
            });
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 缓存所有订单信息
     *
     * @param value
     * @param key
     */
    public void saveOrder(byte[] value, String key) {
//        try {
//            OrderSet orderSet = messagePack.read(value, OrderSet.class);
//            Log.e(TAG, orderSet.getOrders().get(0).toString());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        aCache.put(key, value);
    }

    /**
     * 获取所有订单信息
     *
     * @param key
     * @return
     */
    public OrderSet getOrder(String key) {
        try {
            byte[] bytes = aCache.getAsBinary(key);
            Log.i(TAG, "Get orderSet===>" + bytes);
            OrderSet orderSet = messagePack.read(bytes, OrderSet.class);
//            if (orderSet != null)
//                Log.i(TAG, "Size===>" + orderSet.getOrders().size());
            return orderSet;
        } catch (Exception e) {
//            new Handler(Application.getInstance().getMainLooper()).post(() -> {
//                Toast.makeText(Application.getInstance(), "数据解析异常", Toast.LENGTH_SHORT).show();
//            });
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 缓存单个订单信息
     *
     * @param order
     */
    public void saveOneOrder(Order order) {
        Log.i(TAG, "Save order===>" + order);
        byte[] bytes = null;
        try {
            bytes = messagePack.write(order);
            aCache.put(order.getId() + "", bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取单个订单信息
     *
     * @param key 订单id
     * @return
     */
    public Order getOneOrder(Long key) {
        try {
            byte[] bytes = aCache.getAsBinary(key + "");
            Log.i(TAG, "Get order===>" + key);
            Order order = messagePack.read(bytes, Order.class);
            return order;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 缓存订单流程
     *
     * @param value
     * @param key   订单id
     */
    public void saveOrderProcedure(byte[] value, Long key) {
        Log.i(TAG, "Save procedure===>" + key);
        aCache.put(key + "Procedure", value);
    }

    /**
     * 获取流程块
     *
     * @param pageId 页面的id号
     * @param key    订单id
     * @return 返回一个流程块
     */
    public OrderProcedure getOrderProcedure(Long pageId, Long key) {
        byte[] bytes = aCache.getAsBinary(key + "Procedure");
        try {
            OrderProcedureSet orderProcedureSet = messagePack.read(bytes, OrderProcedureSet.class);
            if (pageId.equals(-1024l)) {
                for (OrderProcedure orderProcedure : orderProcedureSet.getOrderProcedures()) {
                    Log.e(TAG, orderProcedure.getStage() + "");
                    if (orderProcedure.getStage().longValue() == 1l)
                        return orderProcedure;
                }
            } else
                for (OrderProcedure orderProcedure : orderProcedureSet.getOrderProcedures()) {
                    if (orderProcedure.getId().longValue() == pageId)
                        return orderProcedure;
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param pageId
     * @param key
     */
    public void setOrderPageId(Long pageId, Long key) {
        Log.i(TAG, "Save " + key + "'s pageId:" + pageId);
        aCache.put(key + "pageId", pageId + "");
    }

    public Long getOrderPageId(Long key) {
        String str = aCache.getAsString(key + "pageId");
        Log.i(TAG, "get " + key + "'s pageId:" + str);
        if (str == null)
            return null;
        else {
            Long l = new Long(str);
            return l;
        }
    }

    public void removePageId(Long key) {
        aCache.remove(key + "pageId");
    }

    public void clear() {
        aCache.clear();
    }

    public <T> T readData(byte[] bytes, Class<T> c) throws IOException {
        Log.i(TAG, "read:" + bytes.length);
        return messagePack.read(bytes, c);
    }

    /**
     * 修改订单状态为完成
     * @param key
     */

    public void cmpOrder(Long key){
        try {
            byte[] bytes = aCache.getAsBinary(OrderKey.UNDO_ORDER_LIST);
            OrderSet orderSet = messagePack.read(bytes, OrderSet.class);
            for (int i = 0; i < orderSet.getOrders().size(); i++){
                if (orderSet.getOrders().get(i).getId() == key){
                    Order order = orderSet.getOrders().remove(i);
                    order.setOrderState(Status.ORDER_COMPLETE);
                    orderSet.getOrders().add(order);
                    break;
                }
            }
            bytes = messagePack.write(orderSet);
            aCache.put(OrderKey.UNDO_ORDER_LIST, bytes);
            aCache.remove(key + "");
            aCache.remove(key + "Procedure");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void acceptOrder(Long key){
        try {
            byte[] bytes = aCache.getAsBinary(OrderKey.UNDO_ORDER_LIST);
            OrderSet orderSet = messagePack.read(bytes, OrderSet.class);
            for (int i = 0; i < orderSet.getOrders().size(); i++){
                if (orderSet.getOrders().get(i).getId() == key){
                    Order order = orderSet.getOrders().remove(i);
                    order.setOrderState(Status.ORDER_DOING);
                    orderSet.getOrders().add(order);
                    break;
                }
            }
            bytes = messagePack.write(orderSet);
            aCache.put(OrderKey.UNDO_ORDER_LIST, bytes);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
