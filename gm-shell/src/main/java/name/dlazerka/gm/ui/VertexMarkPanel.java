/*
 * GraphMagic, package for scientists working in graph theory.
 * Copyright (C) 2010 Dzmitry Lazerka dlazerka@dlazerka.name
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as
 * published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Author: Dzmitry Lazerka dlazerka@dlazerka.name
 */

package name.dlazerka.gm.ui;

import name.dlazerka.gm.Mark;
import name.dlazerka.gm.Vertex;

import javax.swing.*;
import java.util.Observer;
import java.util.Observable;
import java.awt.*;
import java.awt.event.ComponentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class VertexMarkPanel extends JPanel implements Paintable, Observer {
	private final static Logger logger = LoggerFactory.getLogger(VertexMarkPanel.class);
	private final VertexPanel vertexPanel;
	private final Mark mark;
	private static final int FONT_SIZE = 20;
	protected static final Font MARK_FONT = new Font("courier", Font.PLAIN, FONT_SIZE);
	protected static final Color MARK_COLOR = new Color(0x0, 0x80, 0x0);

	public VertexMarkPanel(VertexPanel vertexPanel) {
		super();
		this.vertexPanel = vertexPanel;
		Vertex vertex = vertexPanel.getVertex();
		this.mark = vertex.getMark();

		Dimension size = new Dimension(100, FONT_SIZE + 3);
		setPreferredSize(size);
		setSize(size);
		setBackground(Color.WHITE);
		setOpaque(false);
		vertexPanel.addComponentListener(new VertexPanelListener());

		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		if (mark != null && mark.size() > 0) {
			g2.setFont(MARK_FONT);
			g2.setColor(MARK_COLOR);

			g2.drawString(mark.toString(), 0, FONT_SIZE - 2);
		}

	}

	@Override
	public void update(Observable o, Object arg) {
		repaint();
	}

	public VertexPanel getVertexPanel() {
		return vertexPanel;
	}

	public Mark getMark() {
		return mark;
	}

	private class VertexPanelListener extends ComponentAdapter {
		@Override
		public void componentMoved(ComponentEvent e) {
			VertexMarkPanel.this.setLocation(vertexPanel.getX() - 10,
			                                 vertexPanel.getY() - FONT_SIZE - 5);
		}
	}
}
