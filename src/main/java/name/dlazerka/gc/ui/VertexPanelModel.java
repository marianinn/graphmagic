package name.dlazerka.gc.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Dzmitry Lazerka
 */
public class VertexPanelModel {
	private final static Logger logger = LoggerFactory.getLogger(VertexPanelModel.class);
/*
	private Point mouseCenteredPosition;

	public VertexPanelModel(Vertex vertex) {
		this.vertex = vertex;
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof VertexPanelModel)) return false;

		VertexPanelModel vertexUI = (VertexPanelModel) o;

		if (!vertex.equals(vertexUI.vertex)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		return vertex.hashCode();
	}

	@Override
	public String toString() {
		return "VertexPanelModel{" +
		       "vertex=" + vertex +
		       ", center=" + center +
		       ", mouseCenteredPosition=" + mouseCenteredPosition +
		       '}';
	}

	public Point getCenter() {
		return center;
	}

	public void setCenter(Point center) {
		this.center = center;
	}

/*
	public boolean contains(Point point) {
		return Math.abs(center.x - point.x) <= SIZE.width / 2 &&
		       Math.abs(center.y - point.y) <= SIZE.height / 2;
	}

	public void startFollowingMouse(MouseEvent e) {
		mouseCenteredPosition = new Point(e.getPoint().x - center.x, e.getPoint().y - center.y);
	}

	public void stopFollowingMouse(MouseEvent e) {
	}

	public void mouseDragged(MouseEvent e) {
		center.x = e.getPoint().x - mouseCenteredPosition.x;
		center.y = e.getPoint().y - mouseCenteredPosition.y;
	}

	public void mouseMoved(MouseEvent e) {
	}
*/

}
