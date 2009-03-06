package name.dlazerka.gc.ui;

import name.dlazerka.gc.Main;
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
import java.util.LinkedList;
import java.util.List;

/**
 * @author Dzmitry Lazerka
 */
public class GraphPanel extends JPanel implements GraphChangeListener {
	private final static Logger logger = LoggerFactory.getLogger(GraphPanel.class);

	private final static Dimension DEFAULT_DIMENSION = new Dimension(600, 400);

	private final Graph graph;
	private final Point popupLocation = new Point();

	private List<VertexPanel> vertexPanelList = new LinkedList<VertexPanel>();

	/**
	 * Panel on which user has started dragging a new edge.
	 * @see #lastHoveredVertexPanel
	 */
	private VertexPanel draggingEdgeFrom;

	/**
	 * For determining of panel on which mouse was released when dragging a new edge.
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
		layoutManager.layoutDefault(this);
	}


	public Component add(Component component) {
		if (component instanceof VertexPanel) {
			VertexPanel panel = (VertexPanel) component;
			vertexPanelList.add(panel);
		}
		return super.add(component);
	}

	private void addVertexPanels() {
		for (Vertex vertex : graph.getVertexSet()) {
			VertexPanel vertexPanel = new VertexPanel(vertex);
			vertexPanel.setLocation(popupLocation);
			add(vertexPanel);
		}
	}

	public List<VertexPanel> getVertexPanelList() {
		return vertexPanelList;
	}

	public void vertexAdded(Vertex vertex) {
		VertexPanel vertexPanel = new VertexPanel(vertex);
		add(vertexPanel);
	}

	public void setHoveredVertexPanel(VertexPanel vertexPanel) {
		logger.debug("setHoveredVertexPanel({})", vertexPanel);
		if (vertexPanel != null) {
			lastHoveredVertexPanel = vertexPanel;
			logger.debug("lastHoveredVertexPanel now {}", lastHoveredVertexPanel);
			setComponentZOrder(vertexPanel, 0);
			repaint(
				vertexPanel.getX(),
				vertexPanel.getY(),
				vertexPanel.getWidth(),
				vertexPanel.getHeight()
			);
		}
		else {
//			addEdgePanel.setVisible(false);
		}
	}

	public void startDraggingEdge(VertexPanel vertexPanel) {
		draggingEdgeFrom = vertexPanel;
		repaint();
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
//		logger.debug("paintComponent()");

		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;

		paintEdges(g2);

//		g.setColor(new Color(0, 0, 0));
		if (draggingEdgeFrom != null) {
			Point mousePos = getMousePosition();
			if (mousePos != null) {
				g2.drawLine(
					draggingEdgeFrom.getVertexCenterX(),
					draggingEdgeFrom.getVertexCenterY(),
					mousePos.x,
					mousePos.y
				);
//				logger.debug("draggingEdgeFrom.getVertexCenterX()={}", draggingEdgeFrom.getVertexCenterX());
			}
		}
	}

	private void paintEdges(Graphics2D g2) {
		for (Edge edge : graph.getEdgeSet()) {
			drawEdge(edge.getTail(), edge.getHead(), g2);
		}
	}

	private void drawEdge(Vertex tail, Vertex head, Graphics2D g2) {
// TODO		
/*
		g2.drawLine(
			draggingEdgeFrom.getVertexCenterX(),
			draggingEdgeFrom.getVertexCenterY(),
			mousePos.x,
			mousePos.y
		);
*/
	}

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
