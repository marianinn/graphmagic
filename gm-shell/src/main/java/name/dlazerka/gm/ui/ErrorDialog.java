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
	private GroupLayout layout;

	public ErrorDialog(Window parent, Throwable throwable) {
		super(parent);
		this.throwable = throwable;
		setupUI();
		setupControls();
	}

	public static void showError(Throwable t, Component owner) {
		ErrorDialog errorDialog;

		if (!(owner instanceof Window)) {
			owner = SwingUtilities.getWindowAncestor(owner);
		}
		errorDialog = new ErrorDialog((Window) owner, t);
		errorDialog.setResizable(true);
		errorDialog.pack();
		errorDialog.setVisible(true);
	}

	protected void setupUI() {
		setContentPane(contentPane);

		layout = new GroupLayout(contentPane);
		contentPane.setLayout(layout);
		layout.setAutoCreateContainerGaps(true);

		JTextArea messageArea = new JTextArea(throwable.getLocalizedMessage());
		messageArea.setRows(1);
		messageArea.setPreferredSize(new Dimension(600, 20));
		messageArea.setBackground(getParent().getBackground());
		messageArea.setFont(getParent().getFont());
		messageArea.setMargin(new Insets(20, 30, 30, 30));
		messageArea.setEditable(false);

		final JCheckBox showStackTrace = new JCheckBox(Main.getString("show.stack.trace"));
		showStackTrace.setFont(getParent().getFont());
		showStackTrace.setForeground(Color.BLUE);
		showStackTrace.setMargin(new Insets(0, 0, 10, 10));
		showStackTrace.addActionListener(new ShowStackTraceListener());

		String stackTraceString = Exceptions.makeStackTrace(throwable);
		JTextArea stackTraceArea = new JTextArea(stackTraceString);
		stackTraceArea.setAutoscrolls(true);
		stackTraceArea.setEditable(false);
		stackTraceArea.setFont(getParent().getFont());
		stackTraceContainer = new JScrollPane(stackTraceArea);
		stackTraceContainer.setVisible(false);

		layout.setHorizontalGroup(
			layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(
					layout.createSequentialGroup()
						.addComponent(messageArea)
						.addComponent(showStackTrace)
				)
//				.addComponent(messageArea)
//				.addComponent(topContainer)
				.addComponent(stackTraceContainer)
		);
		layout.setVerticalGroup(
			layout.createSequentialGroup().addGroup(
				layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
					.addComponent(messageArea)
					.addComponent(showStackTrace)
			)
				.addComponent(stackTraceContainer, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
		);
	}

	protected void setupControls() {
		setTitle(Main.getString("error"));

		if (JDialog.isDefaultLookAndFeelDecorated()) {
			boolean supportsWindowDecorations =
				UIManager.getLookAndFeel().getSupportsWindowDecorations();
			if (supportsWindowDecorations) {
				setUndecorated(true);
				getRootPane().setWindowDecorationStyle(STYLE);
			}
		}
		pack();
		setLocationRelativeTo(getParent());
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
