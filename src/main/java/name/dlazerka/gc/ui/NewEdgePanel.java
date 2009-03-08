package name.dlazerka.gc.ui;

import javax.swing.*;
import java.awt.*;

/**
 * While dragging a new edge, there is no end vertex yet.
 * This panel paints such 'fake' edge. 
 *
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class NewEdgePanel extends JPanel {
	private Point toPoint;
	private VertexPanel tail;// not final

	public NewEdgePanel() {
		setOpaque(false);
	}

	public void setToPoint(Point toPoint) {
		this.toPoint = toPoint;
	}

	public void setTail(VertexPanel tail) {
		this.tail = tail;
	}

	@Override
	protected void paintComponent(Graphics g0) {
		super.paintComponent(g0);

		if (tail == null) {
			return;
		}

		Graphics2D g = (Graphics2D) g0;
		EdgePanel.drawEdge(g, tail.getVertexCenter(), toPoint);
	}
}
