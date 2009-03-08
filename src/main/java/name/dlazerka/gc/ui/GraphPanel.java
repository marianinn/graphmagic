package name.dlazerka.gc.ui;

import name.dlazerka.gc.Main;
import name.dlazerka.gc.bean.Edge;
import name.dlazerka.gc.bean.Graph;
import name.dlazerka.gc.bean.GraphChangeListener;
import name.dlazerka.gc.bean.Vertex;
import name.dlazerka.gc.util.ListMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Map;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class GraphPanel extends JPanel implements GraphChangeListener {
	private final static Logger logger = LoggerFactory.getLogger(GraphPanel.class);

	private final static Dimension DEFAULT_DIMENSION = new Dimension(600, 400);

	private final Graph graph;

	private Map<Vertex, VertexPanel> vertexToVertexPanel = new ListMap<Vertex, VertexPanel>();
	private Map<Edge, EdgePanel> edgeToEdgePanel = new ListMap<Edge, EdgePanel>();

	/**
	 * Panel on which user has started dragging a new edge.
	 *
	 * @see #lastHoveredVertexPanel
	 */
	private VertexPanel draggingEdgeFrom;
	private final NewEdgePanel newEdgePanel = new NewEdgePanel();

	/**
	 * For determining of panel on which mouse was released when dragging a new edge.
	 *
	 * @see #draggingEdgeFrom
	 */
	private VertexPanel lastHoveredVertexPanel;

	public GraphPanel() {
		graph = new Graph();

		GraphLayoutManager layoutManager = new GraphLayoutManager();
		setLayout(layoutManager);
		setSize(DEFAULT_DIMENSION);// for GraphLayoutManager@58

		setComponentPopupMenu(createPopupMenu());

		addVertexPanels();
		addEdgePanels();
		add(newEdgePanel);

		layoutManager.layoutDefault(this);
	}

	/**
	 * Overrides default and returns false since our children can overlap each other.
	 * Though it seems unnecessary because it works for true pretty well. Let is be here for confidence.
	 * See <a href="http://java.sun.com/products/jfc/tsc/articles/painting/#props">Painting in AWT and Swing</a>.
	 *
	 * @return false
	 * @see <a href="http://java.sun.com/products/jfc/tsc/articles/painting/#props">Painting in AWT and Swing</a>
	 */
	@Override
	public boolean isOptimizedDrawingEnabled() {
		return false;
	}

	public Component add(Component component) {

		if (component instanceof VertexPanel) {
			VertexPanel panel = (VertexPanel) component;
			vertexToVertexPanel.put(panel.getVertex(), panel);
		}
		else if (component instanceof EdgePanel) {
			EdgePanel panel = (EdgePanel) component;
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
			EdgePanel edgePanel = createEdgePanel(edge);

			add(edgePanel);
		}
	}

	private EdgePanel createEdgePanel(Edge edge) {
		VertexPanel tailPanel = vertexToVertexPanel.get(edge.getTail());
		VertexPanel headPanel = vertexToVertexPanel.get(edge.getHead());

		EdgePanel edgePanel = new EdgePanel(
			edge,
			tailPanel,
			headPanel
		);

		tailPanel.addAdjacentEdgePanel(edgePanel);
		headPanel.addAdjacentEdgePanel(edgePanel);

		edgePanel.setBounds(0, 0, getWidth(), getHeight());

		return edgePanel;
	}

	public void vertexAdded(Vertex vertex) {
		VertexPanel vertexPanel = new VertexPanel(vertex);
		add(vertexPanel);
	}

	public void setHoveredVertexPanel(VertexPanel vertexPanel) {
		logger.trace("{}", vertexPanel);

		if (vertexPanel != null) {
			lastHoveredVertexPanel = vertexPanel;
			setComponentZOrder(vertexPanel, 0);
		}
	}

	public void startDraggingEdge(VertexPanel vertexPanel) {
		draggingEdgeFrom = vertexPanel;

		newEdgePanel.setTail(vertexPanel);
		newEdgePanel.setVisible(true);
	}

	public boolean isDraggingEdge() {
		return draggingEdgeFrom != null;
	}

	public void stopDraggingEdge() {
		Vertex tail = draggingEdgeFrom.getVertex();
		Vertex head = lastHoveredVertexPanel.getVertex();
		Edge edge = new Edge(tail, head);

		EdgePanel edgePanel = createEdgePanel(edge);
		add(edgePanel);

		graph.addEdge(edge);

		draggingEdgeFrom = null;
		newEdgePanel.setVisible(false);
	}

	public NewEdgePanel getNewEdgePanel() {
		return newEdgePanel;
	}

	private class AddVertexAction extends AbstractAction {
		public AddVertexAction() {
			super(Main.getString("add.vertex"));
		}

		public void actionPerformed(ActionEvent e) {
			Vertex vertex = graph.addVertex();
			VertexPanel vertexPanel = new VertexPanel(vertex);
			add(vertexPanel);
		}
	}

/*
	protected class PopupLocationRememberer extends MouseAdapter {
		protected void rememberLocation(MouseEvent e) {
			popupLocation.x = e.getX();
			popupLocation.y = e.getY();
		}

		@Override
		public void mousePressed(MouseEvent e) {
			logger.trace("point={}", e.getPoint());
			rememberLocation(e);
		}
	}
*/

	private JPopupMenu createPopupMenu() {
		JPopupMenu popupMenu = new JPopupMenu();
		popupMenu.add(new AddVertexAction());
		return popupMenu;
	}
}
