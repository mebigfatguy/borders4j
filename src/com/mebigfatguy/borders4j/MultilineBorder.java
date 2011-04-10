/*
 * borders4j - An collection of swing borders
 * Copyright 2011 MeBigFatGuy.com
 * Copyright 2011 Dave Brosius
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
import java.awt.Stroke;

import javax.swing.border.AbstractBorder;

public class MultilineBorder extends AbstractBorder {

	private static final long serialVersionUID = -392939532187744632L;

	private final Options options;
	private final int borderSize;
	private final Stroke stroke;

	public MultilineBorder() {
		this(new Options());
	}

	public MultilineBorder(Options options) {
		this.options = options;
		borderSize = (options.numLines * (options.lineSize)) + (options.numLines - 1) * options.gapSize;

		stroke = new BasicStroke(options.lineSize);
	}


	@Override
	public Insets getBorderInsets(Component c) {
		return new Insets(borderSize, borderSize, borderSize, borderSize);
	}

	@Override
	public Insets getBorderInsets(Component c, Insets insets) {
		insets.top = borderSize;
		insets.left = borderSize;
		insets.bottom = borderSize;
		insets.right = borderSize;

		return insets;
	}

	@Override
	public void paintBorder(final Component c, Graphics g, int x, int y, int width, int height) {

		Graphics2D g2d = (Graphics2D) g;
		Color saveColor = g.getColor();
		Stroke saveStroke = g2d.getStroke();

		try {
			g.setColor(options.color);
			g2d.setStroke(stroke);

			Rectangle r = c.getBounds();
			r.width -= options.lineSize;
			r.height -= options.lineSize;

			int shrinkSize = (options.lineSize + options.gapSize);
			for (int i = 0; i < options.numLines; i++) {
				g.drawRect(r.x, r.y, r.width, r.height);
				r.x += shrinkSize;
				r.y += shrinkSize;
				r.width -= 2 * shrinkSize;
				r.height -= 2 * shrinkSize;
			}

		} finally {
			g.setColor(saveColor);
			g2d.setStroke(saveStroke);
		}
	}

	public static class Options {
		public int numLines = 3;
		public Color color = Color.BLACK;
		public int lineSize = 1;
		public int gapSize = 1;

		public Options setNumLines(int numLines) {
			this.numLines = numLines;
			return this;
		}

		public Options setColor(Color color) {
			this.color = color;
			return this;
		}

		public Options setLineSize(int lineSize) {
			this.lineSize = lineSize;
			return this;
		}

		public Options setGapSize(int gapSize) {
			this.gapSize = gapSize;
			return this;
		}
	}
}