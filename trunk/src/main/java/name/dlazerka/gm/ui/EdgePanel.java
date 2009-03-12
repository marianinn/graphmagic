package name.dlazerka.gc.ui;

import name.dlazerka.gc.bean.Edge;

import javax.swing.*;
import java.awt.*;

/**
 * @author Dzmitry Lazerka
 */
public class EdgePanel extends JPanel {
	private static final Color COLOR_ARC = new Color(0, 0, 0);
	private static final Stroke STROKE_ARC = new BasicStroke(2.0f);

	private final Edge edge;
	private final VertexPanel head;
	private final VertexPanel tail;

	public EdgePanel(Edge edge, VertexPanel head, VertexPanel tail) {
		this.edge = edge;
		this.head = head;
		this.tail = tail;
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();

		g2.setColor(COLOR_ARC);
		g2.setStroke(STROKE_ARC);
		g2.drawLine(
			head.getLocation().x + head.getSize().width / 2,
			head.getLocation().y + head.getSize().height / 2,
			tail.getLocation().x + tail.getSize().width / 2,
			tail.getLocation().y + tail.getSize().height / 2
		);
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
