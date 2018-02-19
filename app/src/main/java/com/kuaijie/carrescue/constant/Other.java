package com.kuaijie.carrescue.constant;

import android.os.Environment;

import com.kuaijie.carrescue.R;

/**
 * Created by MitsukiSaMa on 12-7.
 */

public class Other {
    //接单倒计时时间
    public static final int ORDER_WATING_TIME = 5 * 60;
    //socket等待时间:20s
    public static final int SOCKET_WATING_TIME = 1000 * 20;
    //首页有未接订单时 语音提醒间隔
    public static final long INTERVAL = 15l;
    //导航转向图标
    public static final int[] customIconTypes = {R.drawable.sou2, R.drawable.sou3,
            R.drawable.sou4, R.drawable.sou5, R.drawable.sou6, R.drawable.sou7,
            R.drawable.sou8, R.drawable.sou9, R.drawable.sou10,
            R.drawable.sou11, R.drawable.sou12, R.drawable.sou13,
            R.drawable.sou14, R.drawable.sou15, R.drawable.sou16,
            R.drawable.sou17, R.drawable.sou18, R.drawable.sou19,
    };
}
