package com.kuaijie.carrescue.constant;

/**
 * Created by MitsukiSaMa on 12-15.
 */

public class PhotoType {
    public static final String getStr(Long id) {
        int i = id.intValue();
        switch (i) {
            case 1:
                return "救援地整车拍照";
            case 2:
                return "车架号拍照";
            case 3:
                return "背车照";
            case 4:
                return "电瓶照";
            case 5:
                return "被救援车车牌";
            case 6:
                return "移动电源";
            case 7:
                return "被困照";
            case 8:
                return "脱困照";
            case 9:
                return "目的地拍照";
            case 10:
                return "卸车照";
            case 11:
                return "完工反馈工单照";
            case 12:
                return "不合格处拍照";
            default:
                return "";
        }
    }
}
