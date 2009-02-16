package name.dlazerka.gc;

import name.dlazerka.gc.ui.GraphUI;

import javax.swing.*;
import java.awt.*;

/**
 * @author Dzmitry Lazerka
 */
public class GraphPanel extends JPanel {
	private final GraphUI graphUI = new GraphUI();


	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (!(g instanceof Graphics2D)) {
			throw new IllegalArgumentException();
		}

		Graphics2D g2 = (Graphics2D) g;

		drawGraph(g2);

	}

	private void drawGraph(Graphics2D g2) {
		graphUI.drawGraph(g2, getSize());
	}
}
