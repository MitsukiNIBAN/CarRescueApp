package com.kuaijie.carrescue.constant;

/**
 * Created by MitsukiSaMa on 11-27.
 */

public class ServerAddress {

//   public static final String IP = "10.26.230.143";
//    public static final String IP = "10.26.230.172";
    public static final String IP = "10.28.75.100";
    public static final int PORT = 8888;

    public static final int WEB_SERVER_PORT = 8080;
    public static final String UPLOAD_IMG_SERVER = "http://" + IP + ":" + WEB_SERVER_PORT + "/android/addOrderPhoto";

    public static final String SUB_COL = "http://" + IP + ":" + WEB_SERVER_PORT + "/android/subMileage";

}
