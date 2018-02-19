package com.kuaijie.carrescue.util;

import android.util.Log;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import io.reactivex.functions.Function;

/**
 * Created by MitsukiSaMa on 12-14.
 */

public class FunctionContainer {
    private static class FunctionContainerHolder{
        private static final FunctionContainer INSTANCE = new FunctionContainer();
    }
    private FunctionContainer(){}
    public static final FunctionContainer getInstance(){
        return FunctionContainerHolder.INSTANCE;
    }

    private ConcurrentMap<String, Function> map = new ConcurrentHashMap<>();

    public void putFunction(String key, Function function){
        map.put(key, function);
        Log.i("FunctionContainer", "putFunction:" + key + "ConcurrentMap size:" + map.size());
    }

    public Function getFunction(String key){
        Log.i("FunctionContainer", "getFunction:" + key);
        return map.remove(key);
    }
}
