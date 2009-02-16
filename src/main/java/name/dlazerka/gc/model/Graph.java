package name.dlazerka.gc.model;


import java.util.HashSet;
import java.util.Set;

/**
 * @author Dzmitry Lazerka
 */
public class Graph {
	private final Set<Vertex> vertexSet = new HashSet<Vertex>();
	private final Set<Arc> arcSet = new HashSet<Arc>();

	public Graph() {
		Vertex vertex1 = new Vertex(1);
//		Vertex vertex2 = new Vertex(2);
		vertexSet.add(vertex1);
//		vertexSet.add(vertex2);


//		arcSet.add(new Arc(vertex1, vertex2));
	}

	public Set<Vertex> getVertexSet() {
		return vertexSet;
	}

	public Set<Arc> getArcSet() {
		return arcSet;
	}

	
}
