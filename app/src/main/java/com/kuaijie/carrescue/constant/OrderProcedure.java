package com.kuaijie.carrescue.constant;

import com.kuaijie.carrescue.ui.technician.CarCheckActivity;
import com.kuaijie.carrescue.ui.technician.CompleteOrderActivity;
import com.kuaijie.carrescue.ui.technician.CompletionFeedbackActivity;
import com.kuaijie.carrescue.ui.technician.RoadTollActivity;
import com.kuaijie.carrescue.ui.technician.SatisfactionDegreeActivity;
import com.kuaijie.carrescue.ui.technician.StartOffActivity;
import com.kuaijie.carrescue.ui.technician.TaskExecutionActivity;
import com.kuaijie.carrescue.ui.technician.WorkOrderActivity;

/**
 * Created by MitsukiSaMa on 12-14.
 */

public class OrderProcedure {
    public static final Class getNextPageClass(Long id) {
        int i = id.intValue();
        switch (i) {
            case 13://拍照
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 23:
            case 24:
            case 28:
            case 41:
                return TaskExecutionActivity.class;
            case 26://满意度输入
                return SatisfactionDegreeActivity.class;
            case 27://完工反馈数据填写
                return CompletionFeedbackActivity.class;
            case 29://返程选择
                return CompleteOrderActivity.class;
            case 12://去往救援地
            case 22://去往目的地
            case 40:
            case 43:
                return StartOffActivity.class;
            case 36://路费填写
            case 44://救援费用填写
                return RoadTollActivity.class;
            case 37://车辆检查
                return CarCheckActivity.class;
            case 45://工单信息
                return WorkOrderActivity.class;
            case 11://联系客户
            case 42://等待指令
            case 14://签字
            case 38:
            case 39:
            case 25://救援费用填写
            default://其他
                //采用dialong，所以返回null
                return null;
        }
    }
}
