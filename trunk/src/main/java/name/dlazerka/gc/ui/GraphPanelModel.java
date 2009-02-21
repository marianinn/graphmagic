package name.dlazerka.gc.ui;

import name.dlazerka.gc.bean.Graph;
import name.dlazerka.gc.bean.GraphChangeListener;
import name.dlazerka.gc.bean.Vertex;

import java.awt.*;
import java.util.Collection;
import java.util.LinkedList;

/**
 * @author Dzmitry Lazerka
 */
public class GraphPanelModel implements GraphChangeListener {
	private final Graph graph;

	protected Collection<Draggable> draggingObjects = new LinkedList<Draggable>();

	public GraphPanelModel(Dimension size) {
		graph = new Graph();
		graph.addChangeListener(this);
	}

	public void addVertex() {
		graph.addVertex();
	}

	public Graph getGraph() {
		return graph;
	}

	/**
	 * Iterates through {@link #vertexModelSet} and checks if any of them {@link VertexPanelModel#contains(Point)}
	 *
	 * @param point point under which to search
	 * @return a {@link VertexPanelModel} or null of not found.
	 *
	 */
	/*
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
	*/
	
	public void vertexAdded(Vertex vertex) {
	}
}
