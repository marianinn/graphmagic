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

package name.dlazerka.gm.ui;

import name.dlazerka.gm.Edge;
import name.dlazerka.gm.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class EdgePanel extends AbstractEdgePanel {
	private static final Logger logger = LoggerFactory.getLogger(EdgePanel.class);

	private final Edge edge;
	private final VertexPanel tail;
	private final VertexPanel head;
	private final Point ctrlPoint;
	private boolean curved = false;
	private boolean dragging;
	private Shape hoverShape = curve;
	private Color color = EDGE_COLOR;
	private Stroke stroke = EDGE_STROKE;

	public EdgePanel(Edge edge, VertexPanel tail, VertexPanel head) {
		this.edge = edge;
		this.tail = tail;
		this.head = head;
		ctrlPoint = new Point(head.getVertexCenterX(), head.getVertexCenterY());

		logger.debug("{}", ctrlPoint);

		setOpaque(false);
		addMouseMotionListener(new DragMouseListener());
		addMouseListener(new MouseListener());
		setComponentPopupMenu(new PopupMenu());
	}

	public Edge getEdge() {
		return edge;
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(color);
		g2.setStroke(stroke);

		curve.setCurve(
			getFromPoint(),
			curved ? ctrlPoint : getFromPoint(),
			getToPoint()
		);

		hoverShape = EDGE_HOVER_STROKE.createStrokedShape(curve);

		g2.draw(curve);
	}

	@Override
	public boolean contains(int x, int y) {
		boolean b = hoverShape.contains(x, y);
//		if (b) logger.debug("{}", b);
		return b;
	}

	public GraphPanel getGraphPanel() {
		return (GraphPanel) getParent();
	}

	protected Point getFromPoint() {
		return head.getVertexCenter();
	}

	protected Point getToPoint() {
		return tail.getVertexCenter();
	}

	public void setCurved(boolean curved) {
		this.curved = curved;
	}

	/**
	 * Moves control poins so that edge will contain given (x, y) point.
	 * If the curve seems like line (contains(ctrlPoint)==true) then sets curved to false
	 *
	 * @param x which point.x the curve should contain
	 * @param y which point.y the curve should contain
	 */
	private void setCurvedTo(int x, int y) {

		int centerX = (getFromPoint().x + getToPoint().x) / 2;
		int centerY = (getFromPoint().y + getToPoint().y) / 2;

		ctrlPoint.move(
			x * 2 - centerX,
			y * 2 - centerY
		);

		setCurved(!contains(ctrlPoint));
	}

	public void setDragging(boolean dragging) {
		this.dragging = dragging;
	}

	public void onAdjacentVertexMoved() {
		repaint();
	}

	private class DragMouseListener extends MouseMotionAdapter {

		@Override
		public void mouseDragged(MouseEvent e) {
//			logger.trace("{}", e.getPoint());

			setCurvedTo(e.getX(), e.getY());

			repaint();
		}
	}

	private class MouseListener extends MouseAdapter {
		@Override
		public void mouseEntered(MouseEvent e) {
			color = EDGE_HOVER_COLOR;
			stroke = EDGE_HOVER_STROKE;
			repaint();
		}

		@Override
		public void mouseExited(MouseEvent e) {
			if (!dragging) {
				color = EDGE_COLOR;
				stroke = EDGE_STROKE;
				repaint();
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			setDragging(true);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			setDragging(false);
		}
	}

	private class PopupMenu extends JPopupMenu {
		private PopupMenu() {
			add(new DeleteAction());
		}

		private class DeleteAction extends AbstractAction {
			public DeleteAction() {
				super(Main.getString("delete.edge"));
			}

			public void actionPerformed(ActionEvent e) {
				getGraphPanel().getGraph().remove(edge);
			}
		}
	}
/*
	public int getX() {
		return Math.min(
			head.getVertexCenterX(),
			tail.getVertexCenterX()
		);
	}

	@Override
	public int getY() {
		return Math.min(
			head.getVertexCenterY(),
			tail.getVertexCenterY()
		);
	}

	@Override
	public int getWidth() {
		return Math.abs(head.getVertexCenterX() - tail.getVertexCenterX());
	}

	@Override
	public int getHeight() {
		return Math.abs(head.getVertexCenterY() - tail.getVertexCenterY());
	}
*/
}
