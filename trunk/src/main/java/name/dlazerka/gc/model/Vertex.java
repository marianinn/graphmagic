package name.dlazerka.gc.model;

/**
 * @author Dzmitry Lazerka
 */
public class Vertex {
	private final int number;
	private int orderNumber;


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

	public int getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}

	@Override
	public String toString() {
		return "Vertex{" +
		       "number=" + number +
		       ", orderNumber=" + orderNumber +
		       '}';
	}
}
