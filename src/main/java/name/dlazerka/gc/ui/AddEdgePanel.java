package name.dlazerka.gc.ui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

/**
 * @author Dzmitry Lazerka
 */
public class AddEdgePanel extends JPanel {
	private static final Logger logger = LoggerFactory.getLogger(AddEdgePanel.class);
	private static final String IMAGE_FILENAME = "addEdgeIcon.png";

	private final ImageIcon icon = new ImageIcon(IMAGE_FILENAME);

	public AddEdgePanel() {
		super();

		logger.debug("AddEdgePanel(): imageLoadStatus={}", icon.getImageLoadStatus());

//		setVisible(false);
		setBorder(BorderFactory.createLineBorder(new Color(0, 0, 0), 1));

		if (icon.getImageLoadStatus() == MediaTracker.COMPLETE) {
			Dimension size = new Dimension(icon.getIconWidth(), icon.getIconHeight());
			setPreferredSize(size);
			setSize(size);
		}
		else {
			logger.error("Unable to load image, filepath='{}'", IMAGE_FILENAME);
		}

		addMouseMotionListener(new DragMouseListener());
		addMouseListener(new MouseListener());
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		icon.paintIcon(null, g, 0, 0);
	}

	public ImageIcon getIcon() {
		return icon;
	}

	private VertexPanel getParentVertexPanel() {
		return (VertexPanel) getParent();
	}

	private class DragMouseListener extends MouseMotionAdapter {
		@Override
		public void mouseDragged(MouseEvent e) {
//			logger.debug("mouseDragged()");
			getParentVertexPanel().getParentGraphPanel().repaint();
		}
	}

	private class MouseListener extends MouseAdapter {
		@Override
		public void mouseEntered(MouseEvent e) {
			logger.debug("mouseEntered()");
			getParentVertexPanel().setHovered(true);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			logger.debug("mousePressed()");
			getParentVertexPanel().startDraggingEdge();
		}


		@Override
		public void mouseReleased(MouseEvent e) {
			logger.debug("mouseReleased()");

			// for repainting vertex when mouse was released outside this vertex
			getParentVertexPanel().checkHovered();

			getParentVertexPanel().stopDraggingEdge();
		}
	}
}
