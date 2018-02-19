package com.kuaijie.carrescue.dto;

import com.kuaijie.carrescue.entity.OrderCollate;
import com.kuaijie.carrescue.entity.OrderPhoto;

import org.msgpack.annotation.Index;
import org.msgpack.annotation.Message;


@Message
public class OrderCollatePhotoSet {
    @Index(0)
    private OrderPhoto orderPhoto;
    @Index(1)
    private OrderCollate orderCollate;
    @Index(2)
    private int type;

    public OrderPhoto getOrderPhoto() {
        return orderPhoto;
    }

    public void setOrderPhoto(OrderPhoto orderPhotos) {
        this.orderPhoto = orderPhotos;
    }

    public OrderCollate getOrderCollate() {
        return orderCollate;
    }

    public void setOrderCollate(OrderCollate orderCollate) {
        this.orderCollate = orderCollate;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "OrderCollatePhotoSet [orderPhotos=" + orderPhoto + ", orderCollate=" + orderCollate + "]";
    }


}
