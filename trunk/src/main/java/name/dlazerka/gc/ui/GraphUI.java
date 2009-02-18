package name.dlazerka.gc.ui;

import name.dlazerka.gc.bean.Arc;
import name.dlazerka.gc.bean.Graph;
import name.dlazerka.gc.bean.Vertex;
import name.dlazerka.gc.model.Model;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Dzmitry Lazerka
 */
public class GraphUI {
	private Graph graph = Model.getGraph();

	private static final Color COLOR_ARC = new Color(0, 0, 0);
	private static final Stroke STROKE_ARC = new BasicStroke(4.0f);

	public void drawGraph(Graphics2D g2, Dimension size) {
		int vertexSetSize = graph.getVertexSet().size();

		Point center = new Point(size.width / 2, size.height / 2);
		int radius = Math.min(size.width * 3 / 4 - center.x, size.height * 3 / 4 - center.y);

		double angleStep = 2 * Math.PI / vertexSetSize;

		Map<Vertex, Point> vertexPositions = new HashMap<Vertex, Point>(vertexSetSize);

		for (int i = 0; i < vertexSetSize; i++) {
			double angle = i * angleStep;

			Point position = new Point(
				(int) Math.round(radius * Math.cos(angle)) + center.x,
				(int) Math.round(radius * -Math.sin(angle)) + center.y
		        );

			Vertex vertex = graph.getVertexByOrder(i);

			vertexPositions.put(vertex, position);
		}

		drawArcs(g2, vertexPositions);

		drawVertices(g2, vertexPositions);
	}

	private void drawArcs(Graphics2D g2, Map<Vertex, Point> vertexPositions) {
		for (Arc arc : graph.getArcSet()) {
			Point headPoint = vertexPositions.get(arc.getHead());
			Point tailPoint = vertexPositions.get(arc.getTail());

			drawArc(g2, headPoint, tailPoint);
		}
	}

	private void drawVertices(Graphics2D g2, Map<Vertex, Point> vertexPositions) {
		for (Vertex vertex : graph.getVertexSet()) {
			Point center = vertexPositions.get(vertex);

			VertexUI.draw(g2, center, vertex.getNumber());
		}
	}

	private static void drawArc(Graphics2D g2, Point headPoint, Point tailPoint) {
		g2 = (Graphics2D) g2.create();

		g2.setColor(COLOR_ARC);
		g2.setStroke(STROKE_ARC);
		g2.drawLine(headPoint.x, headPoint.y, tailPoint.x, tailPoint.y);
	}
}
