package com.kuaijie.carrescue.util.socket.handler;


import android.util.Log;

import com.kuaijie.carrescue.util.socket.HeartBeatReceiveHelper;
import com.kuaijie.carrescue.util.socket.message.Message;
import com.kuaijie.carrescue.util.socket.message.MessageType;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by MitsukiSaMa on 11-3.
 */

public class HeartBeatReqHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Message message = (Message) msg;

        if (message.getCmdType() == MessageType.Login.value() && message.getOpStatus() == MessageType.Success.value()) {
//            HeartBeatReceiveHelper.startReceive();
            Log.i("service ref", message.toString());
        } else if (message.getCmdType() == MessageType.HEARTBEAT.value() && message.getOpStatus() == MessageType.Success.value()) {
            Log.i("service ref", "Client receive server heart beat message : ===> "
                    + message);
        } else
            ctx.fireChannelRead(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        HeartBeatReceiveHelper.stopReceive();
        ctx.fireExceptionCaught(cause);
    }
}
