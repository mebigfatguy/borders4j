package com.mebigfatguy.borders4j;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Stroke;

import javax.swing.border.AbstractBorder;

import com.mebigfatguy.borders4j.bezier.BezierRenderer;

public class CurlyBraceBorder extends AbstractBorder {

	private static final long serialVersionUID = 8493254784459082029L;

	private final Options options;
	private final Stroke stroke;

	public CurlyBraceBorder() {
		this(new Options());
	}

	public CurlyBraceBorder(Options options) {
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
		try {
			Rectangle r = c.getBounds();
			g.setColor(options.color);
			g2d.setStroke(stroke);

			if (options.top > 0) {
				float[][] pts = new float[][]
                {
					{r.x,r.y + options.top},
					{r.x,r.y},
					{r.x + r.width/2,r.y + options.top},
					{r.x + r.width/2,r.y},
                };
				BezierRenderer.draw(g, pts);
				pts = new float[][]
                {
					{r.x + r.width,r.y + options.top},
					{r.x + r.width,r.y},
					{r.x + r.width/2,r.y + options.top},
					{r.x + r.width/2,r.y},
                };
				BezierRenderer.draw(g, pts);
			}
			if (options.left > 0) {
				float[][] pts = new float[][]
                {
					{r.x + options.left,r.y},
					{r.x,r.y},
					{r.x + options.left,r.y + r.height/2},
					{r.x,r.y + r.height/2},
                };
				BezierRenderer.draw(g, pts);
				pts = new float[][]
                {
					{r.x,r.y + r.height / 2},
					{r.x + options.left,r.y + r.height / 2},
					{r.x,r.y + r.height},
					{r.x + options.left,r.y + r.height},
                };
				BezierRenderer.draw(g, pts);
			}
			if (options.bottom > 0) {
				float[][] pts = new float[][]
                {
					{r.x,r.y + r.height - options.bottom},
					{r.x,r.y + r.height},
					{r.x + r.width/2,r.y + r.height - options.bottom},
					{r.x + r.width/2,r.y + r.height},
                };
				BezierRenderer.draw(g, pts);
				pts = new float[][]
                {
					{r.x + r.width,r.y + r.height - options.bottom},
					{r.x + r.width,r.y + r.height},
					{r.x + r.width/2,r.y + r.height - options.bottom},
					{r.x + r.width/2,r.y + r.height},
                };
				BezierRenderer.draw(g, pts);
			}
			if (options.right > 0) {
				float[][] pts = new float[][]
                {
					{r.x + r.width - options.right,r.y},
					{r.x + r.width,r.y},
					{r.x + r.width - options.right,r.y + r.height/2},
					{r.x + r.width,r.y + r.height/2},
                };
				BezierRenderer.draw(g, pts);
				pts = new float[][]
                {
					{r.x + r.width,r.y + r.height / 2},
					{r.x + r.width - options.right,r.y + r.height / 2},
					{r.x + r.width,r.y + r.height},
					{r.x + r.width - options.right,r.y + r.height},
                };
				BezierRenderer.draw(g, pts);
			}

		} finally {
			g.setColor(saveColor);
			g2d.setStroke(saveStroke);
		}
	}

	public static class Options {
		int top = 20;
		int left = 20;
		int bottom = 20;
		int right = 20;
		Color color = Color.BLACK;
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

		public Options setColor(Color color) {
			this.color = color;
			return this;
		}

		public Options setLineWidth(int lineWidth) {
			this.lineWidth = lineWidth;
			return this;
		}
	}
}
