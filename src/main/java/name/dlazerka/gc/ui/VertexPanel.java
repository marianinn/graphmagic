package name.dlazerka.gc.ui;

import name.dlazerka.gc.bean.Vertex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
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
	private static final Dimension VERTEX_OVAL_SIZE = new Dimension(50, 50);

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


	private final Vertex vertex;

	private boolean isHovered = false;
	private final AddEdgePanel addEdgePanel = new AddEdgePanel();
	private boolean isDraggingEdge = false;
	private final Dimension panelSize = new Dimension(
		VERTEX_OVAL_SIZE.width + addEdgePanel.getPreferredSize().width,
		VERTEX_OVAL_SIZE.height
	);

	public VertexPanel(Vertex vertex) {
		super(null);

		this.vertex = vertex;

		setPreferredSize(panelSize);
		setSize(panelSize);
//		setDoubleBuffered(true); is needed?

		Dimension preferredSize = addEdgePanel.getPreferredSize();
		addEdgePanel.setBounds(// top right corner
		                       VERTEX_OVAL_SIZE.width,
		                       0,
		                       preferredSize.width,
		                       preferredSize.height
		);
//		addEdgePanel.addMouseMotionListener(new DragMouseListener());
		add(addEdgePanel);

		addMouseMotionListener(new DragMouseListener());
		addMouseListener(new MouseListener());
	}

	public Vertex getVertex() {
		return vertex;
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
	}

	public void paintComponent(Graphics g) {

		Graphics2D g2 = (Graphics2D) g;

		g2.setColor(COLOR_BORDER);
		g2.fillOval(0, 0, VERTEX_OVAL_SIZE.width, VERTEX_OVAL_SIZE.height);

		if (!isHovered) {
			g2.setColor(COLOR_INNER);
		}
		else {
			g2.setColor(COLOR_INNER_HOVER);
		}
		g2.fillOval(3, 3, VERTEX_OVAL_SIZE.width - 6, VERTEX_OVAL_SIZE.height - 6);

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

	/**
	 * Overrides default {@link JComponent#contains(int, int)} by returning false
	 * when coordinates are not over square containing vertex oval and square containing
	 * {@link #addEdgePanel}. In other words, by removing rectangle under {@link #addEdgePanel}.
	 *
	 *
	 * @param x see {@link JComponent#contains(int, int)}
	 * @param y see {@link JComponent#contains(int, int)}
	 * @return see {@link JComponent#contains(int, int)}
	 */
	@Override
	public boolean contains(int x, int y) {
		if (!super.contains(x, y)) {
			return false;
		}

		if (x > VERTEX_OVAL_SIZE.width && y > addEdgePanel.getHeight()) {
			return false;
		}

		return true;
	}

	protected void setHovered(boolean isHovered) {
		logger.debug("setHovered({})", isHovered);
		this.isHovered = isHovered;

		int newWidth = VERTEX_OVAL_SIZE.width + (isHovered ? 20 : 0);

		setBounds(
			getX(),
			getY(),
			newWidth,
			VERTEX_OVAL_SIZE.height
		);

		if (isHovered) {
			GraphPanel graphPanel = getParentGraphPanel();
			graphPanel.setHoveredVertexPanel(VertexPanel.this);
		}
		
		repaint();
	}

	public GraphPanel getParentGraphPanel() {
		GraphPanel graphPanel = (GraphPanel) getParent();
		return graphPanel;
	}

	public void checkHovered() {
		Point mousePosition = getMousePosition();
		logger.debug("checkHovered(): mousePosition={}", mousePosition);
		setHovered(mousePosition != null);
	}

	/**
	 * @return center of the vertex oval, not the panel itself
	 */
	public int getVertexCenterX() {
		return getX() + VERTEX_OVAL_SIZE.width / 2;
	}

	/**
	 * @return center of the vertex oval, not the panel itself
	 */
	public int getVertexCenterY() {
		return getY() + VERTEX_OVAL_SIZE.height / 2;
	}

	public void startDraggingEdge() {
		isDraggingEdge = true;
		getParentGraphPanel().startDraggingEdge(this);
	}

	public void stopDraggingEdge() {
		isDraggingEdge = false;
		getParentGraphPanel().stopDraggingEdge();
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

	protected class DragMouseListener extends MouseMotionAdapter {
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

			// fix for too-fast-moving mouse :) 
			if (!isHovered) {
				setHovered(true);
			}
		}

	}

	protected class MouseListener extends MouseAdapter {
		@Override
		public void mouseEntered(MouseEvent e) {
			setHovered(true);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			if (!isDraggingEdge) {
				setHovered(false);
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			logger.debug("mousePressed()");
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			logger.debug("mouseReleased()");
		}
	}
}
