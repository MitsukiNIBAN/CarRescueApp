package com.kuaijie.carrescue.constant;

/**
 * Created by MitsukiSaMa on 12-5.
 */

public class Status {
    /** order cache status **/
    public static final int CACHING_COMPLETION = 1;

    public static final int SERVICE_TIME_OUT = -4;

    public static final int SEND_SUCCESS = 2;
    /************************/

    /** order status **/
    //已取消
    public static final byte ORDER_CANCEL = -1;
    //未调度
    public static final byte ORDER_DISPATCH = 0;
    //未接单
    public static final byte ORDER_ACCEPT = 1;
    //进行中
    public static final byte ORDER_DOING = 2;
    //已完成
    public static final byte ORDER_COMPLETE = 3;
    //已报数
    public static final byte ORDER_ = 4;
    //已质检
    public static final byte ORDER_CHECK = 5;
    /************************/
}
