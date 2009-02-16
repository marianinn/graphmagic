package name.dlazerka.gc.ui;

import name.dlazerka.gc.model.Arc;
import name.dlazerka.gc.model.Graph;
import name.dlazerka.gc.model.Vertex;

import java.awt.*;

/**
 * @author Dzmitry Lazerka
 */
public class GraphUI {
	private Graph graph = Model.getGraph(null);

	public void drawGraph(Graphics2D g2, Dimension size) {
		int vertexSetSize = graph.getVertexSet().size();

		Point center = new Point(size.width / 2, size.height / 2);
		int radius = Math.min(size.width * 3 / 2, size.height * 3 / 2);

		double angleStep = 2 * Math.PI / vertexSetSize;

		Point[] positions = new Point[vertexSetSize];

		for (int i = 0; i < vertexSetSize; i++) {
			double angle = i * angleStep;

			Point vertexPosition = new Point(
				(int) Math.round(radius * Math.sin(angle)) + center.x,
				(int) Math.round(radius * Math.cos(angle)) + center.y
		        );

			positions[i] = vertexPosition;
		}

		drawArcs(g2, positions);

		drawVertices(g2, positions);
	}

	private void drawArcs(Graphics2D g2, Point[] positions) {
		for (Arc arc : graph.getArcSet()) {
			Point headPoint = positions[arc.getHead().getNumber()];
			Point tailPoint = positions[arc.getTail().getNumber()];

			g2.drawLine(headPoint.x, headPoint.y, tailPoint.x,  tailPoint.y);
		}
	}

	private void drawVertices(Graphics2D g2, Point[] positions) {
		for (Vertex vertex : graph.getVertexSet()) {
			Point center = positions[vertex.getNumber()];

			g2.drawOval(center.x, center.y, 100, 100);
		}
	}

}
