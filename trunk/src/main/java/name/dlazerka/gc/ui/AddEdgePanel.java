package name.dlazerka.gc.ui;

import javax.swing.*;
import java.awt.*;

/**
 * @author Dzmitry Lazerka
 */
public class AddEdgePanel extends JPanel {
	private final static Dimension SIZE = new Dimension(20, 20);
	
	public AddEdgePanel() {
		super();

		setPreferredSize(SIZE);

		Icon addEdgeIcon = new ImageIcon("addEdgeIcon.png");
		add(new JButton("as"));
		add(new JMenuItem(addEdgeIcon));
	}


}
