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
package com.mebigfatguy.borders4j.explorer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.Border;

import com.mebigfatguy.borders4j.AlphaBorder;
import com.mebigfatguy.borders4j.CheckerboardBorder;
import com.mebigfatguy.borders4j.CircleCornersBorder;
import com.mebigfatguy.borders4j.CurlyBraceBorder;
import com.mebigfatguy.borders4j.MultilineBorder;

public class BorderExplorer extends JFrame {

	private static final Map<String, Border> borders = new LinkedHashMap<String, Border>();

	static {
		borders.put("Animated Checkerboard", new CheckerboardBorder(new CheckerboardBorder.Options().setBlinkDelay(400).setColors(Color.BLUE, Color.YELLOW, Color.RED)));
		borders.put("Checkerboard", new CheckerboardBorder(new CheckerboardBorder.Options().setColors(Color.RED, Color.WHITE)));
		borders.put("Animated Alpha", new AlphaBorder(new AlphaBorder.Options().setTransitionDelay(200).setColor(Color.RED)));
		borders.put("Alpha", new AlphaBorder(new AlphaBorder.Options().setColor(Color.BLUE).setTransparency(0.2f)));
		borders.put("Multiline", new MultilineBorder(new MultilineBorder.Options().setColor(Color.BLUE).setNumLines(5).setGapSize(4)));
		borders.put("CircleCorners", new CircleCornersBorder(new CircleCornersBorder.Options().setFillColor(Color.RED).setLineColor(Color.BLACK).setLineWidth(2)));
		borders.put("CurlyBraces", new CurlyBraceBorder(new CurlyBraceBorder.Options().setColor(Color.BLUE).setLineWidth(2)));
	}

	private final JPanel panel;

	public BorderExplorer() {
		setTitle("Border Explorer");
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout(4, 4));
		panel = new JPanel();
		panel.setPreferredSize(new Dimension(400, 300));
		cp.add(panel, BorderLayout.CENTER);

		initMenus();
		pack();
	}

	private void initMenus() {
		JMenuBar mb = new JMenuBar();
		JMenu menu = new JMenu("Borders");
		mb.add(menu);

		for (String name : borders.keySet()) {
			JMenuItem item = new JMenuItem(new BorderAction(name));
			menu.add(item);
		}

		setJMenuBar(mb);
	}

	public static void main(String[] args) {
		BorderExplorer be = new BorderExplorer();
		be.setLocationRelativeTo(null);
		be.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		be.setVisible(true);
	}

	class BorderAction extends AbstractAction {

		private final String key;

		public BorderAction(String name) {
			super(name);
			key = name;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			Border b = borders.get(key);
			panel.setBorder(b);
		}
	}
}