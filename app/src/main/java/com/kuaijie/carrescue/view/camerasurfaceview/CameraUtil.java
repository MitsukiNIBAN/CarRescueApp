package com.kuaijie.carrescue.view.camerasurfaceview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Environment;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.widget.Toast;


import com.kuaijie.carrescue.util.Application;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by Mitsuki on 12-22.
 */

public class CameraUtil {

    private final String TAG = "CameraUtil";

    private static final int id = 0;
    private Camera c;
    private String fileName = "";
    private saveFinish sf;

    private static class CameraUtilHolder {
        private static final CameraUtil INSTANCE = new CameraUtil();
    }

    private CameraUtil() {
    }

    public static final CameraUtil getInstance() {
        return CameraUtilHolder.INSTANCE;
    }

    public void open(SurfaceHolder holder) {
        if (c == null) {
            c = Camera.open(id);
            try {
                c.setPreviewDisplay(holder);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setCameraParams(Activity activity, int width, int height) {
        Log.i(TAG, "setCameraParams width=" + width + "  height=" + height);
        Camera.Parameters parameters = c.getParameters();
        setPreviewSize(parameters, width, height);
        setPictureSize(parameters, width, height);

        parameters.setJpegQuality(100); // 设置照片质量
        if (parameters.getSupportedFocusModes().contains(android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);// 连续对焦模式
        }
        c.cancelAutoFocus();//自动对焦。
        setCameraDisplayOrientation(c, activity);
        c.setParameters(parameters);
    }

    public void startPreview() {
        c.startPreview();
    }

    public void release() {
        c.stopPreview();
        c.release();
        c = null;
        fileName = "";
    }

    public String takePhoto(saveFinish sf) {
        fileName = "KJCR_" + System.currentTimeMillis() + ".jpg";
        this.sf = sf;
        new Thread(() -> {
            try {
                c.takePicture(null, null, jpeg);
            }catch (Exception e){
                e.printStackTrace();
            }
        }).start();
        return fileName;
    }

    public interface saveFinish {
        void finish();
    }

    private Camera.PictureCallback jpeg = (data, camera) -> {
        BufferedOutputStream bos = null;
        Bitmap bm = null;
        try {
            bm = BitmapFactory.decodeByteArray(data, 0, data.length);
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                Log.i(TAG, "Environment.getExternalStorageDirectory()=" + Environment.getExternalStorageDirectory());

                String filePath = Environment.getExternalStorageDirectory() + "/CarRescue/PhotoCache/";
                //照片保存路径
                Log.i(TAG, "filePath" + filePath);
                File file = new File(filePath);

                if (!file.exists()) {
                    if (!file.mkdirs()) {
                        Log.e(TAG, "创建图片存储路径目录失败");
                        Log.e(TAG, "mediaStorageDir : " + file.getPath());
                        return;
                    }
                }
                bos = new BufferedOutputStream(new FileOutputStream(new File(filePath + fileName)));
                bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);//将图片压缩到流中

            } else {
                Log.e(TAG, "没有检测到内存卡");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bos.flush();//输出
                bos.close();//关闭
                bm.recycle();// 回收bitmap空间
                camera.stopPreview();// 关闭预览
            } catch (IOException e) {
                e.printStackTrace();
            }
            sf.finish();
        }
    };

    private void setPreviewSize(Camera.Parameters parametes, int screenWidth, int screenHeight) {
        List<Camera.Size> localSizes = parametes.getSupportedPreviewSizes();
        Camera.Size biggestSize = null;
        Camera.Size fitSize = null;// 优先选屏幕分辨率
        Camera.Size targetSize = null;// 没有屏幕分辨率就取跟屏幕分辨率相近(大)的size
        Camera.Size targetSiz2 = null;// 没有屏幕分辨率就取跟屏幕分辨率相近(小)的size
        if (localSizes != null) {
            int cameraSizeLength = localSizes.size();
            for (int n = 0; n < cameraSizeLength; n++) {
                Camera.Size size = localSizes.get(n);
//                Log.i(TAG, "PreviewSize width=" + size.width + "  height=" + size.height);
                if (biggestSize == null ||
                        (size.width >= biggestSize.width && size.height >= biggestSize.height)) {
                    biggestSize = size;
                }

                if (size.width == screenHeight
                        && size.height == screenWidth) {
                    fitSize = size;
                } else if (size.width == screenHeight
                        || size.height == screenWidth) {
                    if (targetSize == null) {
                        targetSize = size;
                    } else if (size.width < screenHeight
                            || size.height < screenWidth) {
                        targetSiz2 = size;
                    }
                }
            }

            if (fitSize == null) {
                fitSize = targetSize;
            }

            if (fitSize == null) {
                fitSize = targetSiz2;
            }

            if (fitSize == null) {
                fitSize = biggestSize;
            }
            Log.i(TAG, "setPreviewSize width=" + fitSize.width + "  height=" + fitSize.height);
            parametes.setPreviewSize(fitSize.width, fitSize.height);
        }
    }

    private void setPictureSize(Camera.Parameters parametes, int screenWidth, int screenHeight) {
        List<Camera.Size> localSizes = parametes.getSupportedPictureSizes();
        Camera.Size biggestSize = null;
        Camera.Size fitSize = null;// 优先选预览界面的尺寸
        Camera.Size targetSize = null;// 没有屏幕分辨率就取跟屏幕分辨率相近(大)的size
        Camera.Size targetSiz2 = null;// 没有屏幕分辨率就取跟屏幕分辨率相近(小)的size
        if (localSizes != null) {
            int cameraSizeLength = localSizes.size();
            for (int n = 0; n < cameraSizeLength; n++) {
                Camera.Size size = localSizes.get(n);
//                Log.i(TAG, "PictureSize width=" + size.width + "  height=" + size.height);
                if (biggestSize == null ||
                        (size.width >= biggestSize.width && size.height >= biggestSize.height)) {
                    biggestSize = size;
                }

                if (size.width == screenHeight
                        && size.height == screenWidth) {
                    fitSize = size;
                } else if (size.width == screenHeight
                        || size.height == screenWidth) {
                    if (targetSize == null) {
                        targetSize = size;
                    } else if (size.width < screenHeight
                            || size.height < screenWidth) {
                        targetSiz2 = size;
                    }
                }
            }

            if (fitSize == null) {
                fitSize = targetSize;
            }

            if (fitSize == null) {
                fitSize = targetSiz2;
            }

            if (fitSize == null) {
                fitSize = biggestSize;
            }
            Log.i(TAG, "setPictureSize width=" + fitSize.width + "  height=" + fitSize.height);
            parametes.setPictureSize(fitSize.width, fitSize.height);
        }
    }

    private void setCameraDisplayOrientation(Camera camera, Activity context) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(id, info);
        int rotation = context.getWindowManager()
                .getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

}
