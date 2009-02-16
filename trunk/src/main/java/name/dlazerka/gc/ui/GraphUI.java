package name.dlazerka.gc.ui;

import name.dlazerka.gc.model.Arc;
import name.dlazerka.gc.model.Graph;
import name.dlazerka.gc.model.Vertex;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Dzmitry Lazerka
 */
public class GraphUI {
	private Graph graph = Model.getGraph(null);
	private static final Dimension VERTEX_SIZE = new Dimension(100, 100);
	private static final Color COLOR_VERTEX_BORDER = new Color(0, 0, 0);
	private static final Color COLOR_VERTEX_INNER = new Color(255, 255, 255);
	private static final Color COLOR_VERTEX_NUMBER = new Color(0, 0, 0);
	private static final Color COLOR_ARC = new Color(0, 0, 0);

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
				(int) Math.round(radius * Math.sin(angle)) + center.y
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

			drawVertex(g2, center, VERTEX_SIZE, vertex.getNumber());
		}
	}


	private static void drawVertex(Graphics2D g2, Point center, Dimension size, int num) {
		int x = center.x - size.width / 2;
		int y = center.y - size.height / 2;

		g2.setColor(COLOR_VERTEX_BORDER);
		g2.fillOval(x, y, size.width, size.height);

		g2.setColor(COLOR_VERTEX_INNER);
		g2.fillOval(x + 3, y + 3, size.width - 6, size.height - 6);

		g2.setColor(COLOR_VERTEX_NUMBER);

//		Font font = Font.getFont("courier");
		Font font = g2.getFont();
		FontRenderContext fontRenderContext = g2.getFontRenderContext();
		GlyphVector glyphVector = font.createGlyphVector(fontRenderContext, "" + num);
		g2.drawGlyphVector(glyphVector, center.x, center.y);
	}

	private static void drawArc(Graphics2D g2, Point headPoint, Point tailPoint) {
		g2.setColor(COLOR_ARC);
		Stroke oldStroke = g2.getStroke();
		g2.setStroke(new BasicStroke(4.0f));
		g2.drawLine(headPoint.x, headPoint.y, tailPoint.x, tailPoint.y);
		g2.setStroke(oldStroke);
	}
}
