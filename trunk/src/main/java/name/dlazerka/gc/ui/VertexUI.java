package name.dlazerka.gc.ui;

import name.dlazerka.gc.bean.Vertex;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;

/**
 * @author Dzmitry Lazerka
 */
public class VertexUI {
	private static final Dimension SIZE = new Dimension(50, 50);
	private static final int NUM_SHIFT_X = 8;
	private static final int NUM_SHIFT_Y = 12;
	private static final Color COLOR_BORDER = new Color(0, 0, 0);
	private static final Color COLOR_INNER = new Color(255, 255, 255);
	private static final Color COLOR_NUMBER = new Color(0, 0, 0);

	private final Vertex vertex;
	private Point center;

	public VertexUI(Vertex vertex) {
		this.vertex = vertex;
	}

	public void draw(Graphics2D g2) {
		int x = center.x - SIZE.width / 2;
		int y = center.y - SIZE.height / 2;

		g2 = (Graphics2D) g2.create();

		g2.setColor(COLOR_BORDER);
		g2.fillOval(x, y, SIZE.width, SIZE.height);

		g2.setColor(COLOR_INNER);
		g2.fillOval(x + 3, y + 3, SIZE.width - 6, SIZE.height - 6);

		g2.setColor(COLOR_NUMBER);

		int fontSize = 30;
		Font font = new Font("courier", Font.PLAIN, fontSize);
		FontRenderContext fontRenderContext = g2.getFontRenderContext();
		GlyphVector glyphVector = font.createGlyphVector(fontRenderContext, "" + vertex.getNumber());

		// todo: many digits
		int digits = (int) Math.floor(vertex.getNumber() / 10) + 1;

		int glyphStartX = center.x - NUM_SHIFT_X;
		int glyphStartY = center.y + NUM_SHIFT_Y;

		g2.drawGlyphVector(glyphVector, glyphStartX, glyphStartY);

//		g2.drawLine(center.x, 0, center.x, 600);
//		g2.drawLine(0, center.y, 800, center.y);
//		g2.drawLine(0, center.y, 800, center.y);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof VertexUI)) return false;

		VertexUI vertexUI = (VertexUI) o;

		if (!vertex.equals(vertexUI.vertex)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		return vertex.hashCode();
	}

	@Override
	public String toString() {
		return "VertexUI{" +
		       "vertex=" + vertex +
		       '}';
	}

	public Point getCenter() {
		return center;
	}

	public void setCenter(Point center) {
		this.center = center;
	}
}
