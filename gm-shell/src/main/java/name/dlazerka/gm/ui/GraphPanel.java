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
import name.dlazerka.gm.Graph;
import name.dlazerka.gm.Vertex;
import name.dlazerka.gm.ui.edge.EdgePanel;
import name.dlazerka.gm.ui.edge.NewEdgePanel;
import name.dlazerka.gm.ui.edge.NoPseudoEdgePanel;
import name.dlazerka.gm.ui.edge.PseudoEdgePanel;
import name.dlazerka.gm.basic.GraphModificationListenerAdapter;
import name.dlazerka.gm.exception.EdgeCreateException;
import name.dlazerka.gm.shell.ResourceBundle;
import name.dlazerka.gm.util.ListMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.Map;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class GraphPanel extends JPanel {
	private final static Logger logger = LoggerFactory.getLogger(GraphPanel.class);

	private final Graph graph;

	private Map<Vertex, VertexPanel> vertexToVertexPanel = new ListMap<Vertex, VertexPanel>();
	private Map<Edge, EdgePanel> edgeToEdgePanel = new ListMap<Edge, EdgePanel>();

	/**
	 * Panel on which user has started dragging a new edge.
	 *
	 * @see #lastHoveredVertexPanel
	 */
	private VertexPanel draggingEdgeFrom;
	private final NewEdgePanel newEdgePanel = new NewEdgePanel();

	/**
	 * For determining of panel on which mouse was released when dragging a new edge.
	 *
	 * @see #draggingEdgeFrom
	 */
	private VertexPanel lastHoveredVertexPanel;

	public GraphPanel(Graph graph) {
		this.graph = graph;

		GraphLayoutManager layoutManager = new GraphLayoutManager();
		setLayout(layoutManager);
		setPreferredSize(new Dimension(0, 0));
//		setBorder(BorderFactory.createLineBorder(Color.BLACK));

		setAutoscrolls(true);
		addMouseMotionListener(new MouseMotionListener());

		setComponentPopupMenu(new PopupMenu());

		add(newEdgePanel);

		graph.addChangeListener(new GraphModificationListenerImpl());
	}

	/**
	 * Overrides default and returns false since our children can overlap each other.
	 * Though it seems unnecessary because it works for true pretty well. Let is be here for confidence.
	 * See <a href="http://java.sun.com/products/jfc/tsc/articles/painting/#props">Painting in AWT and Swing</a>.
	 *
	 * @return false
	 * @see <a href="http://java.sun.com/products/jfc/tsc/articles/painting/#props">Painting in AWT and Swing</a>
	 */
	@Override
	public boolean isOptimizedDrawingEnabled() {
		return false;
	}

	@Override
	protected void addImpl(Component comp, Object constraints, int index) {
		if (!(comp instanceof Paintable)) {
			throw new IllegalArgumentException(
				"Unable to add component of type " + comp.getName()
				+ " only Paintable are accepted"
			);
		}

		super.addImpl(comp, constraints, index);
	}

	@Override
	public void remove(Component component) {
		if (component instanceof VertexPanel) {
			VertexPanel panel = (VertexPanel) component;
			vertexToVertexPanel.remove(panel.getVertex());
		}
		else if (component instanceof EdgePanel) {
			EdgePanel panel = (EdgePanel) component;
			edgeToEdgePanel.remove(panel.getEdge());
		}

		super.remove(component);

		repaint();// essential
	}

	public void setHoveredVertexPanel(VertexPanel vertexPanel) {
		logger.trace("{}", vertexPanel);

		if (vertexPanel != null) {
			lastHoveredVertexPanel = vertexPanel;
			setComponentZOrder(vertexPanel, 0);
			setComponentZOrder(vertexPanel.getMarkPanel(), 0);
		}
	}

	public void startDraggingEdge(VertexPanel vertexPanel) {
		draggingEdgeFrom = vertexPanel;

		newEdgePanel.setTail(vertexPanel);
		newEdgePanel.setVisible(true);
	}

	public boolean isDraggingEdge() {
		return draggingEdgeFrom != null;
	}

	public void stopDraggingEdge() {

		Vertex tail = draggingEdgeFrom.getVertex();
		Vertex head = lastHoveredVertexPanel.getVertex();

		try {
			graph.createEdge(tail, head);
		}
		catch (EdgeCreateException e) {
			ErrorDialog.showError(e, this);
		}

		newEdgePanel.setVisible(false);

		draggingEdgeFrom = null;

		lastHoveredVertexPanel.checkHovered();
	}

	public NewEdgePanel getNewEdgePanel() {
		return newEdgePanel;
	}

	public Graph getGraph() {
		return graph;
	}

	public synchronized void adjustBounds(VertexPanel vertexPanel) {
		Rectangle visibleRect = getVisibleRect();

		int oldWidth = getWidth();
		int oldHeight = getHeight();
		{// beyond the western border
			int minX = vertexPanel.getX();
			if (minX < 0) {
				setPreferredSize(new Dimension(oldWidth - minX, oldHeight));
				for (VertexPanel panel : vertexToVertexPanel.values()) {
					panel.moveWithEdges(-minX, 0);
				}
				visibleRect.setLocation(visibleRect.x - minX, visibleRect.y);
			}
		}

		{// beyond the northern border
			int minY = vertexPanel.getY();
			if (minY < 0) {
				setPreferredSize(new Dimension(oldWidth, oldHeight - minY));
				for (VertexPanel panel : vertexToVertexPanel.values()) {
					panel.moveWithEdges(0, -minY);
				}
				visibleRect.setLocation(visibleRect.x, visibleRect.y - minY);
			}
		}

		{// beyond the eastern border
			int maxX = vertexPanel.getX() + vertexPanel.getWidth();
			if (maxX > getWidth()) {
				setPreferredSize(new Dimension(maxX, oldHeight));
			}
		}

		{// beyond the southern border
			int maxY = vertexPanel.getY() + vertexPanel.getHeight();
			if (maxY > getHeight()) {
				setPreferredSize(new Dimension(oldWidth, maxY));
			}
		}

		scrollRectToVisible(visibleRect);
	}


	private class GraphModificationListenerImpl extends GraphModificationListenerAdapter {
		public void notifyAttached() {
			for (Vertex vertex : graph.getVertexSet()) {
				vertexAdded(vertex);
			}
			for (Edge edge : graph.getEdgeSet()) {
				edgeAdded(edge);
			}
		}

		private EdgePanel createEdgePanel(Edge edge) {
			EdgePanel edgePanel;

			if (!edge.isPseudo()) {
				VertexPanel tailPanel = vertexToVertexPanel.get(edge.getSource());
				VertexPanel headPanel = vertexToVertexPanel.get(edge.getTarget());

				edgePanel = new NoPseudoEdgePanel(
					edge,
					tailPanel,
					headPanel
				);

				tailPanel.addAdjacentEdgePanel(edgePanel);
				headPanel.addAdjacentEdgePanel(edgePanel);
			}
			else {
				VertexPanel vertexPanel = vertexToVertexPanel.get(edge.getSource());

				edgePanel = new PseudoEdgePanel(
					edge,
					vertexPanel
				);

				vertexPanel.addAdjacentEdgePanel(edgePanel);
			}

			return edgePanel;
		}

		public void vertexAdded(Vertex vertex) {
			VertexPanel panel = new VertexPanel(vertex);
			add(panel);

			VertexMarkPanel vertexMarkPanel = panel.getMarkPanel();
			add(vertexMarkPanel);
		}

		public void edgeAdded(Edge edge) {
			EdgePanel panel = createEdgePanel(edge);
			add(panel);
		}

		public void vertexDeleted(Vertex vertex) {
			VertexPanel panel = vertexToVertexPanel.remove(vertex);
			remove(panel);
		}

		public void edgeDeleted(Edge edge) {
			EdgePanel panel = edgeToEdgePanel.remove(edge);

			Vertex vertex;
			{
				vertex = edge.getTarget();
				VertexPanel vertexPanel = vertexToVertexPanel.get(vertex);
				vertexPanel.removeAdjacentEdgePanel(panel);
			}
			{
				vertex = edge.getSource();
				VertexPanel vertexPanel = vertexToVertexPanel.get(vertex);
				vertexPanel.removeAdjacentEdgePanel(panel);
			}

			remove(panel);
		}

		private Component add(VertexPanel panel) {
			vertexToVertexPanel.put(panel.getVertex(), panel);
			return GraphPanel.super.add(panel);
		}

		private Component add(EdgePanel panel) {
			edgeToEdgePanel.put(panel.getEdge(), panel);
			return GraphPanel.super.add(panel);
		}

		private Component add(VertexMarkPanel panel) {
			return GraphPanel.super.add(panel);
		}
	}

	private class PopupMenu extends JPopupMenu {
		private PopupMenu() {
			add(new AddVertexAction());
			add(new ClearAllAction());
		}

		private class AddVertexAction extends AbstractAction {
			public AddVertexAction() {
				super(ResourceBundle.getString("add.vertex"));
			}

			public void actionPerformed(ActionEvent e) {
				graph.createVertex();
			}
		}

		private class ClearAllAction extends AbstractAction {
			public ClearAllAction() {
				super(ResourceBundle.getString("clear.all"));
			}

			public void actionPerformed(ActionEvent e) {
				int answer = JOptionPane.showConfirmDialog(
					GraphPanel.this,
					ResourceBundle.getString("clear.all.confirm.message"),
					ResourceBundle.getString("clear.all.confirm.title"),
					JOptionPane.YES_NO_OPTION
				);

				if (answer == JOptionPane.YES_OPTION) {
					graph.clear();
					setBounds(getVisibleRect());
				}
			}
		}

	}

	private class MouseMotionListener extends MouseMotionAdapter {
		@Override
		public void mouseDragged(MouseEvent e) {
			Rectangle r = new Rectangle(e.getX(), e.getY(), 1, 1);
			scrollRectToVisible(r);
//			setAutoscrolls();
		}
	}
}
