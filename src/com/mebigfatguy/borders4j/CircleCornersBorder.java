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

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Area;

import javax.swing.border.AbstractBorder;

public class CircleCornersBorder extends AbstractBorder {

	private static final long serialVersionUID = 1765570151443641304L;

	private final Options options;

	public CircleCornersBorder() {
		this(new Options());
	}

	public CircleCornersBorder(Options options) {
		this.options = options;
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

		Color saveColor = g.getColor();
		Shape saveClip = g.getClip();
		try {
			Rectangle r = c.getBounds();

			Area clip = getClippingArea(r);
			clip.intersect(new Area(saveClip));
			g.setClip(clip);

			g.setColor(options.fillColor);
			g.fillOval(r.x, r.y, options.left*2, options.top*2); //tl
			g.fillOval(r.x, r.y + r.height - 2 * options.bottom - options.lineWidth, options.left * 2, options.bottom * 2); //bl
			g.fillOval(r.x + r.width - 2 * options.right - options.lineWidth, r.y + r.height - 2 * options.bottom - options.lineWidth, options.right * 2, options.bottom * 2); //br
			g.fillOval(r.x + r.width - 2 * options.right - options.lineWidth, r.y, options.right * 2, options.top * 2); //tr

			g.setColor(options.lineColor);
			g.drawOval(r.x, r.y, options.left*2, options.top*2); //tl
			g.drawOval(r.x, r.y + r.height - 2 * options.bottom - options.lineWidth, options.left * 2, options.bottom * 2); //bl
			g.drawOval(r.x + r.width - 2 * options.right - options.lineWidth, r.y + r.height - 2 * options.bottom - options.lineWidth, options.right * 2, options.bottom * 2); //br
			g.drawOval(r.x + r.width - 2 * options.right - options.lineWidth, r.y, options.right * 2, options.top * 2); //tr

			if (options.drawEdges) {
				r.x += options.left - options.lineWidth;
				r.y += options.top - options.lineWidth;
				r.width -= options.left + options.right - options.lineWidth;
				r.height -= options.top + options.bottom - options.lineWidth;
				g.drawRect(r.x, r.y, r.width, r.height);
			} else {
				int lX = r.x + options.left - options.lineWidth;
				int rX = r.x + r.width - options.right;
				int tY = r.y + options.top - options.lineWidth;
				int bY = r.y + r.height - options.bottom;


				g.drawLine(lX, tY, r.x + options.left * 2, tY);
				g.drawLine(lX, tY, lX, r.y + options.top * 2);

				g.drawLine(lX, bY, r.x + options.left * 2, bY);
				g.drawLine(lX, bY, lX, r.height - options.bottom * 2 - options.lineWidth);

				g.drawLine(r.x + r.width - options.right * 2, bY, rX, bY);
				g.drawLine(rX, bY, rX, r.y + r.height - options.bottom * 2);

				g.drawLine(r.x + r.width - options.right * 2, tY, rX, lX);
				g.drawLine(rX, tY, rX, r.y + options.top * 2 - options.lineWidth);

			}

		} finally {
			g.setColor(saveColor);
			g.setClip(saveClip);
		}
	}

	private Area getClippingArea(Rectangle r) {

		Rectangle topRect = new Rectangle(r.x, r.y, r.width, options.top);
		Rectangle leftRect = new Rectangle(r.x, r.y, options.left, r.height);
		Rectangle bottomRect = new Rectangle(r.x, r.y + r.height - options.bottom, r.width, options.bottom);
		Rectangle rightRect = new Rectangle(r.x + r.width - options.right, r.y, options.right, r.height);

		Area clip = new Area(topRect);
		clip.add(new Area(leftRect));
		clip.add(new Area(bottomRect));
		clip.add(new Area(rightRect));

		return clip;
	}

	public static class Options {
		int top = 8;
		int left = 8;
		int bottom = 8;
		int right = 8;
		Color fillColor = Color.WHITE;
		Color lineColor = Color.BLACK;
		boolean drawEdges = false;
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

		public Options setFillColor(Color fillColor) {
			this.fillColor = fillColor;
			return this;
		}

		public Options setLineColor(Color lineColor) {
			this.lineColor = lineColor;
			return this;
		}

		public Options setDrawEdges(boolean drawEdges) {
			this.drawEdges = drawEdges;
			return this;
		}

		public Options setLineWidth(int lineWidth) {
			this.lineWidth = lineWidth;
			return this;
		}
	}
}
