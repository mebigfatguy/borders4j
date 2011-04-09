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

	private final int numLines;
	private final Color color;
	private final int lineSize;
	private final int gapSize;
	private final int borderSize;
	private final Stroke stroke;

	public MultilineBorder() {
		this(3);
	}

	public MultilineBorder(int numLines) {
		this(3, Color.BLACK);
	}

	public MultilineBorder(int numLines, Color color) {
		this(numLines, color, 1);
	}

	public MultilineBorder(int numLines, Color color, int lineSize) {
		this(numLines, color, 1, 1);
	}

	public MultilineBorder(int numLines, Color color, int lineSize, int gapSize) {
		this.numLines = numLines;
		this.color = color;
		this.lineSize = lineSize;
		this.gapSize = gapSize;
		borderSize = (numLines * (lineSize)) + (numLines - 1) * gapSize;

		stroke = new BasicStroke(lineSize);
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
			g.setColor(color);
			g2d.setStroke(stroke);

			Rectangle r = c.getBounds();
			r.width -= lineSize;
			r.height -= lineSize;

			int shrinkSize = (lineSize + gapSize);
			for (int i = 0; i < numLines; i++) {
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
}