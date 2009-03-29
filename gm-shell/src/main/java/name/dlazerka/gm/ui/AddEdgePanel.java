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

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.net.URL;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class AddEdgePanel extends JPanel {
	private static final Logger logger = LoggerFactory.getLogger(AddEdgePanel.class);
	private static final URL DEFAULT_IMAGE_URL = AddEdgePanel.class.getResource("/addEdgeIcon.png"); 
	private static final URL HOVER_IMAGE_URL = AddEdgePanel.class.getResource("/addEdgeIconHover.png");

	private static final ImageIcon DEFAULT_ICON = new ImageIcon(DEFAULT_IMAGE_URL);
	private static final ImageIcon HOVER_ICON = new ImageIcon(HOVER_IMAGE_URL);

	static {
		checkImageLoadedSuccess(DEFAULT_ICON);
		checkImageLoadedSuccess(HOVER_ICON);
	}

	private ImageIcon icon = DEFAULT_ICON;
	private boolean visible;

	private static void checkImageLoadedSuccess(ImageIcon icon) {
		if (icon.getImageLoadStatus() != MediaTracker.COMPLETE) {
			logger.error("Unable to load image, filepath='{}'", DEFAULT_IMAGE_URL);
			throw new RuntimeException();
		}
	}

	public AddEdgePanel() {
		super();

//		logger.debug("imageLoadStatus={}", icon.getImageLoadStatus());

		Dimension size = new Dimension(icon.getIconWidth(), icon.getIconHeight());
		setPreferredSize(size);
		setSize(size);

		setVisible(false);
		setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0x80), 1));

		addMouseMotionListener(new DragMouseListener());
		addMouseListener(new MouseListener());
	}


	@Override
	protected void paintBorder(Graphics g) {
		if (visible) {
			super.paintBorder(g);
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
//		super.paintComponent(g);

		if (visible) {
			icon.paintIcon(null, g, 0, 0);
		}
	}

	@Override
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public ImageIcon getIcon() {
		return icon;
	}

	private VertexPanel getVertexPanel() {
		return (VertexPanel) getParent();
	}

	private void startDraggingEdge() {
		getVertexPanel().startDraggingEdge();
	}

	private boolean isDraggingEdge() {
		return getVertexPanel().isDraggingEdge();
	}

	private void stopDraggingEdge() {
		getVertexPanel().stopDraggingEdge();
	}

	private class DragMouseListener extends MouseMotionAdapter {
		@Override
		public void mouseDragged(MouseEvent e) {
			GraphPanel graphPanel = getVertexPanel().getGraphPanel();

			NewEdgePanel newEdgePanel = graphPanel.getNewEdgePanel();
			newEdgePanel.trackMouseDragged();
		}
	}

	private class MouseListener extends MouseAdapter {
		@Override
		public void mouseEntered(MouseEvent e) {
			logger.trace("");
			icon = HOVER_ICON;
			getVertexPanel().setHovered(true);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			logger.trace("");

			if (!isDraggingEdge()) {
				icon = DEFAULT_ICON;
				getVertexPanel().setHovered(false);
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			logger.trace("");
			startDraggingEdge();
		}


		@Override
		public void mouseReleased(MouseEvent e) {
			logger.trace("");

			stopDraggingEdge();

			if (!contains(e.getX(), e.getY())) {
				icon = DEFAULT_ICON;
			}

			// for repainting vertex when mouse was released outside this vertex
			getVertexPanel().checkHovered();
		}
	}
}
