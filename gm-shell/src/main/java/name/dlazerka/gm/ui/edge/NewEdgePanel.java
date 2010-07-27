/*
 * GraphMagic, package for scientists working in graph theory.
 * Copyright (C) 2009 Dzmitry Lazerka dlazerka@dlazerka.name
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

package name.dlazerka.gm.ui.edge;

import name.dlazerka.gm.ui.GraphPanel;
import name.dlazerka.gm.ui.VertexPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.geom.Line2D;

/**
 * While dragging a new edge, there is no end vertex yet.
 * This panel paints such 'fake' edge.
 *
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class NewEdgePanel extends AbstractEdgePanel {
	private static final Logger logger = LoggerFactory.getLogger(NewEdgePanel.class);

	private Point headPoint = new Point();
	private VertexPanel tail;
	private boolean visible;
	private Line2D line = new Line2D.Float();
	private static final Stroke EDGE_NEW_STROKE = new BasicStroke(2f);

	public NewEdgePanel() {
		setOpaque(false);
	}

	@Override
	public void setVisible(boolean visible) {
		this.visible = visible;
		trackMouseDragged();
	}

	@Override
	protected void paintComponent(Graphics g) {
		if (tail == null) {
			return;
		}

		if (visible) {
			Graphics2D g2 = (Graphics2D) g;
			line.setLine(getFromPoint(), headPoint);

			g2.setStroke(EDGE_NEW_STROKE);
			g2.draw(getShape());
		}
	}

	public Point getHeadPoint() {
		return headPoint;
	}

	public void setHeadPoint(Point headPoint) {
		this.headPoint = headPoint;
	}

	public void setTail(VertexPanel tail) {
		this.tail = tail;
	}

	@Override
	protected Line2D getShape() {
		return line;
	}

	private Point getFromPoint() {
		return tail.getVertexCenter();
	}

	private GraphPanel getGraphPanel() {
		return (GraphPanel) this.getParent();
	}

	public void trackMouseDragged() {
		Point mousePosition = getGraphPanel().getMousePosition();
//		logger.debug("{}", mousePosition);

		if (mousePosition != null) {
			headPoint = mousePosition;
			repaint();
		}
		else {
			// mouse out of container bounds
		}
	}
}
