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

import name.dlazerka.gm.Edge;
import name.dlazerka.gm.Graph;
import name.dlazerka.gm.Mark;
import name.dlazerka.gm.Visual;
import name.dlazerka.gm.shell.ResourceBundle;
import name.dlazerka.gm.ui.GraphPanel;
import name.dlazerka.gm.ui.VertexPanel;
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
import java.util.Observable;
import java.util.Observer;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public abstract class EdgePanel extends AbstractEdgePanel implements Observer {
	private static final Logger logger = LoggerFactory.getLogger(EdgePanel.class);

	protected final Edge edge;
	protected final VertexPanel tail;
	protected final VertexPanel head;
	private boolean dragging;
	protected Shape hoverShape = getShape();
	protected Point oddPoint;
	private static final int FONT_SIZE = 20;
	protected static final Font MARK_FONT = new Font("courier", Font.PLAIN, FONT_SIZE);
	protected static final Color MARK_COLOR = new Color(0x80, 0x0, 0x0);
	private Point oldOddPoint = new Point();
	private Point oldMovedEndPoint = new Point();
	protected transient boolean hovered = false;

	public EdgePanel(Edge edge, VertexPanel tail, VertexPanel head) {
		this.edge = edge;
		this.tail = tail;
		this.head = head;

		Visual visual = edge.getVisual();
		visual.addObserver(this);

		initShape();
		updateGeometry();

		setOpaque(false);
		addMouseMotionListener(new DragMouseListener());
		addMouseListener(new MouseListener());
		setComponentPopupMenu(new PopupMenu());
	}

	public Edge getEdge() {
		return edge;
	}

	protected void initShape() {
		oddPoint = new Point();
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		Mark mark = edge.getMark();
		Visual visual = edge.getVisual();

		Color color = visual.getColor();
		if (color == null) {
			color = EDGE_COLOR_DEFAULT;
		}

		if (hovered) {
			g2.setStroke(EDGE_STROKE_HOVERED);
		}
		else if (visual.isSelected()) {
			g2.setStroke(EDGE_STROKE_SELECTED);
		}
		else {
			g2.setStroke(EDGE_STROKE_DEFAULT);
		}

		g2.setColor(color);

		g2.draw(getShape());

		paintMark(g2, mark);
	}

	protected void paintMark(Graphics2D g2, Mark mark) {
		if (mark != null && mark.size() > 0) {
			g2.setFont(MARK_FONT);
			g2.setColor(MARK_COLOR);

			int x = oddPoint.x;
			int y = oddPoint.y;
			int wx = Math.abs(getFromPoint().x - getToPoint().x);
			int wy = Math.abs(getFromPoint().y - getToPoint().y);

			if (wx > wy) {
				y -= 15;
			}
			else {
				x += 15;
			}

			g2.drawString(mark.toString(), x, y);
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

	/**
	 * Moves control poins so that edge will contain given (x, y) point.
	 * If the curve seems like line (contains(ctrlPoint)==true) then sets curved to false
	 *
	 * @param x which point.x the curve should contain
	 * @param y which point.y the curve should contain
	 */
	protected void pullTo(int x, int y) {
		oddPoint.x = x;
		oddPoint.y = y;

		updateGeometry();
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
			}
			else {
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
		x = (int) Math.round(
			((double) ax * bx * b1x - ay * bx * b1y + ay * by * b1x + ax * by * b1y) / (bx * bx + by * by)
		);
		y = (int) Math.round(
			((double) ay * bx * b1x + ax * bx * b1y - ax * by * b1x + ay * by * b1y) / (bx * bx + by * by)
		);

		x = x + o.x;
		y = y + o.y;

		a.move(x, y);
	}

	protected abstract void updateGeometry();

	@Override
	public void update(Observable o, Object arg) {
		repaint();
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
			setHovered(true);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			if (!dragging) {
				setHovered(false);
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

		@Override
		public void mouseClicked(MouseEvent e) {
			Visual visual = edge.getVisual();
			boolean selected = visual.isSelected();
			visual.setSelected(!selected);
			repaint();
		}
	}

	private void setHovered(boolean hovered) {
		this.hovered = hovered;
		repaint();
	}

	private class PopupMenu extends JPopupMenu {
		private PopupMenu() {
			add(new DeleteAction());
			add(new SetMarkAction());
			addPopupMenuListener(
				new PopupMenuListener() {
					@Override
					public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
					}

					@Override
					public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
						logger.debug("");
						EdgePanel.this.setHovered(false);
					}

					@Override
					public void popupMenuCanceled(PopupMenuEvent e) {
					}
				}
			);
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

		private class SetMarkAction extends AbstractAction {
			public SetMarkAction() {
				super(ResourceBundle.getString("set.mark"));
			}

			public void actionPerformed(ActionEvent e) {
				Mark mark = edge.getMark();
				Object value = mark.get(null);

				String newValue = JOptionPane.showInputDialog(
					EdgePanel.this,
					ResourceBundle.getString("new.mark"),
					value
				);
				mark.put(null, newValue);
				EdgePanel.this.repaint();
			}
		}
	}
}
