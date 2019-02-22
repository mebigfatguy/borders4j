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

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.Timer;
import javax.swing.border.AbstractBorder;

import com.mebigfatguy.borders4j.timer.BorderTimer;

public class AlphaBorder extends AbstractBorder {

	private static final long serialVersionUID = 1683232655601392384L;

	private final Options options;
	private AlphaComposite composite;
	private float alphaDelta = 0.1f;

	public AlphaBorder() {
		this(new Options());
	}

	public AlphaBorder(Options options) {
		this.options = options;
		composite = AlphaComposite.getInstance(options.compositeType, options.transparency);
	}

	@Override
	public boolean isBorderOpaque() {
		return true;
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
		Composite saveComposite = g2d.getComposite();

		try {
			g.setColor(options.color);
			g2d.setComposite(composite);

			final Rectangle r = c.getBounds();

			if (options.top > 0) {
				g.fillRect(r.x, r.y, r.width, options.top);
			}

			if (options.left > 0) {
				g.fillRect(r.x, r.y + options.top, options.left, r.height - options.bottom - options.top);
			}

			if (options.bottom > 0) {
				g.fillRect(r.x, r.y + r.height - options.bottom, r.width, options.bottom);
			}

			if (options.right > 0) {
				g.fillRect(r.x + r.width - options.right, r.y + options.top, options.right, r.height - options.bottom - options.top);
			}

			if (options.transitionDelay > 0) {
				float alpha = composite.getAlpha() + alphaDelta;
				if (alpha < 0.0) {
					alpha = 0.0f;
					alphaDelta *= -1f;
				} else if (alpha > 1.0) {
					alpha = 1.0f;
					alphaDelta *= -1f;
				}
				composite = composite.derive(alpha);

				Timer t = new BorderTimer(c, options.top, options.left, options.bottom, options.right, options.transitionDelay);
				t.setRepeats(false);
				t.start();
			}

		} finally {
			g.setColor(saveColor);
			g2d.setComposite(saveComposite);
		}
	}

	public static class Options {
		int top = 6;
		int left = 6;
		int bottom = 6;
		int right = 6;
		Color color = Color.RED;
		float transparency = 0.5f;
		int compositeType = AlphaComposite.SRC_OVER;
		int transitionDelay = 0;

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

		public Options setTransparency(float transparency) {
			this.transparency = transparency;
			return this;
		}

		public Options setCompositeType(int compositeType) {
			this.compositeType = compositeType;
			return this;
		}

		public Options setTransitionDelay(int transitionDelay) {
			this.transitionDelay = transitionDelay;
			return this;
		}
	}
}