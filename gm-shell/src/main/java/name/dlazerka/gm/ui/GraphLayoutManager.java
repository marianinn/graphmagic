/*
 * GraphMagic, package for scientists working in graph theory.
 * Copyright (C) 2009 Dzmitry Lazerka dlazerka@dlazerka.name
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as
 * published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Author: Dzmitry Lazerka dlazerka@dlazerka.name
 */

package name.dlazerka.gm.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.*;

import name.dlazerka.gm.ui.edge.EdgePanel;
import name.dlazerka.gm.ui.edge.NewEdgePanel;
import name.dlazerka.gm.Mark;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class GraphLayoutManager implements LayoutManager2 {
	public static final Logger logger = LoggerFactory.getLogger(GraphLayoutManager.class);

	//	private List<VertexPanel> vertexPanelList = new LinkedList<VertexPanel>();
	private static final Dimension MINIMUM_LAYOUT_SIZE = new Dimension(0, 0);

	private Collection<VertexPanel> vertexPanels = new LinkedList<VertexPanel>();
	private Collection<EdgePanel> edgePanels = new LinkedList<EdgePanel>();
	private NewEdgePanel newEdgePanel;

	public void addLayoutComponent(String name, Component comp) {
		addLayoutComponent(comp);
	}

	public void addLayoutComponent(Component comp, Object constraints) {
		addLayoutComponent(comp);
	}

	private void addLayoutComponent(VertexPanel panel) {
		Point defaultLocation = panel.getParent().getMousePosition(true);
		if (defaultLocation != null) {
			panel.setVertexPanelCenter(defaultLocation);
		}
		else {
			panel.setVertexPanelCenter(
				panel.getGraphPanel().getWidth() / 2,
				panel.getGraphPanel().getHeight() / 2
			);
		}

		vertexPanels.add(panel);
	}

	private void addLayoutComponent(EdgePanel panel) {
		panel.setBounds(
			0, 0,
			panel.getGraphPanel().getWidth(),
			panel.getGraphPanel().getHeight()
		);
		edgePanels.add(panel);
	}

	private void addLayoutComponent(NewEdgePanel panel) {
		newEdgePanel = panel;
	}

	private void addLayoutComponent(Component component) {
		if (component instanceof VertexPanel) {
			addLayoutComponent(((VertexPanel) component));
		}
		else if (component instanceof EdgePanel) {
			addLayoutComponent(((EdgePanel) component));
		}
		else if (component instanceof NewEdgePanel) {
			addLayoutComponent(((NewEdgePanel) component));
		}
	}

	public void removeLayoutComponent(Component comp) {
		if (comp instanceof VertexPanel) {
			VertexPanel panel = (VertexPanel) comp;
			vertexPanels.remove(panel);
		}
		else if (comp instanceof EdgePanel) {
			EdgePanel panel = (EdgePanel) comp;
			edgePanels.remove(panel);
		}
	}

	public Dimension minimumLayoutSize(Container parent) {
		return MINIMUM_LAYOUT_SIZE;
	}

	public Dimension preferredLayoutSize(Container parent) {
		return null;
//		return parent.getPreferredSize(); // produces infinite loop
	}

	public Dimension maximumLayoutSize(Container target) {
		return null;
	}

	public float getLayoutAlignmentX(Container target) {
		return 0;
	}

	public float getLayoutAlignmentY(Container target) {
		return 0;
	}

	public void invalidateLayout(Container target) {
		logger.trace("");
	}

	public void layoutContainer(Container parent) {
		logger.trace("{}", parent);

		for (EdgePanel edgePanel : edgePanels) {
			edgePanel.setBounds(0, 0, parent.getWidth(), parent.getHeight());
		}

		if (newEdgePanel != null) {
			newEdgePanel.setBounds(0, 0, parent.getWidth(), parent.getHeight());
		}
	}
}
