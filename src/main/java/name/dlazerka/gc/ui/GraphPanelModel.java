package name.dlazerka.gc.ui;

import name.dlazerka.gc.bean.Graph;
import name.dlazerka.gc.bean.Vertex;

import java.awt.*;
import java.util.Collection;
import java.util.LinkedList;

/**
 * @author Dzmitry Lazerka
 */
public class GraphPanelModel  {
	private final Graph graph;

	private Collection<Draggable> draggingObjects = new LinkedList<Draggable>();

	public GraphPanelModel(Dimension size) {
		graph = new Graph();
	}

	public Vertex addVertex() {
		return graph.addVertex();
	}

	public Graph getGraph() {
		return graph;
	}
}
