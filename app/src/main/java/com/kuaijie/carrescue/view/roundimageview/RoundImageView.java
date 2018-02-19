package com.kuaijie.carrescue.view.roundimageview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by MitsukiSaMa on 11-17.
 */

@SuppressLint("AppCompatCustomView")
public class RoundImageView extends ImageView {

    private float width;
    private float height;
    private float radius;
    private boolean isCircle = false;

    public RoundImageView(Context context) {
        super(context);
    }

    public RoundImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RoundImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (Build.VERSION.SDK_INT < 18) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    public void setCircle(boolean is) {
        this.isCircle = is;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
        radius = Math.min(width, height) / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (isCircle) {
            Path path = new Path();
            path.addCircle(width/2, height/2, radius, Path.Direction.CW);
            canvas.clipPath(path);
        } else {
            if (width > FilletConfig.FILLET && height > FilletConfig.FILLET) {
                Path path = new Path();
                path.moveTo(FilletConfig.FILLET, 0);
                path.lineTo(width - FilletConfig.FILLET, 0);
                path.quadTo(width, 0, width, FilletConfig.FILLET);
                path.lineTo(width, height - FilletConfig.FILLET);
                path.quadTo(width, height, width - FilletConfig.FILLET, height);
                path.lineTo(FilletConfig.FILLET, height);
                path.quadTo(0, height, 0, height - FilletConfig.FILLET);
                path.lineTo(0, FilletConfig.FILLET);
                path.quadTo(0, 0, FilletConfig.FILLET, 0);
                canvas.clipPath(path);
            }
        }
        super.onDraw(canvas);
    }

}
