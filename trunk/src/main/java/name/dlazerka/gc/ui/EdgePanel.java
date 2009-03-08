package name.dlazerka.gc.ui;

import name.dlazerka.gc.bean.Edge;

import javax.swing.*;
import java.awt.*;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class EdgePanel extends JPanel {
	private static final Color EDGE_COLOR = new Color(0, 0, 0);
	private static final Stroke EDGE_STROKE = new BasicStroke(2.0f);

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

	protected void drawEdge(Graphics2D g, Point from, Point to) {
		g.setColor(EDGE_COLOR);
		g.setStroke(EDGE_STROKE);
		g.drawLine(
			from.x,
			from.y,
			to.x,
			to.y
		);
	}

	protected Point getFromPoint() {
		return head.getVertexCenter();
	}

	protected Point getToPoint() {
		return tail.getVertexCenter();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof EdgePanel)) return false;

		EdgePanel edgePanel = (EdgePanel) o;

		if (!edge.equals(edgePanel.edge)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		return edge.hashCode();
	}
}
