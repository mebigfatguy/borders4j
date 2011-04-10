package com.mebigfatguy.borders4j.bezier;

import java.awt.Graphics;

public class BezierRenderer {

	private static final float BEZ_LIMIT_SQ = 16.0f;

	private BezierRenderer() {
	}

	public static void draw(Graphics g, float[][] pts) {

		float xdiff = pts[0][0] - pts[3][0];
		xdiff *= xdiff;
		float ydiff = pts[0][1] - pts[3][1];
		ydiff *= ydiff;

		if (xdiff + ydiff < BEZ_LIMIT_SQ) {
			g.drawLine(Math.round(pts[0][0]), Math.round(pts[0][1]), Math.round(pts[3][0]), Math.round(pts[3][1]));

		} else {
			float[][] left = new float[4][2];
			float[][] right = new float[4][2];

			subDivide(pts, left, right);

			draw(g, left);
			left = null;
			draw(g, right);
			right = null;
		}
	}

	private static void subDivide(float[][] src, float[][] left, float[][] right) {
		left[0][0] = src[0][0];
		left[0][1] = src[0][1];

		left[1][0] = (src[0][0] + src[1][0]) / 2.0f;
		left[1][1] = (src[0][1] + src[1][1]) / 2.0f;

		right[3][0] = src[3][0];
		right[3][1] = src[3][1];

		right[2][0] = (src[3][0] + src[2][0]) / 2.0f;
		right[2][1] = (src[3][1] + src[2][1]) / 2.0f;

		float centerX = (src[1][0] + src[2][0]) / 2.0f;
		float centerY = (src[1][1] + src[2][1]) / 2.0f;

		left[2][0] = (left[1][0] + centerX) / 2.0f;
		left[2][1] = (left[1][1] + centerY) / 2.0f;

		right[1][0] = (right[2][0] + centerX) / 2.0f;
		right[1][1] = (right[2][1] + centerY) / 2.0f;

		left[3][0] = (left[2][0] + right[1][0]) / 2.0f;
		left[3][1] = (left[2][1] + right[1][1]) / 2.0f;

		right[0][0] = left[3][0];
		right[0][1] = left[3][1];
	}
}
