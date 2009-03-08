package name.dlazerka.gc.ui;

import java.awt.*;

/**
 * While dragging a new edge, there is no end vertex yet.
 * This panel paints such 'fake' edge. 
 *
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class EndlessEdgePanel extends EdgePanel {
	private Point toPoint;

	public EndlessEdgePanel(VertexPanel tail) {
		super(null, tail, null);
	}

	@Override
	public Point getToPoint() {
		return toPoint;
	}

	public void setToPoint(Point toPoint) {
		this.toPoint = toPoint;
	}
}
