package name.dlazerka.gc.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.QuadCurve2D;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public abstract class AbstractEdgePanel extends JPanel {
	private static final Logger logger = LoggerFactory.getLogger(AbstractEdgePanel.class);

	private static final Color EDGE_COLOR = Color.BLACK;
	private static final Stroke EDGE_STROKE = new BasicStroke(2f);
	private final QuadCurve2D curve = new QuadCurve2D.Double();
	private Shape lastShownShape = curve;

	protected void drawEdge(Graphics2D g, Point from, Point ctrl, Point to) {
		g.setColor(EDGE_COLOR);
		g.setStroke(EDGE_STROKE);

//		Point point = new Point((to.x + from.x) / 2, (to.y + from.y) / 2);
		curve.setCurve(from, ctrl, to);

		lastShownShape = curve;
		lastShownShape = EDGE_STROKE.createStrokedShape(lastShownShape);

		g.draw(lastShownShape);
	}

	@Override
	public boolean contains(int x, int y) {
		boolean b = lastShownShape.contains(x, y);
		if (b) logger.debug("{}", b);
		return b;
	}
}
