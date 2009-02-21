package name.dlazerka.gc.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.List;

/**
 * @author Dzmitry Lazerka
 */
public class GraphLayoutManager implements LayoutManager {
	public static final Logger logger = LoggerFactory.getLogger(GraphLayoutManager.class);

	private final GraphPanelModel model;
//	private List<VertexPanel> vertexPanelList = new LinkedList<VertexPanel>();
	private static final Dimension MINIMUM_LAYOUT_SIZE = new Dimension(0, 0);

	public GraphLayoutManager(GraphPanelModel model) {
		this.model = model;
	}

	public void addLayoutComponent(String name, Component comp) {
		logger.debug("addLayoutComponent(): name={}, comp={}", new Object[]{name, comp});

//		if (comp instanceof VertexPanel) {
//			VertexPanel vertexPanel = (VertexPanel) comp;
//			vertexPanelList.add(vertexPanel);
//		}
	}

	public void removeLayoutComponent(Component comp) {
		// todo
	}

	public Dimension preferredLayoutSize(Container parent) {
		return parent.getPreferredSize();
	}

	public Dimension minimumLayoutSize(Container parent) {
		return MINIMUM_LAYOUT_SIZE;
	}

	public void layoutContainer(Container parent) {
		logger.debug("layoutContainer(): parent={}", parent);

/*
		for (VertexPanel vertexPanel : vertexPanelList) {
			vertexPanel.setLocation(
				vertexPanel.getX(),
				vertexPanel.getY()
			);
		}
*/
	}

	public void layoutDefault(GraphPanel parent) {
		circleVertices(parent);
	}

	private void circleVertices(GraphPanel parent) {
		Dimension size = parent.getPreferredSize();
		List<VertexPanel> vertexPanelList = parent.getVertexPanelList();

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
