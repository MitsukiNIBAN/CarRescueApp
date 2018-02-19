package com.kuaijie.carrescue.dto;

import com.kuaijie.carrescue.entity.OrderProcedure;

import org.msgpack.annotation.Index;
import org.msgpack.annotation.Message;

import java.util.List;

/**
 * Created by MitsukiSaMa on 12-14.
 */

@Message
public class OrderProcedureSet {
    @Index(0)
    private List<OrderProcedure> orderProcedures;

    public List<OrderProcedure> getOrderProcedures() {
        return orderProcedures;
    }

    public void setOrderProcedures(List<OrderProcedure> orderProcedures) {
        this.orderProcedures = orderProcedures;
    }

    @Override
    public String toString() {
        return "orderProcedureSet [orderProcedureSet=" + orderProcedures + "]";
    }
}
