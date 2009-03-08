package name.dlazerka.gc.bean;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class Edge {
	private final Vertex tail;
	private final Vertex head;

	public Edge(Vertex tail, Vertex head) {
		this.tail = tail;
		this.head = head;
	}

	public Vertex getTail() {
		return tail;
	}

	public Vertex getHead() {
		return head;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Edge)) return false;

		Edge edge = (Edge) o;

		if (!tail.equals(edge.tail)) return false;
		if (!head.equals(edge.head)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = tail.hashCode();
		result = 31 * result + head.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "Edge{" +
		       "tail=" + tail +
		       ", head=" + head +
		       '}';
	}
}
