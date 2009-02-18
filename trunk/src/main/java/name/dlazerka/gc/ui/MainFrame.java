package name.dlazerka.gc.ui;

import name.dlazerka.gc.Main;
import name.dlazerka.gc.model.Model;
import name.dlazerka.gc.check.Parser;
import name.dlazerka.gc.check.Condition;
import name.dlazerka.gc.check.ParseException;
import name.dlazerka.gc.check.CheckResult;

import javax.swing.*;
import java.awt.event.*;

public class MainFrame extends JFrame {
	private JPanel contentPane;
	private JButton buttonCheck;
	private JTextArea textArea1;
	private GraphPanel graphPanel;
	private JLabel statusBarLabel;

	public MainFrame() {
		setContentPane(contentPane);
		getRootPane().setDefaultButton(buttonCheck);
		setTitle(Main.getString("main.title"));

		buttonCheck.addActionListener(
			new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					onCheck();
				}
			}
		);

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
	}

	private void onCheck() {
		String conditionString = textArea1.getText();
		Condition condition;
		try {
			condition = Parser.parseString(conditionString);
		}
		catch (ParseException e) {
			statusBarLabel.setText(Main.getString("error.parse"));
			return;
		}

		CheckResult result = condition.check(Model.getGraph());

		if (result.getSatisfy()) {
			statusBarLabel.setText(Main.getString("check.success"));
		}
		else {
			statusBarLabel.setText(Main.getString("check.failure"));
		}
	}

	private void onClose() {
		dispose();
	}

	private void createUIComponents() {
		graphPanel = new GraphPanel(); 
	}
}
