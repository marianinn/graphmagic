package name.dlazerka.gc;

import javax.swing.*;
import java.awt.*;

/**
 * @author Dzmitry Lazerka
 */
public class VertexPanel extends JPanel {

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (!(g instanceof Graphics2D)) {
			throw new IllegalArgumentException();
		}

		Graphics2D g2 = (Graphics2D) g;

		g2.drawOval(0, 0, 100, 100);
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(102, 102);
	}
}
