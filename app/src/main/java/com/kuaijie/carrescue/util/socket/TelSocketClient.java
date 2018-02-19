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

public class TelSocketClient {
    private static final String TAG = "SocketClient";

    private boolean isConnect = false;
    private EventLoopGroup group;
    private Channel channel;

    private long reconnectIntervalTime = 1000;

    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
    private Timer timer = new Timer();
    private HashMap<String, TimerTask> timerPool = new HashMap<>();


    private static class SocketClientHolder {
        private static final TelSocketClient INSTANCE = new TelSocketClient();
    }

    private TelSocketClient() {
    }

    public static final TelSocketClient getInstance() {
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
                                HeartBeatReceiveHelper.stopConnectService();
                                if (TaskThreadPool.getInstance().getPaused())
                                    TaskThreadPool.getInstance().resume();
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
                Log.i(TAG, "Send success");
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                Log.i(TAG, "Send fail");
                return false;
            }
        } else {
            Log.i(TAG, "Send fail");
            return false;
        }
    }
}
