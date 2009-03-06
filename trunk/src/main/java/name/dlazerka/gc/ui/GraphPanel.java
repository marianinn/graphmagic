package name.dlazerka.gc.ui;

import name.dlazerka.gc.Main;
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
	private List<VertexPanel> vertexPanelList = new LinkedList<VertexPanel>();
	private final Point popupLocation = new Point();
	private VertexPanel draggingEdgeFrom;
//	private JComponent addEdgePanel = new AddEdgePanel();

	public GraphPanel() {
		graph = new Graph();

		addMouseListener(new PopupLocationRememberer());

		GraphLayoutManager layoutManager = new GraphLayoutManager();
		setLayout(layoutManager);

		setSize(DEFAULT_DIMENSION);// for GraphLayoutManager@58

		setComponentPopupMenu(createPopupMenu());
		addVertexPanels();
		layoutManager.layoutDefault(this);

//		add(addEdgePanel);
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
		if (vertexPanel != null) {
			setComponentZOrder(vertexPanel, 0);
			repaint(
				vertexPanel.getX(),
				vertexPanel.getY(),
				vertexPanel.getWidth(),
				vertexPanel.getHeight()
			);
/*
			addEdgePanel.setLocation(
				vertexPanel.getX() + vertexPanel.getWidth(),
				vertexPanel.getY()
			);
			addEdgePanel.setVisible(true);
*/
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
		draggingEdgeFrom = null;
		repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
//		logger.debug("paintComponent()");

		super.paintComponent(g);

//		g.setColor(new Color(0, 0, 0));
		if (draggingEdgeFrom != null) {
			Point mousePos = getMousePosition();
			if (mousePos != null) {
				g.drawLine(
					draggingEdgeFrom.getVertexCenterX(),
					draggingEdgeFrom.getVertexCenterY(),
					mousePos.x,
					mousePos.y
				);
//				logger.debug("draggingEdgeFrom.getVertexCenterX()={}", draggingEdgeFrom.getVertexCenterX());
			}
		}
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
