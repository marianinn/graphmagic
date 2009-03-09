package name.dlazerka.gc.ui;

import name.dlazerka.gc.bean.Edge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.event.MouseMotionAdapter;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class EdgePanel extends AbstractEdgePanel {
	private static final Logger logger = LoggerFactory.getLogger(EdgePanel.class);

	private final Edge edge;
	private final VertexPanel tail;
	private final VertexPanel head;
	private final Point ctrlPoint = new Point();
	private boolean curved = false;

	public EdgePanel(Edge edge, VertexPanel tail, VertexPanel head) {
		this.edge = edge;
		this.tail = tail;
		this.head = head;

		logger.debug("{}", ctrlPoint);

		setOpaque(false);
		addMouseMotionListener(new DragMouseListener());
	}

	public Edge getEdge() {
		return edge;
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {
		super.setBounds(x, y, width, height);
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;

		drawEdge(
			g2,
			getFromPoint(),
			curved ? ctrlPoint : getFromPoint(),
			getToPoint()
		);
	}

	protected Point getFromPoint() {
		return head.getVertexCenter();
	}

	protected Point getToPoint() {
		return tail.getVertexCenter();
	}

	public void setCurved() {
		this.curved = true;
		ctrlPoint.move(
			(getFromPoint().x + getToPoint().x) / 2,
			(getFromPoint().y + getToPoint().y) / 2
		);
	}

	private class DragMouseListener extends MouseMotionAdapter {
/* TODO unimportant feature
		@Override
		public void mouseDragged(MouseEvent e) {
			logger.trace("{}", e.getPoint());

			setCurved();

			ctrlPoint.move(e.getX(), e.getY());
			repaint();
		}
*/
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
