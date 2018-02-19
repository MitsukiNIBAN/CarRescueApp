package com.kuaijie.carrescue.util.socket;

import com.kuaijie.carrescue.util.socket.code.MessageDecoder;
import com.kuaijie.carrescue.util.socket.code.MessageEncoder;
import com.kuaijie.carrescue.util.socket.handler.HeartBeatReqHandler;
import com.kuaijie.carrescue.util.socket.handler.OrderHandler;
import com.kuaijie.carrescue.util.socket.handler.PushHandler;
import com.kuaijie.carrescue.util.socket.message.MessageConstant;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * Created by MitsukiSaMa on 11-3.
 */

public class ClientlInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        socketChannel.pipeline().addLast(new MessageDecoder(
                Integer.MAX_VALUE, MessageConstant.lengthFieldOffset,
                MessageConstant.lengthFieldLength,MessageConstant.lengthAdjustment,MessageConstant.initialBytesToStrip));
        socketChannel.pipeline().addLast(new MessageEncoder());
        socketChannel.pipeline().addLast(new HeartBeatReqHandler());
        socketChannel.pipeline().addLast(new OrderHandler());
        socketChannel.pipeline().addLast(new PushHandler());
    }
}
