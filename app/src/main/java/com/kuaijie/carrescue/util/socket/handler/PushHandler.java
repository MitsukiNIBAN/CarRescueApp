package com.kuaijie.carrescue.util.socket.handler;

import android.util.Log;

import com.kuaijie.carrescue.util.PushMsgContainer;
import com.kuaijie.carrescue.util.socket.SocketClient;
import com.kuaijie.carrescue.util.socket.message.Message;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by Mitsuki on 1-05.
 */

public class PushHandler extends SimpleChannelInboundHandler<Object> {
    private static final String TAG = "PushHandler";

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        Message message = (Message) msg;
        byte[] urlBytes = message.getHead().getUrl();
        String url = new String(urlBytes, "utf-8");
        //		if (consumer == null) {
//			ctx.fireChannelRead(msg);
//		}
        try {
            Consumer consumer = PushMsgContainer.getInstance().getConsumer(url);
            Function function = PushMsgContainer.getInstance().getFunction(url);

            SocketClient.getInstance().removeTimerTask(url);
            //found不到任何一个都会抛出异常
            //然后就不会订阅

            Observable observable = Observable.just(message);
            new Thread(() -> {
                try {
                    Log.i(TAG, "PushHandler");
                    if (function != null) observable.map(function);
                    observable.subscribe(consumer);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }).start();
        } catch (Exception e) {
            Log.e(TAG, url + "time out");
            //主动异常超时
            SocketClient.getInstance().removeTimerTask(url);
            e.printStackTrace();
        }
    }
}
