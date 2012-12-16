package com.arhangeldim.graphics;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

public class Diagram extends View {
	
	private final String LOG_TAG = getClass().getName();

	final static int PADDING = 40; 
	
	private final Paint paint =  new Paint(Paint.ANTI_ALIAS_FLAG);

	final private SortedMap<?, Double> data;
	
	private final int width;
	private final int height;
	
	private String name;
	private String xAxis;
	private String yAxis;
	
	private Rect canvasRect;
	private Rect areaRect;
	
	//private Color dataColor = Color.BLUE;
	//private Color axisColor = Color.BLACK;
	
	private double columnWidth;
	
	// top left corner
	// FIXME: set default location in constructor
	private int locationX = 50;
	private int locationY = 50;
	
	public Diagram(Context context, int width, int height, final Map<?, Double> data) {
		super(context);
		paint.setColor(0xffff0000);
		this.data = new TreeMap(data);
		this.width = width;
		this.height = height;
		name = "Diagram";
	}
	
	private Rect getCanvasRect() {
		if (canvasRect == null)
			canvasRect = new Rect(locationX, locationY + height, locationX + width, locationY);
		return canvasRect;
	}
	
	private Rect getAreaRect() {
		if (areaRect == null)
			areaRect = new Rect(locationX + PADDING, locationY + height - PADDING,
				locationX + width - PADDING, locationY + PADDING);
		return areaRect;
	}

	private void drawCanvas(Canvas canvas) {
		paint.setColor(Color.LTGRAY);
		canvas.drawRect(getCanvasRect(), paint);
	}
	
	private void drawArea(Canvas canvas) {
		paint.setColor(Color.LTGRAY);
		canvas.drawRect(getAreaRect(), paint);
	}
	
	private void drawCoordinates(Canvas canvas) {
		paint.setColor(Color.BLACK);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(1);
		Rect bound = getAreaRect();
		// OX
		canvas.drawLine(bound.left, bound.top, bound.right, bound.top, paint);
		// OY
		canvas.drawLine(bound.left, bound.bottom, bound.left, bound.top, paint);
		
	}
	
	private void drawData(Canvas canvas) {
		paint.setColor(Color.GREEN);
		paint.setStyle(Paint.Style.FILL);
		
		int sizeOX = getAreaRect().width();
		int delta = sizeOX / (data.size() + 1);
		Log.d(LOG_TAG, "Delta = " + delta);
		
		int columnWidth = 5;
		
		Rect bound = getAreaRect();
		int i = 1;
		
		for (Map.Entry<?, Double> e : data.entrySet()) {
			Log.d(LOG_TAG, "entry <" + e.getKey() + ", " + e.getValue() + ">");
			
			canvas.drawRect(new Rect(
					bound.left + delta * i - columnWidth,
					bound.top,
					bound.left + delta * i + columnWidth,
					bound.top - e.getValue().intValue()), paint);
			i++;
		}
			
			
	}
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawCanvas(canvas);
		drawArea(canvas);
		drawCoordinates(canvas);
		drawData(canvas);
	}
	

}
