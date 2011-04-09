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

	private final int top, left, bottom, right;
	private final Color fillColor;
	private final Color lineColor;
	private final boolean drawEdges;

	public CircleCornersBorder() {
		this(8, 8, 8, 8);
	}

	public CircleCornersBorder(int top, int left, int bottom, int right) {
		this(top, left, bottom, right, Color.WHITE, Color.BLACK);
	}

	public CircleCornersBorder(int top, int left, int bottom, int right, Color fillColor, Color lineColor) {
		this(top, left, bottom, right, Color.WHITE, Color.BLACK, false);
	}

	public CircleCornersBorder(int top, int left, int bottom, int right, Color fillColor, Color lineColor, boolean drawEdges) {
		this.top = top;
		this.left = left;
		this.bottom = bottom;
		this.right = right;
		this.fillColor = fillColor;
		this.lineColor = lineColor;
		this.drawEdges = drawEdges;
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
		Shape saveClip = g.getClip();
		try {
			Rectangle r = c.getBounds();

			Area clip = getClippingArea(r);
			clip.intersect(new Area(saveClip));
			g.setClip(clip);

			g.setColor(fillColor);
			g.fillOval(r.x, r.y, left*2, top*2); //tl
			g.fillOval(r.x, r.y + r.height - 2 * bottom - 1, left * 2, bottom * 2); //bl
			g.fillOval(r.x + r.width - 2 * right - 1, r.y + r.height - 2 * bottom - 1, right * 2, bottom * 2); //br
			g.fillOval(r.x + r.width - 2 * right - 1, r.y, right * 2, top * 2); //tr

			g.setColor(lineColor);
			g.drawOval(r.x, r.y, left*2, top*2); //tl
			g.drawOval(r.x, r.y + r.height - 2 * bottom - 1, left * 2, bottom * 2); //bl
			g.drawOval(r.x + r.width - 2 * right - 1, r.y + r.height - 2 * bottom - 1, right * 2, bottom * 2); //br
			g.drawOval(r.x + r.width - 2 * right - 1, r.y, right * 2, top * 2); //tr

			if (drawEdges) {
				r.x += left - 1;
				r.y += top - 1;
				r.width -= left + right - 1;
				r.height -= top + bottom - 1;
				g.drawRect(r.x, r.y, r.width, r.height);
			} else {
				int lX = r.x + left - 1;
				int rX = r.x + r.width - right;
				int tY = r.y + top - 1;
				int bY = r.y + r.height - bottom;


				g.drawLine(lX, tY, r.x + left * 2, tY);
				g.drawLine(lX, tY, lX, r.y + top * 2);

				g.drawLine(lX, bY, r.x + left * 2, bY);
				g.drawLine(lX, bY, lX, r.height - bottom * 2 - 1);

				g.drawLine(r.x + r.width - right * 2, bY, rX, bY);
				g.drawLine(rX, bY, rX, r.y + r.height - bottom * 2);

				g.drawLine(r.x + r.width - right * 2, tY, rX, lX);
				g.drawLine(rX, tY, rX, r.y + top * 2 - 1);

			}

		} finally {
			g.setColor(saveColor);
			g.setClip(saveClip);
		}
	}

	private Area getClippingArea(Rectangle r) {

		Rectangle topRect = new Rectangle(r.x, r.y, r.width, top);
		Rectangle leftRect = new Rectangle(r.x, r.y, left, r.height);
		Rectangle bottomRect = new Rectangle(r.x, r.y + r.height - bottom, r.width, bottom);
		Rectangle rightRect = new Rectangle(r.x + r.width - right, r.y, right, r.height);

		Area clip = new Area(topRect);
		clip.add(new Area(leftRect));
		clip.add(new Area(bottomRect));
		clip.add(new Area(rightRect));

		return clip;
	}
}
