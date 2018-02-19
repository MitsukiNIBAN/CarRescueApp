package com.kuaijie.carrescue.util.socket.handler;

import android.util.Log;

import com.kuaijie.carrescue.constant.OrderKey;
import com.kuaijie.carrescue.util.ConsumerContainer;
import com.kuaijie.carrescue.util.FunctionContainer;
import com.kuaijie.carrescue.util.socket.SocketClient;
import com.kuaijie.carrescue.util.socket.message.Message;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class OrderHandler extends SimpleChannelInboundHandler<Object> {
    private static final String TAG = "OrderHandler";

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        Message message = (Message) msg;
        byte[] urlBytes = message.getHead().getUrl();
        String url = new String(urlBytes, "utf-8");
        Log.i(TAG, url);
        //		if (consumer == null) {
//			ctx.fireChannelRead(msg);
//		}
        try {
            Consumer consumer = ConsumerContainer.getInstance().getConsumer(url);
            Function function = FunctionContainer.getInstance().getFunction(url);

            SocketClient.getInstance().removeTimerTask(url);
            //found不到任何一个都会抛出异常
            //然后就不会订阅
            if (consumer == null || function == null) {
                ctx.fireChannelRead(msg);
                return;
            }

            Observable observable = Observable.just(message);
            new Thread(() -> {
                try {
                    Log.i(TAG, "OrderHandler");
                    observable.map(function).subscribe(consumer);
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