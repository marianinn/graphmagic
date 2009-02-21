package name.dlazerka.gc.ui;

import name.dlazerka.gc.Main;
import name.dlazerka.gc.bean.Vertex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Dzmitry Lazerka
 */
public class GraphPanel extends JPanel {
	private final static Logger logger = LoggerFactory.getLogger(GraphPanel.class);
	private final static Dimension DEFAULT_DIMENSION = new Dimension(600, 400);

	private GraphPanelModel model;
	private List<VertexPanel> vertexPanelList = new LinkedList<VertexPanel>();

	public GraphPanel() {
//		addMouseListener(this);
//		addMouseMotionListener(this);
		setModel(new GraphPanelModel(DEFAULT_DIMENSION));

		setLayout(new GraphLayoutManager(model));

		setPreferredSize(DEFAULT_DIMENSION);

		setComponentPopupMenu(createPopupMenu());

		addVertexPanels();

	}


	public Component add(Component component) {
		if (component instanceof VertexPanel) {
			VertexPanel panel = (VertexPanel) component;
			vertexPanelList.add(panel);
		}
		return super.add(component);
	}

	private void addVertexPanels() {
		for (Vertex vertex : model.getGraph().getVertexSet()) {
			VertexPanel vertexPanel = new VertexPanel(vertex);
			add(vertexPanel);
		}
		validate();
		((GraphLayoutManager) getLayout()).layoutDefault(this);
	}

	public void setModel(GraphPanelModel model) {
		this.model = model;
	}

	public List<VertexPanel> getVertexPanelList() {
		return vertexPanelList;
	}

	private class AddVertexAction extends AbstractAction {
		public AddVertexAction() {
			super(Main.getString("add.vertex"));
		}

		public void actionPerformed(ActionEvent e) {
			model.addVertex();
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

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
	}

	@Override
	protected void paintChildren(Graphics g) {
		super.paintChildren(g);
	}

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
