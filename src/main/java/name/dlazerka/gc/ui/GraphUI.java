package name.dlazerka.gc.ui;

import name.dlazerka.gc.model.Graph;

import java.awt.*;
import java.awt.geom.Line2D;

/**
 * @author Dzmitry Lazerka
 */
public class GraphUI {
	private final Graph graph = new Graph();

	public void drawGraph(Graphics2D g2) {
		Point fromPoint = new Point(0, 0);
		Point toPoint = new Point(100, 100);
		Line2D line = new Line2D.Double(fromPoint, toPoint);

		g2.draw(line);
	}
}
