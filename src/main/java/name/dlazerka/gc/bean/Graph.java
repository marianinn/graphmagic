package name.dlazerka.gc.bean;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class Graph {
	private static final Logger logger = LoggerFactory.getLogger(Graph.class);

	private final Set<Vertex> vertexSet = new HashSet<Vertex>();
	private final Set<Edge> edgeSet = new HashSet<Edge>();
	private final List<GraphChangeListener> changeListeners = new LinkedList<GraphChangeListener>();

	public Graph() {
		create4();
	}

	public boolean addChangeListener(GraphChangeListener listener) {
		return changeListeners.add(listener);
	}

	private void create1() {
		Vertex vertex1 = new Vertex(1);

		addVertex(vertex1);
	}

	private void create2() {
		Vertex vertex1 = new Vertex(1);
		Vertex vertex2 = new Vertex(2);

		addVertex(vertex1);
		addVertex(vertex2);
	}

	private void create4() {
		Vertex vertex1 = new Vertex(1);
		Vertex vertex2 = new Vertex(2);
		Vertex vertex3 = new Vertex(3);
		Vertex vertex4 = new Vertex(4);

		addVertex(vertex1);
		addVertex(vertex2);
		addVertex(vertex3);
		addVertex(vertex4);

		Edge arc1 = new Edge(vertex1, vertex2);
		Edge arc2 = new Edge(vertex2, vertex3);
		Edge arc3 = new Edge(vertex3, vertex1);
		Edge arc4 = new Edge(vertex3, vertex4);
		Edge arc5 = new Edge(vertex4, vertex1);

		addEdge(arc1);
		addEdge(arc2);
		addEdge(arc3);
		addEdge(arc4);
		addEdge(arc5);
	}

	public void addEdge(Edge edge) {
		logger.debug("addEdge({})", edge);
		
		edgeSet.add(edge);
	}

	public Vertex addVertex() {
		int max = Integer.MIN_VALUE;
		for (Vertex vertex : vertexSet) {
			if (vertex.getNumber() > max) {
				max = vertex.getNumber();
			}
		}
		Vertex vertex = new Vertex(max + 1);

		return addVertex(vertex);
	}

	public Vertex addVertex(Vertex vertex) {
		logger.debug("addVertex({})", vertex);

		vertexSet.add(vertex);
		
		for (GraphChangeListener listener : changeListeners) {
			listener.vertexAdded(vertex);
		}

		return vertex;
	}

	public Set<Vertex> getVertexSet() {
		return vertexSet;
	}

	public Set<Edge> getEdgeSet() {
		return edgeSet;
	}
}
