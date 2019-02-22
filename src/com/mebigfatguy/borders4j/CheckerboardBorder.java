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
import java.awt.Rectangle;

import javax.swing.Timer;
import javax.swing.border.AbstractBorder;

import com.mebigfatguy.borders4j.timer.BorderTimer;

public class CheckerboardBorder extends AbstractBorder {

	private static final long serialVersionUID = -2891429582120416794L;

	private final Options options;
	private int startIndex = 0;

	public CheckerboardBorder() {
		this(new Options());
	}

	public CheckerboardBorder(Options options) {
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
		try {
			final Rectangle r = c.getBounds();

			if (options.top > 0) {
				int colorIndex = startIndex;
				for (int i = r.x; i < (r.x + r.width); i += options.top) {
					g.setColor(options.colors[colorIndex]);
					colorIndex = (colorIndex + 1) % options.colors.length;
					g.fillRect(i, r.y, options.top, options.top);
				}
			}
			if (options.left > 0) {
				int colorIndex = startIndex;
				for (int i = r.y; i < (r.y + r.height); i += options.left) {
					g.setColor(options.colors[colorIndex]);
					colorIndex = (colorIndex + 1) % options.colors.length;
					g.fillRect(r.x, i, options.left, options.left);
				}
			}
			if (options.bottom > 0) {
				int yPos = r.y + r.height - options.bottom;
				int colorIndex = startIndex;
				for (int i = r.x; i < (r.x + r.width); i += options.bottom) {
					g.setColor(options.colors[colorIndex]);
					colorIndex = (colorIndex + 1) % options.colors.length;
					g.fillRect(i, yPos, options.bottom, options.bottom);
				}
			}
			if (options.right > 0) {
				int xPos = r.x + r.width - options.right;
				int colorIndex = startIndex;
				for (int i = r.y; i < (r.y + r.height); i += options.right) {
					g.setColor(options.colors[colorIndex]);
					colorIndex = (colorIndex + 1) % options.colors.length;
					g.fillRect(xPos, i, options.right, options.right);
				}
			}


			if (options.blinkDelay > 0) {
				startIndex = (startIndex + 1) % options.colors.length;
				Timer t = new BorderTimer(c, options.top, options.left, options.bottom, options.right, options.blinkDelay);
				t.setRepeats(false);
				t.start();
			}
		} finally {
			g.setColor(saveColor);
		}
	}

	public static class Options {
		public int top = 6;
		public int left = 6;
		public int bottom = 6;
		public int right = 6;
		public Color[] colors = new Color[] { Color.BLACK, Color.WHITE };
		public int blinkDelay = 0;

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

		public Options setColors(Color... colors) {
			this.colors = colors;
			return this;
		}

		public Options setBlinkDelay(int blinkDelay) {
			this.blinkDelay = blinkDelay;
			return this;
		}
	}
}