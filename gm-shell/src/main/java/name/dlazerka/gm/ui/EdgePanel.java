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
import name.dlazerka.gm.EdgeMark;
import name.dlazerka.gm.Graph;
import name.dlazerka.gm.Visual;
import name.dlazerka.gm.shell.ResourceBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
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
	private final Point ctrlPoint = new Point();
	private boolean curved = false;
	private boolean dragging;
	private Shape hoverShape = curve;
	private Stroke stroke = EDGE_STROKE;
	private final Point oddPoint = new Point();
	private static final int FONT_SIZE = 20;
	private static final Font WEIGHT_FONT = new Font("courier", Font.PLAIN, FONT_SIZE);
	private static final Color WEIGHT_COLOR = new Color(0x80, 0x0, 0x0);
	private Point oldOddPoint = new Point();
	private Point oldMovedEndPoint = new Point();

	public EdgePanel(Edge edge, VertexPanel tail, VertexPanel head) {
		this.edge = edge;
		this.tail = tail;
		this.head = head;

		oddPoint.x = (getFromPoint().x + getToPoint().x) / 2;
		oddPoint.y = (getFromPoint().y + getToPoint().y) / 2;

		updateGeometry();

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

		EdgeMark mark = edge.getMark();
        Visual visual = edge.getVisual();
        
        Color color = visual.getColor();

		if (color == null) {
			color = EDGE_COLOR_DEFAULT;
		}

		g2.setColor(color);
		g2.setStroke(stroke);

		g2.draw(curve);

		String weight = mark.getWeight();
		if (weight != null) {
			g2.setFont(WEIGHT_FONT);
			g2.setColor(WEIGHT_COLOR);

			int x = oddPoint.x;
			int y = oddPoint.y;
			int wx = Math.abs(getFromPoint().x - getToPoint().x);
			int wy = Math.abs(getFromPoint().y - getToPoint().y);

			if (wx > wy) {
				y -= 15;
			} else {
				x += 15;
			}

			g2.drawString(weight, x, y);
		}
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
		return tail.getVertexCenter();
	}

	protected Point getToPoint() {
		return head.getVertexCenter();
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
	private void pullTo(int x, int y) {
		oddPoint.x = x;
		oddPoint.y = y;

		updateGeometry();

		setCurved(
				!tail.contains(ctrlPoint)
						&& !head.contains(ctrlPoint)
						&& !this.contains(ctrlPoint)
		);
	}

	public void setDragging(boolean dragging) {
		this.dragging = dragging;
	}

	/**
	 * Moves {@link #oddPoint} so like moved edge defined an affine transform.
	 *
	 * @param vertexPanel vertex panel that was moved
	 */
	public void onAdjacentVertexMoved(VertexPanel vertexPanel) {
		Point o; // not moved end
		Point b; // old position of moved end
		Point bb; // new position of moved end

		{// fetch initial values
			if (tail.equals(vertexPanel)) {
				o = head.getVertexCenter();
				bb = tail.getVertexCenter();
			} else {
				o = tail.getVertexCenter();
				bb = head.getVertexCenter();
			}

			b = oldMovedEndPoint;
		}

		oddPoint.move(oldOddPoint.x, oldOddPoint.y);
		affineMove(oddPoint, o, b, bb);

		updateGeometry();
		repaint();
	}

	public void setAdjacentVertexStartedDragging(VertexPanel draggingVertexPanel) {
		oldOddPoint.move(oddPoint.x, oddPoint.y);
		Point center = draggingVertexPanel.getVertexCenter();
		oldMovedEndPoint.move(center.x, center.y);
	}

	/**
	 * Moves point <code>a</code> accordingly as affine transform of line <code>o-b</code> to line <code>o-bb</code>.
	 * See /doc/math/affine.nb for computations.
	 *
	 * @param a  point to move
	 * @param o  not moving point
	 * @param b  old position of moved point
	 * @param bb new position of moved point
	 */
	static void affineMove(Point a, Point o, Point b, Point bb) {
		// make o as base
		int ax, ay;// old position of oddPoint
		int x, y;// new position of oddPoint
		int bx, by;// old position of moved point
		int b1x, b1y;// new position of moved point
		{
			ax = a.x - o.x;
			ay = a.y - o.y;
			bx = b.x - o.x;
			by = b.y - o.y;
			b1x = bb.x - o.x;
			b1y = bb.y - o.y;
		}

		// rounding because simple cast works bad
		x = (int) Math.round(((double) ax * bx * b1x - ay * bx * b1y + ay * by * b1x + ax * by * b1y) / (bx * bx + by * by));
		y = (int) Math.round(((double) ay * bx * b1x + ax * bx * b1y - ax * by * b1x + ay * by * b1y) / (bx * bx + by * by));

		x = x + o.x;
		y = y + o.y;

		a.move(x, y);
	}

	/**
	 * Sets the {@link #ctrlPoint} so, that {@link #oddPoint} will lie on the peak of curve.
	 * Thus, draws a perpendicular from the {@link #oddPoint} to the line drawn between end points (specified by points
	 * {@link #getFromPoint()} and {@link #getToPoint()}).
	 * Then places the {@link #ctrlPoint} on the perpendicular on double
	 * length from the line to {@link #oddPoint}
	 */
	private void updateGeometry() {
		updateGeometry2();
	}


	/**
	 * Places {@link #ctrlPoint} on double distance from center of line drawn between end points to the
	 * {@link #oddPoint}.
	 * It equals to fact that {@link #oddPoint} equals to bezier point position at t=0.5
	 */
	private void updateGeometry2() {
		int centerX = (getFromPoint().x + getToPoint().x) / 2;
		int centerY = (getFromPoint().y + getToPoint().y) / 2;

		ctrlPoint.move(
				oddPoint.x * 2 - centerX,
				oddPoint.y * 2 - centerY
		);

		curve.setCurve(
				getFromPoint(),
				curved ? ctrlPoint : getFromPoint(),
				getToPoint()
		);
		hoverShape = EDGE_HOVER_STROKE.createStrokedShape(curve);
	}

	private class DragMouseListener extends MouseMotionAdapter {

		@Override
		public void mouseDragged(MouseEvent e) {
//			logger.trace("{}", e.getPoint());

			pullTo(e.getX(), e.getY());

			repaint();
		}
	}

	private class MouseListener extends MouseAdapter {
		@Override
		public void mouseEntered(MouseEvent e) {
			stroke = EDGE_HOVER_STROKE;
			repaint();
		}

		@Override
		public void mouseExited(MouseEvent e) {
			if (!dragging) {
				stroke = EDGE_STROKE;
				repaint();
			}
		}

		private boolean isDraggingButton(int button) {
			return button == MouseEvent.BUTTON1;
		}

		@Override
		public void mousePressed(MouseEvent e) {
			if (isDraggingButton(e.getButton())) {
				setDragging(true);
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (isDraggingButton(e.getButton())) {
				setDragging(false);
			}
		}
	}

	private class PopupMenu extends JPopupMenu {
		private PopupMenu() {
			add(new DeleteAction());
			add(new SetWeightAction());
			addPopupMenuListener(new PopupMenuListener() {
				@Override
				public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				}

				@Override
				public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
					logger.debug("");

					stroke = EDGE_STROKE;
					EdgePanel.this.repaint();
				}

				@Override
				public void popupMenuCanceled(PopupMenuEvent e) {
				}
			});
		}

		private class DeleteAction extends AbstractAction {
			public DeleteAction() {
				super(ResourceBundle.getString("delete.edge"));
			}

			public void actionPerformed(ActionEvent e) {
				Graph graph = getGraphPanel().getGraph();
				graph.remove(edge);
			}
		}

		private class SetWeightAction extends AbstractAction {
			public SetWeightAction() {
				super(ResourceBundle.getString("set.weight"));
			}

			public void actionPerformed(ActionEvent e) {
				EdgeMark mark = edge.getMark();
				String weight = mark.getWeight();

				String newWeight = JOptionPane.showInputDialog(EdgePanel.this, ResourceBundle.getString("new.weight"), weight);

				mark.setWeight(newWeight);

				EdgePanel.this.repaint();
			}
		}
	}
}
