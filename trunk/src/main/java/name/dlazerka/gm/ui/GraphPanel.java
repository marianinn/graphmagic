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

import name.dlazerka.gm.Main;
import name.dlazerka.gm.bean.Edge;
import name.dlazerka.gm.bean.Graph;
import name.dlazerka.gm.bean.GraphChangeListener;
import name.dlazerka.gm.bean.Vertex;
import name.dlazerka.gm.util.ListMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Map;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class GraphPanel extends JPanel {
	private final static Logger logger = LoggerFactory.getLogger(GraphPanel.class);

	private final static Dimension DEFAULT_DIMENSION = new Dimension(600, 400);

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

	public GraphPanel() {
		GraphLayoutManager layoutManager = new GraphLayoutManager();
		setLayout(layoutManager);
		setSize(DEFAULT_DIMENSION);// for GraphLayoutManager@58

		setComponentPopupMenu(new PopupMenu());

		add(newEdgePanel);

		graph = new Graph();
		graph.addChangeListener(new GraphChangeListenerImpl());
		layoutManager.layoutDefault(this);
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
		Edge edge = new Edge(tail, head);

		graph.add(edge);

		draggingEdgeFrom = null;
		newEdgePanel.setVisible(false);
	}

	public NewEdgePanel getNewEdgePanel() {
		return newEdgePanel;
	}

	public Graph getGraph() {
		return graph;
	}


	private class GraphChangeListenerImpl implements GraphChangeListener {
		public void notifyAttached() {
			addVertexPanels();
			addEdgePanels();
		}

		private void addVertexPanels() {
			for (Vertex vertex : graph.getVertexSet()) {
				VertexPanel vertexPanel = new VertexPanel(vertex);
				add(vertexPanel);
			}
		}

		private void addEdgePanels() {
			for (Edge edge : graph.getEdgeSet()) {
				EdgePanel edgePanel = createEdgePanel(edge);
				add(edgePanel);
			}
		}

		private EdgePanel createEdgePanel(Edge edge) {
			VertexPanel tailPanel = vertexToVertexPanel.get(edge.getTail());
			VertexPanel headPanel = vertexToVertexPanel.get(edge.getHead());

			EdgePanel edgePanel = new EdgePanel(
				edge,
				tailPanel,
				headPanel
			);

			tailPanel.addAdjacentEdgePanel(edgePanel);
			headPanel.addAdjacentEdgePanel(edgePanel);

			return edgePanel;
		}

		public void vertexAdded(Vertex vertex) {
			VertexPanel panel = new VertexPanel(vertex);
			add(panel);
		}

		public void edgeAdded(Edge edge) {
			EdgePanel panel = createEdgePanel(edge);
			add(panel);
		}

		public void vertexDeleted(Vertex vertex) {
			VertexPanel panel = vertexToVertexPanel.get(vertex);
			remove(panel);
		}

		public void edgeDeleted(Edge edge) {
			EdgePanel panel = edgeToEdgePanel.get(edge);
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
	}

	private class PopupMenu extends JPopupMenu {
		private PopupMenu() {
			add(new AddVertexAction());
		}

		private class AddVertexAction extends AbstractAction {
			public AddVertexAction() {
				super(Main.getString("add.vertex"));
			}

			public void actionPerformed(ActionEvent e) {
				graph.addVertex();
			}
		}
	}
}
