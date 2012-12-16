package com.arhangeldim.graphics;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

public abstract class Curve {

	protected List<PointF> controlPoints;
	protected List<Velocity> velocities;

	protected final static double DELTA = 0.05;
	protected final static int VELOCITY_LIMIT = 10;

	protected boolean isCaptured;
	protected int markerIndex;
	protected boolean drawDividingPoints = true;

	public static final int POINT_MARKER_WIDTH = 8;
	public static final int POINT_MARKER_HEIGHT = 8;

	public Curve() {
		controlPoints = new ArrayList<PointF>();
	}
	
	public void generateCurve(int width, int height) {
		Random r = new Random();
		for (int i = 0; i < 5; i++) {
			controlPoints.add(new PointF(r.nextInt(width), r.nextInt(height)));
		}
	}

	private Velocity generateVelocity() {
		Random r = new Random();
		int dx = r.nextInt(VELOCITY_LIMIT);
		if (r.nextBoolean())
			dx *= -1;
		int dy = r.nextInt(VELOCITY_LIMIT);
		if (r.nextBoolean())
			dy *= -1;
		return new Velocity(dx, dy);
	}

	public void updateControlPointsPosition(int canvasWidth, int canvasHeight) {
		if (velocities == null) {
			velocities = new ArrayList<Velocity>(controlPoints.size());
			for (int i = 0; i < controlPoints.size(); i++) {

				velocities.add(i, generateVelocity());
			}
		} else if (velocities.size() < controlPoints.size()) {
			for (int i = velocities.size(); i < controlPoints.size(); i++)
				velocities.add(i, generateVelocity());
		}

		// If points was added after running, we check only old points
		for (int i = 0; i < velocities.size(); i++) {
			float x = controlPoints.get(i).x;
			float y = controlPoints.get(i).y;
			int dx = velocities.get(i).getDx();
			int dy = velocities.get(i).getDy();
			if (x + dx > canvasWidth) {
				x = 2 * canvasWidth - (x + dx);
				dx *= -1;
			} else if (x + dx < 0) {
				x = Math.abs(x - dx);
				dx *= -1;
			} else {
				x += dx;
			}
			if (y + dy > canvasHeight) {
				y = 2 * canvasHeight - (y + dy);
				dy *= -1;
			} else if (y + dy < 0) {
				y = Math.abs(y - dy);
				dy *= -1;
			} else {
				y += dy;
			}
			controlPoints.get(i).set(x, y);
			velocities.get(i).setVelocity(dx, dy);
		}
	}

	protected void copyPoints(List<PointF> dst, List<PointF> src) {
		assert (dst.isEmpty()) : "Destination list is not empty.";
		for (PointF p : src)
			dst.add(new PointF(p.x, p.y));
	}

	protected void drawCurveByPoints(Canvas canvas, List<PointF> points, Paint paint) {
		if (points.size() < 2)
			return;
		for (int i = 0; i < points.size() - 1; i++)
			canvas.drawLine(points.get(i).x, points.get(i).y, points.get(i + 1).x, points.get(i + 1).y, paint);
	}

	protected void addPoint(PointF p) {
		controlPoints.add(p);
	}

	public void drawMarkers(Canvas canvas, Paint paint) {
		for (PointF p : controlPoints) {
			canvas.drawCircle(p.x, p.y, POINT_MARKER_WIDTH, paint);
		}
	}

	public void drawBounds(Canvas canvas, Paint paint) {
		if (controlPoints.size() < 2)
			return;
		for (int i = 0; i < controlPoints.size() - 1; i++) {
			canvas.drawLine(controlPoints.get(i).x,
					controlPoints.get(i).y,
					controlPoints.get(i + 1).x,
					controlPoints.get(i + 1).y,
					paint);
		}
	}

	public void updateCurve(Canvas canvas, Paint paint)  {
		drawBounds(canvas, paint);
		drawCurve(canvas, paint);
		drawMarkers(canvas, paint);
	}

	public boolean isCaptured() {
		return isCaptured;
	}

	public void setCaptured(boolean c) {
		isCaptured = c;
	}

	public void setCursorPosition(float x, float y) {
		if (isCaptured()) {
			controlPoints.get(markerIndex).set(x, y);
		} else {
			for (PointF p : controlPoints) {
				if (Math.abs(p.x - x) <= POINT_MARKER_WIDTH && Math.abs(p.x - y) <= POINT_MARKER_HEIGHT) {
					// Capture this node
					markerIndex = controlPoints.lastIndexOf(p);
					isCaptured = true;
				}
			}
		}
	}

	public abstract void drawCurve(Canvas canvas, Paint paint);

	class Velocity {

		int dx, dy;

		public Velocity(int dx, int dy) {
			this.dx = dx;
			this.dy = dy;
		}

		public int getDx() {
			return dx;
		}

		public void setDx(int dx) {
			this.dx = dx;
		}

		public int getDy() {
			return dy;
		}

		public void setDy(int dy) {
			this.dy = dy;
		}

		public void setVelocity(int dx, int dy) {
			this.dx = dx;
			this.dy = dy;
		}
	}
}

