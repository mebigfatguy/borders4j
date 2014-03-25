/*
 * borders4j - An collection of swing borders
 * Copyright 2011-2014 MeBigFatGuy.com
 * Copyright 2011-2014 Dave Brosius
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations
 * under the License.
 */
package com.mebigfatguy.borders4j;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;

import javax.swing.border.AbstractBorder;

import com.mebigfatguy.borders4j.bezier.BezierRenderer;

public class ScrollBorder extends AbstractBorder {

	private final Options options;
	private final Stroke stroke;
	private Rectangle cacheBounds;
	private final List<float[][]> bezierPts = new ArrayList<float[][]>();

	public ScrollBorder() {
		this(new Options());
	}

	public ScrollBorder(Options options) {
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
		RenderingHints saveHints = g2d.getRenderingHints();
		try {
			Rectangle r = c.getBounds();

			if ((cacheBounds == null) || !r.equals(cacheBounds)) {
				recalculateBezierPts(r);
				cacheBounds = (Rectangle)r.clone();
			}

			g.setColor(options.color);
			g2d.setStroke(stroke);
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			for (float[][] pts : bezierPts) {
				BezierRenderer.draw(g, pts);
			}

			if (options.top > 0) {
				int midx = r.x + r.width / 2;
  				g.drawLine(midx - 10, r.y + options.top - options.lineWidth, midx, r.y + options.lineWidth);
  				g.drawLine(midx, r.y + options.top - options.lineWidth, midx + 10, r.y + options.lineWidth);
			}

			if (options.left > 0) {
				int midy = r.y + r.height / 2;
  				g.drawLine(r.x + options.lineWidth, midy - 10, r.x + options.left - options.lineWidth, midy);
  				g.drawLine(r.x + options.lineWidth, midy, r.x + options.left - options.lineWidth, midy + 10);
			}

			if (options.bottom > 0) {
				int midx = r.x + r.width / 2;
  				g.drawLine(midx - 10, r.y + r.height - options.lineWidth, midx, r.y + r.height - options.bottom + options.lineWidth);
  				g.drawLine(midx, r.y + r.height - options.lineWidth, midx + 10, r.y + r.height - options.bottom + options.lineWidth);

			}

			if (options.right > 0) {
				int midy = r.y + r.height / 2;
  				g.drawLine(r.x + r.width - options.right - options.lineWidth, midy - 10, r.x + r.width - options.lineWidth, midy);
  				g.drawLine(r.x + r.width - options.right - options.lineWidth, midy, r.x + r.width - options.lineWidth, midy + 10);
			}

		} finally {
			g.setColor(saveColor);
			g2d.setStroke(saveStroke);
			g2d.setRenderingHints(saveHints);
		}
	}

	private void recalculateBezierPts(Rectangle r) {
		bezierPts.clear();

		if (options.top > 0) {

			float lx = r.x + (r.width / 5.0f);
			float rx = r.x + ((r.width * 4) / 5.0f);

			float[][] pts = new float[][]
			{
				{lx, r.y + options.top - options.lineWidth},
				{lx - 10, r.y + options.top - options.lineWidth},
				{lx - 10, r.y + 1},
				{lx, r.y + 1}
			};
			bezierPts.add(pts);

			pts = new float[][]
			{
				{lx, r.y + 1},
				{lx + 30, r.y + 1},
				{rx - 30, r.y + options.top - options.lineWidth},
				{rx, r.y + options.top - options.lineWidth}
			};
			bezierPts.add(pts);

			pts = new float[][]
			{
				{rx, r.y + options.top - options.lineWidth},
				{rx + 10, r.y + options.top - options.lineWidth},
				{rx + 10, r.y + 1},
				{rx, r.y + 1}
			};
			bezierPts.add(pts);
		}

		if (options.left > 0) {
			float ty = r.y + (r.height / 5.0f);
			float by = r.y + ((r.height * 4) / 5.0f);

			float[][] pts = new float[][]
            {
				{r.x + options.left - options.lineWidth, ty},
				{r.x + options.left - options.lineWidth, ty - 10},
				{r.x + options.lineWidth, ty - 10},
				{r.x + options.lineWidth, ty}
            };
			bezierPts.add(pts);

			pts = new float[][]
		    {
				{r.x + options.lineWidth, ty},
				{r.x + options.lineWidth, ty + 30},
				{r.x + options.left - options.lineWidth, by - 30},
				{r.x + options.left - options.lineWidth, by}
		    };
			bezierPts.add(pts);

			pts = new float[][]
			{
				{r.x + options.left - options.lineWidth, by},
				{r.x + options.left - options.lineWidth, by + 10},
				{r.x + options.lineWidth, by + 10},
					{r.x + options.lineWidth, by}
			};
			bezierPts.add(pts);
		}

		if (options.bottom > 0) {

			float lx = r.x + (r.width / 5.0f);
			float rx = r.x + ((r.width * 4) / 5.0f);

			float[][] pts = new float[][]
			{
				{lx, r.y + r.height - options.lineWidth},
				{lx - 10, r.y + r.height - options.lineWidth},
				{lx - 10, r.y + r.height - options.bottom + options.lineWidth},
				{lx, r.y + r.height - options.bottom + options.lineWidth}
			};
			bezierPts.add(pts);

			pts = new float[][]
			{
				{lx, r.y + r.height - options.bottom + options.lineWidth},
				{lx + 30, r.y + r.height - options.bottom + options.lineWidth},
				{rx - 30, r.y + r.height - options.lineWidth},
				{rx, r.y + r.height - options.lineWidth}
			};
			bezierPts.add(pts);

			pts = new float[][]
			{
				{rx, r.y + r.height - options.lineWidth},
				{rx + 10, r.y + r.height - options.lineWidth},
				{rx + 10, r.y + r.height - options.bottom + options.lineWidth},
				{rx, r.y + r.height - options.bottom + options.lineWidth},
			};
			bezierPts.add(pts);
		}

		if (options.right > 0) {
			float ty = r.y + (r.height / 5.0f);
			float by = r.y + ((r.height * 4) / 5.0f);

			float[][] pts = new float[][]
            {
				{r.x + r.width - options.right + options.lineWidth, ty},
				{r.x + r.width - options.right + options.lineWidth, ty - 10},
				{r.x + r.width - options.lineWidth, ty - 10},
				{r.x + r.width - options.lineWidth, ty}
            };
			bezierPts.add(pts);

			pts = new float[][]
		    {
				{r.x + r.width - options.lineWidth, ty},
				{r.x + r.width - options.lineWidth, ty + 30},
				{r.x + r.width - options.right + options.lineWidth, by - 30},
				{r.x + r.width - options.right + options.lineWidth, by}
		    };
			bezierPts.add(pts);

			pts = new float[][]
			{
				{r.x + r.width - options.right + options.lineWidth, by},
				{r.x + r.width - options.right + options.lineWidth, by + 10},
				{r.x + r.width - options.lineWidth, by + 10},
				{r.x + r.width - options.lineWidth, by}
			};
			bezierPts.add(pts);
		}
	}

	public static class Options {
		int top = 16;
		int left = 16;
		int bottom = 16;
		int right = 16;
		Color color;
		int lineWidth;

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
