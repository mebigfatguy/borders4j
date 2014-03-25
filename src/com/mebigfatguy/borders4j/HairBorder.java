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
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.border.AbstractBorder;

public class HairBorder extends AbstractBorder {

	private static final long serialVersionUID = -6466674892728127991L;

	private final Options options;
	private final Stroke stroke;
	private Component listenerComponent;
	private HiliteListener hiliteListener;
	private final Rectangle updateRect = new Rectangle(0, 0, 0, 0);
	private int topHilitePos = -1;
	private int leftHilitePos = -1;
	private int bottomHilitePos = -1;
	private int rightHilitePos = -1;

	public HairBorder() {
		this(new Options());
	}

	public HairBorder(Options options) {
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

		if ((hiliteListener != null) && (c != listenerComponent)) {
			listenerComponent.removeMouseMotionListener(hiliteListener);
			hiliteListener = null;
		}

		if (hiliteListener == null) {
			hiliteListener = new HiliteListener(c);
			c.addMouseMotionListener(hiliteListener);
			listenerComponent = c;
		}

		Graphics2D g2d = (Graphics2D) g;
		Color saveColor = g.getColor();
		Stroke saveStroke = g2d.getStroke();

		try {
			Rectangle r = c.getBounds();
			g2d.setStroke(stroke);
			g.setColor(options.lineColor);

			if (options.top > 1) {
				int right = r.x + r.width - options.right;
				int start = r.y + options.top/2;
				int end = r.y + options.top;
				for (int i = r.x + options.left; i < right; i+= options.lineSpacing) {
					boolean hilite = Math.abs(i - topHilitePos) < options.lineSpacing;
					if (hilite) {
						g.setColor(options.hiliteColor);
						start = r.y;
					}
					g.drawLine(i, start, i, end);
					if (hilite) {
						g.setColor(options.lineColor);
						start = r.y + options.top/2;
					}
				}
			}

			if (options.left > 1) {
				int bottom = r.y + r.height - options.bottom;
				int start = r.x + (options.left/2);
				int end = r.x + options.left;
				for (int i = r.y + options.top; i < bottom; i+= options.lineSpacing) {
					boolean hilite = Math.abs(i - leftHilitePos) < options.lineSpacing;
					if (hilite) {
						g.setColor(options.hiliteColor);
						start = r.x;
					}
					g.drawLine(start, i, end, i);
					if (hilite) {
						g.setColor(options.lineColor);
						start = r.x + (options.left/2);
					}
				}
			}

			if (options.bottom > 1) {
				int right = r.x + r.width - options.right;
				int end = r.x + r.y + r.height - (options.top/2);
				for (int i = r.x + options.left; i < right; i+= options.lineSpacing) {
					boolean hilite = Math.abs(i - bottomHilitePos) < options.lineSpacing;
					if (hilite) {
						g.setColor(options.hiliteColor);
						end = r.x + r.y + r.height;
					}
					g.drawLine(i, end, i, r.y + r.height - options.top);
					if (hilite) {
						g.setColor(options.lineColor);
						end = r.x + r.y + r.height - (options.top/2);
					}
				}
			}

			if (options.right > 1) {
				int bottom = r.y + r.height - options.bottom;
				int end = r.x + r.width - (options.left/2);
				for (int i = r.y + options.top; i < bottom; i+= options.lineSpacing) {
					boolean hilite = Math.abs(i - rightHilitePos) < options.lineSpacing;
					if (hilite) {
						g.setColor(options.hiliteColor);
						end = r.x + r.width;
					}
					g.drawLine(end, i, r.x + r.width - options.left, i);
					if (hilite) {
						g.setColor(options.lineColor);
						end = r.x + r.width - (options.left/2);
					}
				}
			}

			if (!updateRect.isEmpty()) {
				c.repaint(updateRect.x, updateRect.y, updateRect.width, updateRect.height);
				updateRect.setSize(0, 0);
			}

			g2d.setStroke(stroke);
		} finally {
			g.setColor(saveColor);
			g2d.setStroke(saveStroke);
		}
	}


	public static final class Options {
		int top = 14;
		int left = 14;
		int bottom = 14;
		int right = 14;
		Color lineColor = Color.BLACK;
		Color hiliteColor = Color.BLUE;
		int lineWidth = 1;
		int lineSpacing = 4;

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

		public Options setLineColor(Color lineColor) {
			this.lineColor = lineColor;
			return this;
		}

		public Options setHiliteColor(Color hiliteColor) {
			this.hiliteColor = hiliteColor;
			return this;
		}

		public Options setLineWidth(int lineWidth) {
			this.lineWidth = lineWidth;
			return this;
		}

		public Options setLineSpacing(int lineSpacing) {
			this.lineSpacing = lineSpacing;
			return this;
		}
	}

	private class HiliteListener implements MouseMotionListener {

		private final Component component;

		public HiliteListener(Component component) {
			this.component = component;
		}

		@Override
		public void mouseDragged(MouseEvent e) {
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			Rectangle r = component.getBounds();

			int mX = e.getX();
			int mY = e.getY();
			int gap = options.lineSpacing + options.lineWidth;

			if ((mY < (r.y + options.top)) && (mX > (r.x + options.left)) && (mX < r.x + r.width - options.right)) {
				int x = e.getX();
				topHilitePos = x;
				updateRect.setBounds(mX - gap, r.y, mX + gap, r.y + options.top);
				component.repaint(mX - gap, r.y, mX + gap, r.y + options.top);
				leftHilitePos = -1;
				bottomHilitePos = -1;
				rightHilitePos = -1;
			} else if ((mX < (r.x + options.left)) && (mY > (r.y + options.top)) && (mY < (r.y + r.height - options.bottom))) {
				int y = e.getY();
				leftHilitePos = y;
				updateRect.setBounds(r.x, mY - gap, r.x + options.left, mY + gap);
				component.repaint(r.x, mY - gap, r.x + options.left, mY + gap);
				topHilitePos = -1;
				bottomHilitePos = -1;
				rightHilitePos = -1;
			} else if ((mY > (r.y + r.height - options.bottom)) && (mX > r.x + options.left) && (mX < (r.x + r.width - options.right))) {
				int x = e.getX();
				bottomHilitePos = x;
				updateRect.setBounds(mX - gap, r.y + r.height - options.top, mX + gap, r.y + r.height);
				component.repaint(mX - gap, r.y + r.height - options.top, mX + gap, r.y + r.height);
				topHilitePos = -1;
				leftHilitePos = -1;
				rightHilitePos = -1;
			} else if ((mX > (r.x + r.width - options.right)) && (mY > r.y + options.top) && (mY < r.y + r.height - options.bottom)) {
				int y = e.getY();
				rightHilitePos = y;
				updateRect.setBounds(r.x + r.width - options.right, mY - gap, r.x + r.width, mY + gap);
				component.repaint(r.x + r.width - options.right, mY - gap, r.x + r.width, mY + gap);
				topHilitePos = -1;
				leftHilitePos = -1;
				bottomHilitePos = -1;
			} else {
				topHilitePos = -1;
				leftHilitePos = -1;
				bottomHilitePos = -1;
				rightHilitePos = -1;
			}
		}
	}
}
