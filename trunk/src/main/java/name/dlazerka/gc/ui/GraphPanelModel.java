package name.dlazerka.gc.ui;

import name.dlazerka.gc.bean.Graph;
import name.dlazerka.gc.bean.Vertex;
import name.dlazerka.gc.bean.Edge;
import name.dlazerka.gc.bean.GraphChangeListener;
import name.dlazerka.gc.util.LinkedSet;

import java.util.*;
import java.awt.*;

/**
 * @author Dzmitry Lazerka
 */
public class GraphPanelModel implements GraphChangeListener {
	private final Graph graph;

	/**
	 * Order represents Z-index.
	 */
	protected final LinkedSet<VertexUI> vertexUISet = new LinkedSet<VertexUI>();

	/**
	 * Order represents Z-index.
	 */
	protected final LinkedSet<EdgeUI> edgeUISet = new LinkedSet<EdgeUI>();

	private final Map<Vertex, VertexUI> vertexToVertexUI = new HashMap<Vertex, VertexUI>();
	private final Map<Edge, EdgeUI> edgeToEdgeUI = new HashMap<Edge, EdgeUI>();

	protected Collection<Draggable> draggingObjects = new LinkedList<Draggable>();

	public GraphPanelModel(Dimension size) {
		graph = new Graph();
		graph.addChangeListener(this);

		createUIs(size);
	}

	public void vertexAdded(Vertex vertex) {
		VertexUI vertexUI = new VertexUI(vertex);
		addVertexUI(vertexUI);
	}

	public void addVertex() {
		graph.addVertex();
	}

	public boolean addVertexUI(VertexUI vertexUI) {
		return vertexUISet.add(vertexUI);
	}

	private void createUIs(Dimension size) {
		for (Vertex vertex : graph.getVertexSet()) {
			VertexUI vertexUI = new VertexUI(vertex);
//			add(vertexUI);
			vertexUISet.add(vertexUI);
			vertexToVertexUI.put(vertex, vertexUI);
		}
		for (Edge edge : graph.getEdgeSet()) {
			EdgeUI edgeUI = new EdgeUI(
				edge, vertexToVertexUI.get(edge.getHead()), vertexToVertexUI.get(edge.getTail())
			);
			edgeUISet.add(edgeUI);
//			edgeToEdgeUI.put(edge, edgeUI);
		}

		calculateVertexPositions(size);
	}

	private void calculateVertexPositions(Dimension size) {
		int vertexSetSize = vertexToVertexUI.size();

		Point center = new Point(size.width / 2, size.height / 2);
		int radius = Math.min(size.width * 3 / 4 - center.x, size.height * 3 / 4 - center.y);

		double angleStep = 2 * Math.PI / vertexSetSize;

		int i = 0;
		for (VertexUI vertexUI : vertexToVertexUI.values()) {
			double angle = i * angleStep;

			Point position = new Point(
				(int) Math.round(radius * Math.cos(angle)) + center.x,
				(int) Math.round(radius * -Math.sin(angle)) + center.y
			);

			vertexUI.setCenter(position);
			i++;
		}
	}

	/**
	 * Iterates through {@link #vertexUISet} and checks if any of them {@link VertexUI#contains(Point)}
	 *
	 * @param point point under which to search
	 * @return a {@link VertexUI} or null of not found.
	 */
	protected VertexUI getVertexUIUnder(Point point) {
		LinkedSet<VertexUI> set = vertexUISet;
		ListIterator<VertexUI> listIterator = set.listIterator(set.size());
		while (listIterator.hasPrevious()) {
			VertexUI vertexUI = listIterator.previous();
			if (vertexUI.contains(point)) {
				return vertexUI;
			}
		}
		return null;
	}
}
