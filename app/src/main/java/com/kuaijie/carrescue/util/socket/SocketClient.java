package com.kuaijie.carrescue.util.socket;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;


import com.kuaijie.carrescue.constant.OrderKey;
import com.kuaijie.carrescue.constant.Other;
import com.kuaijie.carrescue.constant.ServerAddress;
import com.kuaijie.carrescue.constant.Status;
import com.kuaijie.carrescue.dto.UserDTO;
import com.kuaijie.carrescue.util.Application;
import com.kuaijie.carrescue.util.ConsumerContainer;
import com.kuaijie.carrescue.util.FunctionContainer;
import com.kuaijie.carrescue.util.socket.message.Message;
import com.kuaijie.carrescue.util.threadpool.TaskThreadPool;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by MitsukiSaMa on 11-3.
 */

public class SocketClient {
    private static final String TAG = "SocketClient";

    private boolean isConnect = false;
    private EventLoopGroup group;
    private Channel channel;

    private long reconnectIntervalTime = 1000;

    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
    private Timer timer = new Timer();
    private HashMap<String, TimerTask> timerPool = new HashMap<>();


    private static class SocketClientHolder {
        private static final SocketClient INSTANCE = new SocketClient();
    }

    private SocketClient() {
    }

    public static final SocketClient getInstance() {
        return SocketClientHolder.INSTANCE;
    }

    public synchronized void connect() {
        if (!isConnect) {
            Log.i(TAG, "Start connect");
            group = new NioEventLoopGroup();
            Bootstrap b = new Bootstrap();
            try {
                b.channel(NioSocketChannel.class);
                b.option(ChannelOption.SO_KEEPALIVE, true);
                b.group(group);
                b.handler(new ClientlInitializer());

                Log.i(TAG, ServerAddress.IP + ":" + ServerAddress.PORT);
                ChannelFuture future = b.connect(ServerAddress.IP, ServerAddress.PORT)
                        .addListener((ChannelFutureListener) channelFuture -> {
                            if (channelFuture.isSuccess()) {
                                isConnect = true;
                                channel = channelFuture.channel();
                                Log.i(TAG, "Connect success");



                                HeartBeatReceiveHelper.startReceive();



                                if (TaskThreadPool.getInstance().getPaused())
                                    TaskThreadPool.getInstance().resume();
                                //重新登陆
//                                rlogin();
                            } else {
                                isConnect = false;
                                Log.i(TAG, "Connect fail");
                            }
                        }).sync();
                future.channel().closeFuture().sync();
            } catch (Exception e) {
                e.printStackTrace();
//                reconnect();
                Log.i(TAG, "Connect abnormal");
            } finally {
                Log.i(TAG, "Reconnect");
                if (!TaskThreadPool.getInstance().getPaused())
                    TaskThreadPool.getInstance().pause();
                executorService.execute(() -> {
                    isConnect = false;
                    reconnect();
                });
            }
        }
    }

    public void disconnect() {
        Log.i(TAG, "Disconnect");
        group.shutdownGracefully();
    }

    public void reconnect() {
        if (!isConnect) {
            try {
                TimeUnit.SECONDS.sleep(5);
                try {
                    Log.i(TAG, "Reconnect Start");
                    disconnect();
                    connect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean sendMsg(Message message, String str) {
        Log.i(TAG, "Send msg");
        if (isConnect) {
            try {
                channel.writeAndFlush(message);
                addTimerTask(str);
                Log.i(TAG, "Send success");
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                removeTimerTask(str);
                Log.i(TAG, "Send fail");
                return false;
            }
        } else {
            Log.i(TAG, "Send fail");
            return false;
        }
    }

    public void sendHeartBeat() {
        Log.i(TAG, "Send heartbeat");
        if (isConnect)
            channel.writeAndFlush(Message.getHeartBeat());
    }

    //以下为服务器响应超时timer
    public void removeTimerTask(String key) {
        if (timerPool.get(key) != null) {
            timerPool.remove(key).cancel();
        }
    }

    private void addTimerTask(String key) {
        removeTimerTask(key);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                //在此如果回信等待结束
                //则移去两个rx中的俩key
                //然后订阅新的rxjava，返回失败的function
                //consumer依旧注册原来的
                try {
                    FunctionContainer.getInstance().getFunction(key);
                    Consumer consumer = ConsumerContainer.getInstance().getConsumer(key);
                    Function<String, Integer> function = s -> Status.SERVICE_TIME_OUT;
                    Observable observable = Observable.just("");
                    if (consumer != null)
                        observable.map(function).subscribe(consumer);
                    removeTimerTask(key);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(timerTask, 0, Other.SOCKET_WATING_TIME);
        timerPool.put(key, timerTask);
    }

}
