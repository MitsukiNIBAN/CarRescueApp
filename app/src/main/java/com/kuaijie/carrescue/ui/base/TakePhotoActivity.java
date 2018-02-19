package com.kuaijie.carrescue.ui.base;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.kuaijie.carrescue.R;
import com.kuaijie.carrescue.util.Activity;
import com.kuaijie.carrescue.view.camerasurfaceview.CameraSurfaceView;

/**
 * Created by MitsukiSaMa on 12-15.
 */

public class TakePhotoActivity extends Activity {

    private CameraSurfaceView cameraSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_take_photo);

        cameraSurfaceView = (CameraSurfaceView) findViewById(R.id.sv_camera);

        findViewById(R.id.ib_close).setOnClickListener(view -> finish());
        findViewById(R.id.ib_take_photo).setOnClickListener(view -> {
            String str = cameraSurfaceView.takePic();
            Intent intent = new Intent();
            intent.putExtra("fileName", str);
            // 设置结果，并进行传送
            setResult(0, intent);
        });
    }
}
