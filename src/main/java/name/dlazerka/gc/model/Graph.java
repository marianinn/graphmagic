package name.dlazerka.gc.model;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Dzmitry Lazerka
 */
public class Graph {
	private Set<Vertex> vertexSet = new HashSet<Vertex>();
	private Set<Arc> arcSet = new HashSet<Arc>();

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

	public void setVertexSet(Set<Vertex> vertexSet) {
		this.vertexSet = vertexSet;
	}

	public Set<Arc> getArcSet() {
		return arcSet;
	}

	public void setArcSet(Set<Arc> arcSet) {
		this.arcSet = arcSet;
	}
}
