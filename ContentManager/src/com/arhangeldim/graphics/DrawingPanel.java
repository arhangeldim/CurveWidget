package com.arhangeldim.graphics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;

public class DrawingPanel extends View implements OnTouchListener, OnDragListener {
	
	public final String LOG_TAG = getClass().getName();
	private final Paint paint =  new Paint(Paint.ANTI_ALIAS_FLAG);
	
	private final int width;
	private final int height;
	
	private Curve curve;
	
	public DrawingPanel(Context context, int width, int height) {
		super(context);
		this.width = width;
		this.height = height;
		
		curve = new Bsplain();
		curve.generateCurve(width, height);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		paint.setColor(Color.BLUE);
		curve.updateCurve(canvas, paint);
	}

	@Override
	public boolean onDrag(View arg0, DragEvent e) {
		if (curve.isCaptured()) {
			float x = e.getX();
			float y = e.getY();
			if (x < 0)
				x = 0;
			if (y < 0)
				y = 0;
			if (x > width)
				x = width;
			if (y > height)
				y = height;
			curve.setCursorPosition(x, y);
		}
		return false;
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent e) {
		if (e.getAction() == MotionEvent.ACTION_DOWN) {
			Log.d(LOG_TAG, "Touch down!");
			float x = e.getX();
			float y = e.getY();
			curve.setCursorPosition(x, y);
		} else if (e.getAction() == MotionEvent.ACTION_UP) {
			curve.setCaptured(false);
		}
		
		return false;
	}
	
}
