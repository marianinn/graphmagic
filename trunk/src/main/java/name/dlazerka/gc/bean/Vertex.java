package name.dlazerka.gc.bean;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class Vertex {
	private final int number;


	public Vertex(int number) {
		this.number = number;
	}

	public int getNumber() {
		return number;
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
		return "Vertex{" +
		       "number=" + number +
		       '}';
	}
}
