package name.dlazerka.gc.ui;

import name.dlazerka.gc.Main;

import javax.swing.*;
import java.awt.event.*;

public class MainFrame extends JFrame {
	private JPanel contentPane;
	private JButton buttonOK;
	private JButton buttonCancel;

	public MainFrame() {
		setContentPane(contentPane);
		getRootPane().setDefaultButton(buttonOK);
		setTitle(Main.getString("main.title"));

		buttonCancel.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					onCancel();
				}
			}
		);

// call onCancel() when cross is clicked
		addWindowListener(
			new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					onCancel();
				}
			}
		);

// call onCancel() on ESCAPE
		contentPane.registerKeyboardAction(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					onCancel();
				}
			}, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
		);
	}

	private void onCancel() {
// add your code here if necessary
		dispose();
	}
}
