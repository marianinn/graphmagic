package name.dlazerka.gc.model;

/**
 * @author Dzmitry Lazerka
 */
public class Arc {
	private final Vertex tail;
	private final Vertex head;

	public Arc(Vertex tail, Vertex head) {
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
		if (!(o instanceof Arc)) return false;

		Arc arc = (Arc) o;

		if (!tail.equals(arc.tail)) return false;
		if (!head.equals(arc.head)) return false;

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
		return "Arc{" +
		       "tail=" + tail +
		       ", head=" + head +
		       '}';
	}
}
