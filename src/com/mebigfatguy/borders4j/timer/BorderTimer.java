package com.mebigfatguy.borders4j.timer;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

public class BorderTimer extends Timer {

	private static final long serialVersionUID = 1112283367936059895L;

	public BorderTimer(final Component c, final int top, final int left, final int bottom, final int right, int delay) {
		super(delay, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Rectangle r = c.getBounds();
				c.repaint(r.x, r.y, r.width, top);
				c.repaint(r.x, r.y, left, r.height);
				c.repaint(r.x + r.width - right, r.y, r.width, right);
				c.repaint(r.x, r.y + r.height - bottom, bottom, r.height);
			}
		});
	}
}
