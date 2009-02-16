package name.dlazerka.gc;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;

/**
 * @author Dzmitry Lazerka
 */
public class ArcPanel extends JPanel {
	Point fromPoint = new Point(0, 0);
	Point toPoint = new Point(100, 100);
	Line2D line = new Line2D.Double(fromPoint, toPoint);

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (!(g instanceof Graphics2D)) {
			throw new IllegalArgumentException();
		}

		Graphics2D g2 = (Graphics2D) g;

		g2.draw(line);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(100, 100);
	}
}
