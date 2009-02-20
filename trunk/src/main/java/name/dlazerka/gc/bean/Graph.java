package name.dlazerka.gc.bean;


import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;

/**
 * @author Dzmitry Lazerka
 */
public class Graph {
	private final Set<Vertex> vertexSet = new HashSet<Vertex>();
	private final Set<Edge> edgeSet = new HashSet<Edge>();
	private final Map<Integer, Vertex> vertexByOrder = new HashMap<Integer, Vertex>();

	public Graph() {
		create4();
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

		addArc(arc1);
		addArc(arc2);
		addArc(arc3);
		addArc(arc4);
		addArc(arc5);
	}

	private void addArc(Edge edge) {
		edgeSet.add(edge);
	}

	public void addVertex(Vertex vertex) {
		vertexByOrder.put(vertexSet.size(), vertex);
		vertexSet.add(vertex);
	}

	public Set<Vertex> getVertexSet() {
		return vertexSet;
	}

	public Set<Edge> getEdgeSet() {
		return edgeSet;
	}

	public Vertex getVertexByOrder(int i) {
		return vertexByOrder.get(i);
	}
}
