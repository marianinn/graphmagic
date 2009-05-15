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

import name.dlazerka.gm.ResourceBundle;
import name.dlazerka.gm.util.Exceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * @author Dzmitry Lazerka www.dlazerka.name
 */
public class ErrorDialog extends JDialog {
	private static final Logger logger = LoggerFactory.getLogger(ErrorDialog.class);

	private static final int STYLE = JRootPane.ERROR_DIALOG;
	private Object value;
	private final Throwable throwable;
	private JPanel contentPane = new JPanel();
	private JScrollPane stackTraceContainer;
	private final Font defaultFont = new Font("Dialog", Font.PLAIN, 12);
	private final Color defaultBackground = new Color(240, 240, 240);

	public ErrorDialog(Window parent, Throwable throwable) {
		super(parent);
		this.throwable = throwable;
		setupUI();
		setupControls();
	}

	public static void showError(Throwable t, Component owner) {
		ErrorDialog errorDialog;

		if (owner != null && !(owner instanceof Window)) {
			owner = SwingUtilities.getWindowAncestor(owner);
		}

//		JOptionPane.showMessageDialog(owner, t.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//		if (true) return;

		errorDialog = new ErrorDialog((Window) owner, t);
		errorDialog.setModal(true);
		errorDialog.setResizable(true);
		errorDialog.getRootPane().setWindowDecorationStyle(STYLE);

		errorDialog.pack();
		errorDialog.setVisible(true);
	}

	protected void setupUI() {
		setTitle(ResourceBundle.getString("error"));

		setContentPane(contentPane);

		GroupLayout layout = new GroupLayout(contentPane);
		contentPane.setLayout(layout);
		layout.setAutoCreateContainerGaps(true);

		JTextArea messageArea = new JTextArea(throwable.getLocalizedMessage());
		messageArea.setRows(1);
		messageArea.getPreferredSize().width = 600;
//		messageArea.setPreferredSize(new Dimension(600, -1));
		messageArea.setBackground(defaultBackground);
		messageArea.setFont(defaultFont);

		messageArea.setMargin(new Insets(20, 30, 30, 30));
		messageArea.setEditable(false);

		final JCheckBox showStackTrace = new JCheckBox(ResourceBundle.getString("show.stack.trace"));

		showStackTrace.setFont(defaultFont);

		showStackTrace.setForeground(Color.BLUE);
		showStackTrace.setMargin(new Insets(0, 0, 10, 10));
		showStackTrace.addActionListener(new ShowStackTraceListener());

		JButton okButton = new JButton(ResourceBundle.getString("ok"));
		okButton.setMargin(new Insets(3, 50, 3, 50));
		okButton.getModel().addActionListener(
			new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					onClose();
				}
			}
		);
		JPanel panel = new JPanel();
		panel.add(okButton, BorderLayout.CENTER);

		String stackTraceString = Exceptions.makeStackTrace(throwable);
		JTextArea stackTraceArea = new JTextArea(stackTraceString);
		stackTraceArea.setAutoscrolls(true);
		stackTraceArea.setEditable(false);
		stackTraceArea.setFont(defaultFont);
		stackTraceContainer = new JScrollPane(stackTraceArea);
		stackTraceContainer.setVisible(false);
		Dimension preferredSize = new Dimension(messageArea.getPreferredSize());
		preferredSize.height = 400;
		stackTraceContainer.setPreferredSize(preferredSize);

		layout.setHorizontalGroup(
			layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addComponent(messageArea)
				.addGroup(
					layout.createSequentialGroup()
						.addComponent(panel)
						.addComponent(showStackTrace)
				)
				.addComponent(stackTraceContainer)
		);
		layout.setVerticalGroup(
			layout.createSequentialGroup()
				.addComponent(
					messageArea, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE
				)
				.addGroup(
					layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
						.addComponent(panel)
						.addComponent(showStackTrace)
				)
				.addComponent(
				stackTraceContainer, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE
			)
		);
	}

	protected void setupControls() {
		if (JDialog.isDefaultLookAndFeelDecorated()) {
			boolean supportsWindowDecorations =
				UIManager.getLookAndFeel().getSupportsWindowDecorations();
			if (supportsWindowDecorations) {
//				setUndecorated(true);
				getRootPane().setWindowDecorationStyle(STYLE);
			}
		}
		pack();

		if (getParent() != null) {
			setLocationRelativeTo(getParent());
		}

		addWindowListener(
			new WindowAdapter() {
				public void windowClosing(WindowEvent we) {
					dispose();
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

	private void onClose() {
		dispose();
	}

	public void setValue(Object newValue) {
		Object oldValue = value;
		value = newValue;
		firePropertyChange(JOptionPane.VALUE_PROPERTY, oldValue, value);
	}

	private class ShowStackTraceListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			ErrorDialog dialog = ErrorDialog.this;
			stackTraceContainer.setVisible(!stackTraceContainer.isVisible());
			dialog.pack();
//			invalidate();
		}
	}
}
