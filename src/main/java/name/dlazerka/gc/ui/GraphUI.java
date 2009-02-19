package name.dlazerka.gc.ui;

import name.dlazerka.gc.bean.Edge;
import name.dlazerka.gc.bean.Graph;
import name.dlazerka.gc.bean.Vertex;

import java.awt.*;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;

/**
 * @author Dzmitry Lazerka
 */
public class GraphUI {
	private final Graph graph = new Graph();

	private final Set<VertexUI> vertexUISet = new HashSet<VertexUI>();
	private final Set<EdgeUI> edgeUISet = new HashSet<EdgeUI>();

	private final Map<Vertex, VertexUI> vertexToVertexUI = new HashMap<Vertex, VertexUI>();
	private final Map<Edge, EdgeUI> edgeToEdgeUI = new HashMap<Edge, EdgeUI>();

	private final static Dimension DEFAULT_DIMENSION = new Dimension(600, 400); 

	public GraphUI() {
		createUI();
	}

	public void drawGraph(Graphics2D g2, Dimension size) {
		for (EdgeUI edgeUI : edgeUISet) {
			edgeUI.draw(g2);
		}

		for (VertexUI vertexUI : vertexUISet) {
			vertexUI.draw(g2);
		}
	}

	private void createUI() {
		for (Vertex vertex : graph.getVertexSet()) {
			VertexUI vertexUI = new VertexUI(vertex);
			vertexUISet.add(vertexUI);
			vertexToVertexUI.put(vertex, vertexUI);
		}
		for (Edge edge : graph.getEdgeSet()) {
			EdgeUI edgeUI = new EdgeUI(edge, vertexToVertexUI.get(edge.getHead()), vertexToVertexUI.get(edge.getTail()));
			edgeUISet.add(edgeUI);
			edgeToEdgeUI.put(edge, edgeUI);
		}

		calculateVertexPositions();
	}

	private void calculateVertexPositions() {
		int vertexSetSize = vertexUISet.size();

		Dimension size = DEFAULT_DIMENSION;

		Point center = new Point(size.width / 2, size.height / 2);
		int radius = Math.min(size.width * 3 / 4 - center.x, size.height * 3 / 4 - center.y);

		double angleStep = 2 * Math.PI / vertexSetSize;

		int i = 0;
		for (VertexUI vertexUI : vertexUISet) {
			double angle = i * angleStep;

			Point position = new Point(
				(int) Math.round(radius * Math.cos(angle)) + center.x,
				(int) Math.round(radius * -Math.sin(angle)) + center.y
		        );

			vertexUI.setCenter(position);
			i++;
		}
	}
}
