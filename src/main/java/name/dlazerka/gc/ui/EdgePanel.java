package name.dlazerka.gc.ui;

import name.dlazerka.gc.bean.Edge;

import java.awt.*;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class EdgePanel extends AbstractEdgePanel {
	private final Edge edge;
	private final VertexPanel tail;
	private final VertexPanel head;

	public EdgePanel(Edge edge, VertexPanel tail, VertexPanel head) {
		this.edge = edge;
		this.tail = tail;
		this.head = head;

		setOpaque(false);
	}

	public Edge getEdge() {
		return edge;
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		drawEdge(
			g2,
			getFromPoint(),
			getToPoint()
		);
	}

	protected Point getFromPoint() {
		return head.getVertexCenter();
	}

	protected Point getToPoint() {
		return tail.getVertexCenter();
	}

/*
	public int getX() {
		return Math.min(
			head.getVertexCenterX(),
			tail.getVertexCenterX()
		);
	}

	@Override
	public int getY() {
		return Math.min(
			head.getVertexCenterY(),
			tail.getVertexCenterY()
		);
	}

	@Override
	public int getWidth() {
		return Math.abs(head.getVertexCenterX() - tail.getVertexCenterX());
	}

	@Override
	public int getHeight() {
		return Math.abs(head.getVertexCenterY() - tail.getVertexCenterY());
	}
*/
}
