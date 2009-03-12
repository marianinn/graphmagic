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

package name.dlazerka.gc.ui;

import name.dlazerka.gc.bean.Edge;
import name.dlazerka.gc.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.ActionEvent;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class EdgePanel extends AbstractEdgePanel {
	private static final Logger logger = LoggerFactory.getLogger(EdgePanel.class);

	private final Edge edge;
	private final VertexPanel tail;
	private final VertexPanel head;
	private final Point ctrlPoint = new Point();
	private boolean curved = false;

	public EdgePanel(Edge edge, VertexPanel tail, VertexPanel head) {
		this.edge = edge;
		this.tail = tail;
		this.head = head;

		logger.debug("{}", ctrlPoint);

		setOpaque(false);
		addMouseMotionListener(new DragMouseListener());
		setComponentPopupMenu(new PopupMenu());
	}

	public Edge getEdge() {
		return edge;
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		drawEdge(
			g2,
			getFromPoint(),
			curved ? ctrlPoint : getFromPoint(),
			getToPoint()
		);
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
	 * If the curve seems like line (contains(ctrlPoint)==true) then sets curved to false
	 * @param x which point.x the curve should contain
	 * @param y which point.y the curve should contain
	 */
	private void setCurvedTo(int x, int y) {
		setCurved(true);

		int centerX = (getFromPoint().x + getToPoint().x) / 2;
		int centerY = (getFromPoint().y + getToPoint().y) / 2;

		ctrlPoint.move(
			x * 2 - centerX,
			y * 2 - centerY
		);

		if (contains(ctrlPoint)) {
			setCurved(false);
		}
	}

	private class DragMouseListener extends MouseMotionAdapter {

		//  TODO unimportant feature
		@Override
		public void mouseDragged(MouseEvent e) {
//			logger.trace("{}", e.getPoint());

			setCurvedTo(e.getX(), e.getY());

			repaint();
		}

	}

//	private class MouseListener extends MouseAdapter {
//	}

	private class PopupMenu extends JPopupMenu  {
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
