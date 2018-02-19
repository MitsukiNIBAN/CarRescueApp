package com.kuaijie.carrescue.dto;

import com.kuaijie.carrescue.entity.Order;

import org.msgpack.annotation.Message;

import java.util.List;

/**
 * Created by MitsukiSaMa on 12-4.
 */

@Message
public class OrderSet {
    private List<Order> orders;
    public List<Order> getOrders() {
        return orders;
    }
    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
    @Override
    public String toString() {
        return "OrderSet [orders=" + orders + "]";
    }
}
