package com.arhangeldim.graphics;


public final class Dimension {
	private int width, height;

	public Dimension(Dimension d) {
		width = d.width;
		height = d.height;
	}
	
	public Dimension(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public Dimension getSize() {
		return this;
	}
	
	public void setSize(Dimension d) {
		width = d.width;
		height = d.height;
	}
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
}
