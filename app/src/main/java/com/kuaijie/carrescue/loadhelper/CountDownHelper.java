package com.kuaijie.carrescue.loadhelper;

import android.databinding.BindingAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.kuaijie.carrescue.constant.Other;
import com.kuaijie.carrescue.constant.Status;
import com.kuaijie.carrescue.util.Tools;

import org.msgpack.annotation.Index;

/**
 * Created by MitsukiSaMa on 12-7.
 */

public class CountDownHelper {
    @BindingAdapter({"countDown", "state"})
    public static void countDown(TextView textView, String time, Byte state) {
//        System.out.println("time" + time);
        if (state == Status.ORDER_ACCEPT)
            if (time != null) {
                Long t = Tools.timeDifference(time) + Other.ORDER_WATING_TIME;
                if (t > 0) {
                    textView.setText("");
                    textView.append((t / 3600 >= 10) ? t / 3600 + ":" : "0" + t / 3600 + ":");
                    textView.append((t / 60 % 60 >= 10) ? t / 60 % 60 + ":" : "0" + t / 60 % 60 + ":");
                    textView.append((t % 60 >= 10) ? t % 60 + "" : "0" + t % 60);
                } else
                    textView.setText("00:00:00");
            } else
                textView.setText("00:00:00");
        else
            textView.setText("00:00:00");
    }

    @BindingAdapter({"countDownBtn"})
    public static void countDownBtn(Button button, String time) {
        System.out.println("btn time" + time);
        if (time != null) {
            Long t = Tools.timeDifference(time) + Other.ORDER_WATING_TIME;
            if (t > 0) {
                String str = "接受订单 ";
                str = str + ((t / 3600 >= 10) ? t / 3600 + ":" : "0" + t / 3600 + ":");
                str = str + ((t / 60 % 60 >= 10) ? t / 60 % 60 + ":" : "0" + t / 60 % 60 + ":");
                str = str + ((t % 60 >= 10) ? t % 60 + "" : "0" + t % 60);
                button.setText(str);
            } else
                button.setText("超时未接单");
        } else
            button.setText("超时未接单");
    }
}
