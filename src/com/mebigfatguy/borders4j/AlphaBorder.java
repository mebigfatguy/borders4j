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

	private final int top, left, bottom, right;
	private final Color color;
	private AlphaComposite composite;
	private float alphaDelta = 0.1f;
	private final int transitionDelay;

	public AlphaBorder() {
		this(8, 8, 8, 8);
	}

	public AlphaBorder(int top, int left, int bottom, int right) {
		this(top, left, bottom, right, 0.5f);
	}

	public AlphaBorder(int top, int left, int bottom, int right, float transparency) {
		this(top, left, bottom, right, transparency, Color.BLACK);
	}

	public AlphaBorder(int top, int left, int bottom, int right, float transparency, Color color) {
		this(top, left, bottom, right, transparency, color, AlphaComposite.SRC_OVER);
	}

	public AlphaBorder(int top, int left, int bottom, int right, float transparency, Color color, int compositeType) {
		this(top, left, bottom, right, transparency, color, compositeType, 0);
	}

	public AlphaBorder(int top, int left, int bottom, int right, float transparency, Color color, int compositeType, int transitionDelay) {
		this.top = top;
		this.left = left;
		this.bottom = bottom;
		this.right = right;
		this.color = color;
		composite = AlphaComposite.getInstance(compositeType, transparency);
		this.transitionDelay = transitionDelay;
	}

	@Override
	public boolean isBorderOpaque() {
		return true;
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

		Graphics2D g2d = (Graphics2D) g;
		Color saveColor = g.getColor();
		Composite saveComposite = g2d.getComposite();

		try {
			g.setColor(color);
			g2d.setComposite(composite);

			final Rectangle r = c.getBounds();

			if (top > 0) {
				g.fillRect(r.x, r.y, r.width, top);
			}

			if (left > 0) {
				g.fillRect(r.x, r.y + top, left, r.height - bottom - top);
			}

			if (bottom > 0) {
				g.fillRect(r.x, r.y + r.height - bottom, r.width, bottom);
			}

			if (right > 0) {
				g.fillRect(r.x + r.width - right, r.y + top, right, r.height - bottom - top);
			}

			if (transitionDelay > 0) {
				float alpha = composite.getAlpha() + alphaDelta;
				if (alpha < 0.0) {
					alpha = 0.0f;
					alphaDelta *= -1f;
				} else if (alpha > 1.0) {
					alpha = 1.0f;
					alphaDelta *= -1f;
				}
				composite = composite.derive(alpha);

				Timer t = new BorderTimer(c, top, left, bottom, right, transitionDelay);
				t.setRepeats(false);
				t.start();
			}

		} finally {
			g.setColor(saveColor);
			g2d.setComposite(saveComposite);
		}
	}
}