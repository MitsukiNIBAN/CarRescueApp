package com.kuaijie.carrescue.ui.dialog.writepad;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.kuaijie.carrescue.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DrawView extends View {
	private int view_width = 0;
	private int view_height = 0;
	private float preX;
	private float preY;
	private Path path;
	public Paint paint = null;
	Bitmap cacheBitmap = null;
	Canvas cacheCanvas = null;

	public DrawView(Context context, AttributeSet set) {
		super(context, set);
		view_width = context.getResources().getDisplayMetrics().widthPixels;
		view_height = context.getResources().getDisplayMetrics().heightPixels;
		System.out.println(view_width + "*" + view_height);

		cacheBitmap = Bitmap.createBitmap(view_width, view_height,
				Config.ARGB_8888);
		cacheCanvas = new Canvas();
		path = new Path();
		cacheCanvas.setBitmap(cacheBitmap);

		paint = new Paint(Paint.DITHER_FLAG);
		paint.setColor(Color.BLACK);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStrokeWidth(10);
		paint.setAntiAlias(true);
		paint.setDither(true);
	}

	@Override
	public void onDraw(Canvas canvas) {
		canvas.drawColor(ContextCompat.getColor(getContext(), R.color.wirtePadBackground));
		Paint bmpPaint = new Paint();
		canvas.drawBitmap(cacheBitmap, 0, 0, bmpPaint);
		canvas.drawPath(path, paint);
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		float x = event.getX();
		float y = event.getY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			path.moveTo(x, y);
			preX = x;
			preY = y;
			break;
		case MotionEvent.ACTION_MOVE:
			float dx = Math.abs(x - preX);
			float dy = Math.abs(y - preY);
			if (dx >= 5 || dy >= 5) {
				path.quadTo(preX, preY, (x + preX) / 2, (y + preY) / 2);
				preX = x;
				preY = y;
			}
			break;
		case MotionEvent.ACTION_UP:
			cacheCanvas.drawPath(path, paint);
			path.reset();
			break;
		}
		invalidate();
		return true;
	}


	public void clear() {
//		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
//		paint.setStrokeWidth(500);
		cacheCanvas.drawColor(ContextCompat.getColor(getContext(), R.color.wirtePadBackground));
		invalidate();
	}
	
	public void save()
	{
		try {
			saveBitmap("cache");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@SuppressLint("SdCardPath")
	public void saveBitmap(String fileName) throws IOException {
		File file = new File("/sdcard/pictures/" + fileName);
		file.createNewFile();
		FileOutputStream fileOS = new FileOutputStream(file);
		cacheBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOS);
		fileOS.flush();
		fileOS.close();
	}

}
