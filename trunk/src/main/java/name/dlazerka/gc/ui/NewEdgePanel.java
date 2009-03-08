package name.dlazerka.gc.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

/**
 * While dragging a new edge, there is no end vertex yet.
 * This panel paints such 'fake' edge.
 *
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class NewEdgePanel extends AbstractEdgePanel {
	private static final Logger logger = LoggerFactory.getLogger(NewEdgePanel.class);

	private Point head = new Point();
	private VertexPanel tail;
	private boolean visible;

	public NewEdgePanel() {
		setOpaque(false);
	}

	public Point getHead() {
		return head;
	}

	public void setHead(Point head) {
		this.head = head;
	}

	public void setTail(VertexPanel tail) {
		this.tail = tail;
	}

	@Override
	public void setVisible(boolean visible) {
		this.visible = visible;
		trackMouseDragged();
	}

	@Override
	protected void paintComponent(Graphics g0) {
		if (tail == null) {
			return;
		}

		if (visible) {
			Graphics2D g = (Graphics2D) g0;
			drawEdge(g, tail.getVertexCenter(), head);
		}
	}

	private GraphPanel getGraphPanel() {
		return (GraphPanel) this.getParent();
	}

	public void trackMouseDragged() {
		Point mousePosition = getGraphPanel().getMousePosition();
//		mousePosition.translate(10, 10);
//		logger.debug("{}", mousePosition);

		if (mousePosition != null) {
			head = mousePosition;
			repaint();
		}
		else {
			logger.debug("{}", mousePosition);
		}
	}
}
