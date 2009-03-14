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

import name.dlazerka.gc.bean.Vertex;
import name.dlazerka.gc.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.ActionEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.util.Collection;
import java.util.LinkedList;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class VertexPanel extends JPanel implements Paintable {
	private static final Logger logger = LoggerFactory.getLogger(VertexPanel.class);

	/**
	 * Default diameter of the vertex.
	 */
	private static final Dimension VERTEX_OVAL_SIZE = new Dimension(50, 50);

	private static final int FONT_SIZE = 30;
	private static final Font NUMBER_FONT = new Font("courier", Font.PLAIN, FONT_SIZE);

	/**
	 * Horizintal shift of the vertex number for one-digit numbers.
	 */
	private static final int NUMBER_SHIFT_X1 = 17;

	/**
	 * Horizontal shift of the vertex number for two-digit numbers.
	 */
	private static final int NUMBER_SHIFT_X2 = 9;

	/**
	 * Vertical shift of the vertex number.
	 */
	private static final int NUMBER_SHIFT_Y = 36;


	private static final Color COLOR_BORDER = new Color(0, 0, 0);
	private static final Color COLOR_INNER = new Color(0xFF, 0xFF, 0xFF);
	private static final Color COLOR_INNER_HOVER = new Color(0xA0, 0xFF, 0xA0);
	private static final Color COLOR_NUMBER = new Color(0, 0, 0);


	private final Vertex vertex;

	private boolean isHovered = false;
	private final AddEdgePanel addEdgePanel = new AddEdgePanel();
	private final Dimension panelSize = new Dimension(
		VERTEX_OVAL_SIZE.width + addEdgePanel.getPreferredSize().width / 2,
		VERTEX_OVAL_SIZE.height
	);
	private boolean draggingEdgeFromThis = false;

	private final Point vertexCenter = new Point();
	private final Collection<EdgePanel> adjacentEdgePanels = new LinkedList<EdgePanel>();

	public VertexPanel(Vertex vertex) {
		super(null);
		this.vertex = vertex;

		setPreferredSize(panelSize);
		setSize(panelSize);
		setOpaque(false);
//		setDoubleBuffered(true); // is needed?

//		setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0), 1));

		Dimension preferredSize = addEdgePanel.getPreferredSize();
		addEdgePanel.setBounds(// top right corner
		                       panelSize.width - preferredSize.width,
		                       0,
		                       preferredSize.width,
		                       preferredSize.height
		);
		add(addEdgePanel);

		addMouseMotionListener(new DragMouseListener());
		addMouseListener(new MouseListener());

		setComponentPopupMenu(new PopupMenu());
	}

	public Vertex getVertex() {
		return vertex;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		g2.setColor(COLOR_BORDER);
		g2.fillOval(0, 0, VERTEX_OVAL_SIZE.width, VERTEX_OVAL_SIZE.height);

		if (!isHovered) {
			g2.setColor(COLOR_INNER);
		}
		else {
			g2.setColor(COLOR_INNER_HOVER);
		}
		g2.fillOval(3, 3, VERTEX_OVAL_SIZE.width - 6, VERTEX_OVAL_SIZE.height - 6);

		g2.setColor(COLOR_NUMBER);

		FontRenderContext fontRenderContext = g2.getFontRenderContext();
		GlyphVector glyphVector = NUMBER_FONT.createGlyphVector(fontRenderContext, "" + vertex.getNumber());

		int glyphStartX;
		if (vertex.getNumber() < 10) {
			glyphStartX = NUMBER_SHIFT_X1;
		}
		else {
			glyphStartX = NUMBER_SHIFT_X2;
		}

		int glyphStartY = NUMBER_SHIFT_Y;

		g2.drawGlyphVector(glyphVector, glyphStartX, glyphStartY);

//		g2.drawLine(0, getSize().height / 2, getSize().width, getSize().height / 2);
//		g2.drawLine(getSize().width / 2, 0, getSize().width / 2, getSize().height);
	}

	/**
	 * Overrides default {@link JComponent#contains(int, int)} by returning false
	 * when coordinates are not over square containing vertex oval and square containing
	 * {@link #addEdgePanel}. In other words, by removing rectangle under {@link #addEdgePanel}.
	 *
	 * @param x see {@link JComponent#contains(int, int)}
	 * @param y see {@link JComponent#contains(int, int)}
	 * @return see {@link JComponent#contains(int, int)}
	 */

	@Override
	public boolean contains(int x, int y) {
		if (!super.contains(x, y)) {
			return false;
		}

		if (x > VERTEX_OVAL_SIZE.width && y > addEdgePanel.getHeight()) {
			return false;
		}

		return true;
	}


	protected void setHovered(boolean isHovered) {
		logger.trace("{}", isHovered);

		this.isHovered = isHovered;

		GraphPanel graphPanel = getGraphPanel();

		if (!graphPanel.isDraggingEdge()) {
			addEdgePanel.setVisible(isHovered);
		}

		if (isHovered) {
			graphPanel.setHoveredVertexPanel(VertexPanel.this);
		}
		repaint();
	}

	public GraphPanel getGraphPanel() {
		return (GraphPanel) getParent();
	}

	public void checkHovered() {
		Point mousePosition = getMousePosition();
		logger.trace("mousePosition={}", mousePosition);
		setHovered(mousePosition != null);
	}

	/**
	 * @return center of the vertex oval, not the panel itself
	 */
	public int getVertexCenterX() {
		return getX() + VERTEX_OVAL_SIZE.width / 2;
	}

	/**
	 * @return center of the vertex oval, not the panel itself
	 */
	public int getVertexCenterY() {
		return getY() + VERTEX_OVAL_SIZE.height / 2;
	}

	/**
	 * @return center of the vertex oval, not the panel itself
	 */
	public Point getVertexCenter() {
		vertexCenter.setLocation(
			getVertexCenterX(),
			getVertexCenterY()
		);
		return vertexCenter;
	}

	public void startDraggingEdge() {
		draggingEdgeFromThis = true;
		getGraphPanel().startDraggingEdge(this);
	}

	public boolean isDraggingEdge() {
		return getGraphPanel().isDraggingEdge();
	}

	public boolean isDraggingEdgeFromThis() {
		return draggingEdgeFromThis;
	}

	public void stopDraggingEdge() {
		draggingEdgeFromThis = false;
		getGraphPanel().stopDraggingEdge();
	}

	@Override
	public String toString() {
		return "VertexPanel{" +
		       "vertex=" + vertex +
		       "x=" + getX() +
		       "y=" + getY() +
		       "width=" + getSize().width +
		       "height=" + getSize().height +
		       '}';
	}

	public void addAdjacentEdgePanel(EdgePanel edgePanel) {
		adjacentEdgePanels.add(edgePanel);
	}

	public void setVertexCenter(Point center) {
		setVertexCenter(center.x, center.y);
	}

	public void setVertexCenter(int x, int y) {
		setLocation(
			x - VERTEX_OVAL_SIZE.width / 2,
			y - VERTEX_OVAL_SIZE.height / 2
		);
	}


	protected class DragMouseListener extends MouseMotionAdapter {
		private int mouseX;
		private int mouseY;

		@Override
		public void mouseMoved(MouseEvent e) {
			mouseX = e.getX();
			mouseY = e.getY();
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			int moveByX = e.getX() - mouseX;
			int moveByY = e.getY() - mouseY;
			setLocation(getX() + moveByX, getY() + moveByY);

			for (EdgePanel adjacentEdgePanel : adjacentEdgePanels) {
				adjacentEdgePanel.repaint();
			}

			// fix for too-fast-moving mouse :) 
			if (!isHovered) {
				setHovered(true);
			}
		}

	}

	protected class MouseListener extends MouseAdapter {
		@Override
		public void mouseEntered(MouseEvent e) {
			logger.trace("");
			setHovered(true);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			logger.trace("");
			if (!isDraggingEdgeFromThis()) {
				setHovered(false);
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			logger.trace("");
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			logger.trace("");
		}
	}

	private class PopupMenu extends JPopupMenu  {
		private PopupMenu() {
			add(new DeleteAction());
		}

		private class DeleteAction extends AbstractAction {
			public DeleteAction() {
				super(Main.getString("delete.vertex"));
			}

			public void actionPerformed(ActionEvent e) {
				getGraphPanel().getGraph().remove(vertex);
			}
		}
	}
}