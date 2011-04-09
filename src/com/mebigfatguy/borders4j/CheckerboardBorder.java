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

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;
import javax.swing.border.AbstractBorder;

public class CheckerboardBorder extends AbstractBorder {

	private int type;
	private final int top, left, bottom, right;
	private final Color[] colors;
	private final int blinkDelay;
	private int startIndex = 0;

	public CheckerboardBorder() {
		this(8, 8, 8, 8);
	}

	public CheckerboardBorder(int top, int left, int bottom, int right) {
		this(top, left, bottom, right, Color.BLACK, Color.WHITE);
	}

	public CheckerboardBorder(int top, int left, int bottom, int right, Color color1, Color color2) {
		this(top, left, bottom, right, color1, color2, 0);
	}

	public CheckerboardBorder(int top, int left, int bottom, int right, Color color1, Color color2, int blinkDelay) {
		this.top = top;
		this.left = left;
		this.bottom = bottom;
		this.right = right;
		colors = new Color[] { color1, color2 };
		this.blinkDelay = blinkDelay;
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
			final Rectangle r = c.getBounds();

			if (top > 0) {
				int colorIndex = startIndex;
				for (int i = r.x; i < (r.x + r.width); i += top) {
					g.setColor(colors[colorIndex]);
					colorIndex = 1 - colorIndex;
					g.fillRect(i, r.y, top, top);
				}
			}
			if (left > 0) {
				int colorIndex = startIndex;
				for (int i = r.y; i < (r.y + r.height); i += left) {
					g.setColor(colors[colorIndex]);
					colorIndex = 1 - colorIndex;
					g.fillRect(r.x, i, left, left);
				}
			}
			if (bottom > 0) {
				int yPos = r.y + r.height - bottom;
				int colorIndex = startIndex;
				for (int i = r.x; i < (r.x + r.width); i += bottom) {
					g.setColor(colors[colorIndex]);
					colorIndex = 1 - colorIndex;
					g.fillRect(i, yPos, bottom, bottom);
				}
			}
			if (right > 0) {
				int xPos = r.x + r.width - right;
				int colorIndex = startIndex;
				for (int i = r.y; i < (r.y + r.height); i += right) {
					g.setColor(colors[colorIndex]);
					colorIndex = 1 - colorIndex;
					g.fillRect(xPos, i, right, right);
				}
			}

			startIndex = 1 - startIndex;

			if (blinkDelay > 0) {
				Timer t = new Timer(blinkDelay, new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						c.repaint(r.x, r.y, r.width, top);
						c.repaint(r.x, r.y, left, r.height);
						c.repaint(r.x + r.width - right, r.y, r.width, right);
						c.repaint(r.x, r.y + r.height - bottom, bottom, r.height);
					}
				});
				t.setRepeats(false);
				t.start();
			}
		} finally {
			g.setColor(saveColor);
		}
	}
}