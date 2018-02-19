package com.kuaijie.carrescue.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import io.reactivex.functions.Consumer;

/**
 * Created by MitsukiSaMa on 12-4.
 */

public class ConsumerContainer {
    private static class ConsumerContainerHolder{
        private static final ConsumerContainer INSTANCE = new ConsumerContainer();
    }
    private ConsumerContainer(){}
    public static final ConsumerContainer getInstance(){
        return ConsumerContainerHolder.INSTANCE;
    }

    private ConcurrentMap<String, Consumer> map = new ConcurrentHashMap<>();

    public void putConsumer(String key,Consumer consumer) {
        map.put(key, consumer);
    }

    public Consumer getConsumer(String key) {
        return map.remove(key);
    }
}
