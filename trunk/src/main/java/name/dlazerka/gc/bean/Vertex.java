package name.dlazerka.gc.bean;

import name.dlazerka.gc.util.LinkedSet;

import java.util.Set;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class Vertex {
	private final int number;
	private Set<Edge> adjacentEdgeSet = new LinkedSet<Edge>();

	public Vertex(int number) {
		this.number = number;
	}

	public int getNumber() {
		return number;
	}

	public Set<Edge> getAdjacentEdgeSet() {
		return adjacentEdgeSet;
	}

	protected boolean addAdjacentEdge(Edge edge) {
		return adjacentEdgeSet.add(edge);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Vertex)) return false;

		Vertex vertex = (Vertex) o;

		if (number != vertex.number) return false;

		return true;
	}

	@Override
	public int hashCode() {
		return number;
	}

	@Override
	public String toString() {
		return number + "";
	}
}
