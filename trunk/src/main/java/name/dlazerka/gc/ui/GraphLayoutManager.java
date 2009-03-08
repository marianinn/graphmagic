package name.dlazerka.gc.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.Collection;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class GraphLayoutManager implements LayoutManager {
	public static final Logger logger = LoggerFactory.getLogger(GraphLayoutManager.class);

//	private List<VertexPanel> vertexPanelList = new LinkedList<VertexPanel>();
	private static final Dimension MINIMUM_LAYOUT_SIZE = new Dimension(0, 0);

	public void addLayoutComponent(String name, Component comp) {
		logger.debug("{}, {}", new Object[]{name, comp});

//		if (comp instanceof VertexPanel) {
//			VertexPanel vertexPanel = (VertexPanel) comp;
//			vertexPanelList.add(vertexPanel);
//		}
	}

	public void removeLayoutComponent(Component comp) {
		// todo
	}

	public Dimension preferredLayoutSize(Container parent) {
		return null;
//		return parent.getPreferredSize();
	}

	public Dimension minimumLayoutSize(Container parent) {
		return MINIMUM_LAYOUT_SIZE;
	}

	public void layoutContainer(Container parent) {
		logger.debug("{}", parent);

		GraphPanel panel = (GraphPanel) parent;


		for (EdgePanel edgePanel : panel.getEdgePanels()) {
			edgePanel.setBounds(0, 0, parent.getWidth(), parent.getHeight());
		}

/*
		for (VertexPanel vertexPanel : vertexPanelList) {
			vertexPanel.setLocation(
				vertexPanel.getX(),
				vertexPanel.getY()
			);
		}
*/
	}

	public void layoutDefault(GraphPanel graphPanel) {
		circleVertices(graphPanel);
	}

	private void circleVertices(GraphPanel graphPanel) {
		Dimension size = graphPanel.getSize();
		Collection<VertexPanel> vertexPanelList = graphPanel.getVertexPanels();

		logger.debug("circleVertices(size={})", size);
		
		int vertexCount = vertexPanelList.size();

		Point circleCenter = new Point(size.width / 2, size.height / 2);
		int radius = Math.min(size.width * 3 / 4 - circleCenter.x, size.height * 3 / 4 - circleCenter.y);

		double angleStep = 2 * Math.PI / vertexCount;

		int i = 0;
		for (VertexPanel vertexPanel : vertexPanelList) {
			double angle = i * angleStep;

			vertexPanel.setLocation(
				(int) Math.round(radius * Math.cos(angle)) + circleCenter.x,
				(int) Math.round(radius * -Math.sin(angle)) + circleCenter.y
			);

			i++;
		}
	}
}
