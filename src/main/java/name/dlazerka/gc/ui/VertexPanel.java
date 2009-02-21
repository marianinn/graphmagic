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


	private static final Dimension SIZE = new Dimension(50, 50);
	private static final int NUM_SHIFT_X = 17;
	private static final int NUM_SHIFT_Y = 36;
	private static final Color COLOR_BORDER = new Color(0, 0, 0);
	private static final Color COLOR_INNER = new Color(255, 255, 255);
	private static final Color COLOR_NUMBER = new Color(0, 0, 0);


	protected final Vertex vertex;
	private final InnerMouseAdapter mouseAdapter = new InnerMouseAdapter();
	private boolean dragging = false;

	public VertexPanel(Vertex vertex) {
		this.vertex = vertex;

		setPreferredSize(SIZE);
		setSize(SIZE);

		addMouseListener(mouseAdapter);
		addMouseMotionListener(mouseAdapter);
		addMouseWheelListener(mouseAdapter);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
	}

	public void paintComponent(Graphics g) {

		Graphics2D g2 = (Graphics2D) g;
//		Graphics2D g2 = (Graphics2D) g.create();

		g2.setColor(COLOR_BORDER);
		g2.fillOval(0, 0, SIZE.width, SIZE.height);

		g2.setColor(COLOR_INNER);
		g2.fillOval(3, 3, SIZE.width - 6, SIZE.height - 6);

		g2.setColor(COLOR_NUMBER);

		int fontSize = 30;
		Font font = new Font("courier", Font.PLAIN, fontSize);
		FontRenderContext fontRenderContext = g2.getFontRenderContext();
		GlyphVector glyphVector = font.createGlyphVector(fontRenderContext, "" + vertex.getNumber());

		// todo: many digits
		int digits = (int) Math.floor(vertex.getNumber() / 10) + 1;

		int glyphStartX = NUM_SHIFT_X;
		int glyphStartY = NUM_SHIFT_Y;

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

	private class InnerMouseAdapter extends MouseAdapter {
		private int mouseX;
		private int mouseY;

		@Override
		public void mouseMoved(MouseEvent e) {
			mouseX = e.getX();
			mouseY = e.getY();
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			int moveToX = e.getX() - mouseX;
			int moveToY = e.getY() - mouseY;
			setLocation(getX() + moveToX, getY() + moveToY);
		}
	}
}
