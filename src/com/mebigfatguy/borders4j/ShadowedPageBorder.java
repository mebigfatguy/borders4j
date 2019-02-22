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

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Polygon;
import java.awt.Rectangle;

import javax.swing.border.AbstractBorder;

public class ShadowedPageBorder extends AbstractBorder {

	private static final long serialVersionUID = 7468389521544006247L;
    private final Options options;
	private final int top;
	private final int left;
	private final int bottom;
	private final int right;

	public ShadowedPageBorder() {
		this(new Options());
	}

	public ShadowedPageBorder(Options options) {
		this.options = options;
		top = (options.shadowYOffset < 0) ? -options.shadowYOffset : 0;
		left = (options.shadowXOffset < 0) ? -options.shadowXOffset : 0;
		bottom = (options.shadowYOffset > 0) ? options.shadowYOffset : 0;
		right = (options.shadowXOffset > 0) ? options.shadowXOffset : 0;
	}

	@Override
	public Insets getBorderInsets(Component c) {
		return new Insets(top, left, bottom, right);
	}

	@Override
	public Insets getBorderInsets(Component c, Insets insets) {
		insets.top = top;
		insets.left = left;
		insets.bottom = bottom;
		insets.right = right;

		return insets;
	}

	@Override
	public void paintBorder(final Component c, Graphics g, int x, int y, int width, int height) {

		Color saveColor = g.getColor();
		try {
			g.setColor(options.shadowColor);
			Rectangle r = c.getBounds();
			if (options.shadowXOffset < 0) {
				 if (options.shadowYOffset < 0) {
					 g.fillRect(r.x, r.y, r.width - left - 20, top);
					 g.fillRect(r.x, r.y, left, r.height - top);
				 } else {
					 g.fillRect(r.x, r.y + bottom, left, r.height);
					 g.fillRect(r.x, r.y + r.height - bottom, r.width - left, r.height);
				 }
			} else {
				 if (options.shadowYOffset < 0) {
					 g.fillRect(r.x + right, r.y, r.width, top);
					 g.fillRect(r.x + r.width - right, r.y, r.width, r.height - top);
				 } else {
					 g.fillRect(r.x + r.width - right, r.y + bottom + 20, r.width, r.height);
					 g.fillRect(r.x + right, r.y + r.height - bottom, r.width, r.height);
				 }
			}

			g.setColor(options.lineColor);
			r.x += left - 1;
			r.y += top - 1;
			r.width -= left + right;
			r.height -= top + bottom;

			int[] xpoints =
			{
				r.x,
				r.x + r.width - 20,
				r.x + r.width,
				r.x + r.width,
				r.x,
				r.x
			};

			int[] ypoints =
			{
				r.y,
				r.y,
				r.y + 20,
				r.y + r.height,
				r.y + r.height,
				r.y
			};

			Polygon poly = new Polygon(xpoints, ypoints, 6);

			g.drawPolygon(poly);
			g.drawLine(r.x + r.width - 20, r.y, r.x + r.width - 20, r.y + 20);
			g.drawLine(r.x + r.width - 20, r.y + 20, r.x + r.width, r.y + 20);
		} finally {
			g.setColor(saveColor);
		}
	}

	public static class Options {

		int shadowXOffset = 8;
		int shadowYOffset = 8;
		Color shadowColor = Color.LIGHT_GRAY;
		Color lineColor = Color.BLACK;

		public Options setShadowXOffset(int shadowXOffset) {
			this.shadowXOffset = shadowXOffset;
			return this;
		}

		public Options setShadowYOffset(int shadowYOffset) {
			this.shadowYOffset = shadowYOffset;
			return this;
		}

		public Options setShadowColor(Color shadowColor) {
			this.shadowColor = shadowColor;
			return this;
		}

		public Options setLineColor(Color lineColor) {
			this.lineColor = lineColor;
			return this;
		}
	}
}
