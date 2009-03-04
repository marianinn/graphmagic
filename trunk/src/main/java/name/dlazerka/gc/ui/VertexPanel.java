package name.dlazerka.gc.ui;

import name.dlazerka.gc.bean.Vertex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;

/**
 * @author Dzmitry Lazerka
 */
public class VertexPanel extends JPanel {
	private static final Logger logger = LoggerFactory.getLogger(VertexPanel.class);

	/**
	 * Default diameter of the vertex.
	 */
	private static final Dimension SIZE = new Dimension(50, 50);

	private static final int FONT_SIZE = 30;
	private static final Font NUMBER_FONT = new Font("courier", Font.PLAIN, FONT_SIZE);

	/**
	 * Horizintal shift of the vertex number for one-digit numbers.
	 */
	private static final int NUMBER_SHIFT_X1 = 17;

	/**
	 * Horizontal shift of the vertex number for two-digit numbers.
	 */
	private static final int NUMBER_SHIFT_X2 = 9;

	/**
	 * Vertical shift of the vertex number.
	 */
	private static final int NUMBER_SHIFT_Y = 36;


	private static final Color COLOR_BORDER = new Color(0, 0, 0);
	private static final Color COLOR_INNER = new Color(0xFF, 0xFF, 0xFF);
	private static final Color COLOR_INNER_HOVER = new Color(0xA0, 0xFF, 0xA0);
	private static final Color COLOR_NUMBER = new Color(0, 0, 0);


	protected final Vertex vertex;

	private boolean isHovered = false;

	public VertexPanel(Vertex vertex) {
		this.vertex = vertex;

		setPreferredSize(SIZE);
		setSize(SIZE);

		addMouseMotionListener(new DragMouseListener());
		addMouseListener(new HoverMouseListener());
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
	}

	public void paintComponent(Graphics g) {

		Graphics2D g2 = (Graphics2D) g;

		g2.setColor(COLOR_BORDER);
		g2.fillOval(0, 0, SIZE.width, SIZE.height);

		if (!isHovered) {
			g2.setColor(COLOR_INNER);
		}
		else {
			g2.setColor(COLOR_INNER_HOVER);
		}
		g2.fillOval(3, 3, SIZE.width - 6, SIZE.height - 6);

		g2.setColor(COLOR_NUMBER);

		FontRenderContext fontRenderContext = g2.getFontRenderContext();
		GlyphVector glyphVector = NUMBER_FONT.createGlyphVector(fontRenderContext, "" + vertex.getNumber());

		// todo: many digits
//		int digits = (int) Math.floor(vertex.getNumber() / 10) + 1;
		int glyphStartX;
		if (vertex.getNumber() < 10) {
			glyphStartX = NUMBER_SHIFT_X1;
		}
		else {
			glyphStartX = NUMBER_SHIFT_X2;
		}

		int glyphStartY = NUMBER_SHIFT_Y;

		g2.drawGlyphVector(glyphVector, glyphStartX, glyphStartY);

//		g2.drawLine(0, getSize().height / 2, getSize().width, getSize().height / 2);
//		g2.drawLine(getSize().width / 2, 0, getSize().width / 2, getSize().height);
	}

	@Override
	public String toString() {
		return "VertexPanel{" +
		       "vertex=" + vertex +
		       "x=" + getX() +
		       "y=" + getY() +
		       "width=" + getSize().width +
		       "height=" + getSize().height +
		       '}';
	}

	protected class DragMouseListener extends MouseAdapter {
		private int mouseX;
		private int mouseY;

		@Override
		public void mouseMoved(MouseEvent e) {
			mouseX = e.getX();
			mouseY = e.getY();
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			int moveByX = e.getX() - mouseX;
			int moveByY = e.getY() - mouseY;
			setLocation(getX() + moveByX, getY() + moveByY);
		}
	}

	protected class HoverMouseListener extends MouseAdapter {
		@Override
		public void mouseEntered(MouseEvent e) {
			isHovered = true;

			GraphPanel parent = (GraphPanel) getParent();
			parent.setHoveredVertexPanel(VertexPanel.this);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			isHovered = false;

			GraphPanel parent = (GraphPanel) getParent();
			parent.setHoveredVertexPanel(null);
		}
	}
}
