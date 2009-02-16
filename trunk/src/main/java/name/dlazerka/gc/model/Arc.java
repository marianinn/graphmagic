package name.dlazerka.gc.model;

/**
 * @author Dzmitry Lazerka
 */
public class Arc {
	private final Vertex fromVertex;
	private final Vertex toVertex;

	public Arc(Vertex fromVertex, Vertex toVertex) {
		this.fromVertex = fromVertex;
		this.toVertex = toVertex;
	}

	public Vertex getFromVertex() {
		return fromVertex;
	}

	public Vertex getToVertex() {
		return toVertex;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Arc)) return false;

		Arc arc = (Arc) o;

		if (!fromVertex.equals(arc.fromVertex)) return false;
		if (!toVertex.equals(arc.toVertex)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = fromVertex.hashCode();
		result = 31 * result + toVertex.hashCode();
		return result;
	}
}
