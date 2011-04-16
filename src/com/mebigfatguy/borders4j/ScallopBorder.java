package com.mebigfatguy.borders4j;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;

import javax.swing.border.AbstractBorder;

import com.mebigfatguy.borders4j.bezier.BezierRenderer;

public class ScallopBorder extends AbstractBorder {

	private final Options options;
	private final Stroke stroke;
	private Rectangle cacheBounds;
	private final List<float[][]> bezierPts = new ArrayList<float[][]>();

	public ScallopBorder() {
		this(new Options());
	}

	public ScallopBorder(Options options) {
		this.options = options;
		stroke = new BasicStroke(options.lineWidth);
	}

	@Override
	public Insets getBorderInsets(Component c) {
		return new Insets(options.top, options.left, options.bottom, options.right);
	}

	@Override
	public Insets getBorderInsets(Component c, Insets insets) {
		insets.top = options.top;
		insets.left = options.left;
		insets.bottom = options.bottom;
		insets.right = options.right;

		return insets;
	}

	@Override
	public void paintBorder(final Component c, Graphics g, int x, int y, int width, int height) {

		Graphics2D g2d = (Graphics2D) g;
		Color saveColor = g.getColor();
		Stroke saveStroke = g2d.getStroke();
		Shape saveClip = g.getClip();

		try {
			Rectangle r = c.getBounds();

			g2d.setStroke(stroke);

			if ((cacheBounds == null) || !r.equals(cacheBounds)) {
				recalculateBezierPts(r);
				cacheBounds = (Rectangle)r.clone();
			}

			g.setColor(options.lineColor);
			g2d.setStroke(stroke);
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			for (float[][] pts : bezierPts) {
				BezierRenderer.draw(g, pts);
			}

		} finally {
			g.setColor(saveColor);
			g2d.setStroke(saveStroke);
			g.setClip(saveClip);
		}
	}

	private void recalculateBezierPts(Rectangle r) {
		bezierPts.clear();

		if (options.top > 0) {

			float[][] pts = new float[][]
			{
				{r.x, r.y},
				{r.x + (r.width / 4.0f), r.y + options.top},
				{r.x + (r.width / 4.0f), r.y + options.top},
				{r.x + (r.width / 2.0f), r.y}
			};
			bezierPts.add(pts);

			pts = new float[][]
			{
				{r.x + (r.width / 2.0f), r.y},
				{r.x + (3 * r.width / 4.0f), r.y + options.top},
				{r.x + (3 * r.width / 4.0f), r.y + options.top},
				{r.x + r.width, r.y}
			};
			bezierPts.add(pts);
		}

		if (options.left > 0) {

			float[][] pts = new float[][]
            {
				{r.x, r.y},
				{r.x + options.left, r.y + (r.height / 4.0f)},
				{r.x + options.left, r.y + (r.height / 4.0f)},
				{r.x, r.y + (r.height / 2.0f)}
            };
			bezierPts.add(pts);

			pts = new float[][]
		    {
				{r.x, r.y + (r.height / 2.0f)},
				{r.x + options.left, r.y + (3 * r.height / 4.0f)},
				{r.x + options.left, r.y + (3 * r.height / 4.0f)},
				{r.x, r.y + r.height}
		    };
			bezierPts.add(pts);
		}

		if (options.bottom > 0) {

			float[][] pts = new float[][]
			{
				{r.x, r.y + r.height},
				{r.x + (r.width / 4.0f), r.y + r.height - options.bottom},
				{r.x + (r.width / 4.0f), r.y + r.height - options.bottom},
				{r.x + (r.width / 2.0f), r.y + r.height},
			};
			bezierPts.add(pts);

			pts = new float[][]
			{
				{r.x + (r.width / 2.0f), r.y + r.height},
				{r.x + (3 * r.width/ 4.0f), r.y + r.height - options.bottom},
				{r.x + (3 * r.width/ 4.0f), r.y + r.height - options.bottom},
				{r.x + r.width, r.y + r.height}
			};
			bezierPts.add(pts);
		}

		if (options.right > 0) {

			float[][] pts = new float[][]
            {
				{r.x + r.width, r.y},
				{r.x + r.width - options.right, r.y + (r.height / 4.0f)},
				{r.x + r.width - options.right, r.y + (r.height / 4.0f)},
				{r.x + r.width, r.y + (r.height / 2.0f)},
            };
			bezierPts.add(pts);

			pts = new float[][]
		    {
				{r.x + r.width, r.y + (r.height / 2.0f)},
				{r.x + r.width - options.right, r.y + (3 * r.height / 4.0f)},
				{r.x + r.width - options.right, r.y + (3 * r.height / 4.0f)},
				{r.x + r.width, r.y + r.height},

			};
			bezierPts.add(pts);
		}
	}

	public static class Options {
		int top = 12;
		int left = 12;
		int bottom = 12;
		int right = 12;
		Color lineColor = Color.BLACK;
		int lineWidth = 1;

		public Options setTop(int top) {
			this.top = top;
			return this;
		}

		public Options setLeft(int left) {
			this.left = left;
			return this;
		}

		public Options setBottom(int bottom) {
			this.bottom = bottom;
			return this;
		}

		public Options setRight(int right) {
			this.right = right;
			return this;
		}

		public Options setLineColor(Color lineColor) {
			this.lineColor = lineColor;
			return this;
		}

		public Options setLineWidth(int lineWidth) {
			this.lineWidth = lineWidth;
			return this;
		}
	}
}
