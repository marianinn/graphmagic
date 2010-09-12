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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import name.dlazerka.gm.Graph;
import name.dlazerka.gm.Mark;
import name.dlazerka.gm.Vertex;
import name.dlazerka.gm.Visual;
import name.dlazerka.gm.shell.ResourceBundle;
import name.dlazerka.gm.ui.edge.EdgePanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class VertexPanel extends JPanel implements Paintable, Observer {
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
	private static final Color COLOR_INNER_DEFAULT = new Color(0xFF, 0xFF, 0xFF);
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

	/**
	 * Is this vertex panel currently dragging.
	 */
	private boolean dragging;
	private final VertexMarkPanel markPanel;

	public VertexPanel(Vertex vertex) {
		super(null);
		this.vertex = vertex;
		markPanel = new VertexMarkPanel(this);

		Visual visual = vertex.getVisual();
		visual.addObserver(this);

		setPreferredSize(panelSize);
		setSize(panelSize);
		setOpaque(false);
		//		setDoubleBuffered(true); // is needed?

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
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		g2.setColor(COLOR_BORDER);
		g2.fillOval(0, 0, VERTEX_OVAL_SIZE.width, VERTEX_OVAL_SIZE.height);

		Mark mark = vertex.getMark();
		Visual visual = vertex.getVisual();

		Color color = visual.getColor();
		boolean selected = visual.isSelected();

		if (color == null) {
			color = COLOR_INNER_DEFAULT;
		}

		if (isHovered) {
			color = color.darker();
		}

		if (selected) {
			color = color.darker();
		}

		g2.setColor(color);

		g2.fillOval(3, 3, VERTEX_OVAL_SIZE.width - 6, VERTEX_OVAL_SIZE.height - 6);

		g2.setColor(COLOR_NUMBER);

		FontRenderContext fontRenderContext = g2.getFontRenderContext();
		GlyphVector glyphVector = NUMBER_FONT.createGlyphVector(fontRenderContext, "" + vertex.getId());

		int glyphStartX;
		if (vertex.getId().length() == 1) {
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

	public void removeAdjacentEdgePanel(EdgePanel edgePanel) {
		adjacentEdgePanels.remove(edgePanel);
	}

	public void setVertexPanelCenter(Point center) {
		setVertexPanelCenter(center.x, center.y);
	}

	public void setVertexPanelCenter(int x, int y) {
		setLocation(
			x - VERTEX_OVAL_SIZE.width / 2,
			y - VERTEX_OVAL_SIZE.height / 2
		);
		getGraphPanel().adjustBounds(this);
	}

	@Override
	public void setLocation(int x, int y) {
		super.setLocation(x, y);

		// update vertex.visual
		x += VERTEX_OVAL_SIZE.width / 2;
		y += VERTEX_OVAL_SIZE.height / 2;

		double relX, relY;
		Rectangle rect = getGraphPanel().getVisibleRect();
		relX = (2d * x - 2 * rect.getX() - rect.getWidth()) / rect.getWidth();
		relY = (2d * y - 2 * rect.getY() - rect.getHeight()) / rect.getHeight();

		Visual visual = vertex.getVisual();
		visual.setCenter(relX, relY, false);
	}

	/**
	 * @param dragging see {@link #dragging}
	 */
	public void setDragging(boolean dragging) {
		if (!this.dragging && dragging) {
			for (EdgePanel adjacentEdgePanel : adjacentEdgePanels) {
				adjacentEdgePanel.setAdjacentVertexStartedDragging(this);
			}
		}

		this.dragging = dragging;
	}

	/**
	 * Called when the {@link #vertex}'s {@link Visual} changed, so this panel should be moved.
	 *
	 * @param o   must equal this.{@link #vertex}.{@link Vertex#getVisual()}
	 * @param arg not used
	 */
	@Override
	public void update(Observable o, Object arg) {
		Visual visual = vertex.getVisual();
		if (!visual.equals(o)) {
			throw new IllegalStateException();
		}

		double centerX = visual.getCenterX();
		double centerY = visual.getCenterY();

		Rectangle visibleRect = getGraphPanel().getVisibleRect();
		int x = (int) (visibleRect.getWidth() / 2 * centerX + visibleRect.getX() + visibleRect.getWidth() / 2);
		int y = (int) (visibleRect.getHeight() / 2 * centerY + visibleRect.getY() + visibleRect.getHeight() / 2);

		logger.debug("visual=({},{}), panel=({},{})", new Object[]{centerX, centerY, x, y});

		setVertexPanelCenter(x, y);
	}

	public void moveWithEdges(int moveByX, int moveByY) {
		setLocation(getX() + moveByX, getY() + moveByY);

		for (EdgePanel adjacentEdgePanel : adjacentEdgePanels) {
			adjacentEdgePanel.onAdjacentVertexMoved(VertexPanel.this);
		}
	}

	public VertexMarkPanel getMarkPanel() {
		return markPanel;
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
			moveWithEdges(moveByX, moveByY);
			logger.trace("curX={}, curY={}", new Object[]{VertexPanel.this.getX(), VertexPanel.this.getY()});
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
			if (!isDraggingEdgeFromThis()
				&& !dragging) {
				setHovered(false);
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			logger.trace("");
			setDragging(true);
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			logger.trace("");
			GraphPanel graphPanel = getGraphPanel();
			graphPanel.adjustBounds(VertexPanel.this);
			setDragging(false);

			// adjust visual accordingly
			Visual visual = getVertex().getVisual();
			double cX = VertexPanel.this.getVertexCenterX();
			double cY = VertexPanel.this.getVertexCenterY();
			Rectangle visibleRect = getGraphPanel().getVisibleRect();
			visual.setCenter(
				(2 * cX - 2 * visibleRect.getX() - visibleRect.getWidth()) / visibleRect.getWidth(),
				(2 * cY - 2 * visibleRect.getY() - visibleRect.getHeight()) / visibleRect.getHeight(),
				false
			);
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			logger.trace("");
			Visual visual = vertex.getVisual();
			boolean selected = visual.isSelected();
			visual.setSelected(!selected);
		}
	}

	private class PopupMenu extends JPopupMenu {
		private PopupMenu() {
			add(new DeleteAction());
			add(new MarkAction());
			addPopupMenuListener(
				new PopupMenuListener() {
					@Override
					public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
					}

					@Override
					public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
						checkHovered();
						VertexPanel.this.repaint();
					}

					@Override
					public void popupMenuCanceled(PopupMenuEvent e) {
					}
				}
			);
		}

		private class DeleteAction extends AbstractAction {
			public DeleteAction() {
				super(ResourceBundle.getString("delete.vertex"));
			}

			public void actionPerformed(ActionEvent e) {
				Graph graph = getGraphPanel().getGraph();
				graph.remove(vertex);
			}
		}

		private class MarkAction extends AbstractAction {
			private MarkAction() {
				super(ResourceBundle.getString("set.mark"));
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				Mark mark = vertex.getMark();
				Object value = mark.get(null);

				String newValue = JOptionPane.showInputDialog(
					VertexPanel.this,
					ResourceBundle.getString("new.mark"),
					value
				);
				if (newValue != null) {
					mark.put(null, newValue);
					markPanel.repaint();
				}
			}
		}
	}
}
