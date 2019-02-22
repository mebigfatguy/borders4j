/*
 * borders4j - An collection of swing borders
 * Copyright 2011-2019 MeBigFatGuy.com
 * Copyright 2011-2019 Dave Brosius
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
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Area;

import javax.swing.border.AbstractBorder;

public class CastleBorder extends AbstractBorder {

	private static final long serialVersionUID = -4818846483630232676L;

	private final Options options;
	private final Stroke stroke;
	private Rectangle cacheBounds;
	private Polygon cachePoly;

	public CastleBorder() {
		this(new Options());
	}

	public CastleBorder(Options options) {
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
				cachePoly = recalculatePolygon(r);
				cacheBounds = (Rectangle)r.clone();
			}

			Area clip = new Area(r);
			r.x += options.left;
			r.y += options.top;
			r.width -= options.left + options.right;
			r.height -= options.top + options.bottom;
			clip.subtract(new Area(r));

			g.setClip(clip);

			g.setColor(options.fillColor);
			g.fillPolygon(cachePoly);
			g.setColor(options.lineColor);
			g.drawPolygon(cachePoly);
			g.setClip(saveClip);
			g.drawRect(r.x, r.y, r.width, r.height);
		} finally {
			g.setColor(saveColor);
			g2d.setStroke(saveStroke);
			g.setClip(saveClip);
		}
	}

	private Polygon recalculatePolygon(Rectangle r) {

		int[] xpoints =
		{
			r.x,
			r.x + 2 * options.left,
			r.x + 2 * options.left,
			r.x + r.width - 2 * options.right,
			r.x + r.width - 2 * options.right,
			r.x + r.width,
			r.x + r.width,
			r.x + r.width - options.right / 2,
			r.x + r.width - options.right / 2,
			r.x + r.width,
			r.x + r.width,
			r.x + r.width - 2 * options.right,
			r.x + r.width - 2 * options.right,
			r.x + 2 * options.left,
			r.x + 2 * options.left,
			r.x,
			r.x,
			r.x + options.left / 2,
			r.x + options.left / 2,
			r.x,
			r.x
		};

		int[] ypoints =
		{
			r.y,
			r.y,
			r.y + options.top / 2,
			r.y + options.top / 2,
			r.y,
			r.y,
			r.y + 2 * options.top,
			r.y + 2 * options.top,
			r.y + r.height - 2 * options.bottom,
			r.y + r.height - 2 * options.bottom,
			r.y + r.height,
			r.y + r.height,
			r.y + r.height - options.bottom / 2,
			r.y + r.height - options.bottom / 2,
			r.y + r.height,
			r.y + r.height,
			r.y + r.height - 2 * options.bottom,
			r.y + r.height - 2 * options.bottom,
			r.y + 2 * options.top,
			r.y + 2 * options.top,
			r.y
		};

		return new Polygon(xpoints, ypoints, 21);
	}

	public static class Options {
		int top = 12;
		int left = 12;
		int bottom = 12;
		int right = 12;
		Color fillColor = Color.LIGHT_GRAY;
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

		public Options setFillColor(Color fillColor) {
			this.fillColor = fillColor;
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
