package name.dlazerka.gc.ui;

import name.dlazerka.gc.bean.Edge;
import name.dlazerka.gc.bean.Graph;
import name.dlazerka.gc.bean.Vertex;
import name.dlazerka.gc.util.LinkedSet2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.*;

/**
 * @author Dzmitry Lazerka
 */
public class GraphPanel extends JPanel implements MouseListener, MouseWheelListener {
	private final static Logger logger = LoggerFactory.getLogger(GraphPanel.class);

	private final Graph graph = new Graph();

	/**
	 * Order represents Z-index.
	 */
	private final LinkedSet2<VertexUI> vertexUISet = new LinkedSet2<VertexUI>();

	/**
	 * Order represents Z-index.
	 */
	private final LinkedSet2<EdgeUI> edgeUISet = new LinkedSet2<EdgeUI>();

	private final Map<Vertex, VertexUI> vertexToVertexUI = new HashMap<Vertex, VertexUI>();
	private final Map<Edge, EdgeUI> edgeToEdgeUI = new HashMap<Edge, EdgeUI>();

	private final static Dimension DEFAULT_DIMENSION = new Dimension(600, 400);
	private Collection<Draggable> draggingObjects = new LinkedList<Draggable>();

	public GraphPanel() {
		addMouseListener(this);
		setPreferredSize(DEFAULT_DIMENSION);
		createUI();
	}

	public void drawGraph(Graphics2D g2) {
		for (EdgeUI edgeUI : edgeUISet) {
			edgeUI.paint(g2);
		}

		for (VertexUI vertexUI : vertexUISet) {
			vertexUI.paint(g2);
		}
	}

	private void createUI() {
		for (Vertex vertex : graph.getVertexSet()) {
			VertexUI vertexUI = new VertexUI(vertex);
			vertexUI.setGraphPanel(this);
//			add(vertexUI);
			vertexUISet.add(vertexUI);
			vertexToVertexUI.put(vertex, vertexUI);
		}
		for (Edge edge : graph.getEdgeSet()) {
			EdgeUI edgeUI = new EdgeUI(
				edge, vertexToVertexUI.get(edge.getHead()), vertexToVertexUI.get(edge.getTail())
			);
			edgeUI.setContainer(this);
			edgeUISet.add(edgeUI);
//			edgeToEdgeUI.put(edge, edgeUI);
		}

		calculateVertexPositions();
	}

	private void calculateVertexPositions() {
		int vertexSetSize = vertexToVertexUI.size();

		Dimension size = DEFAULT_DIMENSION;

		Point center = new Point(size.width / 2, size.height / 2);
		int radius = Math.min(size.width * 3 / 4 - center.x, size.height * 3 / 4 - center.y);

		double angleStep = 2 * Math.PI / vertexSetSize;

		int i = 0;
		for (VertexUI vertexUI : vertexToVertexUI.values()) {
			double angle = i * angleStep;

			Point position = new Point(
				(int) Math.round(radius * Math.cos(angle)) + center.x,
				(int) Math.round(radius * -Math.sin(angle)) + center.y
			);

			vertexUI.setCenter(position);
			i++;
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;

		drawGraph(g2);
	}

	/**
	 * Iterates through {@link #vertexUISet} and checks if any of them {@link VertexUI#contains(Point)} 
	 * @param point point under which to search
	 * @return a {@link VertexUI} or null of not found.
	 */
	private VertexUI getVertexUIUnder(Point point) {
		LinkedSet2<VertexUI> set = vertexUISet;
		ListIterator<VertexUI> listIterator = set.listIterator(set.size());
		while (listIterator.hasPrevious()) {
			VertexUI vertexUI = listIterator.previous();
			if (vertexUI.contains(point)) {
				return vertexUI;
			}
		}
		return null;
	}

	public void mouseClicked(MouseEvent e) {
		// NO OP
	}

	public void mousePressed(MouseEvent e) {
		MouseListener listener = getVertexUIUnder(e.getPoint());
		logger.debug("mousePressed() on listener: {}", listener);
		if (listener != null) {
			 listener.mousePressed(e);
		}
	}

	public void mouseReleased(MouseEvent e) {
		MouseListener listener = null;

		for (Draggable draggable : draggingObjects) {
			if (draggable.contains(e.getPoint())) {
				listener = draggable;
				break;
			}
		}

		if (listener == null) {
			listener = getVertexUIUnder(e.getPoint());
		}

		logger.debug("mouseReleased() on listener: {}", listener);
		if (listener != null) {
			 listener.mouseReleased(e);
		}
	}

	public void mouseEntered(MouseEvent e) {
		// NO OP
	}

	public void mouseExited(MouseEvent e) {
		// NO OP
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		// NO OP
	}

	public void addDraggingObject(Draggable object) {
		draggingObjects.add(object);
		addMouseMotionListener(object);
	}

	public void removeDraggingObject(Draggable object) {
		draggingObjects.remove(object);
		removeMouseMotionListener(object);
	}
}
