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
	private final Set<Arc> arcSet = new HashSet<Arc>();
	private final Map<Integer, Vertex> vertexByOrder = new HashMap<Integer, Vertex>();

	public Graph() {
		Vertex vertex1 = new Vertex(1);
		Vertex vertex2 = new Vertex(2);
		Vertex vertex3 = new Vertex(3);
		Vertex vertex4 = new Vertex(4);

		addVertex(vertex1);
		addVertex(vertex2);
		addVertex(vertex3);
		addVertex(vertex4);

		Arc arc1 = new Arc(vertex1, vertex2);
		Arc arc2 = new Arc(vertex2, vertex3);
		Arc arc3 = new Arc(vertex3, vertex1);
		Arc arc4 = new Arc(vertex3, vertex4);
		Arc arc5 = new Arc(vertex4, vertex1);

		addArc(arc1);
		addArc(arc2);
		addArc(arc3);
		addArc(arc4);
		addArc(arc5);
	}

	private void addArc(Arc arc) {
		arcSet.add(arc);
	}

	public void addVertex(Vertex vertex) {
		vertexByOrder.put(vertexSet.size(), vertex);
		vertexSet.add(vertex);
	}

	public Set<Vertex> getVertexSet() {
		return vertexSet;
	}

	public Set<Arc> getArcSet() {
		return arcSet;
	}

	public Vertex getVertexByOrder(int i) {
		return vertexByOrder.get(i);
	}
}
