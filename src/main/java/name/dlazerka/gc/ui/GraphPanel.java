package name.dlazerka.gc.ui;

import name.dlazerka.gc.Main;
import name.dlazerka.gc.util.ListMap;
import name.dlazerka.gc.bean.Edge;
import name.dlazerka.gc.bean.Graph;
import name.dlazerka.gc.bean.GraphChangeListener;
import name.dlazerka.gc.bean.Vertex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class GraphPanel extends JPanel implements GraphChangeListener {
	private final static Logger logger = LoggerFactory.getLogger(GraphPanel.class);

	private final static Dimension DEFAULT_DIMENSION = new Dimension(600, 400);

	private final Graph graph;
	private final Point popupLocation = new Point();

	private Collection<VertexPanel> vertexPanels = new LinkedList<VertexPanel>();
	private Collection<EdgePanel> edgePanels = new LinkedList<EdgePanel>();

	private Map<Vertex, VertexPanel> vertexToVertexPanel = new ListMap<Vertex, VertexPanel>();
	private Map<Edge, EdgePanel> edgeToEdgePanel = new ListMap<Edge, EdgePanel>();

	/**
	 * Panel on which user has started dragging a new edge.
	 *
	 * @see #lastHoveredVertexPanel
	 */
	private VertexPanel draggingEdgeFrom;

	/**
	 * For determining of panel on which mouse was released when dragging a new edge.
	 *
	 * @see #draggingEdgeFrom
	 */
	private VertexPanel lastHoveredVertexPanel;

	public GraphPanel() {
		graph = new Graph();

		addMouseListener(new PopupLocationRememberer());

		GraphLayoutManager layoutManager = new GraphLayoutManager();
		setLayout(layoutManager);

		setSize(DEFAULT_DIMENSION);// for GraphLayoutManager@58

		setComponentPopupMenu(createPopupMenu());
		addVertexPanels();
		addEdgePanels();
		layoutManager.layoutDefault(this);
	}


	public Component add(Component component) {
		if (component instanceof VertexPanel) {
			VertexPanel panel = (VertexPanel) component;
			vertexPanels.add(panel);
			vertexToVertexPanel.put(panel.getVertex(), panel);
		}
		else if (component instanceof EdgePanel) {
			EdgePanel panel = (EdgePanel) component;
			edgePanels.add(panel);
			edgeToEdgePanel.put(panel.getEdge(), panel);
		}

		return super.add(component);
	}

	private void addVertexPanels() {
		for (Vertex vertex : graph.getVertexSet()) {
			VertexPanel vertexPanel = new VertexPanel(vertex);
			add(vertexPanel);
		}
	}

	private void addEdgePanels() {
		for (Edge edge : graph.getEdgeSet()) {
			VertexPanel tailPanel = vertexToVertexPanel.get(edge.getTail());
			VertexPanel headPanel = vertexToVertexPanel.get(edge.getHead());

			EdgePanel edgePanel = new EdgePanel(
				edge,
				tailPanel,
				headPanel
			);

			tailPanel.addAdjacentEdgePanel(edgePanel);
			headPanel.addAdjacentEdgePanel(edgePanel);

			add(edgePanel);
		}
	}

	public Collection<VertexPanel> getVertexPanels() {
		return vertexPanels;
	}

	public Collection<EdgePanel> getEdgePanels() {
		return edgePanels;
	}

	public void vertexAdded(Vertex vertex) {
		VertexPanel vertexPanel = new VertexPanel(vertex);
		add(vertexPanel);
	}

	public void setHoveredVertexPanel(VertexPanel vertexPanel) {
		logger.debug("{}", vertexPanel);
		if (vertexPanel != null) {
			lastHoveredVertexPanel = vertexPanel;
			setComponentZOrder(vertexPanel, 0);
			repaint(
				vertexPanel.getX(),
				vertexPanel.getY(),
				vertexPanel.getWidth(),
				vertexPanel.getHeight()
			);
		}
	}

	public void startDraggingEdge(VertexPanel vertexPanel) {
		draggingEdgeFrom = vertexPanel;

		EndlessEdgePanel endlessEdgePanel = new EndlessEdgePanel(vertexPanel);

		repaint();
	}

	public boolean isDraggingEdge() {
		return draggingEdgeFrom != null;
	}

	public void stopDraggingEdge() {
		Vertex tail = draggingEdgeFrom.getVertex();
		Vertex head = lastHoveredVertexPanel.getVertex();
		Edge edge = new Edge(tail, head);

		graph.addEdge(edge);

		draggingEdgeFrom = null;
		repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;

		if (isDraggingEdge()) {
			Point mousePos = getMousePosition();
			if (mousePos != null) {
//				drawEdge(draggingEdgeFrom, mousePos, g2);
//				logger.debug("draggingEdgeFrom.getVertexCenterX()={}", draggingEdgeFrom.getVertexCenterX());
			}
		}
	}

/*
	private void drawEdge(VertexPanel tailPanel, Point mousePos, Graphics2D g) {
		g.drawLine(
			tailPanel.getVertexCenterX(),
			tailPanel.getVertexCenterY(),
			mousePos.x,
			mousePos.y
		);
	}
*/

	private class AddVertexAction extends AbstractAction {
		public AddVertexAction() {
			super(Main.getString("add.vertex"));
		}

		public void actionPerformed(ActionEvent e) {
			Vertex vertex = graph.addVertex();
			VertexPanel vertexPanel = new VertexPanel(vertex);
			vertexPanel.setLocation(popupLocation);
			add(vertexPanel);
			vertexPanel.repaint();
		}
	}

	protected class PopupLocationRememberer extends MouseAdapter {
		protected void rememberLocation(MouseEvent e) {
			popupLocation.x = e.getX();
			popupLocation.y = e.getY();
		}

		@Override
		public void mousePressed(MouseEvent e) {
			rememberLocation(e);
		}
	}

	private JPopupMenu createPopupMenu() {
		JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.add(new AddVertexAction());
		return popupMenu;
	}
/*
	public void drawGraph(Graphics2D g2) {
		for (EdgePanel edgeUI : model.edgeModelSet) {
			edgeUI.paint(g2);
		}

		for (VertexPanelModel vertexUI : model.vertexModelSet) {
			vertexUI.paint(g2);
		}
	}

*/

	/*
	 private void mousePopupTriggered(MouseEvent e) {
 //		popupMenu.set
 //		popupMenu.setVisible(true);

	 }

	 public void mouseClicked(MouseEvent e) {
		 if (e.isPopupTrigger()) {
			 mousePopupTriggered(e);
			 return;
		 }
	 }

	 public void mousePressed(MouseEvent e) {
		 if (e.isPopupTrigger()) {
			 mousePopupTriggered(e);
			 return;
		 }

		 if (e.getButton() != MouseEvent.BUTTON1) {
			 return;
		 }

		 Draggable draggable = model.getVertexUIUnder(e.getPoint());
		 logger.debug("mousePressed() on draggable: {}", draggable);
		 if (draggable != null) {
			 addDraggingObject(draggable);
			 draggable.startFollowingMouse(e);
		 }
	 }
 */
	/**
	 * Looks at first for currently dragged object and if there is no such under mouse cursor,
	 * looks at other objects.
	 *
	 * @param e a {@link MouseEvent}
	 */
/*
	public void mouseReleased(MouseEvent e) {
		if (e.isPopupTrigger()) {
			mousePopupTriggered(e);
			return;
		}

		if (e.getButton() != MouseEvent.BUTTON1) {
			return;
		}

		Draggable draggable = null;
		for (Draggable dragging : model.draggingObjects) {
			if (dragging.contains(e.getPoint())) {
				draggable = dragging;
				break;
			}
		}

		if (draggable == null) {
			draggable = model.getVertexUIUnder(e.getPoint());
		}

		logger.debug("mouseReleased() on draggable: {}", draggable);
		if (draggable != null) {
			draggable.stopFollowingMouse(e);
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
		model.draggingObjects.add(object);
		addMouseMotionListener(object);
	}

	public void removeDraggingObject(Draggable object) {
		model.draggingObjects.remove(object);
		removeMouseMotionListener(object);
	}

	public void mouseDragged(MouseEvent e) {
		repaint();
	}

	public void mouseMoved(MouseEvent e) {
		MouseMotionListener listener = model.getVertexUIUnder(e.getPoint());

//		logger.debug("mouseMoved() on listener: {}", listener);
		if (listener != null) {
			this.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
		}
		else {
			setCursor(Cursor.getDefaultCursor());
		}

	}
	*/
}
