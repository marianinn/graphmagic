package name.dlazerka.gc.ui;

import name.dlazerka.gc.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.*;

public class MainFrame extends JFrame {
	private final static Logger logger = LoggerFactory.getLogger(MainFrame.class);

	private JPanel contentPane;
	private GraphPanel graphPanel;
	private JLabel statusBarLabel;
	private JTextArea textArea1;
	private JButton button1;

	public MainFrame() {
		setContentPane(contentPane);
		setTitle(Main.getString("main.title"));

// call onClose() when cross is clicked
		addWindowListener(
			new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					onClose();
				}
			}
		);

// call onClose() on ESCAPE
		contentPane.registerKeyboardAction(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					onClose();
				}
			}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
		);

		registerCommands();
	}

	private void registerCommands() {
	}

	private void onClose() {
		dispose();
	}

	private void createUIComponents() {
		graphPanel = new GraphPanel();
	}
}
